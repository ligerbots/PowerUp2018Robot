package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.powerup.Robot;

/**
 *
 */
public class ElevatorPreset extends Command {

    int desiredHeight;
    double height;
    
    public ElevatorPreset(int desiredHeight, double height) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
      this.desiredHeight = desiredHeight;
      this.height = height;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      Robot.elevator.setDesiredHeight(desiredHeight);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.elevator.getDesiredHeight() == desiredHeight && Robot.elevator.getPosition() <= height;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
