package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.subsystems.Pneumatics;
import org.ligerbots.powerup.subsystems.Pneumatics.CompressorState;

/**
 *
 */
public class CompressorCommand extends Command {
  CompressorState state;
    public CompressorCommand(CompressorState state) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
      this.state = state;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      Robot.pneumatics.setCompressor(state);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return true;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}