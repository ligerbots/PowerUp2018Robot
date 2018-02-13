package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.ligerbots.powerup.OI;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.subsystems.Elevator;
import org.ligerbots.powerup.subsystems.Elevator.ElevatorPosition;

/**
 *
 */
public class ElevatorCommand extends Command {

  double position;
  Elevator elevator;
  OI oi;

  public ElevatorCommand() {
    
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    oi = Robot.oi;
    elevator = Robot.elevator;
  }

  // Called just before this Command runs the first time
  protected void initialize() {
    elevator.setPID();
    elevator.zeroEncoder();
    position = 0;
  }

  // Called repeatedly when this Command is scheduled to run
  protected void execute() {
        if (!(Math.abs(oi.getElevatorUp()) <= 0.05 && Math.abs(oi.getElevatorDown()) <= 0.05)) {
          SmartDashboard.putBoolean("holding", false);
          position = elevator.getPosition();
          if (Math.signum(oi.getElevatorUp() - oi.getElevatorDown()) >= 0) {
            if (elevator.getLimitSwitch(true) && !(elevator.getPosition() > 95)) {
              if (elevator.getPosition() >= 90) { 
                elevator.set((oi.getElevatorUp() - oi.getElevatorDown()) * 0.25);
              }
              else {
                elevator.set(oi.getElevatorUp() - oi.getElevatorDown());
                System.out.println("elevatoring");
              }
            }
            else {
              elevator.set(0);
            }
          }
          else {
            if (elevator.getLimitSwitch(false) && !(elevator.getPosition() < 1)) {
              if (elevator.getPosition() <= 6) { 
                elevator.set((oi.getElevatorUp() - oi.getElevatorDown()) * 0.25);
              }
              else {
                elevator.set(oi.getElevatorUp() - oi.getElevatorDown());
              }
            }
            else {
              elevator.set(0);
            }
          }
        }
        else {
          elevator.holdPosition(position);
          SmartDashboard.putBoolean("holding", true);
          SmartDashboard.putNumber("Elevator Position", position);
        }
    }

  // Make this return true when this Command no longer needs to run execute()
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  protected void end() {}

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  protected void interrupted() {}
}
