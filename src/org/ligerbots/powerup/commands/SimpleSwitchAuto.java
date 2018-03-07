package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Arrays;
import java.util.List;

import org.ligerbots.powerup.FieldMap;
import org.ligerbots.powerup.FieldPosition;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.Robot.FirstAction;
import org.ligerbots.powerup.Robot.SecondAction;

/**
 *
 */
public class SimpleSwitchAuto extends CommandGroup {

    public SimpleSwitchAuto(FirstAction first, SecondAction second) {
      
      List<FieldPosition> tempWaypoints;
      
      addSequential(new BadDriveDistance(5.0));
      
      switch (first) {
        case DriveForward:
          addSequential(new BadDriveDistance(125.0));
          break;
        case SwitchA:
          if (Robot.gameData.charAt(0) == 'L') {
            tempWaypoints = FieldMap.wayPointsA;
            for (int i = 0; i < tempWaypoints.size(); i += 1) {
              tempWaypoints.set(i, tempWaypoints.get(i).multiply(1, -1));
            }
          }
          else {
            tempWaypoints = FieldMap.wayPointsA;
          }
          addParallel(new HoldBoxCommand());
          addParallel(new ElevatorAuto(FieldMap.switchScoringHeight, 1));
          addSequential(new DrivePathCommand(tempWaypoints));
          addSequential(new IntakeAuto(true, 0.75, 1.0, FieldMap.switchScoringHeight - 1.0));
          break;
        case SwitchB:
          if (Robot.gameData.charAt(0) == 'L') {
            tempWaypoints = FieldMap.wayPointsB;
            for (int i = 0; i < tempWaypoints.size(); i += 1) {
              tempWaypoints.set(i, tempWaypoints.get(i).multiply(1, -1));
            }
          }
          else {
            tempWaypoints = FieldMap.wayPointsB;
          }
          addParallel(new HoldBoxCommand());
          addParallel(new ElevatorAuto(FieldMap.switchScoringHeight, 1));
          addSequential(new DrivePathCommand(tempWaypoints));
          addSequential(new IntakeAuto(true, 0.75, 1.0, FieldMap.switchScoringHeight - 1.0));
          break;
        case ScaleAlpha:
          if (Robot.gameData.charAt(1) == 'L') {
            tempWaypoints = FieldMap.wayPointsAlpha;
            for (int i = 0; i < tempWaypoints.size(); i += 1) {
              tempWaypoints.set(i, tempWaypoints.get(i).multiply(1, -1));
            }
          }
          else {
            tempWaypoints = FieldMap.wayPointsAlpha;
          }
          addParallel(new HoldBoxCommand());
          addParallel(new ElevatorAuto(FieldMap.scaleScoringHeight, 1));
          addSequential(new DrivePathCommand(tempWaypoints));
          addSequential(new IntakeAuto(true, 0.75, 1.0, FieldMap.scaleScoringHeight - 1.5));
          break;
        case ScaleBeta:
          if (Robot.gameData.charAt(1) == 'L') {
            tempWaypoints = FieldMap.wayPointsBeta;
            for (int i = 0; i < tempWaypoints.size(); i += 1) {
              tempWaypoints.set(i, tempWaypoints.get(i).multiply(1, -1));
            }
          }
          else {
            tempWaypoints = FieldMap.wayPointsBeta;
          }
          addParallel(new HoldBoxCommand());
          addParallel(new ElevatorAuto(FieldMap.scaleScoringHeight, 1));
          addSequential(new DrivePathCommand(tempWaypoints));
          addSequential(new IntakeAuto(true, 0.75, 1.0, FieldMap.scaleScoringHeight - 1.5));
          break;
        default:
          break;
          
      }
          
          
          
          System.out.println("Starting Simple Switch");
      
     // addSequential(new ElevatorAuto(0.6, 0.1));
      
  //    addParallel(new HoldBoxCommand());
      
  //    addParallel(new ElevatorAuto(69.0, 1));
      
  //    addSequential(new DrivePathCommand(FieldMap.wayPointsBeta));
            
      //addSequential(new ElevatorAuto(1.0, 0.2));
      
  //    addSequential(new IntakeAuto(true, 0.75, 1.0, 65.0));
      
      double[] data = SmartDashboard.getNumberArray("vision/target_info", new double[]{0.0,0.0,0.0,0.0,0.0,0.0});
      
      System.out.println("Turn " + Math.toDegrees(data[4]));
      
  //    addSequential(new TurnCommand(Math.toDegrees(data[4]), 0.3));
      
      addParallel(new IntakePistonCommand(true));
      
      System.out.println("Distance: " + data[3]);

 //     addSequential(new BadDriveDistance(data[3]));
      
      
      addSequential(new DriveToCube());
      
      addParallel(new IntakePistonCommand(false));
      
      addParallel(new HoldBoxCommand());
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
