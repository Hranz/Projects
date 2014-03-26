/*
   Programmer: Kristoffer Larson
   Date: March , 2014
   
   Description: Checks the WayPoint for the PlayerBot
*/

import SimulationFramework.*;
import java.awt.Point;

public class CheckWayPoint {

   PlayerBot aBot;
   Point end;
   SimFrame s;
   String status;

   public CheckWayPoint(PlayerBot aBot, SimFrame s) {
      this.aBot = aBot;
      this.s = s;
      end = aBot.getDest();
   }//End SpotCheck() Constructor
   
   public void checkWP(WayPoint aWayPoint) {
      if (aWayPoint.getCityValue() > 0) {
         cityWP(aWayPoint);
      } 
      else if (aWayPoint.getGold() > 0) {
         goldWP(aWayPoint);
      } 
      else if ((aWayPoint.getMapX() > 0 || aWayPoint.getMapY() > 0)) {
         mapWP(aWayPoint);
      } 
      else {
         normalWP(aWayPoint);
      }
   }
   
   private void cityWP(WayPoint aWayPoint) {
      status = "City (" + (int)aWayPoint.getWX() + ", " + (int)aWayPoint.getWY() + ") $ " +
         aWayPoint.getCityValue() + ", Player " + aBot.getStrength() + " $ " +
         aBot.getWealth();
      s.statusReport(status);
      
      int c = aWayPoint.getCityValue();
      if (aBot.getWealth() > c) {
         aBot.setWealth(aBot.getWealth() - c);
         aBot.setStrength(aBot.getStrength() + c);
      }
   }
   
   private void goldWP(WayPoint aWayPoint) {
      status = "Gold (" + (int)aWayPoint.getWX() + ", " + (int)aWayPoint.getWY() + ") $ " +
         aWayPoint.getGold() + ", Player " + aBot.getStrength() + " $ " +
         aBot.getWealth();
      s.statusReport(status);
      
      if (aBot.getTMap()) {
         aBot.setTMap(false);
         aBot.setDest(end);
      }

      aBot.setWealth(aBot.getWealth() + aWayPoint.getGold());
      aWayPoint.makeNormal();
   }
   
   private void mapWP(WayPoint aWayPoint) {
      status = "Map (" + (int)aWayPoint.getWX() + ", " + (int)aWayPoint.getWY() + 
         ") Treasure (" + aWayPoint.getMapX() + ", " + aWayPoint.getMapY() 
         + ") Player " + aBot.getStrength() + " $ " + aBot.getWealth();
      s.statusReport(status);
      
      if (!aBot.getTMap()) {
         aBot.setDest(new Point(aWayPoint.getMapX(), aWayPoint.getMapY()));
         aBot.setTMap(true);
      }
      
      aWayPoint.makeNormal();
   }
   
   private void normalWP(WayPoint aWayPoint) {
      status = "WayPoint (" + (int)aWayPoint.getWX() + ", " + (int)aWayPoint.getWX() +
         "), Player " + aBot.getStrength() + " $ " + aBot.getWealth();
      s.statusReport(status);
   }

}
