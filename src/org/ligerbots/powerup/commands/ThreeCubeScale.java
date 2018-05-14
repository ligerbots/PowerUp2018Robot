package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import java.util.List;
import org.ligerbots.powerup.FieldPosition;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.Robot.StartingPosition;

/**
 *
 */
public class ThreeCubeScale extends CommandGroup {

    public ThreeCubeScale() {
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
      List<FieldPosition> tempWaypoints;
      
      addParallel (new ElevatorAuto());
      Robot.elevator.setDesiredHeight(4.0);
      addSequential(new BadDriveDistance(5.0, false));
      
     /* if (Robot.gameData)
      
      if (Robot.startPos == StartingPosition.One) {
        
      }
      addSequential(new DrivePathCommand())
     */ 
    }
}
