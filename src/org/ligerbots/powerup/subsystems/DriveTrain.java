package org.ligerbots.powerup.subsystems;

import com.ctre.phoenix.*;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Arrays;

import org.ligerbots.powerup.RobotMap;

/**
 *
 */
public class DriveTrain extends Subsystem {

  // Put methods for controlling this subsystem
  // here. Call these from Commands.
  TalonSRX leftMaster;
  boolean navxOn = false;
  TalonSRX leftSlave;
  TalonSRX rightMaster;
  TalonSRX rightSlave;
  SpeedControllerGroup left;
  SpeedControllerGroup right;
  DifferentialDrive robotDrive;
  PIDController turningController;
  double turnOutput = 0;
  double limitedStrafe = 0;
  TalonID[] talons;

  AHRS navx;

  public class TalonID {
    int talonID;
    TalonSRX talon;

    public TalonID(int talonID, TalonSRX talon) {
      this.talonID = talonID;
      this.talon = talon;
    }

  }

  public DriveTrain() {


    SmartDashboard.putNumber("Strafe Ramp Rate", 0.08);

    leftMaster = new TalonSRX(RobotMap.CT_LEFT_1);
    leftSlave = new TalonSRX(RobotMap.CT_LEFT_2);
    rightMaster = new TalonSRX(RobotMap.CT_RIGHT_1);
    rightSlave = new TalonSRX(RobotMap.CT_RIGHT_2);

    // With the new SpeedControlGroups, do we have to do this ourselves anymore?
    // leftSlave.set(ControlMode.Follower, leftMaster.getDeviceID());
    // rightSlave.set(ControlMode.Follower, rightMaster.getDeviceID());


    left = new SpeedControllerGroup((SpeedController) leftMaster, (SpeedController) leftSlave);
    right = new SpeedControllerGroup((SpeedController) rightMaster, (SpeedController) rightSlave);



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
        .forEach((TalonSRX talon) -> talon.setNeutralMode(NeutralMode.Brake));

    talons = new TalonID[] {new TalonID(RobotMap.CT_LEFT_1, leftMaster),
        new TalonID(RobotMap.CT_LEFT_2, leftSlave), new TalonID(RobotMap.CT_RIGHT_1, rightMaster),
        new TalonID(RobotMap.CT_RIGHT_2, rightSlave)};

    robotDrive = new DifferentialDrive(left, right);

    navx = new AHRS(SPI.Port.kMXP, (byte) 200);

    turningController =
        new PIDController(0.045, 0.004, 0.06, navx, output -> this.turnOutput = output);
  }

  double rampRate;

  public void allDrive(double throttle, double rotate) {

    rampRate = SmartDashboard.getNumber("Strafe Ramp Rate", 0.3);
    robotDrive.arcadeDrive(throttle, rotate);
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

  public boolean getNavXOn() {
    return navxOn;
  }

  public void toggleNavXControl() {
    if (navxOn) {
      navxOn = false;
    } else {
      navxOn = true;
    }
  }

  public void checkTalonVoltage() {
    for (TalonID talon : talons) {
      SmartDashboard.putNumber(((Integer) talon.talonID).toString(),
          talon.talon.getMotorOutputVoltage() * talon.talon.getOutputCurrent());
    }
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
    turningController.setToleranceBuffer(1);
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

  public void zeroYaw() {
    navx.zeroYaw();
  }
}

