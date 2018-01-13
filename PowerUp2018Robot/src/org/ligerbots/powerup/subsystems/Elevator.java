package org.ligerbots.powerup.subsystems;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.ligerbots.powerup.RobotMap;
import org.ligerbots.powerup.subsystems.DriveTrain.TalonID;

/**
 *
 */
public class Elevator extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
  
    public enum ElevatorPosition {
      SCALE, 
      SWITCH, 
      EXCHANGE,
      UP,
      DOWN,
      RESTING
    }
    
    TalonSRX elevatorMaster;
    TalonSRX elevatorSlave;
    TalonID elevatorMasterTalon;
    TalonID elevatorSlaveTalon;
    SpeedControllerGroup speedController;
    PIDController ElevatorController;
    double P = 0.05;
    double I = 0.05;
    double D = 0.05;
    double requestedRPM=0.0;//When the elevator is not moving, the 775s should stay in place (maintain 0 RPM)
    double pidOutput;
    double defaultRPM = 10.0;//How fast it should go when it is moved up or down
    public Elevator() {
      
      elevatorMaster = new TalonSRX(RobotMap.CT_ELEVATOR_1);
      elevatorSlave = new TalonSRX(RobotMap.CT_ELEVATOR_2);
      
      speedController = new SpeedControllerGroup((SpeedController)elevatorMaster, (SpeedController)elevatorSlave);
      elevatorMaster.setNeutralMode(NeutralMode.Brake);
      elevatorSlave.setNeutralMode(NeutralMode.Brake);
      
    //  elevatorController = new PIDController(P, I, D, encoder, output -> pidOutput = output);
    }
    public void setRPM(double requestedRPM) {
      this.requestedRPM = requestedRPM;
    }
    public void goUp() {
      setRPM(defaultRPM);
      
    }
    public void goDown() {
      setRPM(-defaultRPM);
    }
    public void holdPosition() {
      setRPM(0.0);
    }
    public void setElevatorPosition(ElevatorPosition position) {
      switch (position) {
        case UP:
          setRPM(defaultRPM);
      }
    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

