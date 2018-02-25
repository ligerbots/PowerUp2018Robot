package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.List;
import org.ligerbots.powerup.FieldPosition;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.RobotMap;
import org.ligerbots.powerup.RobotPosition;
import org.ligerbots.powerup.subsystems.DriveTrain;
import org.ligerbots.powerup.subsystems.DriveTrain.DriveSide;

/**
 *
 */
public class DrivePathCommand extends Command {

    List<FieldPosition> waypoints;
    FieldPosition currentWaypoint;
    RobotPosition currentPosition;
    double prevLeft;
    double prevRight;
    double leftInches;
    double rightInches;
    double delta;
    double angleError;
    double angleToWaypoint;
    double driveSpeed;
    double angle;
    int waypointIndex = 0;
    boolean turn = false;
    boolean drive = false;
    boolean driving = false;
    TurnCommand turnCommand;
    
    public DrivePathCommand(List<FieldPosition> waypoints) {
      this.waypoints = waypoints;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      currentPosition = Robot.driveTrain.getRobotPosition();
      currentWaypoint = waypoints.get(waypointIndex);
      //  prevLeft = Robot.driveTrain.getEncoderDistance(DriveSide.LEFT);
      //  prevRight = Robot.driveTrain.getEncoderDistance(DriveSide.RIGHT);
      angleToWaypoint = 90 - currentPosition.angleTo(currentWaypoint);
      angleError = (angleToWaypoint - currentPosition.getDirection() + 360) % 360;
      Robot.driveTrain.enableTurningControl(angleError, 0.3);

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      
	  double distanceToWaypoint;
      /*currentPosition.add(Math.sin(Math.toRadians(angle)) * delta, Math.cos(Math.toRadians(angle)) * delta)*/
      currentPosition = Robot.driveTrain.getRobotPosition();
      distanceToWaypoint = currentPosition.distanceTo(currentWaypoint);
      
      SmartDashboard.putNumber("Current Position X", currentPosition.getX());
      SmartDashboard.putNumber("Current Position Y", currentPosition.getY());
      
      SmartDashboard.putNumber("Waypoint Index", waypointIndex);
      SmartDashboard.putNumber("Angle Error", angleError);
      SmartDashboard.putNumber("Distance to Waypoint", distanceToWaypoint);
      SmartDashboard.putNumber("Turn Output", Robot.driveTrain.getTurnOutput());
      SmartDashboard.putNumber("Yaw", Robot.driveTrain.getYaw());
      
      angleToWaypoint = 90 - currentPosition.angleTo(currentWaypoint);
      angleError = (angleToWaypoint - currentPosition.getDirection() + 360) % 360;
      if (angleError > 180) {
        angleError -= 360;
      }
     
      if (Math.abs(angleError) > 4.0 && !driving) {
        System.out.println("Angle Error: " + Math.abs(angleError) + "    Turn: " + turn);
        drive = false;
        turn = true;
      } else {
        drive = true;
        turn = false;
      }
      
      if (!turn && !driving) {
    	System.out.print("Starting drive " + currentPosition.toString());
    	System.out.println(" to " + -distanceToWaypoint);
    	
        Robot.driveTrain.PIDDrive(-distanceToWaypoint);
        driving = true;
      }
      
      if (turn) {
        Robot.driveTrain.autoTurn(Robot.driveTrain.getTurnOutput());
      }
      
      if (distanceToWaypoint < RobotMap.AUTO_DRIVE_DISTANCE_TOLERANCE) {
        turn = false;
        drive = false;
        driving = false;
        waypointIndex += 1;
        currentWaypoint = waypoints.get(waypointIndex);
        angleToWaypoint = 90 - currentPosition.angleTo(currentWaypoint);
        angleError = (angleToWaypoint - currentPosition.getDirection() + 360) % 360;
        Robot.driveTrain.enableTurningControl(angleError, 1);
        System.out.println("New Waypoint");
      }
      
      prevLeft = leftInches;
      prevRight = rightInches;
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
