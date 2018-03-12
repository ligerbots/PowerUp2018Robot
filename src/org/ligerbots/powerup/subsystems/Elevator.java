package org.ligerbots.powerup.subsystems;

import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.RobotMap;
import org.ligerbots.powerup.commands.ElevatorCommand;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StickyFaults;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Elevator extends Subsystem {

  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  public enum ElevatorPosition {
    SCALE, SWITCH, EXCHANGE, UP, DOWN, RESTING
  }

  double desiredHeight = 0;
  WPI_TalonSRX elevatorMaster;
  WPI_TalonSRX elevatorSlave;
  StickyFaults elevatorStickyFaults;
  boolean elevatorPresent;
  double tolerance = 0.05;
  double requestedPosition = 0.0;// When the elevator is not moving, the 775s should stay in place
                                 // (maintain 0 RPM)
  double pidOutput;
  double defaultSpeed = 0.05; // How fast it should go when it is moved up or down
  public boolean elevatorGo = false;
  
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
    
    elevatorStickyFaults = new StickyFaults();
    elevatorPresent = elevatorMaster.getStickyFaults(elevatorStickyFaults) == ErrorCode.OK;
    System.out.println("Elevator master Talon is " + (elevatorPresent ? "Present" : "NOT Present"));
    if (elevatorPresent) {
	    elevatorMaster.setNeutralMode(NeutralMode.Brake);
	    elevatorSlave.setNeutralMode(NeutralMode.Brake);
	    elevatorSlave.setInverted(true);
	    elevatorSlave.set(ControlMode.Follower, RobotMap.CT_ELEVATOR_1);
	
	    // Set the encoder for the master Talon.
	    elevatorMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 0);
	    elevatorMaster.setSensorPhase(true);
    
    // TODO: Need to make sure that the elevator is starting out at the lowest point.
    // I think it is in which case we should explicitly zero the encoder here.
    
    //elevatorController = new PIDController(P, I, D, elevatorMaster.getSelectedSensorPosition(0),
    //output -> pidOutput = output);
    }
  }

 /* public void setSpeed(double requestedSpeed) {
     elevatorMaster.set(ControlMode.Velocity, requestedSpeed);
  }*/
  
  public void initDefaultCommand() {
	if (Robot.elevatorCommand == null) Robot.elevatorCommand = new ElevatorCommand();
    setDefaultCommand(Robot.elevatorCommand);
  }

  public void zeroEncoder() {	
	  if (elevatorPresent) elevatorMaster.setSelectedSensorPosition(0, 0, 0);
  }
  
  public void set(double speed) {
	  if (elevatorPresent) elevatorMaster.set(speed);
  }
  
  public void holdPosition(double requestedPosition) {
	  if (elevatorPresent) elevatorMaster.set(ControlMode.Position, requestedPosition / (Math.PI * 0.5) * 4096);
  }
  
  public void setPID () {
	  if (elevatorPresent) {
		  elevatorMaster.config_kP(0, SmartDashboard.getNumber("Elevator P", 0.05), 0);
		  elevatorMaster.config_kI(0, SmartDashboard.getNumber("Elevator I", 0.001), 0);
		  elevatorMaster.config_kD(0, SmartDashboard.getNumber("Elevator D", 0.05), 0);
	  }
  }
   // elevatorController.setSetpoint(requestedPosition);
  // elevatorMaster.config_kI(arg0, arg1, arg2)
   //ControlMode.Position  
  
  //returns position in inches
  public double getPosition() {
	  if (elevatorPresent) return elevatorMaster.getSelectedSensorPosition(0) / 4096d * (Math.PI * 0.5);
	  else return 0.0;
  }
  
  public double getDesiredHeight() {
    return desiredHeight;
  }
  
  public void setDesiredHeight(double height) {
    desiredHeight = height;
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
  
  public void logCurrent() {
	  if (elevatorPresent) { 
		  SmartDashboard.putNumber("Elevator Master Current", elevatorMaster.getOutputCurrent());
		  SmartDashboard.putNumber("Elevator Slave Current", elevatorSlave.getOutputCurrent());
	  }

  }
  
  public boolean isPresent() {
	  return elevatorPresent;
  }
}

