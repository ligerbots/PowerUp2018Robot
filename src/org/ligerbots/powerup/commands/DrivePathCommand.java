package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.List;
import org.ligerbots.powerup.FieldPosition;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.RobotMap;
import org.ligerbots.powerup.subsystems.DriveTrain;
import org.ligerbots.powerup.subsystems.DriveTrain.DriveSide;

/**
 *
 */
public class DrivePathCommand extends Command {

    List<FieldPosition> waypoints;
    FieldPosition currentWaypoint;
    FieldPosition currentPosition;
    double prevLeft;
    double prevRight;
    double leftInches;
    double rightInches;
    double delta;
    double angleError;
    double driveSpeed;
    double angle;
    int waypointIndex = 0;
    boolean turn = false;
    boolean drive = false;
    boolean driving = false;
    
    public DrivePathCommand(List<FieldPosition> waypoints) {
      this.waypoints = waypoints;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      currentPosition = new FieldPosition(0, 0);
      currentWaypoint = waypoints.get(waypointIndex);
      prevLeft = Robot.driveTrain.getEncoderDistance(DriveSide.LEFT);
      prevRight = Robot.driveTrain.getEncoderDistance(DriveSide.RIGHT);
      Robot.driveTrain.enableTurningControl(currentPosition.angleTo(currentWaypoint) , 0.3);

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      
      
      //leftDelta = Robot.driveTrain.getEncoderDistance(DriveSide.LEFT) - leftInches;
      angle = Robot.driveTrain.getAngle();
      leftInches = Robot.driveTrain.getEncoderDistance(DriveSide.LEFT);
      rightInches = Robot.driveTrain.getEncoderDistance(DriveSide.RIGHT);
      delta = ((leftInches - prevLeft) + (rightInches - prevRight)) / 2;
      currentPosition = currentPosition.add(Math.sin(Math.toRadians(angle)) * delta, Math.cos(Math.toRadians(angle)) * delta);
      
      angleError = currentPosition.angleTo(currentWaypoint);
      
      SmartDashboard.putNumber("Delta", delta);
      SmartDashboard.putNumber("Waypoint Index", waypointIndex);
      SmartDashboard.putNumber("Angle Error", angleError);
      SmartDashboard.putNumber("Distance to Waypoint", currentPosition.distanceTo(currentWaypoint));
      SmartDashboard.putNumber("Turn Output", Robot.driveTrain.getTurnOutput());
      if (Math.abs(angleError) > 2.0) {
        drive = false;
        turn = true;
      } else {
        drive = true;
        turn = false;
      }
      
      if (drive && !driving) {
        Robot.driveTrain.PIDDrive(currentPosition.distanceTo(currentWaypoint));
        driving = true;
      }
      
      if (turn) {
        Robot.driveTrain.autoTurn(Math.signum(angleError + 180) / 3);
      }
      
      if (currentPosition.distanceTo(currentWaypoint) < RobotMap.AUTO_DRIVE_DISTANCE_TOLERANCE) {
        turn = false;
        drive = false;
        driving = false;
        waypointIndex += 1;
        currentWaypoint = waypoints.get(waypointIndex);
        Robot.driveTrain.enableTurningControl(currentPosition.angleTo(currentWaypoint) , 1);
      }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return waypointIndex >= (waypoints.size() - 1) && currentPosition.distanceTo(waypoints.get(waypoints.size() - 1)) < RobotMap.AUTO_DRIVE_DISTANCE_TOLERANCE;
    }

    // Called once after isFinished returns true
    protected void end() {
      Robot.driveTrain.disablePID();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
      Robot.driveTrain.disablePID();
    }
}
