package org.ligerbots.powerup.commands;

import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import org.ligerbots.powerup.FieldMap;
import org.ligerbots.powerup.Robot;
import org.ligerbots.powerup.RobotMap;
import org.ligerbots.powerup.subsystems.DriveTrain.DriveSide;

/**
 *
 */
public class PathfinderDrivePath extends Command {

    
    
    TankModifier modifier;
    
    Trajectory trajLeft;
    Trajectory trajRight;
    
    EncoderFollower left;
    EncoderFollower right;
    
    double l;
    double r;
    
    double yaw;
    double desiredYaw;
    
    double angleDifference;
    double turn;
    
    public PathfinderDrivePath(Trajectory traj) {
      
      modifier = new TankModifier(traj);
      
    }

    protected void initialize() {
      
      modifier.modify(RobotMap.DIST_BETWEEN_WHEELS);
      
      trajLeft = modifier.getLeftTrajectory();
      trajRight = modifier.getRightTrajectory();
      
      left = new EncoderFollower(trajLeft);
      right = new EncoderFollower(trajRight);
      
      left.configureEncoder(Robot.driveTrain.getRawEncoderDistance(DriveSide.LEFT), 1024, 3.92);
      right.configureEncoder(Robot.driveTrain.getRawEncoderDistance(DriveSide.RIGHT), 1024, 3.92);
      
      left.configurePIDVA(1.0, 0.0, 0.0, 1/FieldMap.maxJerk, 0.0);
      right.configurePIDVA(1.0, 0.0, 0.0, 1/FieldMap.maxJerk, 0.0);

    }

    protected void execute() {
      
      l = left.calculate(Robot.driveTrain.getRawEncoderDistance(DriveSide.LEFT));
      r = right.calculate(Robot.driveTrain.getRawEncoderDistance(DriveSide.RIGHT));

      yaw = Robot.driveTrain.getRobotPosition().getDirection();
      desiredYaw = Pathfinder.r2d(left.getHeading()); // Should also be in degrees

      angleDifference = Pathfinder.boundHalfDegrees(desiredYaw - yaw);
      turn = -0.01 * angleDifference;
      
      Robot.driveTrain.tankDrive(l+turn, r-turn);
      
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
      
    }

    protected void interrupted() {
      
    }
}
