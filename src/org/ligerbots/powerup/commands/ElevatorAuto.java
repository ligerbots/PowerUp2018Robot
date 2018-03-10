package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.powerup.Robot;

/**
 *
 */
public class ElevatorAuto extends Command {

    double position; //inches
    double error; //also inches
    double sign;
    boolean go;
    
    public ElevatorAuto(double position, double error) {
      this.position = position;
      this.error = error;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
      requires (Robot.elevator);
      this.go = false;
    }
    
    public ElevatorAuto(double position, double error, boolean go) {
      this.position = position;
      this.error = error;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
      requires (Robot.elevator);
      this.go = go;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      sign = Math.signum(position - Robot.elevator.getPosition());
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      if (Robot.elevator.elevatorGo) go = true;
      if (go) {
        if (Math.abs(position - Robot.elevator.getPosition()) >= 25) Robot.elevator.set(1.0 * sign);
        else if (Math.abs(position - Robot.elevator.getPosition()) >= 15) Robot.elevator.set(0.6 * sign); 
        else if (Math.abs(position - Robot.elevator.getPosition()) >= 10) Robot.elevator.set(0.5 * sign);
        else Robot.elevator.set(0.4 * sign);
      }
     // System.out.println(Robot.elevator.getPosition());


      //Robot.elevator.holdPosition(position);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Math.abs(Robot.elevator.getPosition() - position) <= error;
    }

    // Called once after isFinished returns true
    protected void end() {
      Robot.elevator.holdPosition(Robot.elevator.getPosition());
      Robot.elevator.elevatorGo = false;
      go = false;
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
