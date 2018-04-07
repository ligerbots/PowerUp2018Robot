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

  public double position;
  Elevator elevator;
  OI oi;
  private long waitStartTicks = 0;
  private boolean waiting = false;
  double sign;

  public ElevatorCommand() {
    
	requires(Robot.elevator);
    oi = Robot.oi;
    elevator = Robot.elevator;
  }

  // Called just before this Command runs the first time
  protected void initialize() {
    elevator.setPID();
    // TODO: Can't call zeroEncoder here since we don't know where it will be at the start of teleop.
    
    SmartDashboard.putNumber("Elevator Slow", 0.25);
    position = Robot.elevator.getPosition();
  }

  // Called repeatedly when this Command is scheduled to run
  protected void execute() {
        if (!(Math.abs(oi.getElevatorUp()) <= 0.05 && Math.abs(oi.getElevatorDown()) <= 0.05)) {
          SmartDashboard.putBoolean("holding", false);
          waiting = false;
          position = elevator.getPosition();
          if (Math.signum(oi.getElevatorUp() - oi.getElevatorDown()) >= 0) {
            if (!(elevator.getPosition() > 69)) {
              if (elevator.getPosition() >= 66) { 
            	  // TODO: In the following line, the 0.25 should be a parameter in RobotMap and
            	  // settable by the Smart Dashboard.
                elevator.set((oi.getElevatorUp() - oi.getElevatorDown()) * SmartDashboard.getNumber("Elevator Slow", 0.15));
              }
              else {
                elevator.set(oi.getElevatorUp() - oi.getElevatorDown());
              }
            }
            else {
              elevator.set(0);
            }
          }
          else {
        	  // TODO: Need to allow it to go lower. Maybe set the "1" to "0.5" or smaller
            if (!(elevator.getPosition() < 1.5)) {
              if (elevator.getPosition() <= 10) { 
            	  // TODO: In the following line, the 0.25 should be a parameter in RobotMap and
            	  // settable by the Smart Dashboard.
                
                elevator.set((oi.getElevatorUp() - oi.getElevatorDown()) * SmartDashboard.getNumber("Elevator Slow", 0.1));
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
        	/*if (!waiting) {
        		waiting = true;
        		waitStartTicks = Robot.ticks;
        	} else {
        		if (Robot.ticks - waitStartTicks > SmartDashboard.getNumber("Hold Wait Ticks", 0)) {*/
        	        sign = Math.signum(position - Robot.elevator.getPosition());
        	        Robot.elevator.set(0.1 * sign);
                    SmartDashboard.putBoolean("holding", true);
        		//}
        	//}
        }
        SmartDashboard.putNumber("Elevator Position", position);
        SmartDashboard.putNumber("Elevator Actual Pos", elevator.getPosition());
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
