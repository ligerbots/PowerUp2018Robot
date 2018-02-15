package org.ligerbots.powerup;

import org.ligerbots.powerup.FieldPosition;

/*
 * All positions are in inches
 * 
 * To understand this map, refer to the diagram at
 * http://team.ligerbots.com/home/2018-game-info 
 */

public class FieldMap {
	
	public static final double scaleScoringheight = (8.0 * 12.0);	// 8 feet high (in inches)
	
	public FieldPosition[] startPositions = new FieldPosition[5];	// position 0 is not used! (because the diagrom is 1-based)
	
	// The positions for our scoring positions on all platforms is symmetric around the X axis
	// in other words, if one platform is at Y, then the other is at -Y
	
	public FieldPosition[] switchScoringSpot = new FieldPosition[3];	
	public FieldPosition[] scaleScoringSpot = new FieldPosition[2];	

	
	
	public FieldPosition scaleScoringSpotAlpha;	// The normal "inner" scoring position
	public FieldPosition scaleScoringSpotBeta;	// Acessing the platform from the outside
	
	FieldMap () {
		startPositions[1] = new FieldPosition(0.0, 81);
		startPositions[2] = new FieldPosition(0.0, 108);
		startPositions[3] = new FieldPosition(0.0, -8.0);
		startPositions[4] = new FieldPosition(0.0, -54.0);
		startPositions[4] = new FieldPosition(0.0, -96.0);
		
		switchScoringSpot[0] = new FieldPosition(166.2, 119.4);
		switchScoringSpot[1] = new FieldPosition(97.2, 54.8);
		switchScoringSpot[2] = new FieldPosition(252.0, 54.8);
		
		scaleScoringSpot[0] = new FieldPosition(323.2, 134.5);
		scaleScoringSpot[1] = new FieldPosition(254.8, 90.6);
		
				
		// TODO -- create waypoints corresponding to each scoring position
	    //         that will ensure the robot doesn't crash into things
	    //         or enter forbidden areas
		
	};
	  



}
