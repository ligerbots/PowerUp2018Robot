package org.ligerbots.powerup;

public class FieldPosition {

  final double x;
  final double y;
  
  public FieldPosition(double x, double y) {
    this.x = x;
    this.y = y;
  }
  
  public FieldPosition add(FieldPosition other) {
    return new FieldPosition(other.x, other.y);
  }
  
  public FieldPosition add(double x, double y) {
    return new FieldPosition(this.x + x, this.y + y);
  }
  
  public FieldPosition multiply(double xFactor, double yFactor) {
    return new FieldPosition (x * xFactor, y * yFactor);
  }
  
  public double angleTo(FieldPosition other) {
    return Math.toDegrees(Math.atan2(other.y - y, other.x - x));
  }
  
  public double distanceTo(FieldPosition other) {
    double dx = other.x - x;
    double dy = other.y - y;
    return Math.sqrt(dx * dx + dy * dy);
  }
}
