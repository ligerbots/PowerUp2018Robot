package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.subsystems.DriveTrain;
import org.ligerbots.powerup.subsystems.DriveTrain.DriveSide;

/**
 *
 */
public class DriveDistance extends Command {

    double offsetInches;
    double startingLeft;
    double startingRight;
    double tolerance;
    double startAngle;
    double angleTolerance;
    double currentAngle;
    DriveTrain driveTrain;
    public DriveDistance(double offsetInches, double tolerance, double angleTolerance) {
      driveTrain = Robot.driveTrain;
      this.offsetInches = offsetInches;
      this.tolerance = tolerance;
      this.angleTolerance = angleTolerance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      startingLeft = driveTrain.getEncoderDistance(DriveSide.LEFT);
      startingRight = driveTrain.getEncoderDistance(DriveSide.RIGHT);
      startAngle = driveTrain.getAngle();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      currentAngle = driveTrain.getAngle();
      
      
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
      driveTrain.disablePID();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
