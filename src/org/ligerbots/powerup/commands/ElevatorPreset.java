package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.powerup.Robot;

/**
 *
 */
public class ElevatorPreset extends Command {

    double position; //inches
    double sign;
  
    public ElevatorPreset(double position) {
      requires(Robot.elevator);
      this.position = position;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {

    }
    

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      sign = Math.signum(position - Robot.elevator.getPosition());
      if (Math.abs(position - Robot.elevator.getPosition()) >= 25) Robot.elevator.set(1.0 * sign);
      else if (Math.abs(position - Robot.elevator.getPosition()) >= 15) Robot.elevator.set(0.8 * sign); 
      else if (Math.abs(position - Robot.elevator.getPosition()) >= 10) Robot.elevator.set(0.6 * sign);
      else if (Math.abs(position - Robot.elevator.getPosition()) >= 1) Robot.elevator.set(0.5 * sign);
      else Robot.elevator.set(0.1 * sign);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(position - Robot.elevator.getPosition()) <= 1 || (Robot.oi.getElevatorUp() + Robot.oi.getElevatorDown() != 0);
    }

    // Called once after isFinished returns true
    protected void end() {
      Robot.elevatorCommand.start();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
