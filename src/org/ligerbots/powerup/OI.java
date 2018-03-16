/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/

package org.ligerbots.powerup;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Arrays;
import org.ligerbots.powerup.commands.CameraSelectionCommand;
import org.ligerbots.powerup.commands.CameraToggleCommand;
import org.ligerbots.powerup.commands.CompressorCommand;
import org.ligerbots.powerup.commands.DriveDistance;
import org.ligerbots.powerup.commands.DrivePathCommand;
import org.ligerbots.powerup.commands.ElevatorPreset;
import org.ligerbots.powerup.commands.IntakeCommand;
import org.ligerbots.powerup.commands.IntakePistonCommand;
import org.ligerbots.powerup.commands.LEDStripCommand;
import org.ligerbots.powerup.commands.TurnCommand;
//import org.ligerbots.powerup.commands.TurnTester;
import org.ligerbots.powerup.commands.TurnTester;
import org.ligerbots.powerup.subsystems.Pneumatics.CompressorState;
//import org.ligerbots.powerup.commands.LEDStripCommand;
import org.ligerbots.powerup.triggers.JoystickPov;
import org.ligerbots.powerup.triggers.JoystickPov.Direction;


/**
 * This class is the glue that binds the controls on the physical operator interface to the commands
 * and command groups that allow control of the robot.
 */
public class OI {
	
  SendableChooser<Robot.StartingPosition> startingPosition;
  SendableChooser<Robot.FirstAction> firstAction;
  SendableChooser<Robot.SecondAction> secondAction;
  
	
  
  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  // Joystick stick = new Joystick(port);
  // Button button = new JoystickButton(stick, buttonNumber);

  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenPressed(new ExampleCommand());

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());
  XboxController xbox;
  Joystick farm;
  

  
  
  

  public OI() {
    
    
    startingPosition = new SendableChooser<>();
    populateSelect(startingPosition, Robot.StartingPosition.class);
    SmartDashboard.putData("StartingPosition", startingPosition);

    firstAction = new SendableChooser<>();
    populateSelect(firstAction, Robot.FirstAction.class);
    SmartDashboard.putData("FirstAction", firstAction);

    secondAction = new SendableChooser<>();
    populateSelect(secondAction, Robot.SecondAction.class);
    SmartDashboard.putData("SecondAction", secondAction);
	    
    xbox = new XboxController(0);
    farm = new Joystick(1);
        
    JoystickButton xBoxA = new JoystickButton(xbox, 1);
    xBoxA.whenPressed(new TurnCommand(90.0, 0.3));
    
    JoystickButton xBoxB = new JoystickButton(xbox, 2);
    xBoxB.whenPressed(new DrivePathCommand(Arrays.asList(new FieldPosition(0, 10), new FieldPosition(10, 10), new FieldPosition(0,0))));
    
    JoystickButton xBoxBumperRight = new JoystickButton(xbox, 6);
    xBoxBumperRight.whileHeld(new IntakeCommand(false));
    
    JoystickButton xBoxBumperLeft = new JoystickButton(xbox, 5);
    xBoxBumperLeft.whileHeld(new IntakeCommand(true));
    
    JoystickButton xBoxRightJoystick = new JoystickButton(xbox, 10);
    xBoxRightJoystick.whenPressed(new IntakePistonCommand(true));
    
    JoystickButton xBoxLeftJoystick = new JoystickButton(xbox, 9);
    xBoxLeftJoystick.whenPressed(new IntakePistonCommand(false));
    
    JoystickButton xBoxSelect = new JoystickButton(xbox, 7);
    xBoxSelect.whenPressed(new CompressorCommand(CompressorState.TOGGLE));
    
    JoystickButton xBoxX = new JoystickButton(xbox, 3);
    xBoxX.whenPressed(new LEDStripCommand());
    
    JoystickPov povTriggerRight = new JoystickPov(xbox, Direction.EAST);
    povTriggerRight.whenPressed(new TurnTester(1.0));
    
    JoystickPov povTriggerLeft = new JoystickPov(xbox, Direction.WEST);
    povTriggerLeft.whenPressed(new TurnTester(-1.0));
    
    JoystickPov povTriggerTop = new JoystickPov(xbox, Direction.NORTH);
    povTriggerTop.whenPressed(new DriveDistance(10.0, 0.5, 1.0));

    JoystickPov povTriggerDown = new JoystickPov(xbox, Direction.SOUTH);
    povTriggerDown.whenPressed(new CameraToggleCommand());

    
    JoystickButton farmOne = new JoystickButton(farm, 1);
    
    farmOne.whenPressed(new CameraSelectionCommand("driver"));
    JoystickButton farmTwo = new JoystickButton(farm,2);
    
    farmTwo.whenPressed(new CameraSelectionCommand("intake"));
    
    JoystickButton farmThree = new JoystickButton(farm, 6);
    
    farmThree.whenPressed(new CameraSelectionCommand("switch"));
    JoystickButton farmFour = new JoystickButton(farm, 7);
    
    farmFour.whenPressed(new CameraSelectionCommand("cube"));
    
    JoystickButton farmTwentyTwo = new JoystickButton(farm, 22);
    farmTwentyTwo.whenPressed(new ElevatorPreset(FieldMap.scaleScoringHeight));
    
    JoystickButton farmTwentyThree = new JoystickButton(farm, 23);
    farmTwentyThree.whenPressed(new ElevatorPreset(FieldMap.switchScoringHeight));
    
    JoystickButton farmTwentyFour = new JoystickButton(farm, 24);
    farmTwentyFour.whenPressed(new ElevatorPreset(1.5));
    
    System.out.println("OI constructed");

    
    // TODO: Add a button to switch the camera mode
    // options are ""switch", "cube", and "driver"
    // switch is for start of autonomous
    // cube is also for autonomous after we get past the switch target
    // cube can also be used to view when the elevator is raised
    // "driver" is the camera mounted on the elevator support.
    // Allows driver to watch cube being loaded

  }

  private <T extends Enum<?>> void populateSelect(SendableChooser<T> chooser, Class<T> options) {
    boolean first = true;
    for (T value : options.getEnumConstants()) {
      if (first) {
        first = false;
        chooser.addDefault(value.toString(), value);
      } 
      else {
        chooser.addObject(value.toString(), value);
      }
    }
  }
  

  public double getThrottle() {
    return -xbox.getY(GenericHID.Hand.kLeft);
  }
  

  public double getTurn() {
    return xbox.getX(GenericHID.Hand.kRight);
  }

  public double getElevatorUp() {
    return xbox.getTriggerAxis(GenericHID.Hand.kRight);
  }
  
  public boolean getDown() {
    return xbox.getStartButton();
  }

  public double getElevatorDown() {
    return xbox.getTriggerAxis(GenericHID.Hand.kLeft);
  }
  
  public Robot.StartingPosition getStartingPosition() {
    return startingPosition.getSelected();
  }
  
  public Robot.FirstAction getFirstAction() {
    return firstAction.getSelected();
  }
  
  public Robot.SecondAction getSecondAction() {
    return secondAction.getSelected();
  }
}
