package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TurnTester extends Command {
	
	TurnCommand turnCommand;
	double turnAngle;
	double turnTolerance;
	double sign;

    // pass 1.0 for turning right, or -1.0 for turning left
	public TurnTester(double sign) {
    	// angle and tolerance. We actually override these from the Dashboard
    	turnCommand = new TurnCommand(sign*20.0, 1.0);
    	turnAngle = 20.0;
    	turnTolerance = 1.0;
    	this.sign = sign;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	turnAngle = SmartDashboard.getNumber("Turn Angle", turnAngle);
    	turnTolerance = SmartDashboard.getNumber("Turn Tolerance", turnTolerance);
    	System.out.printf("Turn started for %5.1f degrees, %3.1f tolerance.\n", 
    	    sign*turnAngle, turnTolerance);
    	turnCommand.set(sign*turnAngle, turnTolerance);
    	turnCommand.initialize();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	turnCommand.execute();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return turnCommand.isFinished();
    }

    // Called once after isFinished returns true
    protected void end() {
        turnCommand.end();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
        turnCommand.interrupted();    	
    }
}
