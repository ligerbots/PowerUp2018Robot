package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.powerup.Robot;

/**
 *
 */
public class ElevatorAuto extends Command {

    double position; //inches
    double sign;
    
    public ElevatorAuto() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
      requires (Robot.elevator);
    }
    // Called just before this Command runs the first time
    protected void initialize() {
      sign = Math.signum(position - Robot.elevator.getPosition());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      
      
      position = Robot.elevator.getDesiredHeight();
      sign = Math.signum(position - Robot.elevator.getPosition());
      if (Math.abs(position - Robot.elevator.getPosition()) >= 25) Robot.elevator.set(1.0 * sign);
      else if (Math.abs(position - Robot.elevator.getPosition()) >= 15) Robot.elevator.set(0.8 * sign); 
      else if (Math.abs(position - Robot.elevator.getPosition()) >= 10) Robot.elevator.set(0.6 * sign);
      else if (Math.abs(position - Robot.elevator.getPosition()) >= 1) Robot.elevator.set(0.5 * sign);
      else Robot.elevator.set(0.1 * sign);
     // System.out.println(Robot.elevator.getPosition());


      //Robot.elevator.holdPosition(position);
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
