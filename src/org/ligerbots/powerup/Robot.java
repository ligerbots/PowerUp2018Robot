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
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Arrays;
import org.ligerbots.powerup.commands.AutoCommandGroup;
import org.ligerbots.powerup.commands.DriveCommand;
import org.ligerbots.powerup.commands.DriveDistance;
import org.ligerbots.powerup.commands.DrivePathCommand;
import org.ligerbots.powerup.commands.ElevatorCommand;
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

  Command m_autonomousCommand;
  SendableChooser<Command> m_chooser = new SendableChooser<>();
  public DriverStation.Alliance alliance;	// Red or Blue
  // Game data -- the field configuration
  // L
  
  public String gameData;
  
  public static Intake intake;
  public static DriveTrain driveTrain;
  public static OI oi;
  public static DriveCommand driveCommand;
  public static Elevator elevator;
  public static ElevatorCommand elevatorCommand;
  public static LEDStrip ledstrip;
  public static ProximitySensor proximitySensor;
  
  public enum StartingPosition {
	One("1"),
	Two("2"),
	Three("3"),
	Four("4");
	    
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
	SwitchC("Switch C"),
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
	SwitchC("Switch C"),
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
    elevator = new Elevator();	 // init this before DriveTrain for now
    oi = new OI();
    driveTrain = new DriveTrain();
    driveCommand = new DriveCommand();
    ledstrip = new LEDStrip();
    proximitySensor = new ProximitySensor();
    elevatorCommand = new ElevatorCommand();
    // m_chooser.addDefault("Default Auto", new ExampleCommand());
    // chooser.addObject("My Auto", new MyAutoCommand());
    SmartDashboard.putData("Auto mode", m_chooser);
    
    SmartDashboard.putNumber("DriveP", 0.045);
    SmartDashboard.putNumber("DriveI", 0.004);
    SmartDashboard.putNumber("DriveD", 0.06);
    //CameraServer.getInstance().startAutomaticCapture();
    
    gameData = "";		// zero it here in case of restart
  }

  /**
   * This function is called once each time the robot enters Disabled mode. You can use it to reset
   * any subsystem information you want to clear when the robot is disabled.
   */
  @Override
  public void disabledInit() {

  }

  @Override
  public void disabledPeriodic() {
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

	SmartDashboard.putString("/vision/active_mode", "switch");
    SmartDashboard.putData(new ZeroEncoderCommand());
    //m_autonomousCommand = new TurnCommand(90, 0.3);

    alliance = DriverStation.getInstance().getAlliance();
	// http://wpilib.screenstepslive.com/s/currentCS/m/getting_started/l/826278-2018-game-data-details	
	do gameData = DriverStation.getInstance().getGameSpecificMessage(); 
	while (gameData.length() == 0);

	System.out.println("Game Data: " + gameData);
	SmartDashboard.putString("Game Data", gameData);
    
    m_autonomousCommand = new DrivePathCommand(Arrays.asList(new FieldPosition(10, 0), new FieldPosition(10, 10), new FieldPosition(0,0)));
    
    //AutoCommandGroup auto = new AutoCommandGroup(Arrays.asList(new FieldPosition(10, 0), new FieldPosition(10, 10), new FieldPosition(0,0)), 90.0);

    /*
     * String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
     * switch(autoSelected) { case "My Auto": autonomousCommand = new MyAutoCommand(); break; case
     * 
     * "Default Auto": default: autonomousCommand = new ExampleCommand(); break; }
     */

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
     // m_autonomousCommand.start();
      m_autonomousCommand.start();
    }
    
  }
 
  
  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    driveTrain.printEncoder();
    Scheduler.getInstance().run();
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    
    // Switch cmaera to Driver mode
	SmartDashboard.putString("/vision/active_mode", "driver");
    
//    SmartDashboard.putNumber("DriveP", 1);
//    SmartDashboard.putNumber("DriveI", 0);
//    SmartDashboard.putNumber("DriveD", 0.05);
    driveTrain.configTeleop();
    driveCommand.start();
    if (elevator.isPresent()) elevatorCommand.start();
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    Scheduler.getInstance().run();
    driveTrain.logInversion();

   
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {}
}
