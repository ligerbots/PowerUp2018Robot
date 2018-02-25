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
    double startTime;
    boolean pidTurn = false;
 

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
      if (pidTurn) {
        Robot.driveTrain.setPID(SmartDashboard.getNumber("DriveP", 1), SmartDashboard.getNumber("DriveI", 0.01), SmartDashboard.getNumber("DriveD", 0.5));
      }
      System.out.printf("Started turn at %5.2f seconds\n", startTime);
    
      Robot.driveTrain.enableTurningControl(angleOffset, tolerance);
      System.out.printf("%d: targetAngle: %5.2f degrees, Turn output: %5.2f, " +  
          "currentAngle : %5.2f,  turnError: %5.2f\n", Robot.ticks,
          Robot.driveTrain.getSetpoint(), Robot.driveTrain.getTurnOutput(), Robot.driveTrain.getYaw(),
          Robot.driveTrain.turnError()
          );      
      startTime = Robot.time();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      double turnOutput = Robot.driveTrain.getTurnOutput();
      
      Robot.driveTrain.autoTurn(turnOutput);

      if ((Robot.ticks % 5) == 0) {
       System.out.printf("%d: targetAngle: %5.2f degrees, Turn output: %5.2f, " +  
        					        "currentAngle : %5.2f,  turnError: %5.2f\n", Robot.ticks,
        					        Robot.driveTrain.getSetpoint(), turnOutput, Robot.driveTrain.getYaw(),
        					        Robot.driveTrain.turnError()
        					        );
      }
      
      SmartDashboard.putNumber("Angle offset", Robot.driveTrain.turnError());
      SmartDashboard.putNumber("Setpoint", Robot.driveTrain.getSetpoint());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        // 2 second timeout for turning
    	boolean timedOut;
    	
    	timedOut = (Robot.time() - startTime) > 2.0;
    	if (timedOut) System.out.println("Turn stopped due to timeout");
    	
    	return Robot.driveTrain.isTurnOnTarget() || timedOut;
    }

    // Called once after isFinished returns true
    protected void end() {
      System.out.printf("%d: targetAngle: %5.2f degrees, Turn output: %5.2f, " +  
        "currentAngle : %5.2f,  turnError: %5.2f\n", Robot.ticks,
        Robot.driveTrain.getSetpoint(), Robot.driveTrain.getTurnOutput(), Robot.driveTrain.getYaw(),
        Robot.driveTrain.turnError()
        );           
    	System.out.println("Turn done");
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
