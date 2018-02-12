package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.powerup.Robot;

/**
 *
 */
public class TurnCommand extends Command {
  
    public double angleOffset;
    public double tolerance;

    public TurnCommand(double angleOffset, double tolerance) {
      this.angleOffset = angleOffset;
      this.tolerance = tolerance;
      
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      Robot.driveTrain.enableTurningControl(angleOffset, tolerance);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      Robot.driveTrain.autoTurn(Robot.driveTrain.getTurnOutput());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.driveTrain.isTurnOnTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
