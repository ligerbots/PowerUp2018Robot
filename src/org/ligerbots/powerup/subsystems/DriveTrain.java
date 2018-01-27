package org.ligerbots.powerup.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Arrays;
import org.ligerbots.powerup.RobotMap;

/**
 *
 */
public class DriveTrain extends Subsystem {

  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  WPI_TalonSRX leftMaster;
  WPI_TalonSRX leftSlave;
  WPI_TalonSRX rightMaster;
  WPI_TalonSRX rightSlave;
  SpeedControllerGroup left;
  SpeedControllerGroup right;
  DifferentialDrive robotDrive;
  PIDController turningController;
  double turnOutput = 0;
  double limitedStrafe = 0;
  TalonID[] talons;

  public enum DriveSide {
    LEFT,
    RIGHT
  }
  AHRS navx;

  public class TalonID {
    int talonID;
    WPI_TalonSRX talon;

    public TalonID(int talonID, WPI_TalonSRX talon) {
      this.talonID = talonID;
      this.talon = talon;
    }

  }

  public DriveTrain() {


    SmartDashboard.putNumber("Strafe Ramp Rate", 0.08);

    leftMaster = new WPI_TalonSRX(RobotMap.CT_LEFT_2);
    leftSlave = new WPI_TalonSRX(RobotMap.CT_LEFT_1);
    rightMaster = new WPI_TalonSRX(RobotMap.CT_RIGHT_2);
    rightSlave = new WPI_TalonSRX(RobotMap.CT_RIGHT_1);
    
 //   leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

    // With the new SpeedControlGroups, do we have to do this ourselves anymore?
    // leftSlave.set(ControlMode.Follower, leftMaster.getDeviceID());
    // rightSlave.set(ControlMode.Follower, rightMaster.getDeviceID());


    left = new SpeedControllerGroup(leftMaster, leftSlave);
    right = new SpeedControllerGroup(rightMaster, rightSlave);

    leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
    rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
    
    // deprecated CANTalon methods of doing things:
    // leftMaster.changeControlMode(TalonControlMode.PercentVbus);
    // rightMaster.changeControlMode(TalonControlMode.PercentVbus);
    // centerMaster.changeControlMode(TalonControlMode.PercentVbus);

    // leftSlave.changeControlMode(TalonControlMode.Follower);
    // rightSlave.changeControlMode(TalonControlMode.Follower);
    // centerSlave.changeControlMode(TalonControlMode.Follower);

    // leftSlave.set(RobotMap.CT_LEFT_1);
    // rightSlave.set(RobotMap.CT_RIGHT_1);
    // centerSlave.set(RobotMap.CT_CENTER_1);

    Arrays.asList(leftMaster, rightMaster, leftSlave, rightSlave)
        .forEach((WPI_TalonSRX talon) -> talon.setNeutralMode(NeutralMode.Brake));
    

    robotDrive = new DifferentialDrive(left, right);

    navx = new AHRS(Port.kMXP, (byte) 200);

    turningController =
        new PIDController(0.045, 0.004, 0.06, navx, output -> this.turnOutput = output);
  }
  
  public void talonCurrent() {
    Arrays.asList(leftMaster, rightMaster, leftSlave, rightSlave)
    .forEach((WPI_TalonSRX talon) -> SmartDashboard.putNumber(((Integer)talon.getDeviceID()).toString(), talon.getOutputCurrent()));
  }
  

  double rampRate;

  public void allDrive(double throttle, double rotate) {

 //   rampRate = SmartDashboard.getNumber("Strafe Ramp Rate", 0.3);
    robotDrive.arcadeDrive(-throttle, -rotate);
  }

  public double getYaw() {
    return navx.getYaw();
  }

  public double getRate() {
    return navx.getRate();
  }

  public double getAngle() {
    return navx.getAngle();
  }

  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
  
  public double getEncoderDistance (DriveSide driveSide) {
    if (driveSide == DriveSide.LEFT) {
      return (leftSlave.getSelectedSensorPosition(0)/1024.0) * RobotMap.GEARING_FACTOR * RobotMap.WHEEL_CIRCUMFERENCE;
    }
    else {
      return (-rightSlave.getSelectedSensorPosition(0)/1024.0) * RobotMap.GEARING_FACTOR * RobotMap.WHEEL_CIRCUMFERENCE;
    }
  }

  public void printEncoder() {
    System.out.println("Left: " + (leftSlave.getSelectedSensorPosition(0) / 1024.0) * RobotMap.WHEEL_CIRCUMFERENCE + 
        " Right: " + (-rightSlave.getSelectedSensorPosition(0) / 1024.0) * RobotMap.WHEEL_CIRCUMFERENCE);
  }
  
  double temporaryFixDegrees(double input) {
    if (input > 180) {
      return input - 360;
    } else if (input < -180) {
      return input + 360;
    } else {
      return input;
    }
  }

  public void enableTurningControl(double angle, double tolerance) {
    double startAngle = this.getYaw();
    double temp = startAngle + angle;
    // RobotMap.TURN_P = turningController.getP();
    // RobotMap.TURN_D = turningController.getD();
    // RobotMap.TURN_I = turningController.getI();
    temp = temporaryFixDegrees(temp);
    turningController.setSetpoint(temp);
    turningController.enable();
    turningController.setInputRange(-180.0, 180.0);
    turningController.setOutputRange(-1.0, 1.0);
    turningController.setAbsoluteTolerance(tolerance);
//    turningController.setToleranceBuffer(1);
    turningController.setContinuous(true);
    turningController.setSetpoint(temp);
  }


  public void setPID(double p, double i, double d) {
    turningController.setPID(p, i, d);
  }

  public void disablePID() {
    turningController.disable();
  }

  public boolean isPidOn() {
    return turningController.isEnabled();
  }

  public double getTurnOutput() {
    return turnOutput;
  }
  
  public void configClosedLoop (double p, double i, double d) {
    leftMaster.config_kP(0, p, 100000);
    leftMaster.config_kI(0, i, 100000);
    leftMaster.config_kD(0, d, 100000);
    
    rightMaster.config_kP(0, p, 100000);
    rightMaster.config_kI(0, i, 100000);
    rightMaster.config_kD(0, d, 100000);
    
    rightMaster.setInverted(true);
  }
  
  public void PIDDrive(double dist) {
   
    leftMaster.set(ControlMode.Position, dist * 1024.0 / RobotMap.WHEEL_CIRCUMFERENCE);
    rightMaster.set(ControlMode.Position, dist * 1024.0 / RobotMap.WHEEL_CIRCUMFERENCE);

  }

  public void zeroYaw() {
    navx.zeroYaw();
  }
}


