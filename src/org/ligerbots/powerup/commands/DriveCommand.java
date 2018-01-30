package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.ligerbots.powerup.OI;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.subsystems.DriveTrain;

/**
 *
 */
public class DriveCommand extends Command {

  OI oi;
  DriveTrain driveTrain;
  int i = 0;

  public DriveCommand() {
    requires(Robot.driveTrain);
    driveTrain = Robot.driveTrain;
    oi = Robot.oi;
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  protected void initialize() {}

  // Called repeatedly when this Command is scheduled to run
  protected void execute() {
    
    if (i % 10 == 0) {
      driveTrain.printEncoder();
    }
    driveTrain.allDrive(oi.getThrottle(), oi.getTurn());
    i+=1;

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
