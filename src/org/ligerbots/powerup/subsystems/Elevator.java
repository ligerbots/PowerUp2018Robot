package org.ligerbots.powerup.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
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
    SCALE, SWITCH, EXCHANGE, UP, DOWN, RESTING
  }

  WPI_TalonSRX elevatorMaster;
  WPI_TalonSRX elevatorSlave;
  SpeedControllerGroup speedController;
  PIDController elevatorController;
  double P = 0.05;
  double I = 0.05;
  double D = 0.05;
  double tolerance = 0.05;
  double requestedPosition = 0.0;// When the elevator is not moving, the 775s should stay in place
                                 // (maintain 0 RPM)
  double pidOutput;
  double defaultSpeed = 0.05;// How fast it should go when it is moved up or down
  
  /*class EncoderPID implements PIDSource {
    
    public double pidGet() {
       return elevatorMaster.getSelectedSensorPosition(0);
     }

    @Override
    public void setPIDSourceType(PIDSourceType pidSource) {
      // TODO Auto-generated method stub
      
    }

    @Override
    public PIDSourceType getPIDSourceType() {
      // TODO Auto-generated method stub
      return null;
    }
  }*/
  
  public Elevator() {

    elevatorMaster = new WPI_TalonSRX(RobotMap.CT_ELEVATOR_1);
    elevatorSlave = new WPI_TalonSRX(RobotMap.CT_ELEVATOR_2);

    speedController =
        new SpeedControllerGroup((SpeedController) elevatorMaster, (SpeedController) elevatorSlave);
    elevatorMaster.setNeutralMode(NeutralMode.Brake);
    elevatorSlave.setNeutralMode(NeutralMode.Brake);
   //elevatorController = new PIDController(P, I, D, elevatorMaster.getSelectedSensorPosition(0),
    //output -> pidOutput = output);
  }

  public void setSpeed(double requestedSpeed) {
     elevatorMaster.set(ControlMode.Velocity, requestedSpeed);
  }
  public void setPosition(double requestedPosition) {
    elevatorMaster.set(ControlMode.Position, requestedPosition);
  }
  public void setRequestedPosition(double requestedPosition) {
    this.requestedPosition = requestedPosition;
    elevatorController.setSetpoint(requestedPosition);
  // elevatorMaster.config_kI(arg0, arg1, arg2)
   //ControlMode.Position  
  }
  public void goUp() {
    setSpeed(defaultSpeed);
  }

  public void goDown() {
    setSpeed(-defaultSpeed);
  }

  public void holdPosition() {
    setRequestedPosition(elevatorMaster.getSelectedSensorPosition(0));
  }

  public void initializePIDControl() {
    elevatorController.enable();
    elevatorController.setInputRange(-10.0, 10.0);
    elevatorController.setOutputRange(-10.0,10.0);
    elevatorController.setAbsoluteTolerance(tolerance);
    //elevatorController.setToleranceBuffer(1);
    elevatorController.setContinuous(true);
    elevatorController.setSetpoint(0.0);
  }
  public double getPIDOutput() {
    return pidOutput;
  }

  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}

