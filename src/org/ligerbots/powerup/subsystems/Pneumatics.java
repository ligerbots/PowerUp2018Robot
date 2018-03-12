package org.ligerbots.powerup.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.RobotMap;
import edu.wpi.first.wpilibj.Compressor;

/**
 *
 */
public class Pneumatics extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    Compressor compressor;
    public enum CompressorState {
      ON, OFF, TOGGLE
    }
    public Pneumatics() {
        compressor = new Compressor(org.ligerbots.powerup.RobotMap.PCM_ID);
        compressor.setClosedLoopControl(false);
    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void setCompressor(CompressorState state) {
      if (state == CompressorState.ON) {
        compressor.setClosedLoopControl(true);
      }
      if (state == CompressorState.OFF) {
        compressor.setClosedLoopControl(false);
      }
      if (state == CompressorState.TOGGLE) {
        compressor.setClosedLoopControl(!(compressor.getClosedLoopControl()));
      }
    }
}
