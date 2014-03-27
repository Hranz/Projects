/*
   Programmers: Kristoffer Larson, Josue Ruiz
   Date: , 2014
   
   Description: 
      Extends SimFrame, this reads values from a file to create
      WayPoint objects for a "bot" to traverse. At the start of
      the simulation, it prompts for a start point and destination
      point for the "bot" to move. Creates a "bot" at the start 
      location, and runs simulateAlgorithm() which takes care of
      the movement and halting conditions for the simulation.
*/

import java.awt.*;
import java.awt.event.*;  
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.HashMap;
import java.util.HashSet;
import java.util.*;

import SimulationFramework.*;

public class LarsonRuizTreasurePath extends SimFrame   {
	// eliminate warning @ serialVersionUID
   private static final long serialVersionUID = 42L;
   // GUI components for application's menu
   /** the simulation application */
   private LarsonRuizTreasurePath app;
   // application variables;
   /** the actors "bots" of the simulation */
   private ArrayList <PlayerBot> bot;
      
   private Point start, end;
   
   HashMap<Point, WayPoint> map = new HashMap<Point, WayPoint>();

   public static void main(String args[]) {
      LarsonRuizTreasurePath app = new LarsonRuizTreasurePath("LarsonRuizTreasurePath", "terrain282.png");
      app.start();  // start is inherited from SimFrame
   }

/**
Make the application:  create the MenuBar, "help" dialogs, 
*/

   public LarsonRuizTreasurePath(String frameTitle, String imageFile) {
      super(frameTitle, imageFile);
      // create menus
      JMenuBar menuBar = new JMenuBar();
      // set About and Usage menu items and listeners.
      aboutMenu = new JMenu("About");
      aboutMenu.setMnemonic('A');
      aboutMenu.setToolTipText(
         "Display informatiion about this program");
      // create a menu item and the dialog it invoke 
      usageItem = new JMenuItem("usage");
      authorItem = new JMenuItem("author");
      usageItem.addActionListener( // anonymous inner class event handler
            new ActionListener() {        
               public void actionPerformed(ActionEvent event) {
                  JOptionPane.showMessageDialog( LarsonRuizTreasurePath.this, 
                     "An informational message string \n" +
                     "about how to use the program \n" +
                     "that can span several lines \n" +
                     "if needed.",
                     "Usage",   // dialog window's title
                     JOptionPane.PLAIN_MESSAGE);
               }}
         );
      // create a menu item and the dialog it invokes
      authorItem.addActionListener(
            new ActionListener() {          
               public void actionPerformed(ActionEvent event) {
                  JOptionPane.showMessageDialog( LarsonRuizTreasurePath.this, 
                     "Kristoffer Larson and Josue Ruiz \n" +
                     "kristoffer.larson.967@my.csun.edu \n" +
                     "josue.ruiz.744@my.csun.edu\n" +
                     "Comp 282",
                     "author",  // dialog window's title
                     JOptionPane.INFORMATION_MESSAGE,
                     //  author's picture 
                     new ImageIcon("author.png"));
               }}
         );
      // add menu items to menu 
      aboutMenu.add(usageItem);   // add menu item to menu
      aboutMenu.add(authorItem);
      menuBar.add(aboutMenu);
      setJMenuBar(menuBar);
      validate();  // resize layout managers
      // construct the application specific variables
      
      
   }//End Constructor

   private void makeBot(String name, Point a, int h) {
      PlayerBot b = new PlayerBot(name, a, h, null, animatePanel);
      b.gainFrame(this);
      bot.add(b); 
      animatePanel.addBot(b);
   }//End makeBot
   
   private WayPoint nearestWayPoint(int x, int y) {
      //Finds the nearest WayPoint to the mouse click
      int tx, ty;
      Point temp = null;
      double dist, oldDist = 2000.0;
      
      Iterator<WayPoint> iterator = map.values().iterator();
      WayPoint aWayPoint;
      
      while (iterator.hasNext()) {
         aWayPoint = iterator.next();
         tx = aWayPoint.getWX();
         ty = aWayPoint.getWY();
         dist = Math.sqrt(Math.pow((y - ty), 2) 
            + Math.pow((x - tx), 2));
         if (dist < oldDist) {
            oldDist = dist;
            temp = new Point(tx,ty);
         }//End if
      }//End while
      
      return map.get(temp);
   }//End nearestWayPoint
   
   public void drawMap() {
      //HashSet<WayPoint> hs = new HashSet<WayPoint>();
      ArrayList<Point> neigh;
      WayPoint aWayPoint;
      Point a = new Point(0,0);
      Point b = new Point(0,20);
      Connector c;
      
      Iterator<WayPoint> iterator = map.values().iterator();
      while (iterator.hasNext()) { //Draw WayPoints
         animatePanel.addPermanentDrawable(iterator.next());
      }//End while
      
      iterator = map.values().iterator(); //re-initialize iterator
      //addPermanentDrawable for connectors
      while (iterator.hasNext()) {
         aWayPoint = iterator.next();
         neigh = aWayPoint.getNeigh();
         for (int i = 0; i < aWayPoint.getNeighbors(); i++) {
            //Puts in a HashSet so there are no duplicate connectors
            
            //c = new Connector(aWayPoint.getPoint(), neigh.get(i), Color.BLACK);
            //animatePanel.addPermanentDrawable(c);
         
            //If I'm making both connectors, I might as well just draw both.
            //Is there a better way to do this?
            //if (!hs.contains(neigh.get(i))) {
            if (aWayPoint.getPoint().distance(a) > neigh.get(i).distance(a)) {
               c = new Connector(aWayPoint.getPoint(), neigh.get(i), Color.BLACK);
               animatePanel.addPermanentDrawable(c);
               animatePanel.repaint(); //Test connectors being drawn
              // hs.add(aWayPoint);
            } else if (aWayPoint.getPoint().distance(b) > neigh.get(i).distance(b)) {
               c = new Connector(aWayPoint.getPoint(), neigh.get(i), Color.BLACK);
               animatePanel.addPermanentDrawable(c);
               animatePanel.repaint(); //Test connectors being drawn
            }//End if
         }//End for
      }//End while
      
      animatePanel.repaint();
   }//End drawConnectors() method
   
