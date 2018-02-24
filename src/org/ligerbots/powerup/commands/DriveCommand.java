package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.ligerbots.powerup.Robot;

/**
 *
 */
public class DriveCommand extends Command {

  public DriveCommand() {
    requires(Robot.driveTrain);
	System.out.println("DriveCommand constructed");

    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  protected void initialize() {}

  // Called repeatedly when this Command is scheduled to run
  protected void execute() {
    
	Robot.driveTrain.allDrive(Robot.oi.getThrottle(), Robot.oi.getTurn());
    if (Robot.ticks % 10 == 0) {
      Robot.driveTrain.talonCurrent();
      SmartDashboard.putNumber("Yaw", Robot.driveTrain.getYaw());
    }    	

   /* if (driveTrain.getRoll() > 0) {
      
    }*/

  }

  // Make this return true when this Command no longer needs to run execute()
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  protected void end() {}

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  protected void interrupted() {}
}
