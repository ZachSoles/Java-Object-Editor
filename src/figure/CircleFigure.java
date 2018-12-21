
package figure;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Zachery Soles
 */
public class CircleFigure extends Figure 
{
  private final double diameter;
  //---Gets the diameter of the circle
  public CircleFigure(double diameter) {
    this.diameter = diameter;
    shape = new Ellipse2D.Double(0, 0, diameter, diameter);
  }
  //---Gets the shape of the circle
  @Override
  public Shape getShape() {
    return new Ellipse2D.Double(xLoc, yLoc, diameter * scale, diameter * scale);
  }
  //---Draws the circle
  @Override
  public void draw(Graphics2D g2) {
    if (stroke == null) {
      stroke = new BasicStroke(strokeWidth);
    }
    g2.setStroke(stroke);

    g2.setColor(fillColor);  // set color
    g2.fill(shape);          // and fill the shape

    g2.setColor(lineColor);  // set color
    g2.draw(shape);          // draw the shape (the outline)
  }
}
