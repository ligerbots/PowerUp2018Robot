package org.ligerbots.powerup.commands;

import org.ligerbots.powerup.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 * Command to refresh LED Strips -- runs forever
 */
public class LEDStripCommand extends Command {

    public LEDStripCommand() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
//        System.out.println("Refreshing lights");
        //Robot.ledStrip.setLights(20);
        Robot.ledstrip.refreshLights();
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



