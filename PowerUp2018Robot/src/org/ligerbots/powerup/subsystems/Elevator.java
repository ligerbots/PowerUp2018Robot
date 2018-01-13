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
    PIDController elevatorController;
    double P = 0.05;
    double I = 0.05;
    double D = 0.05;
    double requestedPosition = 0.0;//When the elevator is not moving, the 775s should stay in place (maintain 0 RPM)
    double pidOutput;
    double defaultSpeed = 0.05;//How fast it should go when it is moved up or down
    public Elevator() {
      
      elevatorMaster = new TalonSRX(RobotMap.CT_ELEVATOR_1);
      elevatorSlave = new TalonSRX(RobotMap.CT_ELEVATOR_2);
      
      speedController = new SpeedControllerGroup((SpeedController)elevatorMaster, (SpeedController)elevatorSlave);
      elevatorMaster.setNeutralMode(NeutralMode.Brake);
      elevatorSlave.setNeutralMode(NeutralMode.Brake);
      
      elevatorController = new PIDController(P, I, D, elevatorMaster.getPosition(), output -> pidOutput = output);
    }
    public void setSpeed(double requestedSpeed) {
      elevatorMaster.set(ControlMode.Speed, requestedSpeed);
    }
    public void goUp() {
      setSpeed(defaultSpeed);
      
    }
    public void goDown() {
      setSpeed(-defaultSpeed);
    }
    public void holdPosition() {
      elevatorController.setSetPoint(elevatorMaster.getPosition());
    }
    public void enablePIDControl() {
      
    }
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

