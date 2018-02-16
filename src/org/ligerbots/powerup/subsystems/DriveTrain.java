package org.ligerbots.powerup.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.AHRSProtocol.AHRSUpdateBase;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SPI.Port;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Arrays;
import org.ligerbots.powerup.RobotMap;
import org.ligerbots.powerup.commands.DriveDistance;
import org.ligerbots.powerup.RobotPosition;

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
    LEFT, RIGHT
  }

  AHRS navx;

  double positionX;
  double positionY;
  double rotation;
  double absoluteDistanceTraveled;

  double prevEncoderLeft;
  double prevEncoderRight;
  double rotationOffset = 0;
  double lastOutputLeft = 0;
  double lastOutputRight = 0;

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

    leftMaster = new WPI_TalonSRX(RobotMap.CT_LEFT_1);
    leftSlave = new WPI_TalonSRX(RobotMap.CT_LEFT_2);
    rightMaster = new WPI_TalonSRX(RobotMap.CT_RIGHT_1);
    rightSlave = new WPI_TalonSRX(RobotMap.CT_RIGHT_2);

    rightSlave.set(ControlMode.Follower, RobotMap.CT_RIGHT_1);
    leftSlave.set(ControlMode.Follower, RobotMap.CT_LEFT_1);
    // leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

    // With the new SpeedControlGroups, do we have to do this ourselves anymore?
    // leftSlave.set(ControlMode.Follower, leftMaster.getDeviceID());
    // rightSlave.set(ControlMode.Follower, rightMaster.getDeviceID());


    left = new SpeedControllerGroup(leftMaster, leftSlave);
    right = new SpeedControllerGroup(rightMaster, rightSlave);

    leftMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
    rightMaster.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);

    rightMaster.setSensorPhase(true);
    leftMaster.setSensorPhase(true);

    turningController =
        new PIDController(0.05, 0.005, 0.05, navx, output -> this.turnOutput = output);

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


    // robotDrive = new DifferentialDrive(left, right);

    navx = new AHRS(Port.kMXP, (byte) 50); 

    navx.registerCallback(
            (long systemTimestamp, long sensorTimestamp, AHRSUpdateBase sensorData, Object context) -> {
              updatePosition(sensorData.yaw);
     /*         turningController.setP(SmartDashboard.getNumber("DriveP", 1));
              turningController.setI(SmartDashboard.getNumber("DriveI", 0.01));
              turningController.setD(SmartDashboard.getNumber("DriveD", 0.5));*/
            }, new Object());

    //calibrateYaw();
        
    
        
    System.out.println(navx.isConnected() ? "00000000000000000000000000000Connected" : "00000000000000000000Not Connected");
  }
  
  public double getPitch() {
    return (double) navx.getPitch();
  }
  
  public double getRoll() {
    return (double) navx.getRoll();
  }

  public void talonCurrent() {
    Arrays.asList(leftMaster, rightMaster, leftSlave, rightSlave)
        .forEach((WPI_TalonSRX talon) -> SmartDashboard
            .putNumber(((Integer) talon.getDeviceID()).toString(), talon.getOutputCurrent()));
  }
  
  public void zeroEncoders() {
    leftMaster.setSelectedSensorPosition(0, 0, 0);
    rightMaster.setSelectedSensorPosition(0, 0, 0);

  }


  double rampRate;

  public void allDrive(double throttle, double rotate) {

    // rampRate = SmartDashboard.getNumber("Strafe Ramp Rate", 0.3);
    //robotDrive.arcadeDrive(-throttle, -rotate);
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

  public double getEncoderDistance(DriveSide driveSide) {
    if (driveSide == DriveSide.LEFT) {
      return (leftSlave.getSelectedSensorPosition(0) / 1024.0) * RobotMap.GEARING_FACTOR
          * RobotMap.WHEEL_CIRCUMFERENCE;
    } else {
      return (-rightSlave.getSelectedSensorPosition(0) / 1024.0) * RobotMap.GEARING_FACTOR
          * RobotMap.WHEEL_CIRCUMFERENCE;
    }
  }

  public void configTeleop() {
    
    //System.out.println("Differential Drive Exists");
    if (robotDrive == null) {
      //robotDrive = new DifferentialDrive(left, right);
    }
  }

  public void printEncoder() {
    SmartDashboard.putNumber("Left Encoder",
        leftMaster.getSelectedSensorPosition(0) / 1024.0 * RobotMap.WHEEL_CIRCUMFERENCE);
    SmartDashboard.putNumber("Right Encoder",
        rightMaster.getSelectedSensorPosition(0) / 1024.0 * RobotMap.WHEEL_CIRCUMFERENCE);
    //SmartDashboard.putData("Drive Distance Command", new DriveDistance(512.0, 1.00, 1.0));
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
    // turningController.setToleranceBuffer(1);
    turningController.setContinuous(true);
    turningController.setSetpoint(temp);
  }
  
  public boolean isTurnOnTarget() {
    return turningController.onTarget();
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

  public void configClosedLoop(double p, double i, double d) {
    leftMaster.config_kP(0, p, 0);
    leftMaster.config_kI(0, i, 0);
    leftMaster.config_kD(0, d, 0);

    rightMaster.config_kP(0, p, 0);
    rightMaster.config_kI(0, i, 0);
    rightMaster.config_kD(0, d, 0);

  }

  public void endClosedLoop() {
    // rightMaster.setInverted(false);
    // rightSlave.setInverted(false);
  }

  public void PIDDrive(double dist) {

    leftMaster.setSelectedSensorPosition(0, 0, 0);
    rightMaster.setSelectedSensorPosition(0, 0, 0);
 //   leftMaster.configAllowableClosedloopError(0, 5, 0);
 //   rightMaster.configAllowableClosedloopError(0, 5, 0);
    leftMaster.set(ControlMode.Position,
        /*leftMaster.getSelectedSensorPosition(0) / 1024.0 * RobotMap.WHEEL_CIRCUMFERENCE*/
            -dist * 1024.0 * 1.25 / RobotMap.WHEEL_CIRCUMFERENCE);
    rightMaster.set(ControlMode.Position,
        /*rightMaster.getSelectedSensorPosition(0) / 1024.0 * RobotMap.WHEEL_CIRCUMFERENCE
            +*/ dist * 1024.0 * 1.25 / RobotMap.WHEEL_CIRCUMFERENCE);
    // leftSlave.set(ControlMode.Position, leftMaster.getSelectedSensorPosition(0) / 1024.0 *
    // RobotMap.WHEEL_CIRCUMFERENCE + dist * 1024.0 / RobotMap.WHEEL_CIRCUMFERENCE);
    // rightSlave.set(ControlMode.Position, leftMaster.getSelectedSensorPosition(0) / 1024.0 *
    // RobotMap.WHEEL_CIRCUMFERENCE + dist * 1024.0 / RobotMap.WHEEL_CIRCUMFERENCE);

    System.out.println("Destination: " + -dist * 1024.0 / RobotMap.WHEEL_CIRCUMFERENCE);

  }

  public void logInversion() {
    SmartDashboard.putBoolean("Left Master Inversion", leftMaster.getInverted());
    SmartDashboard.putBoolean("Right Master Inversion", rightMaster.getInverted());
    SmartDashboard.putBoolean("Left Slave Inversion", leftSlave.getInverted());
    SmartDashboard.putBoolean("Right Slave Inversion", rightSlave.getInverted());
   // SmartDashboard.putData("DifferentialDrive", robotDrive);

  }
  
  //-1 to 1 input
  public void autoTurn(double speed) {
    rightMaster.set(ControlMode.PercentOutput, speed);
    rightSlave.set(ControlMode.PercentOutput, speed);
    leftMaster.set(ControlMode.PercentOutput, speed);
    leftSlave.set(ControlMode.PercentOutput, speed);
  }

  public double getClosedLoopError(DriveSide side) {
    if (side == DriveSide.LEFT) {
      return leftMaster.getClosedLoopError(0) / 1024.0 * RobotMap.WHEEL_CIRCUMFERENCE;
    } else {
      return rightMaster.getClosedLoopError(0) / 1024.0 * RobotMap.WHEEL_CIRCUMFERENCE;

    }
  }

  /**
   * Sets initial yaw based on where our starting position is.
   */
  public void calibrateYaw() {
    if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.Blue) {
      rotationOffset = -90.0;
    } else {
      rotationOffset = 90.0;
    }
  }

  public void zeroYaw() {
    navx.zeroYaw();
  }

  /**
   * Updates the dead reckoning for our current position.
   */
  public void updatePosition(double navXYaw) {
    rotation = temporaryFixDegrees(navXYaw + rotationOffset);

    double encoderLeft = getEncoderDistance(DriveSide.LEFT);
    double encoderRight = getEncoderDistance(DriveSide.RIGHT);

    double deltaEncoderLeft = encoderLeft - prevEncoderLeft;
    double deltaEncoderRight = encoderRight - prevEncoderRight;

    double deltaInches = (deltaEncoderLeft + deltaEncoderRight) / 2;

    absoluteDistanceTraveled += Math.abs(deltaInches);

    positionX = positionX + Math.cos(Math.toRadians(90 - rotation)) * deltaInches;
    positionY = positionY + Math.sin(Math.toRadians(90 - rotation)) * deltaInches;

    prevEncoderLeft = encoderLeft;
    prevEncoderRight = encoderRight;
    
    SmartDashboard.putNumber("Robot Direction", getRobotPosition().getDirection());
   // SmartDashboard.putNumber("Turn setPoint", turningController.getSetpoint());
  }
  
  public RobotPosition getRobotPosition() {
    return new RobotPosition(positionX, positionY, rotation);
  }
  
  public double turnError() {
    return turningController.getError();
  }

}


