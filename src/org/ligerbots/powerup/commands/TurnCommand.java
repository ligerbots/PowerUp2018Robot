package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.ligerbots.powerup.Robot;

/**
 *
 */
public class TurnCommand extends Command {
  
    public double angleOffset;
    public double tolerance;
 

    public TurnCommand(double angleOffset, double tolerance) {
      System.out.println("TurnCommand constructed");

      requires(Robot.driveTrain);
      set(angleOffset, tolerance);
    }
    
    // so we can keep reusing the same command instead of creating and throwing them away
    public void set(double angleOffset, double tolerance) {
        this.angleOffset = angleOffset;
        this.tolerance = tolerance;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      Robot.driveTrain.setPID(SmartDashboard.getNumber("DriveP", 1), SmartDashboard.getNumber("DriveI", 0.01), SmartDashboard.getNumber("DriveD", 0.5));
      Robot.driveTrain.enableTurningControl(angleOffset, tolerance);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      double turnOutput = Robot.driveTrain.getTurnOutput();
      
      Robot.driveTrain.autoTurn(turnOutput);

      if ((Robot.ticks % 5) == 0) {
        System.out.printf("Left to turn %5.2f degrees, Turn output: %5.2f, setPoint %5.2f\n turnError: %5.2f, setPoint: %52.f",
    		  			 turnOutput, Robot.driveTrain.turnError(), Robot.driveTrain.getSetpoint(),
    		  			 Robot.driveTrain.turnError(), Robot.driveTrain.getSetpoint());
      }
      
      SmartDashboard.putNumber("Angle offset", Robot.driveTrain.turnError());
      SmartDashboard.putNumber("Setpoint", Robot.driveTrain.getSetpoint());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.driveTrain.isTurnOnTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	System.out.println("Turn done");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
