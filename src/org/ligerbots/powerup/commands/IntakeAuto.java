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
    
    double otherStart;
    
    double startTime;
    
    double height;
    
    boolean check = false;
  
    public IntakeAuto(boolean reverse, double speed, double time, double height) {
      requires (Robot.intake);
      this.reverse = reverse;
      this.speed = speed;
      this.time = time;
      this.height = height;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      otherStart = Robot.autoStart();
      System.out.println("Intake On");
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      if ((Robot.elevator.getPosition() >= height && !check)) {
        startTime = Robot.time();
        check = true;
      }
      if (Robot.elevator.getPosition() >= height) {
        
        Robot.intake.intakeOn(reverse, speed);
        if (Robot.ticks % 10 == 0) {
          System.out.println("Intake out");
        }
      }
      
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if (!check) return false;
        else return (Robot.time() - startTime) >= time;
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
