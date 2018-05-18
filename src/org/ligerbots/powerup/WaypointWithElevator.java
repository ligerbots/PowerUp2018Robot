package org.ligerbots.powerup;

import jaci.pathfinder.Waypoint;

public class WaypointWithElevator extends Waypoint {

  double elevatorHeight;
  
  public WaypointWithElevator(double x, double y, double angle, double elevatorHeight) {
    super(x, y, angle);
    this.elevatorHeight = elevatorHeight;
  }
  
  public WaypointWithElevator(double x, double y, double angle) {
    super(x, y, angle);
    this.elevatorHeight = Robot.elevator.getDesiredHeight();
  }

}
