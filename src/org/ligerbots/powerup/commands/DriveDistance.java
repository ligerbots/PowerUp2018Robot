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
    double currentLeft;
    double currentRight;
    double currentInches;
    double delta;
    double error;
    boolean onTarget = false;
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
      driveTrain.enableTurningControl(startAngle - driveTrain.getYaw(), angleTolerance);
      
      currentLeft = driveTrain.getEncoderDistance(DriveSide.LEFT);
      currentRight = driveTrain.getEncoderDistance(DriveSide.RIGHT);
      
      delta = ((currentLeft - startingLeft)
          + (currentRight - startingRight)) / 2;
      
      error = Math.abs(delta - offsetInches);
      
      onTarget = error < tolerance;
      
      driveTrain.allDrive(Math.signum(offsetInches), driveTrain.getTurnOutput());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return onTarget;
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
