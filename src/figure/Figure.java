package figure;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.io.Serializable;

public abstract class Figure implements Serializable {
  protected Shape shape;
  
  protected Color lineColor = Color.BLACK;
  protected Color fillColor = Color.WHITE;
  
  protected double xLoc = 0, yLoc = 0;
  protected double scale = 1.0;
  
  private String title;
  
  public double getXloc() { return xLoc; }
  public double getYloc() { return yLoc; }
  
  public void setLoc(double xLoc, double yLoc) {
    this.xLoc = xLoc;
    this.yLoc = yLoc;
  }
  
  protected float strokeWidth = 1.0f;
  protected transient Stroke stroke = null;
  public final void setStrokeWidth(float strokeWidth) {
    this.strokeWidth = strokeWidth;
    stroke = null;
  }
 
  public void incLoc(double xInc, double yInc) {
    this.xLoc += xInc;
    this.yLoc += yInc;
  }
  
  public double getScale() {
    return scale;
  }
  
  public void setScale(double scale) {
    this.scale = scale;
  }
  
  public abstract void draw(Graphics2D g2);
  public abstract Shape getShape();
 
  public void setLineColor(Color color) {
    this.lineColor = color;
  }
 
  public void setFillColor(Color color) {
    this.fillColor = color;
  }

  public final void setTitle(String title) {
    this.title = title;
  }
  
  @Override
  public String toString() {
    return title;
  }
  
}
