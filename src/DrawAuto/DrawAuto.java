package DrawAuto;

import javax.swing.JFrame;
import org.ligerbots.powerup.FieldMap;

public class DrawAuto {

  public DrawAuto() {
    // TODO Auto-generated constructor stub
  }

  public static void main(String[] args) {
    
     // @SuppressWarnings("unused")
      FieldMap fieldMap = new FieldMap(); 
      
      Draw points = new Draw(FieldMap.startPositions[5].multiply(-1, 1), FieldMap.wayPointsAlpha, false, false);
      JFrame frame = new JFrame("Points");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.add(points);
      frame.pack();
      frame.setResizable(false);    
      frame.setVisible(true);
    }
}
