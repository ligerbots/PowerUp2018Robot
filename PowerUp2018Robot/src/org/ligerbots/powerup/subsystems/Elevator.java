package org.ligerbots.powerup.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.CANTalon;
import com.ctre.phoenix.*;

/**
 *
 */
public class Elevator extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    TalonSRX elevatorMaster;
    TalonSRX elevatorSlave;
    TalonID elevatorMasterTalon;
    TalonID elevatorSlaveTalon;
    SpeedControllerGroup speedController;
    PIDController ElevatorController;
    const int P 0.05;
    const int I = 0.05;
    const int D = 0.05;
    double requestedRPM=0.0;//When the elevator is not moving, the 775s should stay in place (maintain 0 RPM)
    public Elevator() {
      elevatorMaster = TalonSRX(RobotMap.CT_ELEVATOR_1);
      elevatorSlave = TalonSRX(RobotMap.CT_ELEVATOR_2);
      
      speedController = new SpeedControllerGroup((SpeedController)elevatorMaster, (SpeedController)elevatorSlave);
      elevatorMaster.setNeutralMode(NeutralMode.Brake);
      elevatorSlave.setNeutralMode(NeutralMode.Brake);
      
      elevatorMasterTalon = TalonID(RobotMap.CT_ELEVATOR_1, elevatorMaster);
      elevatorSlaveTalon = TalonID(RobotMap.CT_ELEVATOR_2, elevatorSlave);
      
      elevatorController = new PIDController(P, I, D, )
    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

