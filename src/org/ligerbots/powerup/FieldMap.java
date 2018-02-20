package org.ligerbots.powerup;

import java.util.ArrayList;

import org.ligerbots.powerup.RobotPosition;

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
    public static ArrayList<FieldPosition> wayPointsA = new ArrayList<FieldPosition>();
    public static ArrayList<FieldPosition> wayPointsB = new ArrayList<FieldPosition>();
    public static ArrayList<FieldPosition> wayPointsAlpha = new ArrayList<FieldPosition>();
    public static ArrayList<FieldPosition> wayPointsBeta = new ArrayList<FieldPosition>();

    
    FieldMap () {
        // start positions are in terms of the robot center
        startPositions[1] = new RobotPosition(rW2, 117.6, 0.0);  // 1
        startPositions[2] = new RobotPosition(rW2, 81.0, 0.0);   // 2
        startPositions[3] = new RobotPosition(rW2, -7.6, 0.0);   // 3
        startPositions[4] = new RobotPosition(rW2, -54.0, 0.0);  // 4
        startPositions[5] = new RobotPosition(rW2, -96.0, 0.0);  // 5
        
        // scoring positions are also robot center
        switchScoringSpot[0] = new RobotPosition(139.5-rW2, 54.8, 0.0);
        switchScoringSpot[1] = new RobotPosition(167.3, 76.0+rL2, -90.0);
        //switchScoringSpot[2] = new RobotPosition(209.9+rW2, 54.8, 180.0);
        
        scaleScoringSpot[0] = new RobotPosition(297.3-rW2, 84.4, 0.0);
        scaleScoringSpot[1] = new RobotPosition(323.2, 92.6+rL2, -90.0);
                
        // TODO -- create waypoints corresponding to each scoring position
        //         that will ensure the robot doesn't crash into things
        //         or enter forbidden areas
        // Waypoints are just field positions. Don't need angles.
        
        // There's an implicit waypoint for all robots 8" out from their starting position
        // to ensure there's enough space for them to rotate without hitting the back wall
        
        wayPointsA.add(new FieldPosition(90, 54.8));
        wayPointsA.add(switchScoringSpot[0]);
        
        wayPointsB.add(new FieldPosition(143.0, 114.0));
        wayPointsB.add(new FieldPosition(143.0+62.0, 114.0));
        wayPointsB.add(switchScoringSpot[1]);
        
        wayPointsAlpha.add(new FieldPosition(120.0, 100.0));
        wayPointsAlpha.add(new FieldPosition(185.0, 100.0));
        wayPointsAlpha.add(new FieldPosition(240.0, 84.4));
        wayPointsAlpha.add(scaleScoringSpot[0]);

        wayPointsBeta.add(new FieldPosition(120.0, -00.0));
        wayPointsBeta.add(new FieldPosition(237.5, 135.0));
        wayPointsBeta.add(new FieldPosition(240.0, 84.4));
        wayPointsBeta.add(new FieldPosition(323.2, 132.6));
        wayPointsBeta.add(scaleScoringSpot[1]);

    };
      



}
 
