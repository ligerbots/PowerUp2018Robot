package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.List;
import org.ligerbots.powerup.FieldPosition;
import org.ligerbots.powerup.FieldPosition.Action;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.RobotMap;
import org.ligerbots.powerup.RobotPosition;
import org.ligerbots.powerup.subsystems.DriveTrain;
import org.ligerbots.powerup.subsystems.DriveTrain.DriveSide;

/**
 *
 */
public class ComplexDrivePath extends Command {
    

    enum DriveState {
      RAMP_UP,
      RAMP_DOWN,
      TURNING,
      DRIVE,
      COLLIDED
    }
  
    List<FieldPosition> waypoints;
    int waypointIndex = 0;
    
    FieldPosition currentWaypoint;
    RobotPosition currentPosition;
   
    double angleError;
    double angleToWaypoint;
    
    boolean finished;
    
    double startAbsDistance;
        
    double turn;
    double drive;
    
    double rampUpDist;
    double rampDownDist;
    double rampUpDelta;
    double rampDownDelta;
    
    double oldDist;
    double dist; 
    double distToNextWaypoint;
    double distToPreviousWaypoint;
    
    double nextAngle;
    
    DriveState state;
    
    public ComplexDrivePath(List<FieldPosition> waypoints) {
      requires (Robot.driveTrain);
      this.waypoints = waypoints;
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      
      finished = false;
      startAbsDistance = Robot.driveTrain.getAbsoluteDistanceTraveled();
      
      rampUpDist = 25.0;
      rampDownDist = 40.0;
      
      currentPosition = Robot.driveTrain.getRobotPosition();
      currentWaypoint = waypoints.get(waypointIndex);

      angleToWaypoint = Robot.driveTrain.getRobotPosition().angleTo(currentWaypoint);
      
      System.out.printf("ADC: WaypointIndex = %d, WaypointX = %5.2f, WaypointY = %5.2f, FinalTurn = %5.2f, Turn Output = %5.2f, Angle Error = %5.2f, Drive Speed = %5.2f\n",
          waypointIndex, currentWaypoint.getX(), currentWaypoint.getY(), turn, Robot.driveTrain.getTurnOutput(), angleError, drive);
      
      dist = Robot.driveTrain.getRobotPosition().distanceTo(currentWaypoint);
      oldDist = Robot.driveTrain.getRobotPosition().distanceTo(currentWaypoint);
            
      Robot.elevator.setDesiredHeight(currentWaypoint.elevatorHeight);
      
      drive = 0.0;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      
      
     // angleToWaypoint = Robot.driveTrain.getRobotPosition().angleTo(currentWaypoint);
          
      angleError = (waypoints.get(waypointIndex).action == Action.REVERSE) ? -90 - angleToWaypoint - Robot.driveTrain.getRobotPosition().getDirection() : 90 - angleToWaypoint - Robot.driveTrain.getRobotPosition().getDirection();
      
      if (angleError > 180) angleError -= 360;
      else if (angleError < -180) angleError += 360;
      
      turn = Math.signum(angleError) * Math.min(Math.abs(angleError * 0.01 + Math.signum(angleError) * 0.45), 0.9);
      
      rampUpDelta = Robot.driveTrain.getAbsoluteDistanceTraveled() - startAbsDistance;
      rampDownDelta = currentPosition.distanceTo(waypoints.get(waypoints.size() - 1));
      
      nextAngle = waypointIndex == waypoints.size() - 1 ? 0.0 : 90 - currentWaypoint.angleTo(waypoints.get(waypointIndex + 1)) - Robot.driveTrain.getRobotPosition().getDirection();
      
      distToNextWaypoint = currentPosition.distanceTo(waypoints.get(waypointIndex));
      if (waypointIndex != 0) distToPreviousWaypoint = currentPosition.distanceTo(waypoints.get(waypointIndex - 1));
      else distToPreviousWaypoint = currentPosition.distanceTo(Robot.startPosition);

      if (Robot.driveTrain.collided) state = DriveState.COLLIDED;
      else if (Math.abs(angleError) >= 10) state = DriveState.TURNING;
      else if (distToNextWaypoint <= rampDownDist && (nextAngle >= 35 || waypointIndex == waypoints.size() - 1)) state = DriveState.RAMP_DOWN;
      else if (drive >= 0.9) state = DriveState.DRIVE;
      else state = DriveState.RAMP_UP; 
      
      switch (state) {
        case COLLIDED: 
            finished = true;
            drive = 0.0;
            turn = 0.0;
            break;
        case TURNING:
            if (Math.abs(angleError) >= 20.0) drive = 0.0;
            else drive = 0.2; //Will try to think of something better later
            break;
        case RAMP_DOWN:
            if (nextAngle >= 45) drive = distToNextWaypoint/60.0 + 0.3;
            else drive = distToNextWaypoint/60.0 + 0.5; 
            break;
        case RAMP_UP:
            drive = distToPreviousWaypoint/(rampUpDist * 2) + 0.5;
            break;
        case DRIVE:
            drive = 1.0;
            break;
        default:
            break;
            
      }
     
      Robot.driveTrain.allDrive(drive, turn);
      
      
      System.out.printf("X: %5.2f, Y: %5.2f, Dist: %5.2f, Angle: %5.2f, Angle Error: %5.2f, Drive: %5.2f, Turn: %5.2f \n", 
          Robot.driveTrain.getRobotPosition().getX(), Robot.driveTrain.getRobotPosition().getY(), dist, Robot.driveTrain.getYaw(), angleError, drive, turn);

     
      if ((currentPosition.distanceTo(currentWaypoint) < RobotMap.AUTO_DRIVE_DISTANCE_TOLERANCE) ||
          (Robot.driveTrain.getRobotPosition().distanceTo(currentWaypoint) - oldDist >= 0.1 && Math.abs(angleError) <= 10.0)) {
        
        if (waypointIndex == waypoints.size() - 1) {
          finished = true;
        }
        else {
                            
          Robot.driveTrain.allDrive(0, 0);
          
          waypointIndex += 1;
                    
          currentWaypoint = waypoints.get(waypointIndex);
          
          angleToWaypoint = waypoints.get(waypointIndex - 1).angleTo(currentWaypoint);
          
          angleError = 90 - angleToWaypoint - Robot.driveTrain.getRobotPosition().getDirection();
          
          dist = waypoints.get(waypointIndex - 1).distanceTo(currentWaypoint);//Robot.driveTrain.getRobotPosition().distanceTo(currentWaypoint);
          
          System.out.printf("ADC: WaypointIndex = %d, WaypointX = %5.2f, WaypointY = %5.2f, FinalTurn = %5.2f, Turn Output = %5.2f, Angle Error = %5.2f, Drive Speed = %5.2f\n",
                waypointIndex, currentWaypoint.getX(), currentWaypoint.getY(), turn, Robot.driveTrain.getTurnOutput(), angleError, drive);
                    
                    
        }
        
      }
      
      Robot.elevator.setDesiredHeight(currentWaypoint.elevatorHeight);
      
      oldDist = Robot.driveTrain.getRobotPosition().distanceTo(currentWaypoint);
      
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return (waypointIndex >= (waypoints.size() - 1) && currentPosition.distanceTo(waypoints.get(waypoints.size() - 1)) < RobotMap.AUTO_DRIVE_DISTANCE_TOLERANCE) || finished;
    }

    // Called once after isFinished returns true
    protected void end() {
      System.out.println("Finished");
    }
    
    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
