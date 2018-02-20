package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.powerup.Robot;

/**
 *
 */
public class ElevatorAuto extends Command {

    double position; //inches
    double error; //also inches
    public ElevatorAuto(double position, double error) {
      this.position = position;
      this.error = error;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
      requires (Robot.elevator);
      
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      Robot.elevator.holdPosition(position);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(Robot.elevator.getPosition() - position) <= error;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
