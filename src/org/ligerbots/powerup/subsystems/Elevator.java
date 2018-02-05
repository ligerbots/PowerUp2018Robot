package org.ligerbots.powerup.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DigitalInput;
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
  double tolerance = 0.05;
  double requestedPosition = 0.0;// When the elevator is not moving, the 775s should stay in place
                                 // (maintain 0 RPM)
  double pidOutput;
  double defaultSpeed = 0.05;// How fast it should go when it is moved up or down
  
  DigitalInput topLimitSwitch;
  DigitalInput bottomLimitSwitch;
  
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

    topLimitSwitch = new DigitalInput(0);
    bottomLimitSwitch = new DigitalInput(1);
    elevatorMaster = new WPI_TalonSRX(RobotMap.CT_ELEVATOR_1);
    elevatorSlave = new WPI_TalonSRX(RobotMap.CT_ELEVATOR_2);
    

    elevatorMaster.setNeutralMode(NeutralMode.Brake);
    elevatorSlave.setNeutralMode(NeutralMode.Brake);
    elevatorSlave.setInverted(true);
    
    elevatorSlave.set(ControlMode.Follower, RobotMap.CT_ELEVATOR_1);

    // Set the encoder for the master Talon.
    elevatorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
    
    //elevatorController = new PIDController(P, I, D, elevatorMaster.getSelectedSensorPosition(0),
    //output -> pidOutput = output);
  }

 /* public void setSpeed(double requestedSpeed) {
     elevatorMaster.set(ControlMode.Velocity, requestedSpeed);
  }*/
  public void zeroEncoder() {
    elevatorMaster.setSelectedSensorPosition(0, 0, 0);
  }
  
  public void set(double speed) {
    elevatorMaster.set(speed);
  }
  
  public void holdPosition(double requestedPosition) {
    elevatorMaster.set(ControlMode.Position, (requestedPosition - getPosition()) / (Math.PI * 0.5) * 4096);
  }
   // elevatorController.setSetpoint(requestedPosition);
  // elevatorMaster.config_kI(arg0, arg1, arg2)
   //ControlMode.Position  
  
  //returns position in inches
  public double getPosition() {
    return elevatorMaster.getSelectedSensorPosition(0) / 4096 * (Math.PI * 0.5);
  }


 /* public void initializePIDControl() {
    elevatorController.enable();
    elevatorController.setInputRange(-10.0, 10.0);
    elevatorController.setOutputRange(-10.0,10.0);
    elevatorController.setAbsoluteTolerance(tolerance);
    //elevatorController.setToleranceBuffer(1);
    elevatorController.setContinuous(true);
 //   elevatorController.setSetpoint(0.0);
  }*/

  
  public boolean getLimitSwitch(boolean top) {
    return top ? topLimitSwitch.get() : bottomLimitSwitch.get();
  }

  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}

