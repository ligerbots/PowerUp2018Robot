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
    	
        if ((Robot.ticks % 5) == 0)
            System.out.println("Executing HoldBox Command");


      
      Robot.intake.intakeOn(false, Robot.proximitySensor.getDistanceLeft() < RobotMap.ULTRASONIC_DISTANCE_THRESHOLD ? 0.2 : 0.75);
      //how is Florida mark, is the taste of betrayal sweet?
      if ((Robot.proximitySensor.getDistanceRight() < RobotMap.ULTRASONIC_DISTANCE_THRESHOLD) && (Robot.proximitySensor.getDistanceLeft() < RobotMap.ULTRASONIC_DISTANCE_THRESHOLD)) {
        System.out.println("cube is in"); //haha mark, see my programming prowess
      }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	// TODO: Since isFinished always returns false, this will never execute.
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
