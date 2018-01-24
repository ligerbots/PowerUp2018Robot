/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. */
/*----------------------------------------------------------------------------*/

package org.ligerbots.powerup;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.ligerbots.powerup.commands.DriveDistance;
import org.ligerbots.powerup.commands.IntakeCommand;

/**
 * This class is the glue that binds the controls on the physical operator interface to the commands
 * and command groups that allow control of the robot.
 */
public class OI {
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

  public OI() {
    xbox = new XboxController(0);
    
    JoystickButton xBoxA = new JoystickButton(xbox, 1);
    xBoxA.whenPressed(new DriveDistance(36, 1, 0.3));
    
    JoystickButton xBoxX = new JoystickButton(xbox, 3);
    xBoxX.whileHeld(new IntakeCommand(false));
  }

  public double getThrottle() {
    System.out.println(-xbox.getY(GenericHID.Hand.kLeft));
    return -xbox.getY(GenericHID.Hand.kLeft);
  }
  

  public double getTurn() {
    return xbox.getX(GenericHID.Hand.kRight);
  }

  public double getElevatorUp() {
    return xbox.getTriggerAxis(GenericHID.Hand.kRight);
  }

  public double getElevatorDown() {
    return xbox.getTriggerAxis(GenericHID.Hand.kLeft);
  }
}
