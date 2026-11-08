// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static edu.wpi.first.units.Units.*;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.FeedbackConfigs;
import com.ctre.phoenix6.configs.MotionMagicConfigs;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.configs.SoftwareLimitSwitchConfigs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.MotionMagicExpoVoltage;
import com.ctre.phoenix6.hardware.TalonFX;

import frc.robot.*;


public class Bowl extends SubsystemBase {
  private final TalonFX m_fx = new TalonFX(Constants.BowlConstants.kBowlMotor_ID); 
  private final MotionMagicExpoVoltage m_mmReq = new MotionMagicExpoVoltage(0);
  private double RRR = Constants.BowlConstants.kDegrees_Zero;
  private double maxRRR = Constants.BowlConstants.kDegrees_Max;
  private double minRRR = Constants.BowlConstants.kDegrees_Min;

  // ----------------------------------------------------------------------------------------------------
  /** Creates a new ExampleSubsystem. */
  public Bowl() {
    TalonFXConfiguration cfg = new TalonFXConfiguration();

    // motor up & down limit
    SoftwareLimitSwitchConfigs ml = cfg.SoftwareLimitSwitch;
    ml.withForwardSoftLimitThreshold(Degrees.of(maxRRR));
    ml.withReverseSoftLimitThreshold(Degrees.of(minRRR));
    // ml.ForwardSoftLimitThreshold = maxRRR;
    // ml.ReverseSoftLimitThreshold = minRRR;
    ml.ForwardSoftLimitEnable = true;
    ml.ReverseSoftLimitEnable = true;

    // motor A limit
    CurrentLimitsConfigs cfgla = cfg.CurrentLimits;
    cfgla.StatorCurrentLimit = 100;
    cfgla.StatorCurrentLimitEnable = true;

    /* Configure gear ratio */
    FeedbackConfigs fdb = cfg.Feedback;
    fdb.SensorToMechanismRatio = 23; // 12.8 rotor rotations per mechanism rotation
    
    // Choose MotionMagicExpo kV and kA.
    // Use the default values scaled with the sensor ratio.
    // Increase these values for a "softer" response
    // Note these two configs are always in V (Volts) even when using
    // MotionMagicExpoDutyCycle or MotionMagicExpoTorqueCurrentFOC
    var newMotionMagicExpo_kV = Volts.per(Rotations.per(Second))
        .ofNative(0.12 * fdb.SensorToMechanismRatio); // 12 V / 100 RPS_motor * 12.8 (mechanism) rotations per motor
                                                      // rotation => 1.536 V / RPS (mechanism)
    var newMotionMagicExpo_kA = Volts.per(Rotations.per(Second).per(Second))
        .ofNative(0.10 * fdb.SensorToMechanismRatio); // 0.1 V / RPS^2 * 12.8 (mechanism) rotations per motor rotation
                                                      // => 1.28 V / RPS^2 (mechanism)

    /* Configure Motion Magic */
    MotionMagicConfigs mm = cfg.MotionMagic;
    mm.withMotionMagicCruiseVelocity(RotationsPerSecond.of(Constants.BowlConstants.kVelocity)) // 5 (mechanism) rotations per second cruise
      .withMotionMagicAcceleration(RotationsPerSecondPerSecond.of(Constants.BowlConstants.kAcc)) // Take approximately 0.5 seconds to reach max vel
      .withMotionMagicJerk(RotationsPerSecondPerSecond.per(Second).of(Constants.BowlConstants.kJerk)) // Take approximately 0.1 seconds to reach max accel
      .withMotionMagicExpo_kV(newMotionMagicExpo_kV)
      .withMotionMagicExpo_kA(newMotionMagicExpo_kA); // setup MotionMagicExpo kV and kA

    Slot0Configs slot0 = cfg.Slot0;
    slot0.kS = Constants.BowlConstants.ks; // Add 0.25 V output to overcome static friction
    slot0.kV = Constants.BowlConstants.kv; // A velocity target of 1 rps results in 0.12 V output
    slot0.kA = Constants.BowlConstants.ka; // An acceleration of 1 rps/s requires 0.01 V output
    slot0.kP = Constants.BowlConstants.kp; // A position error of 0.2 rotations results in 12 V output
    slot0.kI = Constants.BowlConstants.ki; // No output for integrated error
    slot0.kD = Constants.BowlConstants.kd; // A velocity error of 1 rps results in 0.5 V output

    Slot1Configs slot1 = cfg.Slot1;
    slot1.kS = Constants.BowlConstants.ks1; // Add 0.25 V output to overcome static friction
    slot1.kV = Constants.BowlConstants.kv1; // A velocity target of 1 rps results in 0.12 V output
    slot1.kA = Constants.BowlConstants.ka1; // An acceleration of 1 rps/s requires 0.01 V output
    slot1.kP = Constants.BowlConstants.kp1; // A position error of 0.2 rotations results in 12 V output
    slot1.kI = Constants.BowlConstants.ki1; // No output for integrated error
    slot1.kD = Constants.BowlConstants.kd1; // A velocity error of 1 rps results in 0.5 V output

    StatusCode status = StatusCode.StatusCodeNotInitialized;
    for (int i = 0; i < 5; ++i) {
      status = m_fx.getConfigurator().apply(cfg);
      if (status.isOK()) break;
    }
    if (!status.isOK()) {
      System.out.println("Could not configure device. Error: " + status.toString());
    }
  }

  // ----------------------------------------------------------------------------------------------------
  public Command PourTheBall(double DD){
    return PourTheBall(DD, 0);
  }

  public Command PourTheBall(double DD, int SlotID){
    return Commands.runOnce(() ->
      m_fx.setControl(m_mmReq.withPosition(Degrees.of(RRR)).withSlot(SlotID))
    );
  }

  // ----------------------------------------------------------------------------------------------------
  /**
   * Example command factory method.
   *
   * @return a command
   */
  public Command exampleMethodCommand() {
    // Inline construction of command goes here.
    // Subsystem::RunOnce implicitly requires `this` subsystem.
    return runOnce(
        () -> {
          /* one-time action goes here */
        });
  }

  /**
   * An example method querying a boolean state of the subsystem (for example, a digital sensor).
   *
   * @return value of some boolean subsystem state, such as a digital sensor.
   */
  public boolean exampleCondition() {
    // Query some boolean state, such as a digital sensor.
    return false;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    
  }

  @Override
  public void simulationPeriodic() {
    // This method will be called once per scheduler run during simulation
  }
}
