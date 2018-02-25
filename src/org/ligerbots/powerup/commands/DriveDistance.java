package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.subsystems.DriveTrain;
import org.ligerbots.powerup.subsystems.DriveTrain.DriveSide;

/**
 *
 */
public class DriveDistance extends Command {

    double offsetInches;
    double startingLeft;
    double startingRight;
    double tolerance;
    double startAngle;
    double angleTolerance;
    double currentLeft;
    double currentRight;
    double currentInches;
    double delta;
    double error;
    boolean onTarget = false;
    double timeStarted;
    public DriveDistance(double offsetInches, double tolerance, double angleTolerance) {
      System.out.printf("Drive Distance Constructed: %5.2f inches, %5.2f tolerance, %5.2f angleTolerance.\n",
    		  			offsetInches, tolerance, angleTolerance);
      requires(Robot.driveTrain);
      this.offsetInches = offsetInches;
      this.tolerance = tolerance;
      this.angleTolerance = angleTolerance;
      
    }

    // Called just before this Command runs the first time
    protected void initialize() {
      SmartDashboard.putBoolean("Drive Distance Done", false);
      onTarget = false;
      timeStarted = Robot.time();
      System.out.printf("Drive Distance started: %5.2f inches, %5.2f tolerance, %5.2f angleTolerance.\n",
	  			offsetInches, tolerance, angleTolerance);      
      startingLeft = Robot.driveTrain.getEncoderDistance(DriveSide.LEFT);
      startingRight = Robot.driveTrain.getEncoderDistance(DriveSide.RIGHT);
      startAngle = Robot.driveTrain.getAngle();
      double p = SmartDashboard.getNumber("Drive P", 3.0);
      double i = SmartDashboard.getNumber("Drive I", 2.0);
      double d = SmartDashboard.getNumber("Drive D", 0.0);
      Robot.driveTrain.PIDDrive(offsetInches);
      Robot.driveTrain.configClosedLoop(p, i, d);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    //  Robot.driveTrain.enableTurningControl(startAngle - Robot.driveTrain.getYaw(), angleTolerance);
      
      currentLeft = Robot.driveTrain.getEncoderDistance(DriveSide.LEFT);
      currentRight = Robot.driveTrain.getEncoderDistance(DriveSide.RIGHT);
      
      delta = (currentLeft - startingLeft);
      
      error = Math.abs(delta - offsetInches);
      
      
      onTarget = Robot.driveTrain.getClosedLoopError(DriveSide.LEFT) < tolerance && Robot.driveTrain.getClosedLoopError(DriveSide.RIGHT) < tolerance;
      // An untuned PID loop has a tendency the never complete and/or the stuff above just doesn't work
      // So let's see if we've passed the target
      onTarget = onTarget || Math.abs(delta) > Math.abs(offsetInches);
      
      if ((Robot.ticks % 5) == 0)
    	  System.out.printf("Drive Distance distance travelled: %5.2f, PID Left: %5.2f PID Right: %5.2f\n",
    			  delta, Robot.driveTrain.getClosedLoopError(DriveSide.LEFT), Robot.driveTrain.getClosedLoopError(DriveSide.RIGHT));        
     // Robot.driveTrain.allDrive(Math.signum(offsetInches), Robot.driveTrain.getTurnOutput());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	double timePassed = Robot.time() - timeStarted; 
        
    	// if we're managing less than 0.5 fps, with some allowance for startup, it's time to give up
    	double fps = (Math.abs(delta+4.0)/12.0) / timePassed; 
    	if (fps < 0.5) {
    		System.out.printf("DriveDistance %5.2f fps too slow. Timeout out!\n", fps);
    		return true;
    	}
        else return onTarget;
    }
    

    // Called once after isFinished returns true
    protected void end() {
        System.out.println("Drive Distance Done");
        SmartDashboard.putBoolean("Drive Distance Done", true);
        Robot.driveTrain.endClosedLoop();
     //   Robot.driveCommand.start();
     //  Robot.driveTrain.disablePID();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
      System.out.println("Drive Distance was interrupted");
      Robot.driveTrain.endClosedLoop();
    }
}
