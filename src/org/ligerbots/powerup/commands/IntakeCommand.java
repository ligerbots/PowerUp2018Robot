package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.RobotMap;

/**
 *
 */
public class IntakeCommand extends Command {

    boolean reverse;
    double speed;
    boolean boxIn;
    
    public IntakeCommand(boolean reverse) {
      requires (Robot.intake);
      this.reverse = reverse;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      speed = SmartDashboard.getNumber("Intake Speed", 0.5);
      Robot.intake.intakeOn(reverse, speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return reverse ? false : Robot.proximitySensor.getDistanceLeft() < RobotMap.ULTRASONIC_DISTANCE_THRESHOLD && 
            Robot.proximitySensor.getDistanceRight() < RobotMap.ULTRASONIC_DISTANCE_THRESHOLD;
    }

    // Called once after isFinished returns true
    protected void end() {
      Robot.intake.intakeOn(false, 0);
      if (!reverse) {
        HoldBoxCommand holdBoxCommand = new HoldBoxCommand();
        holdBoxCommand.start();
      }
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
      Robot.intake.intakeOn(false, 0);
      if (!reverse) {
        HoldBoxCommand holdBoxCommand = new HoldBoxCommand();
        holdBoxCommand.start();
      }

    }
}
