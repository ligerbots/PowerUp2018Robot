package org.ligerbots.powerup;

public class SensorData {
    
    public double[] values;
    
    public SensorData(double throttle, double rotate, double encoderLeft, double encoderRight, double accelX, double accelY, double gyro) {
      this.values = new double[] {throttle, rotate, encoderLeft, encoderRight, accelX, accelY, gyro};
    }
    
    public SensorData(double[] values) {
      if (values.length == 7) this.values = values;
    }
    
    @Override 
    public String toString() {
      return String.format("%5.2f,%5.2f,%5.2f,%5.2f,%5.2f,%5.2,%5.2", values[0], values[1], values[2], values[3], values[4], values[5], values[6]);
    }
    
    public static SensorData getSnapFromString (String input) {
      String[] pieces = input.split(",");
      return new SensorData(Double.parseDouble(pieces[0]), Double.parseDouble(pieces[1]), 
          Double.parseDouble(pieces[2]), Double.parseDouble(pieces[3]), 
          Double.parseDouble(pieces[4]), Double.parseDouble(pieces[5]), 
          Double.parseDouble(pieces[6]));
    }
}
