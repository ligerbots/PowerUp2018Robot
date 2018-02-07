package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import org.ligerbots.powerup.Robot;

public class LEDStripCommand extends Command {

  @Override
  protected boolean isFinished() {
    // TODO Auto-generated method stub
    return false;
    
  }
  
  //Called repeatedly when this Command is scheduled to run
  protected void execute() {
    Robot.ledstrip.setLights(1);    //indicate the column value as the input
  }

  // Called once after isFinished returns true
  protected void end() {
    Robot.ledstrip.lightsOff();
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  protected void interrupted() {
    end();
  }

}
