package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class CameraToggleCommand extends Command {

    public CameraToggleCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	switch (SmartDashboard.getString("vision/active_mode", "driver")) {
    	case "driver":
        	SmartDashboard.putString("vision/active_mode", "intake");
        	break;
    	case "intake":
        	SmartDashboard.putString("vision/active_mode", "cube");
        	break;
    	case "cube":
        	SmartDashboard.putString("vision/active_mode", "switch");
        	break;
    	case "switch":
        	SmartDashboard.putString("vision/active_mode", "driver");
        	break;
        default:
        	SmartDashboard.putString("vision/active_mode", "driver");
        	break;
    	}
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
