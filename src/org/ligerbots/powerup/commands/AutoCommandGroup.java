package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import java.util.List;
import org.ligerbots.powerup.FieldPosition;
import org.ligerbots.powerup.Robot;

/**
 *
 */
public class AutoCommandGroup extends CommandGroup {

    double angleToWaypoint;
    boolean lowerQuadrants;
    
    // list of waypoints, and number of crates to dump
    // 0 = none, we're just moving forward
    // 1 = place the crate we started with on either the switch or the scale
    // 2 = place the first crate, then pick up and place a second
    public AutoCommandGroup(List<FieldPosition> wayPoints, int crates) {
      
      // move forward 8" to get us away from the wall
      System.out.println("Auto Command Group starting");
      addSequential(new BadDriveDistance(8.0));
    	
      // Check my math
      for (FieldPosition i: wayPoints) {
        angleToWaypoint = Robot.driveTrain.getRobotPosition().angleTo(i);
        if (i.getY() - Robot.driveTrain.getRobotPosition().getY() >= 0) {
          lowerQuadrants = false;
        }
        else {
          lowerQuadrants = true;
        }
        addSequential(new TurnCommand(lowerQuadrants ? angleToWaypoint - Robot.driveTrain.getRobotPosition().getDirection() 
            : Math.signum(angleToWaypoint) * (90 - Math.abs(angleToWaypoint)) - Robot.driveTrain.getRobotPosition().getDirection(), 0.3));
        addSequential(new BadDriveDistance(Robot.driveTrain.getRobotPosition().distanceTo(i)));
      }
      
      // no more endAngle. The waypoints are placed to make sure the robot ends up square to the platform
      
      // TODO -- raise elevator (should probably start that just before the final waypoint)
      // TODO -- push the crate out of the intake
      
      // TODO -- all the stuff for the second crate, second action
      
      // addSequential(new TurnCommand(Robot.driveTrain.getRobotPosition().getDirection() - endAngle, 0.3));
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.

        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
