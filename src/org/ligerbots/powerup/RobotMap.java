/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/

package org.ligerbots.powerup;

import edu.wpi.first.wpilibj.DigitalOutput;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into to a variable name.
 * This provides flexibility changing wiring, makes checking the wiring easier and significantly
 * reduces the number of magic numbers floating around.
 */
public class RobotMap {
  // For example to map the left and right motors, you could define the
  // following variables to use with your drivetrain subsystem.
  // public static int leftMotor = 1;
  // public static int rightMotor = 2;

  // If you are using multiple modules, make sure to define both the port
  // number and the module. For example you with a rangefinder:
  // public static int rangefinderPort = 1;
  // public static int rangefinderModule = 1;

  public static final int CT_LEFT_1 = 4; //Should be 5 on production
  public static final int CT_LEFT_2 = 5;
  public static final int CT_RIGHT_1 = 6; // Should be 3 on production
  public static final int CT_RIGHT_2 = 3;
  public static final int CT_ELEVATOR_1 = 2;
  public static final int CT_ELEVATOR_2 = 1;
  public static final int CT_INTAKE_1 = 7;
  public static final int CT_INTAKE_2 = 8;
  
  public static final int PCM_ID = 9;
  
  public static final double GEARING_FACTOR = 1d;
  public static final double WHEEL_CIRCUMFERENCE = 3.92 * Math.PI;
  public static final int REV_BLINKIN = 1;
  public static final double AUTO_DRIVE_DISTANCE_TOLERANCE = 2;
  
  public static final int ULTRASONIC_LEFT_TRIGGER = 4;
  public static final int ULTRASONIC_LEFT_ECHO = 5;
  public static final int ULTRASONIC_RIGHT_TRIGGER = 6;
  public static final int ULTRASONIC_RIGHT_ECHO = 7;
  public static final double ULTRASONIC_DISTANCE_THRESHOLD = 3.4; //inches suck it mark, i programmed something
  
  public static final int PRESSURE_GAUGE = 0;	// Analog port zero
  public static final int LED_PWM_CHANNEL = 3;
}
