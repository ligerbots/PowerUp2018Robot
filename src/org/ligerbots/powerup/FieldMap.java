package org.ligerbots.powerup;

import org.ligerbots.powerup.RobotPosition;

/*
 * All positions are in inches
 * 
 * To understand this map, refer to the diagram at
 * http://team.ligerbots.com/home/2018-game-info 
 */

public class FieldMap {
    
    public static final double scaleScoringheight = (8.0 * 12.0);   // 8 feet high (in inches)
    
    // Robot dimensions with bumpers
    public static final double robotWidth = 27.125 + 7.0;
    public static final double robotLength = 32.0 + 7.0;
    // half dimensions - for getting the center point of the robot
    public static final double rW2 = robotWidth/2.0;
    public static final double rL2 = robotWidth/2.0;
    
    public RobotPosition[] startPositions = new RobotPosition[5];   // position 0 is not used! (because the diagrom is 1-based)
    
    // The positions for our scoring positions on all platforms is symmetric around the X axis
    // in other words, if one platform is at Y, then the other is at -Y
    
    public RobotPosition[] switchScoringSpot = new RobotPosition[3];    
    public RobotPosition[] scaleScoringSpot = new RobotPosition[2]; 

    public RobotPosition scaleScoringSpotAlpha; // The normal "inner" scoring position
    public RobotPosition scaleScoringSpotBeta;  // Acessing the platform from the outside
    
    FieldMap () {
        // start positions are in terms of the robot center
        startPositions[1] = new RobotPosition(rW2, 81., 0.0);   // A
        startPositions[2] = new RobotPosition(rW2, 108, 0.0);   // B
        startPositions[3] = new RobotPosition(rW2, -54.0, 0.0); // C
        startPositions[4] = new RobotPosition(rW2, -96.0, 0.0); // D
        
        // scoring positions are where we want the nose of the robot to end up
        switchScoringSpot[0] = new RobotPosition(167.3, 76.0, -90.0);
        switchScoringSpot[1] = new RobotPosition(139.5, 54.8, 0.0);
        switchScoringSpot[2] = new RobotPosition(209.9, 54.8, 180.0);
        
        scaleScoringSpot[0] = new RobotPosition(323.2, 92.6, -90.0);
        scaleScoringSpot[1] = new RobotPosition(297.3, 84.4, 0.0);
        
                
        // TODO -- create waypoints corresponding to each scoring position
        //         that will ensure the robot doesn't crash into things
        //         or enter forbidden areas
        // Waypoints are just field positions. Don't need angles.
        
    };
      



}
