package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.powerup.Robot;

/**
 *
 */
public class IntakeAuto extends Command {

    boolean reverse;
    double time; //seconds
    double speed;
    
    double startTime;
  
    public IntakeAuto(boolean reverse, double speed, double time) {
      this.reverse = reverse;
      this.speed = speed;
      this.time = time;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      startTime = Robot.time();
      
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      Robot.intake.intakeOn(reverse, speed);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (Robot.time() - startTime) >= time;
    }

    // Called once after isFinished returns true
    protected void end() {
      Robot.intake.intakeOn(false, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
