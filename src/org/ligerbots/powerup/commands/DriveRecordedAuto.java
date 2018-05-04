package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.Robot.InputSnapshot;

/**
 *
 */
public class DriveRecordedAuto extends Command {

    BufferedReader auto;
    InputSnapshot workingSnapshot;
    String line = "";
    boolean finished = false;
    public DriveRecordedAuto(String autoName) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
      try {
        auto = new BufferedReader(new FileReader(new File(String.format("%s", "/home/lvuser/").concat(autoName.concat(".txt")))));
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      try {
        if ((line = auto.readLine()) != null) {   
          workingSnapshot = InputSnapshot.getSnapFromString(line);
          Robot.driveTrain.allDrive(workingSnapshot.values[0], workingSnapshot.values[1]);
          Robot.elevator.set(workingSnapshot.values[2]);
          Robot.intake.intakeOn(workingSnapshot.values[3] < 0.0, Math.abs(workingSnapshot.values[3]));
          Robot.intake.setPistons(workingSnapshot.pistons);
        }
        else {
          finished = true;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return finished;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
