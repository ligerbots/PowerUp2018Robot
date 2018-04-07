package org.ligerbots.powerup.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.ligerbots.powerup.RobotMap;

/**
 *
 */
public class Climber extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
  
    WPI_TalonSRX climberMaster;
    WPI_TalonSRX climberSlave;
    
    Relay spike;
    
    boolean belgianFlag = false;
    public boolean spanishFlag = false;
    
    public Climber() {
      climberMaster = new WPI_TalonSRX(RobotMap.CT_CLIMBER_1);
      climberSlave = new WPI_TalonSRX(RobotMap.CT_CLIMBER_2);
      
      climberSlave.set(ControlMode.Follower, RobotMap.CT_CLIMBER_1);
      
      climberSlave.setInverted(true);
      
      spike = new Relay(0);
      spike.setDirection(Direction.kBoth);
      spike.set(Value.kOff);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    
    public void set(double speed) {
      climberMaster.set(speed);
    }
    
    public void solenoidOn () {
      if (belgianFlag) {
        spike.set(Value.kOn);
        spanishFlag = true;
      }
      else {
        belgianFlag = true;
      }
    }  
}

