/*
   Programmers: Kristoffer Larson, Josue Ruiz
   Date: May 8, 2014
   
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

public class LarsonRuizTreasureHunt extends SimFrame   {
	// eliminate warning @ serialVersionUID
   private static final long serialVersionUID = 42L;
   // GUI components for application's menu
   /** the simulation application */
   private LarsonRuizTreasureHunt app;
   // application variables;
   /** the actors "bots" of the simulation */
   private ArrayList <PlayerBot> bot;
   
   private static String fileName;
   
   private AccessDB adb;
         
   public HashMap<Point, WayPoint> map = new HashMap<Point, WayPoint>();

   public static void main(String args[]) {
      if (args.length > 0)
         fileName = args[0]; //waypointNeighbor.txt
      else
         fileName = "waypointNeighbor.txt";
      LarsonRuizTreasureHunt app = new LarsonRuizTreasureHunt("LarsonRuizTreasureHunt", "terrain282.png");
      app.start();  // start is inherited from SimFrame
   }

/**
Make the application:  create the MenuBar, "help" dialogs, 
*/

   public LarsonRuizTreasureHunt(String frameTitle, String imageFile) {
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
                  JOptionPane.showMessageDialog( LarsonRuizTreasureHunt.this, 
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
                  JOptionPane.showMessageDialog( LarsonRuizTreasureHunt.this, 
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
            } 
            else if (aWayPoint.getPoint().distance(b) > neigh.get(i).distance(b)) {
               animatePanel.addPermanentDrawable(new Connector(aWayPoint.getPoint(), neigh.get(i), 
                  Color.BLACK));
            }//End if
         }//End for
      }//End while
      animatePanel.repaint();
   }//End drawConnectors() method
   
   private void makeBot(int id, Point start, Point dest, int str, Color c, AccessDB adb) {
      //(int id, Point place, Point dest, int w, int str,
      //Stack<WayPoint> path, Color c, AnimatePanel animatePanel)
      
      PlayerBot b = new PlayerBot(id, start, dest, str, null, c, adb); //Remove animatePanel after finished
      b.gainMap(map);
      bot.add(b); 
      animatePanel.addBot(b);
   }//End makeBot
   
   public void setSimModel() {
      setStatus("Initial state of simulation");
      
      //Read and store waypoints using FileReader class
      FileReader fr = new FileReader(map, fileName);
      
      statusReport("Create HashMap " + map.size() + " City " + fr.getNumCities() + " Gold " +
         fr.getNumGold() + " Map " + fr.getNumMaps());
      
   	// sets any initial visual Markers or Connectors
      drawMap();
      
      //Create/Recreate database for players and waypoints
      MakeDB mdb = new MakeDB(map);
      
      //Create Bot collection
      if (bot == null) {
         bot = new ArrayList<PlayerBot>(); 
      //Resets bot for a new clean run
      } 
      else {
         bot.clear();
      }//End if
      //Create Players from the Database. Figure out when to copy initial tables into game tables.
      adb = new AccessDB();
      for(int i = 1; i <= adb.getNumPlayers(); i++) {
         makeBot(i, adb.getPlayerStart(i), adb.getPlayerDest(i), adb.getPlayerStrength(i), 
            adb.getPlayerColor(i), adb);
      }//End for
   
   }//End setSimModel
   
   private void nullPath(PlayerBot aBot, Point d) {
      Point s = aBot.getPoint();
      int id = aBot.getID();
      if (aBot.getPath() == null) {
         aBot.stopPlaying();
         statusReport("Player " + id + " No path (" + (int)s.getX() + ", " + (int)s.getY() + ") " + 
                  "to (" + (int)d.getX() + ", " + (int)d.getY() + ")");
      }
      else {
         statusReport("Player " + id + " Path (" + (int)s.getX() + ", " + (int)s.getY() + 
               ") to (" + (int)d.getX() + ", " + (int)d.getY() + "), length " + aBot.getPath().size());
      }
   }//End nullPath

   public synchronized void simulateAlgorithm() {
      setStatus("simulateAlgorithm()");
      
      Random rand = new Random(System.currentTimeMillis());
      int num, id, w, str, wealth, gold, cost, activeCount, conflict;
      int numPlayers = adb.getNumPlayers();
      // get aBot from the list of created bots
      PlayerBot aBot = null;
      
      Point s, e, p, tmap;
      
      while (runnable()) {
         activeCount = 0;
         for (int i = 0; i < numPlayers; i++) {
            if (bot.get(i).isPlaying()) {
               activeCount++;
            }
         }
         
         //Check if at least one player is still playing
         if (activeCount > 0) {
            do { //Pick an active player to act
               num = rand.nextInt(numPlayers);
               aBot = bot.get(num);
            } while (!aBot.isPlaying());
         } 
         else {
            setSimRunning(false);
            setModelValid(false);
            animatePanel.setComponentState(false, false, false, false, true);
            //Do end of game stuff here
            int[] rank = adb.wealthiestPlayers();
            String order = "";
            for (int i = 0; i < rank.length; i++) {
               order += " " + rank[i];
            }
            System.out.println("Game Over. Players finished" + order + ".");
            adb.disconnect();
            return;
         }
         
         if (aBot.getPath() == null) {
            id = aBot.getID();
            s = adb.getPlayerStart(id);
            e = adb.getPlayerDest(id);
            wealth = adb.getPlayerWealth(id);
         
            statusReport("Player " + id + " Start (" + (int)s.getX() + ", " + (int)s.getY() + 
               "), Stop (" + (int)e.getX() + ", " + (int)e.getY() + "), Player " + aBot.getStrength() + 
               " $ " + wealth);
            
            //create first path here
            aBot.aStarMove(aBot.getGoal());
            animatePanel.clearTemporaryDrawables();
            nullPath(aBot, aBot.getGoal());
         }
         else {
            id = aBot.getID();
            conflict = adb.getPlayerConflict(id); 
            p = aBot.getPoint();
            w = adb.getPlayerWealth(id);
            str = aBot.getStrength();
            gold = adb.getTreasure(p);
            cost = adb.getCity(p);
            tmap = adb.getMap(p);
            
            //incoming player competes with the current location's wealthest player
            if (conflict != id) { 
               int w1 = adb.getPlayerWealth(conflict);
               
               if (w1 > w) {
                  statusReport("Contest! Player " + conflict + " $ " + w1 + " wins against player " + id + 
                     " $ " + w);
                  adb.setPlayerWealth(conflict, w1 + (w / 3));
                  adb.setPlayerWealth(id, w / 3);
               }
               else if (w > w1) {
                  statusReport("Contest! Player " + id + " $ " + w + " wins against player " + conflict + 
                     " $ " + w1);
                  adb.setPlayerWealth(id, w + (w1 / 3));
                  adb.setPlayerWealth(conflict, w1 / 3);
               }
            }
            //check for gold
            if (gold > 0) {
               statusReport("Player " + id + " Gold (" + (int)p.getX() + ", " + (int)p.getY() + 
                  ") $ " + gold + ", " + str + " $ " + w);
               map.get(p).makeNormal();
               aBot.setWealth(gold);
               adb.setTreasure(aBot.getPoint());
            } 
            //check for city
            else if (cost > 0 && w > cost) {
               statusReport("Player " + id + " City (" + (int)p.getX() + ", " + (int)p.getY() + 
                  ") $ " + cost + ", " + str + " $ " + w);
               aBot.setStrength(cost + aBot.getStrength());
               aBot.setWealth((cost * -1) + w);
            } 
            //check for map
            else if (tmap != null && !aBot.hasMap()) {
               statusReport("Player " + id + " Map (" + (int)p.getX() + ", " + (int)p.getY() + ") Treasure (" + 
                  (int)tmap.getX() + ", " + (int)tmap.getY() + ") " + str + " $ " + w);
               aBot.setHasMap();
               adb.setMap(p);
               map.get(p).makeNormal();
               aBot.aStarMove(tmap);
               nullPath(aBot, tmap);
            } 
            //check if player is at it's goal
            if (aBot.getPoint().equals(aBot.getGoal()) && !aBot.hasMap()) {
               
               statusReport("Player " + id + " Success, goal (" + (int)p.getX() + ", " + (int)p.getY() + 
                  ") " + str + " $ " + w);
               aBot.stopPlaying();
               //have display statement
            } 
            else if (aBot.getPath().isEmpty() && aBot.hasMap()) {
               aBot.setHasMap(); //should set hasMap from true to false
               
               aBot.aStarMove(aBot.getGoal());
               nullPath(aBot, aBot.getGoal());
               animatePanel.clearTemporaryDrawables();
            }
            else {
               aBot.move();
            }
         }//End if
         
         checkStateToWait();
      }//End while
   }//End simulateAlgorithm
      
}//End Class
