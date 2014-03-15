/*
   Programmers: Kristoffer Larson
   Date: February 24, 2014
   
   Description: 
      PlayerBot is a child of Bot. It travels between WayPoints
      and loses strength as it moves. It will gain strength at
      City WayPoints by paying gold, and gold at Gold WayPoints.
      Map WayPoints aren't handled in this project.
*/

import java.awt.*;
import java.util.*;
import SimulationFramework.*;

public class PlayerBot extends Bot {
   
   private int lastX, lastY, lastHeight, strength, wealth;
   private WayPoint go;
   
   //Creates a "bot" to travel between WayPoints
   public PlayerBot(String label, Point a, int height) {
      super(label, a, Color.RED);
      strength = 500;
      wealth = 1000;
      lastX = (int)a.getX();
		lastY = (int)a.getY();
      lastHeight = height;
   }//End Constructor
   
   public int getWealth() {
      return wealth;
   }//End getWealth
   
   public int getStrength() {
      return strength;
   }//End getStrength
   
   public void setWealth(int wealth) {
      this.wealth = wealth;
   }//End setWealth
   
   public void setStrength(int strength) {
      this.strength = strength;
   }//End setStrength
   
   public void setMove(WayPoint go) {
      this.go = go;
   }//End setMove
   
   public void reset() {
		super.reset();
		lastX = (int) point.getX(); 
		lastY = (int) point.getY();
   }//End reset
   
   //Handles the movement between WayPoints
   public void move() {
      int newX, newY, height;
      
      newX = go.getWX();
      newY = go.getWY();
      height = go.getHeight();

      int distanceOfMove = (int)(Math.sqrt(Math.pow(newX - lastX, 2)
         + Math.pow(newY - lastY, 2) + Math.pow(lastHeight - height, 2)));
		
      moveTo(newX, newY);
      //Reduces the strength of the "bot"
      strength -= distanceOfMove;
      //Sets current location of the "bot"
		lastX = newX;
	   lastY = newY;
      lastHeight = height;
   }//End move
   
}//End Class