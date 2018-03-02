package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.List;
import org.ligerbots.powerup.FieldPosition;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.RobotMap;
import org.ligerbots.powerup.RobotPosition;
import org.ligerbots.powerup.subsystems.DriveTrain.DriveSide;

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
    boolean drivingCheck = false;
    boolean driving;
    
    double startDist;
    
    double saveX;
    double saveY;
    
    
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
       turn =  Math.signum(angleToWaypoint) * (90 - Math.abs(angleToWaypoint)) - Robot.driveTrain.getRobotPosition().getDirection();
      }
      
      System.out.println("Final turn: " + turn);
      
      Robot.driveTrain.enableTurningControl(turn, 0.3);
      
      SmartDashboard.putNumber("WaypointX", currentWaypoint.getX());
      SmartDashboard.putNumber("WaypointY", currentWaypoint.getY());
      SmartDashboard.putNumber("Final Turn", turn);
      SmartDashboard.putNumber("Turn Output", Robot.driveTrain.getTurnOutput());

      saveX = Robot.driveTrain.getRobotPosition().getX();
      saveY = Robot.driveTrain.getRobotPosition().getY();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      
      
      currentPosition = Robot.driveTrain.getRobotPosition();
      
      turning = !(Math.abs(Robot.driveTrain.turnError()) <= RobotMap.AUTO_ACCEEPTABLE_TURN_ERROR);
      
      if (turning && !driving) {
        Robot.driveTrain.allDrive(0, -Robot.driveTrain.getTurnOutput());
        //driving = false;
      }
      else {    
        driving = true;
      }
      
      if (driving && !drivingCheck) {
        drivingCheck = true;
        Robot.driveTrain.setPosition(saveX, saveY);
        Robot.driveTrain.getRobotPosition();
      }
      
        
      if (driving) {
       // driving = true;
       // Robot.driveTrain.PIDDrive(currentPosition.distanceTo(currentWaypoint));
      //  Robot.driveTrain.enableTurningControl(0, 1);
        Robot.driveTrain.allDrive(0.6, 0);
      }
      
      if (currentPosition.distanceTo(currentWaypoint) < RobotMap.AUTO_DRIVE_DISTANCE_TOLERANCE/* Math.abs(((startLeft - leftEnc)  + (startRight - rightEnc))) / 2 >= startDist*/ && waypointIndex <= (waypoints.size() - 2)) {
        driving = false;
        drivingCheck = false;
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
        
        saveX = Robot.driveTrain.getRobotPosition().getX();
        saveY = Robot.driveTrain.getRobotPosition().getY();
        Robot.driveTrain.enableTurningControl(turn, 0.3);
        
      }
      
      SmartDashboard.putNumber("Turn Output", Robot.driveTrain.getTurnOutput());
      SmartDashboard.putNumber("Final Turn", turn);
      SmartDashboard.putNumber("Turn Error", Robot.driveTrain.turnError());
      SmartDashboard.putNumber("Waypoint Index", waypointIndex);
      SmartDashboard.putNumber("Distance from waypoint", Robot.driveTrain.getRobotPosition().distanceTo(currentWaypoint));
      SmartDashboard.putBoolean("Driving", driving);
      SmartDashboard.putBoolean("Turning", turning);
      
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return waypointIndex == (waypoints.size() - 1) && currentPosition.distanceTo(currentWaypoint) < RobotMap.AUTO_DRIVE_DISTANCE_TOLERANCE;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
