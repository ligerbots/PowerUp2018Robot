package org.ligerbots.powerup.subsystems;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.ligerbots.powerup.RobotMap;

/**
 *
 */
public class Ramps extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
  
    DoubleSolenoid solenoid1;
    DoubleSolenoid solenoid2;
    
    public Ramps () {
      solenoid1 = new DoubleSolenoid(RobotMap.PCM_ID, 4, 5);
      solenoid2 = new DoubleSolenoid(RobotMap.PCM_ID, 6, 7);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void solenoid1 () {
      solenoid1.set(Value.kForward);
    }
    
    public void solenoid2() {
      solenoid2.set(Value.kForward);
    }
}

