package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.RobotMap;

/**
 *
 */
public class HoldBoxCommand extends Command {

    public HoldBoxCommand() {
      requires (Robot.intake);
      requires (Robot.proximitySensor);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      
      Robot.intake.setSlave(false, Robot.proximitySensor.getDistanceRight() < RobotMap.ULTRASONIC_DISTANCE_THRESHOLD ? 0 : 0.75);
      Robot.intake.intakeOn(false, Robot.proximitySensor.getDistanceLeft() < RobotMap.ULTRASONIC_DISTANCE_THRESHOLD ? 0 : 0.75);
      
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
        Robot.intake.setSlave(true, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
      Robot.intake.setSlave(true, 0);
    }
}
