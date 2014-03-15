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
      PlayerBot b = new PlayerBot(name, a, h);
      bot.add(b); 
      animatePanel.addBot(b);
   }//End makeBot
   
   private WayPoint nearestWayPoint(int x, int y) {
      //Finds the nearest WayPoint to the mouse click
      int tx, ty;
      Point temp = null;
      double dist, oldDist = 2000.0;
      
      Iterator <WayPoint> iterator = map.values().iterator();
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
   
   public void permDraw() {
      ArrayList<Point> neigh;
      WayPoint aWayPoint;
      HashSet<Connector> hs = new HashSet<Connector>();
      Connector c, d;
      
      Iterator <WayPoint> iterator = map.values().iterator();
      while (iterator.hasNext()) { //Draw WayPoints
         animatePanel.addPermanentDrawable(iterator.next());
      }//End while
      //animatePanel.repaint();
      
      iterator = map.values().iterator(); //re-initialize iterator
      //addPermanentDrawable for connectors
      while (iterator.hasNext()) {
         aWayPoint = iterator.next();
         neigh = aWayPoint.getNeigh();
         for (int i = 0; i < aWayPoint.getNeighbors(); i++) {
            //Puts in a HashSet so there are no duplicate connectors
            c = new Connector(aWayPoint.getPoint(), neigh.get(i), Color.BLACK);
            d = new Connector(neigh.get(i), aWayPoint.getPoint(), Color.BLACK);
            //If I'm making both connectors, I might as well just draw both.
            //Is there a better way to do this?
            if (!hs.contains(d)) {
               animatePanel.addPermanentDrawable(c);
               animatePanel.repaint(); //Test connectors being drawn
               hs.add(c);
            }//End if
         }//End for
      }//End while
      
      animatePanel.repaint();
   }//End permDraw

   public void setSimModel() {
      setStatus("Initial state of simulation");
      
      //Read and store waypoints using FileReader class
      FileReader fr = new FileReader(map, "waypointNeighbor.txt");
   	// set any initial visual Markers or Connectors
   	// get any required user mouse clicks for positional information.
   	// initialize any algorithm halting conditions (ie, number of steps/moves).
      permDraw();
      
      //Create Bot collection
      if (bot == null) {
         System.out.println("make new bots");
         bot = new ArrayList<PlayerBot>(); 
      } else {
         bot.clear();
         System.out.println("reset bots"); 
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
      //ArrayList<WayPoint> moves = new ArrayList<WayPoint>(8);
      aBot.gainMap(map);
      
      Point aPoint, temp = null, prevPoint = null;
      
      int c, g, tx, ty;
      //double dist, oldDist;
      //int destH = map.get(dest).getHeight();
      WayPoint aWayPoint;
      boolean hasTMap = false;
      
      
      String status = "Start (" + (int)start.getX() + ", " + (int)start.getY() + "), Stop (" +
         (int)end.getX() + ", " + (int)end.getY() + "), Player " + aBot.getStrength() + " $ " +
         aBot.getWealth();
      
      statusReport(status);
      List<WayPoint> list = new ArrayList<WayPoint>(); 

      while (runnable()) {
      
         if ((list == null || list.isEmpty()) && !hasTMap) {
            list = aBot.aStarMove(end, list);
         } else {
         
         aPoint = aBot.getPoint();
         aWayPoint = map.get(aPoint);
         
         if ((aWayPoint.getMapX() > 0 || aWayPoint.getMapY() > 0) && !(hasTMap)) {
            //Accessing a Map WayPoint
            status = "Map (" + (int)aPoint.getX() + ", " + (int)aPoint.getY() + 
               ") Treasure (" + aWayPoint.getMapX() + ", " + aWayPoint.getMapY() 
               + ") Player " + aBot.getStrength() + " $ " + aBot.getWealth();
            statusReport(status);
            
            //Repath towards treasure
            list.clear();
            aBot.aStarMove(new Point(aWayPoint.getMapX(), aWayPoint.getMapY()), list);
         } else if (aWayPoint.getCityValue() > 0) {
            //Accessing a City WayPoint
            status = "City (" + (int)aPoint.getX() + ", " + (int)aPoint.getY() + ") $ " +
               aWayPoint.getCityValue() + ", Player " + aBot.getStrength() + " $ " +
               aBot.getWealth();
            statusReport(status);
                  
            c = aWayPoint.getCityValue();
            if (aBot.getWealth() >= c) {
               aBot.setWealth(aBot.getWealth() - c);
               aBot.setStrength(aBot.getStrength() + c);
            }//End if
         } else if (aWayPoint.getGold() > 0) {
            //Accessing a Gold WayPoint
            status = "Gold (" + (int)aPoint.getX() + ", " + (int)aPoint.getY() + ") $ " +
               aWayPoint.getGold() + ", Player " + aBot.getStrength() + " $ " +
               aBot.getWealth();
            statusReport(status);
            
            //If the "bot" travels to a gold WayPoint because of a Map and this point has
            //already been visted, then no gold is taken.
            if (!aWayPoint.getVisted()) {
               g = aWayPoint.getGold();
               aBot.setWealth(aBot.getWealth() + g);
            }//End if
         } else {
            //At a Normal WayPoint
            status = "WayPoint (" + (int)aPoint.getX() + ", " + (int)aPoint.getY() +
               "), Player " + aBot.getStrength() + " $ " + aBot.getWealth();
            statusReport(status);
         }//End if
            
         aWayPoint.setVisted(); //Set WayPoint to visited

         if (aBot.getPoint().equals(end)) {
            setSimRunning(false);
            setModelValid(false);
            animatePanel.setComponentState(false, false, false, false, true);
            status = "Success, goal (" + (int)aPoint.getX() + ", " + (int)aPoint.getY() + ") " + 
               ", Player " + aBot.getStrength() + " $ " + aBot.getWealth();
            statusReport(status);
            return;
         }//End if
         
         aBot.setMove(list.get(0));
         list.remove(0);
         aBot.move();
         }
         
         checkStateToWait();
      }//End while
   }//End simulateAlgorithm
   
}//End Class
