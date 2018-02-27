package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import java.util.List;
import org.ligerbots.powerup.FieldPosition;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.RobotMap;
import org.ligerbots.powerup.RobotPosition;

/**
 *
 */
public class AlternativeDrivePathCommand extends Command {
  
    List<FieldPosition> waypoints;
    FieldPosition currentWaypoint;
    RobotPosition currentPosition;
    int waypointIndex = 0;
    double angleToWaypoint;
    boolean lowerQuadrants;
    double turn;
    boolean turning;
    boolean drivingCheck;
    boolean driving;

    public AlternativeDrivePathCommand(List<FieldPosition> waypoints) {
      this.waypoints = waypoints;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      currentPosition = Robot.driveTrain.getRobotPosition();
      currentWaypoint = waypoints.get(waypointIndex);
      
      angleToWaypoint = Robot.driveTrain.getRobotPosition().angleTo(currentWaypoint);
      
      System.out.println("Angle to waypoint: " + angleToWaypoint);
      
      if (currentWaypoint.getY() - Robot.driveTrain.getRobotPosition().getY() >= 0) {
        lowerQuadrants = false;
      } else {
        lowerQuadrants = true;
      }
     
      if (lowerQuadrants) {
        turn = angleToWaypoint - Robot.driveTrain.getRobotPosition().getDirection();
      } else {
       turn =  Math.signum(angleToWaypoint) * (90 - Math.abs(angleToWaypoint) - Robot.driveTrain.getRobotPosition().getDirection());
      }
      
      System.out.println("Final turn: " + turn);
      
      Robot.driveTrain.enableTurningControl(turn, 0.3);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      
      currentPosition = Robot.driveTrain.getRobotPosition();
      
      turning = !Robot.driveTrain.isTurnOnTarget();
      
      if (turning) {
        Robot.driveTrain.autoTurn(Robot.driveTrain.getTurnOutput());
      }
        
      if (!turning && !driving) {
        driving = true;
        Robot.driveTrain.PIDDrive(currentPosition.distanceTo(currentWaypoint));
      }
      
      if (currentPosition.distanceTo(currentWaypoint) < RobotMap.AUTO_DRIVE_DISTANCE_TOLERANCE && waypointIndex <= (waypoints.size() - 2)) {
        driving = false;
        waypointIndex += 1;
        
        currentWaypoint = waypoints.get(waypointIndex);
        
        angleToWaypoint = Robot.driveTrain.getRobotPosition().angleTo(currentWaypoint);
        
        if (currentWaypoint.getY() - Robot.driveTrain.getRobotPosition().getY() >= 0) {
          lowerQuadrants = false;
        } else {
          lowerQuadrants = true;
        }
       
        if (lowerQuadrants) {
          turn = angleToWaypoint - Robot.driveTrain.getRobotPosition().getDirection();
        } else {
         turn =  Math.signum(angleToWaypoint) * (90 - Math.abs(angleToWaypoint)) - Robot.driveTrain.getRobotPosition().getDirection();
        }
        
        
        Robot.driveTrain.enableTurningControl(turn, 0.3);
      }
      
      
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return waypointIndex == (waypoints.size() - 1);
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
