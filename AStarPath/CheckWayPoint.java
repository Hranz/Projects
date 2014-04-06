/*
   Programmer: Kristoffer Larson
   Date: March 28, 2014
   
   Description: Checks the WayPoint for the PlayerBot
*/

import SimulationFramework.*;
import java.awt.Point;

public class CheckWayPoint {

   private PlayerBot aBot;
   private Point end;
   private SimFrame s;

   public CheckWayPoint(PlayerBot aBot, SimFrame s) {
      this.aBot = aBot;
      this.s = s;
      end = aBot.getDest();
   }//End SpotCheck() Constructor
   
   //Check the type of WayPoint
   public void checkWP(WayPoint aWayPoint) {
      if (aWayPoint.getCityValue() > 0) {
         cityWP(aWayPoint);
      } 
      else if (aWayPoint.getGold() > 0) {
         goldWP(aWayPoint);
      } 
      else if ((aWayPoint.getMapX() > 0 || aWayPoint.getMapY() > 0) && !aBot.getTMap()) {
         mapWP(aWayPoint);
      } 
      else {
         normalWP(aWayPoint);
      }
   }//End checkWP() 
   
   //Player accesses a city
   private void cityWP(WayPoint aWayPoint) {
      s.statusReport("City (" + (int)aWayPoint.getWX() + ", " + (int)aWayPoint.getWY() + ") $ " +
         aWayPoint.getCityValue() + ", Player " + aBot.getStrength() + " $ " +
         aBot.getWealth());
      
      int c = aWayPoint.getCityValue();
      if (aBot.getWealth() > c) {
         aBot.setWealth(aBot.getWealth() - c);
         aBot.setStrength(aBot.getStrength() + c);
      }
   }//End cityWP()
   
   //Player accesses gold
   private void goldWP(WayPoint aWayPoint) {
      s.statusReport("Gold (" + (int)aWayPoint.getWX() + ", " + (int)aWayPoint.getWY() + ") $ " +
         aWayPoint.getGold() + ", Player " + aBot.getStrength() + " $ " +
         aBot.getWealth());

      aBot.setWealth(aBot.getWealth() + aWayPoint.getGold());
      aWayPoint.makeNormal();
   }//End goldWP()
   
   //Player accesses a map
   private void mapWP(WayPoint aWayPoint) {
      s.statusReport("Map (" + (int)aWayPoint.getWX() + ", " + (int)aWayPoint.getWY() + 
         ") Treasure (" + aWayPoint.getMapX() + ", " + aWayPoint.getMapY() 
         + ") Player " + aBot.getStrength() + " $ " + aBot.getWealth());
      
      if (!aBot.getTMap()) {
         aBot.setDest(new Point(aWayPoint.getMapX(), aWayPoint.getMapY()));
         aBot.setTMap(true);
      }
      
      aWayPoint.makeNormal();
   }//End mapWP()
   
   //Player accesses a normal point
   private void normalWP(WayPoint aWayPoint) {
      s.statusReport("WayPoint (" + (int)aWayPoint.getWX() + ", " + (int)aWayPoint.getWY() +
         "), Player " + aBot.getStrength() + " $ " + aBot.getWealth());
         
      if (aBot.getTMap()) {
         aBot.setTMap(false);
         aBot.setDest(end);
      }
   }//End normalWP()

}//End CheckWayPoint class