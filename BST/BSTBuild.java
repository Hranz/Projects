/*
   Programmers: Kristoffer Larson

   Description:
      Create 40 Binary Search Trees. 20 randomly created trees and 20 balanced trees.
      Each tree is created with 2^10-1 nodes, then has 2^9-1 nodes deleted, finally
      2^8-1 nodes are inserted. After each operation an average and max level will be
      calculated then stored in an output file, for documentation.


*/
import java.util.*;
import java.io.*;

public class BSTBuild {
   private ArrayList<Integer> list = new ArrayList<Integer>();
   private BinarySearchTree tree = new BinarySearchTree<Integer>();
   protected int m;
   
   public static void main(String[] args) {
      try {
         File targetFile = new File("BSTLevels.txt");
			   if (targetFile.exists()) {
               targetFile.delete();
			   }  
         PrintWriter output = new PrintWriter(targetFile);
         Project2 start = new Project2(output); //Must pass output into the constructor all the way to BST methods, find solution.
      }
      catch (IOException  e) {
         System.err.println("IOException : " + e.getMessage());
      }
      
   }
   
   public BSTBuild(PrintWriter output) {  
      for (m = 1; m < 3; m++) { //Loops twice. First for random trees. Second for balanced trees.
         BST(output);    
      }  //End for loop
      output.close();
   } //End constructor
   
   public void BST(PrintWriter output) { //Create 20 random binary search trees
      for (int i = 1; i < 21; i++) {
         arrayCreation(10); //Create ArrayList
         for (int j = 1; j < 4; j++) {
            if (m == 1)
               arrayShuffle();
            switch (j) {
               case 1: createBST(0, list.size() - 1); writeToFile(output, j, i); break;
               case 2: deleteValue(9); writeToFile(output, j, i); break;
               case 3: reInsertValue(8); writeToFile(output, j, i); break;
            } //End switch
         } //End inner loop
         tree.makeEmpty(); //So there's a fresh tree for the next loop
         list.clear(); //So there's a fresh ArrayList for the next loop
      } //End outer loop
   } //End of tree
   
   public void arrayCreation(int num) { //Fills ArrayList    
      for (int i = 0; i < (int)Math.pow(2,num) - 1; i++) {
         Integer a = new Integer(i); //Change int to Integer Object for the ArrayList and tree
         list.add(a); //Store num into ArrayList<Integer> list
      }     
   } //End arrayCreation
   
   public void arraySort() { //Sort the ArrayList
      for (int i = 0; i < list.size(); i++) {
         for (int j = i + 1; j < list.size(); j++) {
            if (list.get(i).compareTo(list.get(j)) > 0) {
               Integer temp = list.get(i);
               list.set(i,list.get(j));
               list.set(j,temp);
            } //End if
         } //End inner loop
      } //End outer loop     
   } //End arraySort
   
   public void arrayShuffle() {
      for (int i = 0; i < list.size(); i++) {
         Random gen = new Random();
         int j = gen.nextInt(list.size() - i) + i;
            Integer temp = list.get(i);
            list.set(i,list.get(j));
            list.set(j,temp);
      } //End for loop
   } //End arrayShuffle
      
   public void createBST(int low, int high) { //Binary insertion from the ArrayList 
      if (high < low) //Base case
         return;
      else if (low < high) {
         int mid = (low + high) / 2;
         tree.insert(list.get(mid)); //Insert middle values of ArrayList into tree
         createBST(low, mid - 1);
         createBST(mid + 1, high);
      }
      else if (low == high) {
         tree.insert(list.get(low)); //Insert the end most values of ArrayList into tree
         return;
      }
      else
         return;
   } //End insertValue
   
   public void deleteValue(int num) { //Delete items from the tree
      arrayShuffle();
      for (int i = 0; i < (int)Math.pow(2, num) - 1; i++) {
         list.remove((int)Math.pow(2,num)); //Remove values from the ArrayList, from Math.pow(2,num) to the end
      } //End for loop
      for (int i = 0; i < (int)Math.pow(2, num); i++) {
         tree.delete(list.get(i));
      } //End for loop
   } //End deleteValue
   
   public void reInsertValue(int num) { //Insert ArrayList items into the binary search tree
      arrayShuffle();
      for (int i = 0; i < Math.pow(2, num) - 1; i++) {
         tree.insert(list.get(i));
      } //End for loop
   } //End of reInsertValue
   
   public double averageLevel() {
      TreeIterator step = new TreeIterator(tree);
      double sum = 0;
      step.setInorder();
      
      if (step.hasNext()) {
         for (int i = 0; i < step.queue.size(); i++) {
            int level = tree.findLevel((Integer)((TreeNode)step.queue.get(i)).item);
            sum += level;
         }
         sum /= step.queue.size();
      } //End if
      return sum;
   }
   
   public void writeToFile(PrintWriter output, int a, int i) {
      switch (m) {
         case 1: output.print("Random BST " + i + "\n"); break;
         case 2: output.print("Balanced BST " + i +"\n"); break;
      } //End switch
      
      switch (a) {
         case 1: output.println("Created BST"); break;
         case 2: output.println("Deleted from BST"); break;
         case 3: output.println("Inserted into BST"); break;
      } //End switch
         
      output.println("\tMax level is " + tree.maxLevel()); //Write maxLevel to file
      output.println("\tAverage level is " + averageLevel() + "\n"); //Write averageLevel to file
   } //End writeToFile
} //End Project2
