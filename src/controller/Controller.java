package controller;

import figure.Figure;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import views.AddCircleDialog;
import views.AddRectangleDialog;
import views.Canvas;
import views.FigureFrame;

public class Controller {
  private final FigureFrame frame = new FigureFrame();
  private final Canvas canvas = frame.getCanvas();

  //---Figures which appear in canvas and in list
  List<Figure> figureList = new ArrayList<>();
  Figure figure;
  Figure clickedFigure = null;
  Figure selected;
  int newX, newY;

  // ---Model for Figure selection list
  private final DefaultComboBoxModel comboModel = new DefaultComboBoxModel();

  //---Dialogs
  private AddRectangleDialog addRectDialog = new AddRectangleDialog(frame, true);
  private AddCircleDialog addCircDialog = new AddCircleDialog(frame, true);
  
  //---File choosed for Load and Save features
  private static JFileChooser getFileChoice() 
  {
    JFileChooser choice = new JFileChooser();
    //---Specify where chooser should open up
    File workingDirectory = new File(System.getProperty("user.dir"));
    choice.setCurrentDirectory(workingDirectory);
    //---Only show files that are .txt
    choice.addChoosableFileFilter(new FileNameExtensionFilter("only .fig files", "fig" , "java"));
    //---Do not accept all files
    choice.setAcceptAllFileFilterUsed(false);
    return choice;
  }
  //---Sets moveToTop, remove, and save buttons to disabled
  public void startDisabled()
  {
    frame.getMoveToTop().setEnabled(false);
    frame.getRemove().setEnabled(false);
    frame.getSave().setEnabled(false);
  }
  //---Sets moveToTop, remove, and save buttons to enabled
  public void startEnabled()
  {
    frame.getMoveToTop().setEnabled(true);
    frame.getRemove().setEnabled(true);
    frame.getSave().setEnabled(true);
  }

