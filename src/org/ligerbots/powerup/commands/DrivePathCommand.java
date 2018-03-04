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
    int waypointIndex = 0;
    
    FieldPosition currentWaypoint;
    RobotPosition currentPosition;
    
    boolean lowerQuadrants;
    double angleError;
    double angleToWaypoint;
    
    double startAbsDistance;
    
    double turn;
    double drive;
    
    double rampUpDist;
    double rampDownDist;
    double rampUpDelta;
    double rampDownDelta;
    
    public DrivePathCommand(List<FieldPosition> waypoints) {
      this.waypoints = waypoints;
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      
      startAbsDistance = Robot.driveTrain.getAbsoluteDistanceTraveled();
      
      rampUpDist = 24.0;
      rampDownDist = 60.0;
      
      currentPosition = Robot.driveTrain.getRobotPosition();
      currentWaypoint = waypoints.get(waypointIndex);

      angleToWaypoint = Robot.driveTrain.getRobotPosition().angleTo(currentWaypoint);
      
      if (currentWaypoint.getY() - Robot.driveTrain.getRobotPosition().getY() >= 0) {
        lowerQuadrants = false;
      } else {
        lowerQuadrants = true;
      }
      
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
     
      if (lowerQuadrants) {
        angleError = angleToWaypoint - Robot.driveTrain.getRobotPosition().getDirection();
      } else {
       angleError = Math.signum(angleToWaypoint) * (90 - Math.abs(angleToWaypoint)) - Robot.driveTrain.getRobotPosition().getDirection();
      }
      
      turn = angleError * 0.01 + Math.signum(angleError) * 0.4;
      
      double rampUpDelta = Robot.driveTrain.getAbsoluteDistanceTraveled() - startAbsDistance;
      double rampDownDelta = currentPosition.distanceTo(waypoints.get(waypoints.size() - 1));
      
      if (Math.abs(angleError) >= 30) {
        drive = 0.0;
      } else {
        if (rampDownDelta < rampDownDist) {
          drive = (rampDownDelta * (0.5 - 0.1) / rampDownDist)
              + 0.35;
        } else if (rampUpDelta < rampUpDist) {
          drive = (Math.abs(rampUpDelta) * (0.5 - 0.1) / rampUpDist) + 0.4;
        }

      }
      
      Robot.driveTrain.allDrive(drive, turn);
      
      SmartDashboard.putNumber("Drive Speed", drive);
      SmartDashboard.putNumber("Turn Speed", turn);
      SmartDashboard.putNumber("Angle Error", angleError);
      SmartDashboard.putNumber("Waypoint Index", waypointIndex);

      
      if (currentPosition.distanceTo(currentWaypoint) < RobotMap.AUTO_DRIVE_DISTANCE_TOLERANCE && waypointIndex <= (waypoints.size() - 2)) {
        Robot.driveTrain.allDrive(0, 0);
        
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
      }
      
      if (waypointIndex == waypoints.size() - 1) {
        Robot.elevator.elevatorGo = true;  
      }
	  
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return waypointIndex >= (waypoints.size() - 1) && currentPosition.distanceTo(waypoints.get(waypoints.size() - 1)) < RobotMap.AUTO_DRIVE_DISTANCE_TOLERANCE;
    }

    // Called once after isFinished returns true
    protected void end() {
      Robot.driveTrain.disablePID();
      System.out.println("Finished");
    }
    
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
      Robot.driveTrain.disablePID();
    }
}
