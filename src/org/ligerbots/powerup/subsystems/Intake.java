package org.ligerbots.powerup.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.ligerbots.powerup.RobotMap;

/**
 *
 */
public class Intake extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    WPI_TalonSRX intakeMaster;
    WPI_TalonSRX intakeSlave;
    SpeedControllerGroup controllerGroup;

    public Intake() {
      intakeMaster = new WPI_TalonSRX(RobotMap.CT_INTAKE_1);
      intakeSlave = new WPI_TalonSRX(RobotMap.CT_INTAKE_2);
      
      intakeSlave.setInverted(true);
      
      controllerGroup = new SpeedControllerGroup(intakeMaster, intakeSlave);
    }
    
    public void intakeOn(boolean reverse) {
       if (reverse) {
         intakeMaster.set(-1);
       }
       else {
         intakeMaster.set(1);
       }
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

