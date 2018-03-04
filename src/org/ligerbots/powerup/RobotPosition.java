package org.ligerbots.powerup;


public class RobotPosition extends FieldPosition {
  /**
   * The direction the robot is facing in degrees clockwise, where 0.0 degrees 
   * is facing the long way down the field. (Note -- this is different from 2017!)
   */
    enum Action {
  	NOTHING,
  	PUT_ON_SWITCH,
  	PUT_ON_SCALE,
  	PICK_UP_CUBE
    };
    
    protected double direction;
    Action action;

    public RobotPosition(double x, double y) {
      super(x, y);
      this.direction = 0.0;
      action = Action.NOTHING;
    }
    
  public RobotPosition(double x, double y, double direction) {
    super(x, y);
    this.direction = direction;
    action = Action.NOTHING;
  }
  
  public RobotPosition(double x, double y, double direction, Action action) {
    super(x, y);
    this.direction = direction;
    this.action = action;
  }

  public void setRobotPosition(double x, double y, double direction) {
	  super.x = x;
	  super.y = y;
	  this.direction = direction;
  }
  
  public double getDirection() {
    return direction;
  }

  @Override
  public String toString() {
    return "RobotPosition [direction=" + direction + ", x=" + x + ", y=" + y + "]";
  }
  
  
}
