/*
Programmers: Kris Larson

Description: 

*/

import java.util.*;
import java.awt.*;

public class TestGameMenu {

   Scanner input = new Scanner(System.in);
   
   public TestGameMenu() {
      int choice;
      
      for (;;) {
         options();
         choice = input.nextInt();
         System.out.println("");
         
         switch(choice) {
            case 1:  startGame(); break;
            case 2:  instructions(); break;
            case 3:  about(); break;
            case 4:  futureAdditions(); break;
            case 5:  exitGame(); break;
            
            default:
            System.out.println("INVALID INPUT!\n");
         }
         
      }
   }
   
   public void options() {
      System.out.println("\n----------\nMain Menu\n----------");
      System.out.print("1) Start Game\n2) Instructions\n3) About\n4) Future Additions\n5) Exit Game\n");
      System.out.print("Please enter your choice (1-5): ");
   }
   
   public void startGame() {
      //Start game :)
      GameSimulator run = new GameSimulator();
   }
   
   public void instructions() {
      boolean loop = true;
      while (loop) {
         System.out.println("There were instructions for this thing?");
         
         System.out.print("Enter any key to go back. ");
         String exit = input.next();
         
         if (!exit.isEmpty())
            loop = false;
      }
   
   }
   
   public void about() {
      boolean loop = true;
      while (loop) {
         System.out.println("Enter epic and or strange story here.");
         
         System.out.print("Enter any key to go back. ");
         String exit = input.next();
         
         if (!exit.isEmpty())
            loop = false;
      }
   }
   
   public void futureAdditions() {
      boolean loop = true;
      while (loop) {
         System.out.println("Here's a list of things that may be added in the future:");
         System.out.print("-Saving\n-Difficulty Options\n-Score Board\n-Achievements\n-Graphical Interface\n");
         
         System.out.print("Enter any key to go back. ");
         String exit = input.next();
         
         if (!exit.isEmpty())
            loop = false;
      }
   }
   
   public void exitGame() {
      boolean loop = true;
      while (loop) {
         System.out.println("All gameplay will be lost. Are you sure you want to quit (Y/N)? ");
         String exit = input.next();
         
         if (exit.compareToIgnoreCase("Y") == 0) {
            System.out.print("Bye!");
            System.exit(9);
         }
         else if (exit.compareToIgnoreCase("N") == 0)
            loop = false;
         else
            System.out.println("INVALID INPUT!\n");
      }
   }
   	
	public static void main(String[] args) {  
      TestGameMenu start = new TestGameMenu();
	}
}

