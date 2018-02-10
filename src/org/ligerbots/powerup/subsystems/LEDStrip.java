package org.ligerbots.powerup.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Spark;


/**
 *
 */
public class LEDStrip extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    Spark revBlinkin = new Spark(0);

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void setLights(int column) {
      //column is the column number of the light pattern
      revBlinkin.setSpeed(-1.01+(0.02*column));
    }
    
    public void lightsOff() {
      revBlinkin.setSpeed(0.0);
    }

	public void doNothing(int i) {
		// TODO Auto-generated method stub
		
	}
}

