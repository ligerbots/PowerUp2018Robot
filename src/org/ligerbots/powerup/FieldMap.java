package org.ligerbots.powerup;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.ligerbots.powerup.RobotPosition;
import org.ligerbots.powerup.RobotPosition.Action;
import org.ligerbots.powerup.FieldPosition;

/*
 * All positions are in inches
 * 
 * To understand this map, refer to the diagram at
 * http://team.ligerbots.com/home/2018/2018-game-info
 */

public class FieldMap {
    
    public static final double scaleScoringHeight = 70.0;   // 70" is our max elevator height
    public static final double switchScoringHeight = 18.0;
    
    // Robot dimensions with bumpers
    public static final double robotWidth = 27.125 + 7.0;
    public static final double robotLength = 32.0 + 7.0;
    // half dimensions - for getting the center point of the robot
    public static final double rW2 = robotWidth/2.0;
    public static final double rL2 = robotLength/2.0;
    
    public RobotPosition[] startPositions = new RobotPosition[5];   // position 0 is not used! (because the diagrom is 1-based)
    
    // The positions for our scoring positions on all platforms is symmetric around the X axis
    // in other words, if one platform is at Y, then the other is at -Y
    
    public RobotPosition[] switchScoringSpot = new RobotPosition[3];    
    public RobotPosition[] scaleScoringSpot = new RobotPosition[2]; 

    public RobotPosition scaleScoringSpotAlpha; // The normal "inner" scoring position
    public RobotPosition scaleScoringSpotBeta;  // Acessing the platform from the outside
    
    // Way Points are the intermediate points on the way to a particular scoring position
    // They are meant to be universal -- the first point should be something that can be
    // reached in a straight line from any starting position. In practice, some might not
    // work so well. We depend on the user to not choose a waypoint that would drive
    // clear across the field, or that is likely to collide with an alliance partner
    public static ArrayList<RobotPosition> wayPointsA = new ArrayList<RobotPosition>();
    public static ArrayList<RobotPosition> wayPointsB = new ArrayList<RobotPosition>();
    public static ArrayList<RobotPosition> wayPointsAlpha = new ArrayList<RobotPosition>();
    public static ArrayList<RobotPosition> wayPointsBeta = new ArrayList<RobotPosition>();

    
    FieldMap () {
        // start positions are in terms of the robot center
        startPositions[1] = new RobotPosition(rW2, 117.6, 0.0);  // 1
        startPositions[2] = new RobotPosition(rW2, 81.0, 0.0);   // 2
        startPositions[3] = new RobotPosition(rW2, -7.6, 0.0);   // 3
        startPositions[4] = new RobotPosition(rW2, -54.0, 0.0);  // 4
        startPositions[5] = new RobotPosition(rW2, -96.0, 0.0);  // 5
        
        // scoring positions are also robot center
        switchScoringSpot[0] = new RobotPosition(139.5-rW2, 54.8, 0.0, Action.PUT_ON_SWITCH);
        switchScoringSpot[1] = new RobotPosition(167.3, 76.0+rL2, -90.0, Action.PUT_ON_SWITCH);
        //switchScoringSpot[2] = new RobotPosition(209.9+rW2, 54.8, 180.0);
        
        scaleScoringSpot[0] = new RobotPosition(297.3-rW2, 84.4, 0.0, Action.PUT_ON_SCALE);
        scaleScoringSpot[1] = new RobotPosition(323.2, 92.6+rL2, -90.0, Action.PUT_ON_SCALE);
                
        // TODO -- create waypoints corresponding to each scoring position
        //         that will ensure the robot doesn't crash into things
        //         or enter forbidden areas
        // Waypoints are just field positions. Don't need angles.
        
        // There's an implicit waypoint for all robots 8" out from their starting position
        // to ensure there's enough space for them to rotate without hitting the back wall
        
        wayPointsA.add(new RobotPosition(90, 54.8));
        wayPointsA.add(switchScoringSpot[0]);
        
        wayPointsB.add(new RobotPosition(143.0, 114.0));
        wayPointsB.add(new RobotPosition(143.0+62.0, 114.0));
        wayPointsB.add(switchScoringSpot[1]);
        
        wayPointsAlpha.add(new RobotPosition(120.0, 100.0));
        wayPointsAlpha.add(new RobotPosition(185.0, 100.0));
        wayPointsAlpha.add(new RobotPosition(240.0, 84.4));
        wayPointsAlpha.add(scaleScoringSpot[0]);

        wayPointsBeta.add(new RobotPosition(120.0, -00.0));
        wayPointsBeta.add(new RobotPosition(237.5, 135.0));
        wayPointsBeta.add(new RobotPosition(240.0, 84.4));
        wayPointsBeta.add(new RobotPosition(323.2, 132.6));
        wayPointsBeta.add(scaleScoringSpot[1]);
        
        FieldPosition x = wayPointsBeta.get(0);

    };
    
  
    
