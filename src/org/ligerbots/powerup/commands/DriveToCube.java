package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.subsystems.DriveTrain.DriveSide;

/**
 *
 */
public class DriveToCube extends Command {

    double currentLeft;
    double currentRight;
    
    double startLeft;
    double startRight;
    
    double delta;
  
    double turn;
    double drive;
    double driveDist;
    
    double[] empty = new double[] {0.0,0.0,0.0,0.0,0.0,0.0};
    double[] data;
  
    public DriveToCube() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      
      startLeft = Robot.driveTrain.getEncoderDistance(DriveSide.LEFT);
      startRight = Robot.driveTrain.getEncoderDistance(DriveSide.RIGHT);
      
      driveDist = SmartDashboard.getNumberArray("vision/target_info", empty)[3] - 8.0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      
      currentLeft = Robot.driveTrain.getEncoderDistance(DriveSide.LEFT);
      currentRight = Robot.driveTrain.getEncoderDistance(DriveSide.RIGHT);
      
      delta = ((currentRight - startRight) + (currentLeft - startLeft)) / 2;
     
      data = SmartDashboard.getNumberArray("vision/target_info", empty);

      turn = Math.toDegrees(data[4]) * 0.01 + Math.signum(data[4]) * 0.42;
      
      if (Math.abs(Math.toDegrees(data[4])) > 20) {
        drive = 0.0;
      } else {
        drive = Math.signum(driveDist) * 0.7;
      }
      
      Robot.driveTrain.allDrive(drive, turn);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return delta >= driveDist;
    }

    // Called once after isFinished returns true
    protected void end() {
      Robot.driveTrain.allDrive(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
