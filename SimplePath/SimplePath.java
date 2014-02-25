/*
   Programmers: Kristoffer Larson
   Date: February 24, 2014
   
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
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.HashMap;

import SimulationFramework.*;

public class SimplePath extends SimFrame   {
	// eliminate warning @ serialVersionUID
   private static final long serialVersionUID = 42L;
   // GUI components for application's menu
   /** the simulation application */
   private SimplePath app;
   // application variables;
   /** the actors "bots" of the simulation */
   private ArrayList <PlayerBot> bot;
      
   private int moves;
   private Point start, dest;
   
   HashMap<Point, WayPoint> map = new HashMap<Point, WayPoint>();
   Point keyPoint;

   public static void main(String args[]) {
      SimplePath app = new SimplePath("SimplePath", "terrain282.png");
      app.start();  // start is inherited from SimFrame
   }

/**
Make the application:  create the MenuBar, "help" dialogs, 
*/

   public SimplePath(String frameTitle, String imageFile) {
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
                  JOptionPane.showMessageDialog( SimplePath.this, 
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
                  JOptionPane.showMessageDialog( SimplePath.this, 
                     "Kristoffer Larson and Josue Ruiz \n" +
                     "kristoffer.larson.967@my.csun.edu " +
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

   public void setSimModel() {
      readWayPoints();
   	// set any initial visual Markers or Connectors
   	// get any required user mouse clicks for positional information.
   	// initialize any algorithm halting conditions (ie, number of steps/moves).
      setStatus("Initial state of simulation");
   
      Iterator <WayPoint> iterator = map.values().iterator();
      while (iterator.hasNext()) { //Draw WayPoints
         animatePanel.addPermanentDrawable(iterator.next());
      }//End while
      animatePanel.repaint();
   
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
      Marker mark = new Marker(start, Color.GREEN, 6);
      animatePanel.addPermanentDrawable(mark);
      
      //Destination
      setStatus("Enter bot destination");
      waitForMousePosition();
      x = (int)mousePosition.getX();
      y = (int)mousePosition.getY();
      
      a = nearestWayPoint(x, y);
      dest = new Point(a.getWX(), a.getWY());
      mark = new Marker(dest, Color.RED, 6);
      animatePanel.addPermanentDrawable(mark); 
   }//End setSimModel

   public void readWayPoints() {
      //Read from file
      String fileName = "waypoint.txt";
      File sourceFile = new File(fileName);
      
      if (!sourceFile.exists()) {
         System.out.print(fileName + " doesn't exist.");
         System.exit(0);
      }//End if
      
      try {
         Scanner input = new Scanner(sourceFile);
         while(input.hasNext()) {
            readFile(input); 
         }//End while
      } catch (IOException e) {
         System.err.println("IOException: " + e.getMessage());
      }//End try/catch   
   }//End readWayPoints
   
   private void readFile(Scanner input) {
      //Creates WayPoints with values that are read in
      int x = input.nextInt();
      int y = input.nextInt();
      keyPoint = new Point(x, y);
      int height = input.nextInt();
      int cityValue = input.nextInt();
      int gold = input.nextInt();
      int mapX = input.nextInt();
      int mapY = input.nextInt();
      int neighbor = input.nextInt();
   
      if (cityValue > 0) { //City WayPoint
         map.put(keyPoint, 
            (new WayPoint(keyPoint, height, cityValue, neighbor)));
      } else if (gold > 0) //Gold WayPoint
         map.put(keyPoint, 
            (new WayPoint(x, y, height, gold, neighbor)));
      else if (mapX > 0 || mapY > 0) //Map WayPoint
         map.put(keyPoint, 
            (new WayPoint(x, y, height, mapX, mapY, neighbor)));
      else //Normal WayPoint
         map.put(keyPoint, 
            (new WayPoint(x, y, height, neighbor)));
   }//End readFile

   public synchronized void simulateAlgorithm() {
      setStatus("simulateAlgorithm()");
     
      ArrayList<WayPoint> moves = new ArrayList<WayPoint>(8);
      
      Point aPoint, temp = null, prevPoint = null;
      
      int c, g, tx, ty;
      double dist, oldDist;
      int destH = map.get(dest).getHeight();
      
      WayPoint aWayPoint;
            
      PlayerBot aBot = bot.get(0);
         
      System.out.printf("Start (%d, %d), Stop (%d, %d), Player %d $ %d\n", (int)start.getX(),
         (int)start.getY(), (int)dest.getX(), (int)dest.getY(), aBot.getStrength(), 
         aBot.getWealth());
         
      while (runnable()) {         
         aPoint = aBot.getPoint();
         aWayPoint = map.get(aPoint);
            
         if (aWayPoint.getCityValue() > 0) {
            //Accessing a city WayPoint
            setStatus("city");
            System.out.printf("City (%d, %d) $ %d, Player %d $ %d\n", (int)aPoint.getX(),
               (int)aPoint.getY(), aWayPoint.getCityValue(), aBot.getStrength(), 
               aBot.getWealth());
                  
            c = aWayPoint.getCityValue();
            if (aBot.getWealth() >= c) {
               aBot.setWealth(aBot.getWealth() - c);
               aBot.setStrength(aBot.getStrength() + c);
            }//End if
         } else if (aWayPoint.getGold() > 0) {
            //Accessing a gold WayPoint
            setStatus("gold");
            System.out.printf("Gold (%d, %d) $ %d, Player %d $ %d\n", (int)aPoint.getX(),
               (int)aPoint.getY(), aWayPoint.getGold(), aBot.getStrength(), aBot.getWealth());
                  
            g = aWayPoint.getGold();
            aBot.setWealth(aBot.getWealth() + g);
         }//End if
            
         aWayPoint.setVisted(); //Set WayPoint to visited

         if (aBot.getStrength() < 0) {
            setSimRunning(false);
            setModelValid(false);
            animatePanel.setComponentState(false, false, false, false, true);
            setStatus("Player has run out of strength, algorithm is done");
            System.out.printf("Failure (%d, %d), Player %d $ %d\n", (int)start.getX(),
               (int)start.getY(), aBot.getStrength(), aBot.getWealth());
            return;
         } else if (aBot.getPoint().equals(dest)) {
            setSimRunning(false);
            setModelValid(false);
            animatePanel.setComponentState(false, false, false, false, true);
            setStatus("Player has won, algorithm is done");
            System.out.printf("Success, goal (%d, %d) Player %d $ %d\n", (int)dest.getX(),
               (int)dest.getY(), aBot.getStrength(), aBot.getWealth());
            return;
         }//End if
         
         moves.clear(); //Clear out previous WayPoints
         //Check spaces around for moving
         Point topLeft = new Point((int)aBot.getPoint().getX() - 20,
            (int)aBot.getPoint().getY() - 20);
            
         for (int i = (int)topLeft.getX(); i <= (int)topLeft.getX() + 40; i += 20) {
            for (int j = (int)topLeft.getY(); j <= (int)topLeft.getY() + 40; j += 20) {
               aWayPoint = map.get(new Point(i, j));
               if (aWayPoint != null) {
                  if (!aWayPoint.getVisted()) { 
                     moves.add(aWayPoint);
                  }//End if
               }//End if
            }//End j for loop
         }//End i for loop
         
         if (!moves.isEmpty()) {
            //Find closest WayPoint to destination
            oldDist = 200000.0;
            for (int i = 0; i < moves.size(); i++) {
               aWayPoint = moves.get(i);
               dist = Math.sqrt(Math.pow(((int)dest.getY() - aWayPoint.getWY()), 2) 
                  + Math.pow(((int)dest.getX() - aWayPoint.getWX()), 2) 
                  + Math.pow(destH - aWayPoint.getHeight(), 2));
                  
               if (dist < oldDist) {
                  oldDist = dist;
                  temp = new Point(aWayPoint.getWX(), aWayPoint.getWY());
               }//End if
            }//End for
            
            if (temp != null) {
               aBot.setMove(map.get(temp));
               aBot.move();//Bot moves and reduces strength
            }//End if
         } else {
            setSimRunning(false);
            setModelValid(false);
            animatePanel.setComponentState(false, false, false, false, true);
            setStatus("Player has no more available moves, algorithm is done");
            System.out.printf("Failure no path, Player %d $ %d\n", aBot.getStrength(), 
               aBot.getWealth());
            return;
         }//End if
            
         checkStateToWait();
      }//End while
   }//End simulateAlgorithm
   
}//End Class
