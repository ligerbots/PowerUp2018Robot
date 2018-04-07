package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.ligerbots.powerup.Robot;

/**
 *
 */
public class WinchCommand extends Command {

  
    boolean reverse = false;
    
    public WinchCommand(boolean reverse) {
      
      requires (Robot.climber);
      this.reverse = reverse;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      if (Robot.climber.spanishFlag) Robot.climber.set(reverse ? 0.5 : -Math.abs(SmartDashboard.getNumber("Winch Speed", 1.0)));
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
      Robot.climber.set(0.0);
    }
}
