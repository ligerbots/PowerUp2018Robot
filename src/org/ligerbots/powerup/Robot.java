/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/

package org.ligerbots.powerup;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.ligerbots.powerup.commands.DriveCommand;
import org.ligerbots.powerup.commands.ElevatorCommand;
import org.ligerbots.powerup.commands.LEDStripCommand;
import org.ligerbots.powerup.commands.TwoCubeAuto;
import org.ligerbots.powerup.commands.WinchCommand;
import org.ligerbots.powerup.commands.ZeroEncoderCommand;
import org.ligerbots.powerup.subsystems.Climber;
import org.ligerbots.powerup.subsystems.DriveTrain;
import org.ligerbots.powerup.subsystems.Elevator;
import org.ligerbots.powerup.subsystems.Intake;
import org.ligerbots.powerup.subsystems.LEDStrip;
import org.ligerbots.powerup.subsystems.Pneumatics;
import org.ligerbots.powerup.subsystems.ProximitySensor;
import org.ligerbots.powerup.subsystems.Ramps;

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
  public static Pneumatics pneumatics;
  public static Climber climber;
  public static Ramps ramps;
  
  public static InputSnapshot currentSnapshot;
  
  public static double autoStart;
  boolean autoCheck = false;
  
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
	ScaleGamma("Scale Gamma"),
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
	Switch("Switch"),
	Scale("Scale");

	public final String name;
	SecondAction(String name) {
	  this.name = name;
	}
	
	@Override
	public String toString() {
	  return name;
	}
  } 
  
  public enum Priority {
    SWITCH, SCALE
  }
  
  public static class InputSnapshot {
    
    public double[] values;
    public boolean pistons;
    
    public InputSnapshot(double throttle, double rotate, double elevator, double intake, boolean pistons) {
      this.values = new double[] {throttle, rotate, elevator, intake};
      this.pistons = pistons;
    }
    
    @Override 
    public String toString() {
      return String.format("%5.2f,%5.2f,%5.2f,%5.2f,%b", values[0], values[1], values[2], values[3], pistons);
    }
    
    public static InputSnapshot getSnapFromString (String input) {
      String[] pieces = input.split(",");
      return new InputSnapshot(Double.parseDouble(pieces[0]), Double.parseDouble(pieces[1]), 
          Double.parseDouble(pieces[2]), Double.parseDouble(pieces[3]), Boolean.getBoolean(pieces[4]));
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
    driveTrain = new DriveTrain();
    driveCommand = new DriveCommand();
    ledstrip = new LEDStrip();
    ledStripCommand = new LEDStripCommand();
    proximitySensor = new ProximitySensor();
    pneumatics = new Pneumatics();
    climber = new Climber();
    ramps = new Ramps();
    oi = new OI();
    currentSnapshot = new InputSnapshot(0.0, 0.0, 0.0, 0.0, false);
    
    elevatorCommand = new ElevatorCommand();
    
	Robot.driveTrain.zeroYaw();

    // chooser.addDefault("Default Auto", new ExampleCommand());
    // chooser.addObject("My Auto", new MyAutoCommand());
    SmartDashboard.putData("Auto mode", chooser);
    
    SmartDashboard.putNumber("Winch Speed", 1.0);
    SmartDashboard.putData(new WinchCommand(true));
    
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
  public static StartingPosition startPos;
  @Override
  public void autonomousInit() {
	  
    autoStart = Robot.time();
    
	startPos = oi.getStartingPosition();

	SmartDashboard.putString("vision/active_mode", "cube");
    SmartDashboard.putData(new ZeroEncoderCommand());
    //autonomousCommand = new TurnCommand(90, 0.3);
    switch (startPos) {
    	case One: Robot.driveTrain.setPosition(FieldMap.startPositions[1].x, FieldMap.startPositions[1].y); break;
    	case Two: Robot.driveTrain.setPosition(FieldMap.startPositions[2].x, FieldMap.startPositions[2].y); break;
    	case Three: Robot.driveTrain.setPosition(FieldMap.startPositions[3].x, FieldMap.startPositions[3].y); break;
    	case Four: Robot.driveTrain.setPosition(FieldMap.startPositions[4].x, FieldMap.startPositions[4].y); break;
    	case FIVE: Robot.driveTrain.setPosition(FieldMap.startPositions[5].x, FieldMap.startPositions[5].y); break;
    	default: System.out.println("WHAT?!"); break;
    }
    Robot.driveTrain.zeroYaw();

    alliance = DriverStation.getInstance().getAlliance();
	// http://wpilib.screenstepslive.com/s/currentCS/m/getting_started/l/826278-2018-game-data-details	
	do gameData = DriverStation.getInstance().getGameSpecificMessage(); 
	while (gameData.length() == 0);
    
    FirstAction first;
    SecondAction second;
       
    switch (startPos) {
      case One:
       /* if (Robot.gameData.charAt(0) == 'L') {
          if (Robot.gameData.charAt(1) == 'L') {
            first = oi.getPriority() == Priority.SWITCH ? FirstAction.SwitchB : FirstAction.ScaleAlpha; //change back later or something
            second = oi.getPriority() == Priority.SWITCH ? SecondAction.Nothing : SecondAction.Switch;
          }
          else {
            first = FirstAction.SwitchB;
            second = SecondAction.Nothing;
          }
        }
        else {
          if (Robot.gameData.charAt(1) == 'L') {
            first = FirstAction.ScaleAlpha;
            second = SecondAction.Nothing;
          }
          else {
            first = FirstAction.DriveForward;
            second = SecondAction.Nothing;
          }
        }*/
        if (oi.getPriority() == Priority.SCALE || Robot.gameData.charAt(0) == 'R') {
          first = Robot.gameData.charAt(1) == 'L' ? FirstAction.ScaleAlpha : FirstAction.ScaleGamma;
          second = SecondAction.Switch;
        }
        else {
          first = FirstAction.SwitchB;
          second = SecondAction.Nothing;
        }
        break;
      case Two:
        first = FirstAction.Nothing;
        second = SecondAction.Nothing;
        break;
      case Three:
        first = FirstAction.SwitchA;
        second = SecondAction.Nothing;
        break;
      case Four:
        first = FirstAction.Nothing;
        second = SecondAction.Nothing;
        break;
      case FIVE:
       /* if (Robot.gameData.charAt(0) == 'R') {
          if (Robot.gameData.charAt(1) == 'R') {
            first = oi.getPriority() == Priority.SWITCH ? FirstAction.SwitchB : FirstAction.ScaleAlpha; //change later
            second = oi.getPriority() == Priority.SWITCH ? SecondAction.Nothing : SecondAction.Switch;
          }
          else {
            first = FirstAction.SwitchB;
            second = SecondAction.Nothing;
          }
        }
        else {
          if (Robot.gameData.charAt(1) == 'R') {
            first = FirstAction.ScaleAlpha;;
            second = SecondAction.Nothing;
          }
          else {
            first = FirstAction.DriveForward;
            second = SecondAction.Nothing;
          }
        }*/
        if (oi.getPriority() == Priority.SCALE || Robot.gameData.charAt(0) == 'L') {
          first = Robot.gameData.charAt(1) == 'R' ? FirstAction.ScaleAlpha : FirstAction.ScaleGamma;
          second =  SecondAction.Switch;
        }
        else {
          first = FirstAction.SwitchB;
          second = SecondAction.Nothing;
        }
        break;
      default:
        first = FirstAction.DriveForward;
        second = SecondAction.Nothing;
        break;
    }
      
	System.out.println("Game Data: " + gameData);
	SmartDashboard.putString("Game Data", gameData);

	auto = new TwoCubeAuto(first, second);
    
    if (auto != null) {
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
   /* if (Robot.time() - autoStart >= 11.0 && !autoCheck) {
      Scheduler.getInstance().removeAll();
      IntakeAuto temp = new IntakeAuto(true, 0.7, 1.5, 0.0);
      temp.start();
      autoCheck = true;
    }*/
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
    
    // Switch camera to Driver mode
	SmartDashboard.putString("vision/active_mode", "driver");
    SmartDashboard.putNumber("LEDStripKey", 0.0);

    driveCommand.start();
    ledStripCommand.start();
    elevatorCommand.start();
    
  }
  

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    commonPeriodic();  
    Scheduler.getInstance().run();

    if ((ticks % 20) == 0) {
      SmartDashboard.putNumber("UltrasonicLeft", proximitySensor.getDistanceLeft());
      SmartDashboard.putNumber("UltrasonicRight", proximitySensor.getDistanceRight());
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
      if ((ticks % 4) == 0) {
      }
	  ticks ++;
  }
  
  public static double autoStart() {
    return autoStart;
  }
  public static InputSnapshot getCurrentSnapshot() {
    currentSnapshot.values = new double[] {oi.getThrottle(), oi.getTurn(), elevator.finalElevatorSpeed, oi.isIntakeOn() ? intake.finalIntakeSpeed : 0.0};
    currentSnapshot.pistons = intake.open;
    return currentSnapshot;
  }
}
