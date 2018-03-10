/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/

package org.ligerbots.powerup;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Arrays;
import org.ligerbots.powerup.commands.AutoCommandGroup;
import org.ligerbots.powerup.commands.DriveCommand;
import org.ligerbots.powerup.commands.DriveDistance;
import org.ligerbots.powerup.commands.DrivePathCommand;
import org.ligerbots.powerup.commands.ElevatorAuto;
import org.ligerbots.powerup.commands.ElevatorCommand;
import org.ligerbots.powerup.commands.LEDStripCommand;
import org.ligerbots.powerup.commands.SimpleSwitchAuto;
import org.ligerbots.powerup.commands.TurnCommand;
import org.ligerbots.powerup.commands.ZeroEncoderCommand;
import org.ligerbots.powerup.subsystems.DriveTrain;
import org.ligerbots.powerup.subsystems.Elevator;
import org.ligerbots.powerup.subsystems.Intake;
import org.ligerbots.powerup.subsystems.LEDStrip;
import org.ligerbots.powerup.subsystems.ProximitySensor;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {

  Command autonomousCommand;
  CommandGroup auto;
  SendableChooser<Command> chooser = new SendableChooser<>();
  public DriverStation.Alliance alliance;	// Red or Blue
  // Game data -- the field configuration
  // L
  
  public static long ticks = 0;
  public static String gameData;
  
  public static Intake intake;
  public static DriveTrain driveTrain;
  public static OI oi;
  public static DriveCommand driveCommand;
  public static Elevator elevator;
  public static ElevatorCommand elevatorCommand;
  public static LEDStrip ledstrip;
  public static LEDStripCommand ledStripCommand;
  public static ProximitySensor proximitySensor;
  public static FieldMap fieldMap;
  
  public enum StartingPosition {
	One("1"),
	Two("2"),
	Three("3"),
	Four("4"),
    FIVE("5");
	    
	public final String name;
	StartingPosition(String name) {
	  this.name = name;
	}
	
	@Override
	public String toString() {
	  return name;
	}
  }
  
  public enum FirstAction {
    DriveForward("Drive Forward"),
	SwitchA("Switch A"),
	SwitchB("Switch B"),
	ScaleAlpha("Scale Alpha"),
	ScaleBeta("Scale Beta"),
	Nothing("Do Nothing");
	    
	public final String name;
	FirstAction(String name) {
	  this.name = name;
	}
	
	@Override
	public String toString() {
	  return name;
	}
  }  
  
  public enum SecondAction {
    Nothing("Do Nothing"),
	SwitchA("Switch A"),
	SwitchB("Switch B"),
	ScaleAlpha("Scale Alpha"),
	ScaleBeta("Scale Beta");

	public final String name;
	SecondAction(String name) {
	  this.name = name;
	}
	
	@Override
	public String toString() {
	  return name;
	}
  }  

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    
    intake = new Intake();
    elevator = new Elevator();
    fieldMap = new FieldMap();
// init this before DriveTrain for now
    driveTrain = new DriveTrain();
    driveCommand = new DriveCommand();
    ledstrip = new LEDStrip();
    ledStripCommand = new LEDStripCommand();
    proximitySensor = new ProximitySensor();
    // Put this after all commands and subsystems have been initialized!
    oi = new OI();
    
    elevatorCommand = new ElevatorCommand();
    
    // zero everything before we start moving
	Robot.driveTrain.zeroYaw();

    // chooser.addDefault("Default Auto", new ExampleCommand());
    // chooser.addObject("My Auto", new MyAutoCommand());
    SmartDashboard.putData("Auto mode", chooser);
    
    SmartDashboard.putNumber("DriveP", 0.045);
    SmartDashboard.putNumber("DriveI", 0.004);
    SmartDashboard.putNumber("DriveD", 0.06);
    
    
    SmartDashboard.putNumber("Drive P", 0.1);
    SmartDashboard.putNumber("Drive I", 0.0001);
    SmartDashboard.putNumber("Drive D", 0.05);
    
    // For TurnTester command
    SmartDashboard.putNumber("Turn Angle", 20.0);
    SmartDashboard.putNumber("Turn Tolerance", 1.0);
    
    SmartDashboard.putNumber("Elevator P", 0.1);
    SmartDashboard.putNumber("Elevator I", 0.001);
    SmartDashboard.putNumber("Elevator D", 0.05);
    
    SmartDashboard.setPersistent("Elevator P");
    SmartDashboard.setPersistent("Elevator I");
    SmartDashboard.setPersistent("Elevator D");
    
    //CameraServer.getInstance().startAutomaticCapture();
    
    gameData = "";		// zero it here in case of restart
  }

  public static double time() {
	  return (double)System.nanoTime() / 1.0E9;
  }
  
  /**
   * This function is called once each time the robot enters Disabled mode. You can use it to reset
   * any subsystem information you want to clear when the robot is disabled.
   */
  @Override
  public void disabledInit() {

    // Switch cmaera to Switch mode
    SmartDashboard.putString("vision/active_mode", "cube");
   
  }

  @Override
  public void disabledPeriodic() {
    commonPeriodic();  
    Scheduler.getInstance().run();
  }

  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString code to get the auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional commands to the chooser code above (like
   * the commented example) or additional comparisons to the switch structure below with additional
   * strings & commands.
   */
  @Override
  public void autonomousInit() {

	SmartDashboard.putString("vision/active_mode", "cube");
    SmartDashboard.putData(new ZeroEncoderCommand());
    //autonomousCommand = new TurnCommand(90, 0.3);
    Robot.driveTrain.setPosition(fieldMap.startPositions[3].x, fieldMap.startPositions[3].y);
    Robot.driveTrain.zeroYaw();

    alliance = DriverStation.getInstance().getAlliance();
	// http://wpilib.screenstepslive.com/s/currentCS/m/getting_started/l/826278-2018-game-data-details	
	//do gameData = DriverStation.getInstance().getGameSpecificMessage(); 
	//while (gameData.length() == 0);
    
    gameData = "LLL";
      
	System.out.println("Game Data: " + gameData);
	SmartDashboard.putString("Game Data", gameData);
	
	
	auto = new SimpleSwitchAuto(FirstAction.SwitchA, SecondAction.Nothing);
    
	
	
    
    //AutoCommandGroup auto = new AutoCommandGroup(Arrays.asList(new FieldPosition(10, 0), new FieldPosition(10, 10), new FieldPosition(0,0)), 90.0);

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
     * switch(autoSelected) { case "My Auto": autonomousCommand = new MyAutoCommand(); break; case
     * 
     * "Default Auto": default: autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (auto != null) {
     // autonomousCommand.start();
      auto.start();
    }
    
  }
 
  
  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    commonPeriodic();  
    driveTrain.printEncoder();
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    elevator.setPID();
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (auto != null) {
      auto.cancel();
    }
    
    // Switch cmaera to Driver mode
	SmartDashboard.putString("vision/active_mode", "driver");
    SmartDashboard.putNumber("LEDStripKey", 0.0);
    //    SmartDashboard.putNumber("DriveP", 1);
    //    SmartDashboard.putNumber("DriveI", 0);
    //    SmartDashboard.putNumber("DriveD", 0.05);
    driveCommand.start();
    ledStripCommand.start();

  }
  

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    commonPeriodic();  
    Scheduler.getInstance().run();
    if ((ticks % 100) ==0) {
//      driveTrain.logInversion();
      SmartDashboard.putNumber("UltrasonicLeft", Robot.proximitySensor.getDistanceLeft());
      SmartDashboard.putNumber("UltrasonicRight", Robot.proximitySensor.getDistanceRight());
//      System.out.println("UltrasonicLeft = " + Robot.proximitySensor.getDistanceLeft());
//      System.out.println("UltrasonicRight = " + Robot.proximitySensor.getDistanceRight());
    }
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
	  commonPeriodic();
  }
  
  public void commonPeriodic() {
      SmartDashboard.putNumber("RobotX", Robot.driveTrain.getRobotPosition().getX());
      SmartDashboard.putNumber("RobotY", Robot.driveTrain.getRobotPosition().getY());
	  ticks ++;
  }
}
