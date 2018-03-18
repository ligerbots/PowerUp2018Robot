package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
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
public class TwoCubeAuto extends CommandGroup {
  
   // REMEMBER TO SWITCH SIGNS AND STUFF (if u don't know what this means for some reason don't do anything / ask Mark 

    public TwoCubeAuto(FirstAction first, SecondAction second) {
      
      List<FieldPosition> tempWaypoints;
      
      addParallel (new ElevatorAuto());
      Robot.elevator.setDesiredHeight(4.0);
      addSequential(new BadDriveDistance(5.0, false));
      
      //addSequential(new ElevatorAuto(2.0, 1.0, true));
      
      switch (first) {
        case DriveForward:
          addSequential(new BadDriveDistance(115.0, false));
          break;
        case SwitchA:
          if (Robot.gameData.charAt(0) == 'L') {
            tempWaypoints = (List<FieldPosition>) FieldMap.wayPointsA.clone();
            for (int i = 0; i < tempWaypoints.size(); i += 1) {
              tempWaypoints.set(i, tempWaypoints.get(i).multiply(-1, 1, tempWaypoints.get(i).elevatorHeight));
            }
          }
          else {
            tempWaypoints = FieldMap.wayPointsA;
          }
          addParallel(new HoldBoxCommand());
          addSequential(new DrivePathCommand(tempWaypoints));
          addSequential(new IntakeAuto(true, 1.0, 3, FieldMap.switchScoringHeight - 1.0));
          
          break;
        case SwitchB:
          if (Robot.gameData.charAt(0) == 'L') {
            tempWaypoints = (List<FieldPosition>) FieldMap.wayPointsB.clone();
            for (int i = 0; i < tempWaypoints.size(); i += 1) {
              tempWaypoints.set(i, tempWaypoints.get(i).multiply(-1, 1, tempWaypoints.get(i).elevatorHeight));
            }
          }
          else {
            tempWaypoints = FieldMap.wayPointsB;
          }
          addParallel(new HoldBoxCommand());
          addSequential(new DrivePathCommand(tempWaypoints));
          addSequential(new IntakeAuto(true, 0.8, 1.5, FieldMap.switchScoringHeight - 0.5));
          break;
        case ScaleAlpha:
          if (Robot.gameData.charAt(1) == 'L') {
            tempWaypoints = (List<FieldPosition>) FieldMap.wayPointsAlpha.clone();
            for (int i = 0; i < tempWaypoints.size(); i += 1) {
              tempWaypoints.set(i, tempWaypoints.get(i).multiply(1, -1, tempWaypoints.get(i).elevatorHeight));
            }
          }
          else {
            tempWaypoints = FieldMap.wayPointsAlpha;
          }
          addParallel(new HoldBoxCommand());
          addSequential(new DrivePathCommand(tempWaypoints));
          addSequential(new IntakeAuto(true, 0.8, 1.5, FieldMap.scaleScoringHeight - 1.0));
          break;
        case ScaleBeta:
          if (Robot.gameData.charAt(1) == 'L') {
            tempWaypoints = (List<FieldPosition>) FieldMap.wayPointsBeta.clone();
            for (int i = 0; i < tempWaypoints.size(); i += 1) {
              tempWaypoints.set(i, tempWaypoints.get(i).multiply(-1, 1, tempWaypoints.get(i).elevatorHeight));
            }
          }
          else {
            tempWaypoints = FieldMap.wayPointsBeta;
          }
          addParallel(new HoldBoxCommand());
          addSequential(new DrivePathCommand(tempWaypoints));
          addSequential(new IntakeAuto(true, 0.8, 1.5, FieldMap.scaleScoringHeight - 1.0));
          break;
        default:
          break;
          
      }
         
      switch (second) {
        case Switch:
          if (Robot.gameData.charAt(0) == Robot.gameData.charAt(1)) {
            Robot.elevator.setDesiredHeight(1.0);
            addSequential(new BadDriveDistance(10.0, true));
            addSequential(new TurnCommand(180.0, 1.0));
            addSequential(new IntakePistonCommand(true));
            addSequential(new DriveToCube());
            addParallel(new IntakeAuto(false, 1.0, 1.0, 0.0));
            addSequential(new IntakePistonCommand(false));
            //if (Math.abs(Robot.driveTrain.getRobotPosition().getX()) < 85/* && Robot.driveTrain.getRobotPosition().getY() > 160.0*/) {
              Robot.elevator.setDesiredHeight(FieldMap.switchScoringHeight);
              addSequential(new IntakeAuto(true, 0.7, 1.0, 18.0));
          /*  }
            else if (Robot.driveTrain.getRobotPosition().getY() > 165.0) {
              addSequential(new DrivePathCommand(Robot.gameData.charAt(0) == 'R' ? Arrays.asList(new FieldPosition(75.0, 165.0), FieldMap.switchScoringSpot[2]) : Arrays.asList(new FieldPosition(-75.0, 165.0), FieldMap.switchScoringSpot[2].multiply(-1, 1))));
            } NOT AT ALL DONE YET*/
          }
          else {
            
          }
        case Scale:
          break;
        case Nothing:
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
      
  //    addParallel(new IntakePistonCommand(true));
      
      System.out.println("Distance: " + data[3]);

 //     addSequential(new BadDriveDistance(data[3]));
      
      
    /*  addSequential(new DriveToCube());
      
      addParallel(new IntakePistonCommand(false));
      
      addParallel(new HoldBoxCommand());*/
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
