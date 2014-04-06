/*
   Programmers: Kristoffer Larson, Josue Ruiz
   Date: March 28, 2014
   
   Description: 
      Extends SimFrame, this is the driver class. This class initiates and
      calls appropriate classes for tasks to be done. In no particular order
      the following are called by this class: FileReader, PlayerBot, and 
      CheckWayPoint.
      
*/

import java.awt.*;
import java.awt.event.*;  
import javax.swing.*;
import java.util.ArrayList;
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
   
   public HashMap<Point, WayPoint> map = new HashMap<Point, WayPoint>();

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
   
   /**Finds the nearest WayPoint to the mouse click*/
   private WayPoint nearestWayPoint(int x, int y) {
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
   
   /**Draws WayPoints and Connectors onto the panel*/
   private void drawMap() {
      ArrayList<Point> neigh;
      WayPoint aWayPoint;
      Point a = new Point(0,0);
      Point b = new Point(0,20);
      
      Iterator<WayPoint> iterator = map.values().iterator();
      while (iterator.hasNext()) { //Draw WayPoints
         animatePanel.addPermanentDrawable(iterator.next());
      }//End while
      
      iterator = map.values().iterator(); //re-initialize iterator
      //Each point contains a list of it's neighbors. Without a check
      //both connectors are drawn wasting time and memory.
      while (iterator.hasNext()) {
         aWayPoint = iterator.next();
         neigh = aWayPoint.getNeigh();
         for (int i = 0; i < aWayPoint.getNeighbors(); i++) {
            //If aWayPoint is further from Point a than the neighbor is, draw the connector.
            //If not, change the reference to Point b.
            if (aWayPoint.getPoint().distance(a) > neigh.get(i).distance(a)) {
               //Create a connector between two WayPoints
               animatePanel.addPermanentDrawable(new Connector(aWayPoint.getPoint(), neigh.get(i), 
                  Color.BLACK));
            //If aWayPoint is closer from Point b than the neighbor is, don't draw the connector.
            //The opposite case has either already been drawn or will be drawn.
            } else if (aWayPoint.getPoint().distance(b) > neigh.get(i).distance(b)) {
               animatePanel.addPermanentDrawable(new Connector(aWayPoint.getPoint(), neigh.get(i), 
                  Color.BLACK));
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
      
   	// sets any initial visual Markers or Connectors
   	// get any required user mouse clicks for positional information.
      drawMap();
      
      //Create Bot collection
      if (bot == null) {
         bot = new ArrayList<PlayerBot>(); 
      //Resets bot for a new clean run
      } else {
         bot.clear();
      }//End if
      //Create 1 Bot 
      setStatus("Enter bot start position");
      waitForMousePosition();
      int x = (int)mousePosition.getX();
      int y = (int)mousePosition.getY();
      //Find nearest WayPoint to mouse click
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
      
      // get aBot from the list of created bots
      PlayerBot aBot = bot.get(0);
      // set the bot's dest to the end point
      aBot.setDest(end);
      // give the player a map of the field for traversal
      aBot.gainMap(map);
      // create a CheckWayPoint object for checking each location the bot traverses
      CheckWayPoint check = new CheckWayPoint(aBot, this);;
      
      Point aPoint = aBot.getPoint();
      Point dest;
      
      boolean mapMove = false;
      
      WayPoint aWayPoint = map.get(aPoint);
   
      Stack<WayPoint> list = new Stack<WayPoint>();
      
      statusReport("Start (" + (int)start.getX() + ", " + (int)start.getY() + "), Stop (" +
         (int)end.getX() + ", " + (int)end.getY() + "), Player " + aBot.getStrength() + " $ " +
         aBot.getWealth());
      
      while (runnable()) {
         if (list.isEmpty() || (aBot.getTMap() && !mapMove)) {
            // clear open and closed set nodes from the frame
            animatePanel.clearTemporaryDrawables();
            // obtain the bot's destination
            dest = aBot.getDest();
            //Pathing algorithm
            list = aBot.aStarMove(dest, app);
          
            //if the player has a map and has just made the path to it, make mapMove true
            //so that a new path isn't created. Once the player has reached the gold WP from
            //the TMap, hasTMap is set to false and a new path must be made back to the original
            //destination. When the player doesn't have a TMap and the move has been made, mapMove
            //is set back to false as a reset.
            if (!mapMove) mapMove = true;
            if (!aBot.getTMap() && mapMove) mapMove = false;
            
            // if the list is null, no path is available to the chosen destination
            if (list == null) {
               setSimRunning(false);
               setModelValid(false);
               animatePanel.setComponentState(false, false, false, false, true);
               statusReport("No path (" + (int)start.getX() + ", " + (int)aPoint.getY() + ") " + 
                  "to (" + (int)dest.getX() + ", " + (int)dest.getY() + ")");
               return;
            }//End if
            
            statusReport("Path (" + (int)aBot.getPoint().getX() + ", " + (int)aBot.getPoint().getY() + 
               ") to (" + (int)dest.getX() + ", " + (int)dest.getY() + "), length " + list.size());
         } 
         else {
         
            aPoint = aBot.getPoint();
            aWayPoint = map.get(aPoint);
            
            check.checkWP(aWayPoint);
            // check if end is reached and no treasure map is being held
            if (aBot.getPoint().equals(end) && !aBot.getTMap()) {
               setSimRunning(false);
               setModelValid(false);
               animatePanel.setComponentState(false, false, false, false, true);
               statusReport("Success, goal (" + (int)aPoint.getX() + ", " + (int)aPoint.getY() + ") " + 
                  ", Player " + aBot.getStrength() + " $ " + aBot.getWealth());
               return;
            }//End if
            
            // make a move if the player hasn't just picked up a treasure map
            if (!aBot.getTMap() || mapMove) {
               aBot.setMove(list.pop());
               aBot.move();
            }//End if
         }//End if
         
         checkStateToWait();
      }//End while
   }//End simulateAlgorithm
      
}//End Class
