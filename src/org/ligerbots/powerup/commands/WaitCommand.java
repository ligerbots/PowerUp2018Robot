package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.powerup.Robot;

/**
 *
 */
public class WaitCommand extends Command {

    double timeout;
    double startTime;
    public WaitCommand(double timeout) {
      this.timeout = timeout;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      startTime = Robot.time();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.time() - startTime >= timeout;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