    public static List<FieldPosition> generateCatmullRomSpline(List<FieldPosition> controlPoints) {
      LinkedList<FieldPosition> output = new LinkedList<>();

      for (int i = 1; i < controlPoints.size() - 2; i++) {
        FieldPosition p0 = controlPoints.get(i - 1);
        FieldPosition p1 = controlPoints.get(i);
        FieldPosition p2 = controlPoints.get(i + 1);
        FieldPosition p3 = controlPoints.get(i + 2);

        generateSegment(p0, p1, p2, p3, output, i == 1);
      }

      return output;
    }

    /**
     * Generates a Catmull-Rom spline segment.
     * 
     * @param p0 Control point
     * @param p1 Control point
     * @param p2 Control point
     * @param p3 Control point
     * @param output The list to add points to
     */
    private static void generateSegment(FieldPosition p0, FieldPosition p1, FieldPosition p2,
        FieldPosition p3, List<FieldPosition> output, boolean isFirst) {
      int numPoints = (int) Math.ceil(p1.distanceTo(p2) / 4.0);
      if (numPoints < 5) {
        numPoints = 5;
      }

      double t0 = 0;
      double t1 = calculateT(t0, p0, p1);
      double t2 = calculateT(t1, p1, p2);
      double t3 = calculateT(t2, p2, p3);

      double deltaT = Math.abs(t2 - t1) / (numPoints - 1);

      for (int i = isFirst ? 0 : 1; i < numPoints; i++) {
        double ti = i * deltaT + t1;
        FieldPosition a1 = p0.multiply((t1 - ti) / (t1 - t0)).add(p1.multiply((ti - t0) / (t1 - t0)));
        FieldPosition a2 = p1.multiply((t2 - ti) / (t2 - t1)).add(p2.multiply((ti - t1) / (t2 - t1)));
        FieldPosition a3 = p2.multiply((t3 - ti) / (t3 - t2)).add(p3.multiply((ti - t2) / (t3 - t2)));

        FieldPosition b1 = a1.multiply((t2 - ti) / (t2 - t0)).add(a2.multiply((ti - t0) / (t2 - t0)));
        FieldPosition b2 = a2.multiply((t3 - ti) / (t3 - t1)).add(a3.multiply((ti - t1) / (t3 - t1)));

        output.add(b1.multiply((t2 - ti) / (t2 - t1)).add(b2.multiply((ti - t1) / (t2 - t1))));
      }
    }

    private static final double alpha = 0.5;

    private static double calculateT(double ti, FieldPosition p0, FieldPosition p1) {
      double x0 = p0.x;
      double y0 = p0.y;
      double x1 = p1.x;
      double y1 = p1.y;

      double dx = x1 - x0;
      double dy = y1 - y0;

      return Math.pow(Math.sqrt(dx * dx + dy * dy), alpha) + ti;
    }
    



}
 
