/* 
   Programmers: Kristoffer Larson
   
   Description: 
      It creates a queue that's used to step through the BST.

*/
import java.util.LinkedList;
public class TreeIterator<T> implements java.util.Iterator<T> {
   private BinaryTreeBasis<T> binTree;
   private TreeNode<T> currentNode;
   public LinkedList <TreeNode<T>> queue;
   
   public TreeIterator(BinaryTreeBasis<T> bTree) {
      binTree = bTree;
      currentNode = null;
      // empty queue indicates no traversal type currently
      // selected or end of current traversal has been reached
      queue = new LinkedList <TreeNode<T>>();
   } // end constructor
   
   public boolean hasNext() {
      return !queue.isEmpty();
   } // end hasNext
   
   public T next() throws java.util.NoSuchElementException {
      currentNode = queue.remove();
      return currentNode.item;
   } // end next
   
   public void remove() throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
   } // end remove

   public void setInorder() {
      queue.clear();
      inorder(binTree.root);
   } // end setInorder

   private void inorder(TreeNode<T> treeNode) {
   if (treeNode != null) {
         inorder(treeNode.leftChild);
         queue.add(treeNode);
         inorder(treeNode.rightChild);
      } // end if
   } // end inorder
} // end TreeIterator
