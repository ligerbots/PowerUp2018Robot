package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    public DriveDistance(double offsetInches, double tolerance, double angleTolerance) {
     // requires(Robot.driveTrain);
      this.offsetInches = offsetInches;
      this.tolerance = tolerance;
      this.angleTolerance = angleTolerance;
      
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      /*startingLeft = Robot.driveTrain.getEncoderDistance(DriveSide.LEFT);
      startingRight = Robot.driveTrain.getEncoderDistance(DriveSide.RIGHT);
      startAngle = Robot.driveTrain.getAngle();*/
      double p = SmartDashboard.getNumber("DriveP", 1);
      double i = SmartDashboard.getNumber("DriveI", 0);
      double d = SmartDashboard.getNumber("DriveD", 0.05);
      Robot.driveTrain.configClosedLoop(p, i, d);
      Robot.driveTrain.PIDDrive(offsetInches);


    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

      //Robot.driveTrain.enableTurningControl(startAngle - Robot.driveTrain.getYaw(), angleTolerance);
      
      currentLeft = Robot.driveTrain.getEncoderDistance(DriveSide.LEFT);
      currentRight = Robot.driveTrain.getEncoderDistance(DriveSide.RIGHT);
      
      delta = (currentLeft - startingLeft);
      
      error = Math.abs(delta - offsetInches);
      
      
      onTarget = Robot.driveTrain.getClosedLoopError(DriveSide.LEFT) < tolerance && Robot.driveTrain.getClosedLoopError(DriveSide.RIGHT) < tolerance;
      
     // Robot.driveTrain.allDrive(Math.signum(offsetInches), Robot.driveTrain.getTurnOutput());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return onTarget;
    }
    

    // Called once after isFinished returns true
    protected void end() {
        Robot.driveTrain.endClosedLoop();
     //   Robot.driveCommand.start();
     //  Robot.driveTrain.disablePID();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
      Robot.driveTrain.endClosedLoop();
    }
}