   public void setSimModel() {
      setStatus("Initial state of simulation");
      
      //Read and store waypoints using FileReader class
      FileReader fr = new FileReader(map, "waypointNeighbor.txt");
      
      statusReport("Create HashMap " + map.size() + " City " + fr.getNumCities() + " Gold " +
         fr.getNumGold() + " Map " + fr.getNumMaps());
      
   	// set any initial visual Markers or Connectors
   	// get any required user mouse clicks for positional information.
   	// initialize any algorithm halting conditions (ie, number of steps/moves).
      long startTime = System.nanoTime();
      drawMap();
      long endTime = System.nanoTime();
   
      long duration = endTime - startTime;
      System.out.println(duration);
      
      //Create Bot collection
      if (bot == null) {
         bot = new ArrayList<PlayerBot>(); 
      } 
      else {
         bot.clear();
      }//End if
      //Create 1 Bot 
      setStatus("Enter bot start position");
      waitForMousePosition();
      int x = (int)mousePosition.getX();
      int y = (int)mousePosition.getY();
      
      WayPoint a = nearestWayPoint(x, y);
      start = new Point(a.getWX(), a.getWY());
      makeBot("Red", start, a.getHeight());
      Marker mark = new Marker(start, Color.BLUE, 4);
      animatePanel.addPermanentDrawable(mark);
      
      //Destination
      setStatus("Enter bot destination");
      waitForMousePosition();
      x = (int)mousePosition.getX();
      y = (int)mousePosition.getY();
      
      a = nearestWayPoint(x, y);
      end = new Point(a.getWX(), a.getWY());
      mark = new Marker(end, Color.BLUE, 4);
      animatePanel.addPermanentDrawable(mark); 
   }//End setSimModel

   public synchronized void simulateAlgorithm() {
      setStatus("simulateAlgorithm()");
      PlayerBot aBot = bot.get(0);
      aBot.setDest(end);
      aBot.gainMap(map);
      CheckWayPoint check = new CheckWayPoint(aBot, this);;
      
      Point aPoint = aBot.getPoint();
      Point temp = null;
      Point prevPoint = null;
      Point dest = end;
      
      boolean mapMove = false;
      
      WayPoint aWayPoint = map.get(aPoint);
   
      Stack<WayPoint> list = new Stack<WayPoint>();
      
      String status = "Start (" + (int)start.getX() + ", " + (int)start.getY() + "), Stop (" +
         (int)end.getX() + ", " + (int)end.getY() + "), Player " + aBot.getStrength() + " $ " +
         aBot.getWealth();
      
      statusReport(status);
      
      while (runnable()) {
         if ((list == null || list.isEmpty()) || (aBot.getTMap() && !mapMove)) {
            animatePanel.clearTemporaryDrawables();
            dest = aBot.getDest();
            
            //Pathing algorithm
            list = aBot.aStarMove(dest, app);
         
            status = "Path (" + (int)aBot.getPoint().getX() + ", " + (int)aBot.getPoint().getY() + 
               ") to (" + (int)dest.getX() + ", " + (int)dest.getY() + "), length " + list.size();
            statusReport(status);
            
            if (!mapMove) mapMove = true;
            if (!aBot.getTMap() && mapMove) mapMove = false;
            //if (!mapMove || (!aBot.hasTMap && mapMove)) mapMove = !mapMove; //test this if the other two work
            //if the player has a map and has just made the path to it, make mapMove true
            //so that a new path isn't created. Once the player has reached the gold WP from
            //the TMap, hasTMap is set to false and a new path must be made back to the original
            //destination. When the player doesn't have a TMap and the move has been made, mapMove
            //is set back to false as a reset.
            
            if (list == null) {
               setSimRunning(false);
               setModelValid(false);
               animatePanel.setComponentState(false, false, false, false, true);
               status = "No path (" + (int)start.getX() + ", " + (int)aPoint.getY() + ") " + 
                  "to (" + (int)dest.getX() + ", " + (int)dest.getY() + ")";
               statusReport(status);
               return;
            }//End if
         } 
         else {
         
            aPoint = aBot.getPoint();
            aWayPoint = map.get(aPoint);
            
            check.checkWP(aWayPoint);
         
            if (aBot.getPoint().equals(end) && !aBot.getTMap()) {
               setSimRunning(false);
               setModelValid(false);
               animatePanel.setComponentState(false, false, false, false, true);
               status = "Success, goal (" + (int)aPoint.getX() + ", " + (int)aPoint.getY() + ") " + 
                  ", Player " + aBot.getStrength() + " $ " + aBot.getWealth();
               statusReport(status);
               return;
            }//End if
            
            if (!aBot.getTMap() || mapMove) {
               aBot.setMove(list.pop());
               aBot.move();
            }//End if
         }//End if
         
         checkStateToWait();
      }//End while
   }//End simulateAlgorithm
      
}//End Class
