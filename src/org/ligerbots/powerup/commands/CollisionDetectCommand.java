package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.RobotMap;

/**
 *
 */
public class CollisionDetectCommand extends Command {
  
    double lastAccelX;
    double lastAccelY;
    double currentAccelX;
    double currentAccelY;
    double jerkX;
    double jerkY;

    public CollisionDetectCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      lastAccelX = 0.0;
      lastAccelY = 0.0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      
      currentAccelX = Robot.driveTrain.getLinearAccelX();
      currentAccelY = Robot.driveTrain.getLinearAccelY();
      
      jerkX = currentAccelX - lastAccelX;
      jerkY = currentAccelY- lastAccelY;
      
      if (jerkX > RobotMap.JERK_THRESHOLD || jerkY > RobotMap.JERK_THRESHOLD) Robot.driveTrain.collided = true;
      
      lastAccelX = currentAccelX;
      lastAccelY = currentAccelY;
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
