package org.ligerbots.powerup;

import org.ligerbots.powerup.FieldPosition;

/*
 * All positions are in inches
 */

public class FieldMap {
	
	public static final double scaleScoringheight = (8.0 * 12.0);	// 8 feet high (in inches)
	
	public FieldPosition[] startPositions = new FieldPosition[6];	// position 0 is not used! (because the diagrom is 1-based)
	
	// The positions for our scoring positions on all platforms is symmetric around the X axis
	// in other words, if one platform is at Y, then the other is at -Y
	
	public FieldPosition switchScoringSpotA;	// The "normal" switch scoring positions
	public FieldPosition switchScoringSpotB;	// The "inner" positions, if we need to avoid our partners
	public FieldPosition switchScoringSpotE;	// The outside edges of the switch -- to avoid our partners entirely
	
	public FieldPosition scaleScoringSpotAlpha;	// The normal "inner" scoring position
	public FieldPosition scaleScoringSpotBeta;	// Acessing the platform from the outside
	
	FieldMap () {
		
		startPositions[1] = new FieldPosition(0.0, 76.7);
	};
	  



}
