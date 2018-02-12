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
    public AutoCommandGroup(List<FieldPosition> wayPoints, double endAngle) {
      
      //Check my math
    
      for (FieldPosition i: wayPoints) {
        angleToWaypoint = Robot.driveTrain.getRobotPosition().angleTo(i);
        addSequential(new TurnCommand(Math.signum(angleToWaypoint) * (90 - Math.abs(angleToWaypoint)) - Robot.driveTrain.getRobotPosition().getDirection(), 0.3));
        addSequential(new DriveDistance(Robot.driveTrain.getRobotPosition().distanceTo(i), 1.0, 1.0));
      }
      
      addSequential(new TurnCommand(Robot.driveTrain.getRobotPosition().getDirection() - endAngle, 0.3));
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
