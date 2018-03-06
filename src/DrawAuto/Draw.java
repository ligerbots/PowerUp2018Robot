package DrawAuto;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import org.ligerbots.powerup.FieldMap;
import org.ligerbots.powerup.FieldPosition;

public class Draw extends JPanel {

  boolean spline;
  FieldPosition startPos;
  List<FieldPosition> waypoints;
  
  public Draw (FieldPosition startPos, List<FieldPosition> waypoints, boolean spline, boolean flip) {
    this.spline = spline;
    this.waypoints = waypoints;
    if (flip) {
      for (int i = 0; i < this.waypoints.size(); i += 1) {
        this.waypoints.set(i, this.waypoints.get(i).multiply(-1, 1));
      }
    }
    this.waypoints.forEach(i -> System.out.println(i.getX()));
    this.startPos = startPos;
  }
  
  /**
   * 
   */
  private static final long serialVersionUID = -6413169990137235299L;

  @Override
  protected void paintComponent(Graphics g) {
    
    super.paintComponent(g);
        
    double scale = 537.0/648.0;
    
    try {
      Image fieldImg = ImageIO.read(new File("C:\\Ligerbots\\fpp-field.png"));
      g.drawImage(fieldImg, 0, 0, Color.white, null);
    }
    catch (IOException e) {
      e.printStackTrace();
    }
        
    g.setColor(Color.red);
    
    g.drawLine(136 + (int)(startPos.getX() * scale), (int) (537.0 - (startPos.getY() * scale)), (int)(waypoints.get(0).getX() * scale) + 136, 537 - (int)(waypoints.get(0).getY() * scale));
    
    g.setColor(Color.blue);
    
    g.fillRect(136 + (int)(startPos.getX() * scale) - 5, (int) (537.0 - (startPos.getY() * scale)) - 5, 10, 10);
    
    if (spline) {
      for (int i = 1; i < waypoints.size() - 3; i += 1) {
        FieldPosition temp = waypoints.get(i);
        
        g.setColor(Color.red);
        g.drawLine((int)(temp.getX() * scale) + 136, 537 - (int)(temp.getY() * scale), (int)(waypoints.get(i+1).getX() * scale) + 136, 537 - (int)(waypoints.get(i+1).getY() * scale));
        
        g.setColor(Color.blue);
        g.fillRect(136 + (int)(temp.getX() * scale) - 5, 537 - (int)(temp.getY() * scale) - 5, 10, 10);
        
        g.fillRect(136 + (int)(waypoints.get(waypoints.size()-2).getX() * scale) - 5, 537 - (int)(waypoints.get(waypoints.size()-2).getY() * scale) - 5, 10, 10);

      }
    }
    else {
      for (int i = 0; i < waypoints.size() - 1; i += 1) {
        FieldPosition temp = waypoints.get(i);
        
        g.setColor(Color.red);
        g.drawLine((int)(temp.getX() * scale) + 136, 537 - (int)(temp.getY() * scale), (int)(waypoints.get(i+1).getX() * scale) + 136, 537 - (int)(waypoints.get(i+1).getY() * scale));
        
        g.setColor(Color.blue);
        g.fillRect(136 + (int)(temp.getX() * scale) - 5, 537 - (int)(temp.getY() * scale) - 5, 10, 10);
      }
      
      g.fillRect(136 + (int)(waypoints.get(waypoints.size()-1).getX() * scale) - 5, 537 - (int)(waypoints.get(waypoints.size()-1).getY() * scale) - 5, 10, 10);
    }
    //g.drawLine(5, 5, 10, 10);
    
    //g.fillRect(50, 50, 100, 100);
    
  }
  
  @Override
  public Dimension getPreferredSize() {
      return new Dimension(272, 537);
  }
}
