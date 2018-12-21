package controller;

import figure.RectangleFigure;
import figure.CircleFigure;
import figure.Figure;
import figure.PolyFigure;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import views.AddRectangleDialog;
import views.AddCircleDialog;
import views.Canvas;
import views.FigureFrame;

class Helpers {

  //---List of Sample figures when Load Samples is pressed
  static List<Figure> getSampleFigureList() {
    List<Figure> figures = new ArrayList<>();
    Figure figure;

    figure = new RectangleFigure(300, 100);
    figure.setStrokeWidth(12f);
    figure.setLoc(220, 140);
    figure.setLineColor(Color.magenta);
    figure.setFillColor(Color.yellow);
    figure.setTitle("aura glow");
    figures.add(figure);

    figure = new RectangleFigure(300, 200);
    figure.setStrokeWidth(3f);
    figure.setLoc(85, 100);
    figure.setTitle("austere");
    figures.add(figure);

    List<Point> points = new ArrayList<>();
    points.add(new Point(180, 20));
    points.add(new Point(20, 200));
    points.add(new Point(20, 75));
    figure = new PolyFigure(points);
    figure.setLoc(0, 0);
    figure.setFillColor(Color.DARK_GRAY);
    figure.setLineColor(Color.magenta);
    figure.setStrokeWidth(4.2f);
    figure.setTitle("zorro");
    figures.add(figure);

    figure = new RectangleFigure(250, 180);
    figure.setStrokeWidth(5.1f);
    figure.setLoc(40, 40);
    figure.setLineColor(Color.blue);
    figure.setFillColor(Color.red);
    figure.setTitle("red square");
    figures.add(figure);

    return figures;
  }
  //---Event Handler for AddCircleDialog
  static void addEventHandlersForCircle(AddCircleDialog addCircleDialog, List<Figure> figureList, DefaultComboBoxModel comboModel, FigureFrame frame) 
  {  
    Canvas canvas = frame.getCanvas();
    
    //---Event handler for the Add buttone of AddCircleDialog
    addCircleDialog.getAddButton().addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        try {
          //---Adds the figure from the dialog to the figureList
          Figure fig = Helpers.makeFigureFromDialogForCircle(addCircleDialog);
          figureList.add(0, fig);
          //---Adds the figure from the dialog to the comboModel
          comboModel.removeAllElements();
          for (Figure figure : figureList) {
            comboModel.addElement(figure);
          }
          //---Repaints the canvas
          canvas.repaint();
          //---Sets the dialog box to not visible
          addCircleDialog.setVisible(false);
        }
        catch (Exception ex) {
          JOptionPane.showMessageDialog(addCircleDialog, ex);
        }
      }
    });
     
    //---Event Handler for the cancel button of AddCircleDialog
    addCircleDialog.getCancelButton().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        addCircleDialog.setVisible(false);
      }
    });
    
    // the ChooseLineColor button of AddCircleDialog
    addCircleDialog.getChooseLineColor().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Color color = JColorChooser.showDialog(addCircleDialog, "Choose color", 
            addCircleDialog.getLineColorField().getBackground());
        
        if (color != null) {
          addCircleDialog.getLineColorField().setBackground(color);
        }
      }
    });

    // the Choose FillColor button of AddCircleDialog
    addCircleDialog.getChooseFillColor().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Color color = JColorChooser.showDialog(addCircleDialog, "Choose color", 
            addCircleDialog.getFillColorField().getBackground());
        
        if (color != null) {
          addCircleDialog.getFillColorField().setBackground(color);
        }
      }
    });
  }
  //---Event Handler for AddRectangleDialog
  static void addEventHandlersforRectangle(AddRectangleDialog addRectDialog, List<Figure> figureList, DefaultComboBoxModel comboModel, FigureFrame frame) 
  {  
    Canvas canvas = frame.getCanvas();

    // the Add Button
    addRectDialog.getAddButton().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          Figure fig = Helpers.makeFigureFromDialog(addRectDialog);
          figureList.add(0, fig);
          
          comboModel.removeAllElements();
          for (Figure figure : figureList) {
            comboModel.addElement(figure);
          }
          canvas.repaint();
          addRectDialog.setVisible(false);
        }
        
        catch (Exception ex) {
          JOptionPane.showMessageDialog(addRectDialog, ex);
        }
      }
    });

    // the Cancel Button
    addRectDialog.getCancelButton().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        addRectDialog.setVisible(false);
      }
    });
    

    // the ChooseLineColor button
    addRectDialog.getChooseLineColor().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Color color = JColorChooser.showDialog(addRectDialog, "Choose color", 
            addRectDialog.getLineColorField().getBackground());
        
        if (color != null) {
          addRectDialog.getLineColorField().setBackground(color);
        }
      }
    });

    // the Choose FillColor button
    addRectDialog.getChooseFillColor().addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Color color = JColorChooser.showDialog(addRectDialog, "Choose color", 
            addRectDialog.getFillColorField().getBackground());
        
        if (color != null) {
          addRectDialog.getFillColorField().setBackground(color);
        }
      }
    });

  }

  static Figure makeFigureFromDialog(AddRectangleDialog dialog) throws Exception {
    String widthText = dialog.getWidthField().getText().trim();
    String heightText = dialog.getHeightField().getText().trim();
    String strokeWidthText = dialog.getStrokeWidthField().getText().trim();

    double width = Double.parseDouble(widthText);
    double height = Double.parseDouble(heightText);
    float strokeWidth = Float.parseFloat(strokeWidthText);

    if (width <= 0 || height <= 0 || strokeWidth <= 0) {
      throw new Exception("fields must have positive values");
    }

    String title = dialog.getTitleField().getText().trim();
    if (title.isEmpty()) {
      title = "untitled";
    }

    Color lineColor = dialog.getLineColorField().getBackground();
    Color fillColor = dialog.getFillColorField().getBackground();

    Figure fig = new RectangleFigure(width, height);

    fig.setStrokeWidth(strokeWidth);
    fig.setTitle(title);
    fig.setLineColor(lineColor);
    fig.setFillColor(fillColor);

    return fig;
  }
  static Figure makeFigureFromDialogForCircle(AddCircleDialog dialog) throws Exception {
    String diameterText = dialog.getDiameterField().getText().trim();
    String strokeWidthText = dialog.getStrokeWidthField().getText().trim();
    //---Gets diameter from the dialog box
    double diameter = Double.parseDouble(diameterText);
    float strokeWidth = Float.parseFloat(strokeWidthText);

    if (diameter <= 0 ||  strokeWidth <= 0) {
      throw new Exception("fields must have positive values");
    }
    //---Gtes the title of the object
    String title = dialog.getTitleField().getText().trim();
    //---If title is empty, then the title is untitles
    if (title.isEmpty()) {
      title = "untitled";
    }
    //---gets the line and fill color
    Color lineColor = dialog.getLineColorField().getBackground();
    Color fillColor = dialog.getFillColorField().getBackground();
    //---Creates the figure and sets the variables
    Figure fig = new CircleFigure(diameter);
    fig.setStrokeWidth(strokeWidth);
    fig.setTitle(title);
    fig.setLineColor(lineColor);
    fig.setFillColor(fillColor);

    return fig;
  }
}
