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
    double startTime;
    BufferedWriter writer;
    public RecordAuto(String autoName) {
      
      auto = new File(String.format("%s%s", "/home/lvuser/", autoName.concat(".txt")));
      auto.setWritable(true);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      System.out.println("AUTO FILE PATH: " + auto.getAbsolutePath());
      startTime = Robot.time();
      try {
        writer = new BufferedWriter(new FileWriter(String.format("%s%s", "/home/lvuser/", auto.getName())));
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
      try {
        writer.write(Robot.getCurrentSnapshot().toString());
        writer.newLine();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      recording += "\n";
      System.out.println(Robot.getCurrentSnapshot().toString());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return Robot.time() - startTime >= 15.0;
    }

    // Called once after isFinished returns true
    protected void end() {
        try {
          writer.flush();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
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
