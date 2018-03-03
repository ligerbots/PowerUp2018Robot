package org.ligerbots.powerup.commands;

import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.subsystems.DriveTrain.DriveSide;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class BadDriveDistance extends Command {

	double startingRight;
	double currentRight;
	double startingLeft;
	double currentLeft;
	double distance; //inches
	double delta;
	
    public BadDriveDistance(double distance) {
    	this.distance = distance;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        startingLeft = Robot.driveTrain.getEncoderDistance(DriveSide.LEFT);
        startingRight = Robot.driveTrain.getEncoderDistance(DriveSide.RIGHT);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    	currentLeft = Robot.driveTrain.getEncoderDistance(DriveSide.LEFT);
        currentRight = Robot.driveTrain.getEncoderDistance(DriveSide.RIGHT);
        
        delta = ((currentRight - startingRight) + (currentLeft - startingLeft)) / 2;
        
        SmartDashboard.putNumber("Delta for bad", delta);
        
    	Robot.driveTrain.allDrive(0.45, 0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return delta >= distance;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
