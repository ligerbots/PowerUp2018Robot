package org.ligerbots.powerup.subsystems;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.Faults;
import com.ctre.phoenix.motorcontrol.StickyFaults;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
    StickyFaults intakeMasterFaults;
    boolean intakePresent;
    private DoubleSolenoid solenoid;
    

    public Intake() {
    
      SmartDashboard.putNumber("Intake Speed", 0.7);
      SmartDashboard.putNumber("Out-take Speed", 0.65);
      
      intakeMaster = new WPI_TalonSRX(RobotMap.CT_INTAKE_1);
      intakeMasterFaults = new StickyFaults();
      intakePresent = (intakeMaster.getStickyFaults(intakeMasterFaults) == ErrorCode.OK);
      System.out.println("Intake master Talon is " + (intakePresent ? "Present" : "NOT Present"));
      
      if (intakePresent) {
	      intakeSlave = new WPI_TalonSRX(RobotMap.CT_INTAKE_2);
	      solenoid = new DoubleSolenoid(RobotMap.PCM_ID, 0, 1);
	      
	      intakeSlave.setInverted(true);
	      intakeSlave.set(ControlMode.Follower, RobotMap.CT_INTAKE_1);
      }
      
      intakeMaster.configPeakCurrentLimit(20, 0);
      intakeSlave.configPeakCurrentLimit(20, 0);
      intakeMaster.configPeakCurrentDuration(10, 0);
      intakeSlave.configPeakCurrentDuration(10, 0);
      intakeMaster.configContinuousCurrentLimit(15, 0);
      intakeSlave.configContinuousCurrentLimit(15, 0);
      intakeMaster.enableCurrentLimit(true);
      intakeSlave.enableCurrentLimit(true);



    }
    
    public void intakeOn(boolean reverse, double speed) {
    	if (intakePresent) {
	        if (reverse) {
	          intakeMaster.set(-speed);
	        }
	        else {
	          intakeMaster.set(speed);
	        }
    	}
    }
    
    public void setSlave(boolean slave, double speed) {
    	if (intakePresent) {
	      if (slave) {
	        intakeSlave.set(ControlMode.Follower, RobotMap.CT_INTAKE_1);
	      }
	      else {
	        intakeSlave.set(ControlMode.PercentOutput, speed);
	      }
    	}
    }
    
    public void setPistons(boolean open) {
    	if (intakePresent) {
	      if (open) {
	        solenoid.set(Value.kReverse);
	      }
	      else {
	        solenoid.set(Value.kForward);
	      }
    	}
    }
    

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

