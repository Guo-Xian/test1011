// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static class OperatorConstants {
    public static final int kDriverControllerPort = 0;
  }

  public static class CanIds  {
    // public static final int kTestMotor_ID = 9;
    // public static final int k = 2;
  }
  
  // Elevator
  public static class ElevatorConstants {
    public static final double kTop = 6;
    public static final double kMid = 3; 
    public static final double kDow = 0.5; 

    public static final double kp = 35;
    public static final double ki = 0;
    public static final double kd = 0.15;
    public static final double ks = 0.2;
    public static final double kv = 0.6;
    public static final double ka = 0;
    public static final double kg = 1.5;
        
    public static final double kp1 = 1;
    public static final double ki1 = 0;
    public static final double kd1 = 0;
    public static final double ks1 = 0;
    public static final double kv1 = 0;
    public static final double ka1 = 0;
    public static final double kg1 = 0;

    public static final double kvelocity = 16;
    public static final double kacc = 80;
  }

  // Bowl
  public static class BowlConstants {
    public static final int kBowlMotor_ID = 19;

    public static final int kDegrees_Zero = 0;
    public static final int kDegrees_Max = kDegrees_Zero + 90;
    public static final int kDegrees_Min = kDegrees_Zero - 20;
    public static final int kDegrees_Pour = kDegrees_Zero + 30;

    public static final double kp = 1;
    public static final double ki = 0;
    public static final double kd = 0;
    public static final double ks = 0;
    public static final double kv = 0;
    public static final double ka = 0;
    public static final double kg = 0;

    public static final double kp1 = 1;
    public static final double ki1 = 0;
    public static final double kd1 = 0;
    public static final double ks1 = 0;
    public static final double kv1 = 0;
    public static final double ka1 = 0;
    public static final double kg1 = 0;

    public static final double kVelocity = 5;
    public static final int kAcc = 10;
    public static final int kJerk = 100;
  }

  // Vision
  public static class VisionID {
    public static final String kCCD_ID= "limelight";
  }
  public static class VisionConstants {
    public static final float kP = 1;
    public static final float kI = 0;
    public static final float kD = 0;
    public static final float kMaxAngSpeed = 5;
    public static final float ktx = 2; 
  }
  
}
