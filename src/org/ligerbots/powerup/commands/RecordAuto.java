package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.ligerbots.powerup.Robot;

/**
 *
 */
public class RecordAuto extends Command {

    File auto;
    String recording = "";
    public RecordAuto(String autoName) {
      
      auto = new File(String.format("%s%s", System.getProperty("user.dir"), "\\").concat(autoName.concat(".csv")));
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      recording.concat(Robot.getCurrentSnapshot().toString()).concat("\n");
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
      try {
        BufferedWriter writer = new BufferedWriter(new FileWriter(auto.getName()));
        writer.write(recording);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
}