  public Controller() 
  {
    frame.setTitle("Figures");
    frame.setLocationRelativeTo(null);
    frame.setSize(800, 500);
    
    startDisabled();

    canvas.setFigures(figureList);

    frame.getFigureList().setModel(comboModel);
    
    JFileChooser choice = getFileChoice();

    // set the spinner model
    frame.getScaleSpinner().setModel(new SpinnerNumberModel(1.0, 0.1, 5.0, 0.05));
    //---Listens for changes in the spinner and sets the scale of the selected figure
    frame.getScaleSpinner().addChangeListener(new ChangeListener()
    {
      @Override
      public void stateChanged(ChangeEvent e)
      {
        if(selected != null)
        {
          //---Gets the value from the scale spinner
          double scale = (double) frame.getScaleSpinner().getValue();
          //---Sets the scale of the selected figure
          selected.setScale(scale);
          canvas.repaint();
        }
      }
    });
    //---Action listener for getFigureList
    frame.getFigureList().addActionListener(new ActionListener() 
    {
      @Override
      public void actionPerformed(ActionEvent e) 
      {
        double x = 0;
        //---Gets the selected figure
        selected = (Figure) comboModel.getSelectedItem();
        System.out.println("selected = " + selected);
        //---Checks to see if the selected figure is null
        if(selected != null)
        {
          //---if not null, then it gets the scale and sets it to the scaleSpinner
          x = selected.getScale();
          System.out.println(x);
          frame.getScaleSpinner().setValue(x);
        }
      }
    });
    //---Load Samples button
    frame.getLoadSamples().addActionListener(new ActionListener() 
    {
      @Override
      public void actionPerformed(ActionEvent evt) {
        startEnabled();
        //---Creates a temp arraylist and sets it to the sample
        List<Figure> samples = Helpers.getSampleFigureList();
        figureList.clear();
        //---Assigns the sample to the comboModel and figureList
        for (Figure sample : samples) {
          figureList.add(sample);
        }
        comboModel.removeAllElements();
        for (Figure figure : figureList) {
          comboModel.addElement(figure);
        }
        canvas.repaint();
      }
    });
    //---Action Listener for Load
    frame.getLoad().addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent evt)
      {
        startEnabled();
        //---JFileChooser
        int status = choice.showOpenDialog(frame);
        if(status != JFileChooser.APPROVE_OPTION)
          return;
        File f = choice.getSelectedFile();
        try
        {
          //---Reads the serialized object
          FileInputStream fis = new FileInputStream(f);
          ObjectInputStream ois = new ObjectInputStream(fis);
          //---Enters the file into a tempList
          List<Figure> tempList = (List<Figure>)ois.readObject();
          //---Clears figureList and enters the tempList to figureList
          figureList.clear();
          for(Figure temp : tempList)
            figureList.add(temp);
          //---Clears comboModel and enters figureList in
          comboModel.removeAllElements();
          for(Figure figure : figureList)
            comboModel.addElement(figure);
          //---Repaints the canvas
          canvas.repaint();
          ois.close();
        }
        //---Prints exception
        catch (Exception ex)
        {
          System.out.println(ex);
        }
      }
    });
    //--Action Listener for Save
    frame.getSave().addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent evt)
      {
        //---JFile Chooder
        int status = choice.showSaveDialog(frame);
        if(status != JFileChooser.APPROVE_OPTION)
          return;
        File f = choice.getSelectedFile();
        //---Sees if there is an extention in the file name
        if(!f.toString().contains("."))
        {
          //---Adds '.txt. if its not there
          String name = f.getAbsoluteFile()+".fig";
          f = new File(name);
        }
        //---Saves figureList to a serialized file
        try 
        {
          //---Saves the file
          FileOutputStream fos = new FileOutputStream(f);
          ObjectOutputStream oos = new ObjectOutputStream(fos);
          oos.writeObject(figureList);
          oos.close();
        }
        //---Prints the exception
        catch (Exception ex) 
        {
          System.out.println(ex);
        }
      }
    });
    //---Invoke the addRectangle dialog
    frame.getAddRectDialog().addActionListener(new ActionListener() 
    {
      @Override
      public void actionPerformed(ActionEvent evt) 
      {
        addRectDialog.setLocationRelativeTo(null);
        addRectDialog.setTitle("Add a Rectangle Figure");
        startEnabled();
        //--Sets fields to 0
        addRectDialog.getHeightField().setText("" + 100);
        addRectDialog.getWidthField().setText("" + 200);
        addRectDialog.getStrokeWidthField().setText("" + 1F);
        addRectDialog.getTitleField().setText("");
        //---Sets color fields to not editable
        addRectDialog.getLineColorField().setEditable(false);
        addRectDialog.getFillColorField().setEditable(false);
        //---Sets color fields to a certain color
        addRectDialog.getLineColorField().setBackground(Color.black);
        addRectDialog.getFillColorField().setBackground(Color.white);
        //---Shows the dialog box
        addRectDialog.setVisible(true);
      }
    });
    //---Invoke the addCircle dialog
    frame.getAddCircleDialog().addActionListener(new ActionListener() 
    {
      @Override
      public void actionPerformed(ActionEvent evt) 
      {
        addCircDialog.setLocationRelativeTo(null);
        addCircDialog.setTitle("Add a Circle Figure");
        startEnabled();
        //--Sets fields to 0
        addCircDialog.getDiameterField().setText("" + 100);
        addCircDialog.getStrokeWidthField().setText("" + 1F);
        addCircDialog.getTitleField().setText("");
        //---Sets color fields to not editable
        addCircDialog.getLineColorField().setEditable(false);
        addCircDialog.getFillColorField().setEditable(false);
        //---Sets color fields to a certain color
        addCircDialog.getLineColorField().setBackground(Color.black);
        addCircDialog.getFillColorField().setBackground(Color.white);
        //---Shows the dialog box
        addCircDialog.setVisible(true);
      }
    });
    //---Action Listener for MoveToTop button
    frame.getMoveToTop().addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent evt)
      {
        //---Removes the selected figure, and adds them
        int index = figureList.indexOf(selected);
        figureList.remove(index);
        figureList.add(0, selected);
        //---Resets all of the elements in the comboModel and makes the selected figure the 1st
        comboModel.removeAllElements();
        for (Figure figure : figureList) {
          comboModel.addElement(figure);
        }
        //---Repaints the canvas
        canvas.repaint();
      }
    });
    //---Action Listener for remove button
    frame.getRemove().addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent evt)
      {
        //---Removes the selected element from the figureList and comboModel
        int index = figureList.indexOf(selected);
        figureList.remove(index);
        comboModel.removeElement(selected);
        //---Repaints the canvas
        canvas.repaint();
        //---Sets save and move to top button to disabled if the figureList is empty
        //---Also sets scaleSpinner to 1.0
        if(figureList.isEmpty())
        {
          startDisabled();
          frame.getScaleSpinner().setValue(1.0);
        }
      }
    });
    //---Both mouseListeners allow for figures to be moved in the canvas
    canvas.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mousePressed(MouseEvent e)
      {
        //---Stops if there is no figure
        if(selected == null)
          return;
        //---Gets the starting axis of the mouse
        int x = e.getX();
        int y = e.getY();
        //---Checks to see if the figure was clicked or not
        //---If so, then it sets the selected figure to clickedFigure
        if (selected.getShape().contains(x, y)) {
          clickedFigure = selected;
        }
        newX = x;
        newY = y;
      }
      
      @Override
      public void mouseReleased(MouseEvent e)
      {
        //---If released, the clicked figure is null
        clickedFigure = null;
      }
    });
    canvas.addMouseMotionListener(new MouseMotionAdapter()
    {
      @Override
      public void mouseDragged(MouseEvent e)
      {
        //---Stops if there is no figure
        if(clickedFigure == null)
          return;
        int x = e.getX();
        int y = e.getY();
        //---Moves the figure by comparing the new x and y to the old x and y
        int finalX = x - newX;
        int finalY = y - newY;
        
        newX = x;
        newY = y;
        //Repaints the canvas
        clickedFigure.incLoc(finalX, finalY);
        canvas.repaint();
      }
    });
    // addRectDialog and addCircleDialog needs the remaining arguments to do its work in events
    Helpers.addEventHandlersforRectangle(addRectDialog, figureList, comboModel, frame);
    Helpers.addEventHandlersForCircle(addCircDialog, figureList, comboModel, frame);
  }

  public static void main(String[] args) {
    Controller app = new Controller();
    app.frame.setVisible(true);
  }
}