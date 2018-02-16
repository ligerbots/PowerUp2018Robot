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
      
    //  requires (Robot.driveTrain);
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
      Robot.driveTrain.autoTurn(Robot.driveTrain.getTurnOutput());
      SmartDashboard.putNumber("Angle offset", Robot.driveTrain.turnError());
      SmartDashboard.putNumber("Setpoint", Robot.driveTrain.getSetpoint());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.driveTrain.isTurnOnTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
