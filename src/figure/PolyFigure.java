package figure;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.util.List;

public class PolyFigure extends Figure {
  private final List<Point> points;

  public PolyFigure(List<Point> points) {
    this.points = points;
    Polygon s = new Polygon();
    for(Point point: points) {
      s.addPoint(point.x, point.y);
    }
    shape = s;
  }

  @Override
  public Shape getShape() {
    Polygon s = new Polygon();
    for(Point point: points) {
      s.addPoint((int)(xLoc+scale*point.x), (int)(yLoc+scale*point.y));
    }
    return s;
  }

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
