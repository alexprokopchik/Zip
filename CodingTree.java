package compressedLiterature;
/*
 * Alex Prokopchik
 * February 20, 2014
 * CodingTree.java
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * This class compresses data based on character frequency in a message using 
 * David Huffman's method.
 * 
 * @authors alexpro
 * @version 1.0
 */
public class CodingTree {
	/**
	 * The root node of the tree of nodes.
	 */
	Node root;
	int size;
	/**
	 * A public data member that is a map of characters to codes created by the tree.
	 */
	Map<Character, String> codes;
	List<Character> newChars;
	List<Node> nodes;
	List<Integer> count;
	int total;
	/**
	 * A constructor that takes the text of a message to be compressed. This constructor is 
	 * responsible for calling all methods that carry out the Huffman coding algorithm.
	 * 
	 * @param string the list of letters that will have its frequency counted
	 */
	public CodingTree(String message) {
		root = null;
		total = message.length();
		size = 0;
		codes = new HashMap<>();
		nodes = new ArrayList<>();
		count = new ArrayList<>();
		newChars = new ArrayList<>();
		createNodes(message);
	}
	/**
	 * This method creates the nodes for the tree.
	 * 
	 * @param message the list of characters created from the text passed.
	 */
	private void createNodes(String message) {
		Node temp;
		for (int i = 0; i < message.length(); i++) {			
			if (newChars.contains(message.charAt(i))) {
				int index = newChars.indexOf(message.charAt(i));
				count.set(index, count.get(index)+1);
			} else {
				newChars.add(message.charAt(i));
				count.add(1);
			}
		}
		for (int i = 0; i < newChars.size(); i++) {
			temp = new Node(newChars.get(i), count.get(i));
			nodes.add(temp);
		}
		PriorityQueue<Node> leastFirst = createPriority();
		//System.out.println(nodes);
		//System.out.println(leastFirst);
		adding(leastFirst);
		order();
		//System.out.println(codes);
	}
	/**
	 * 
	 * @return a Priority queue that contians the list of nodes
	 * with the weight of its frequency
	 */
	private PriorityQueue<Node> createPriority() {
		PriorityQueue<Node> pq = new PriorityQueue<Node>(nodes);
		return pq;
	}
	
	/**
	 * Creates the node tree based on the weights of frequency count
	 * @param pq the priorityQueue
	 */
	private void adding(PriorityQueue<Node> pq) {
		while (pq.size() > 1) {
			Node temp = pq.poll();
			Node temp2 = pq.poll();
			pq.add(combineNodes(temp, temp2));
		}
		root = pq.poll();
		Node tempNode = root;
		while (!(tempNode.left == null)) {
			tempNode = tempNode.left;
		}
		
	}
	
	/**
	 * Recursive function that reads codes out of the finished map
	 * 
	 * @param n1 node 1 to be combined
	 * @param n2 node 2 to be combined with n1
	 * @return the head node with combined nodes
	 */
	public Node combineNodes(Node n1, Node n2) {
		Node tempnode = new Node();
		int frequency =n1.frequency + n2.frequency;
		if (n1.frequency > n2.frequency) {
			tempnode.right = n1;
			tempnode.left = n2;
			tempnode.frequency = frequency;
		} else {
			tempnode.right = n2; 
			tempnode.left = n1;
			tempnode.frequency = frequency;
		}
		return tempnode;
	}
	/**
	 * uses a preorder traversal to get chars and to list of codes
	 * helper method that gets called recursively. 
	 * 
	 * @param bits String, the bitstring coding
	 * @param root Node, the current place of the node
	 * 
	 */
private void preorder(String bits, Node root){
		//If root is not equal to an empty node and null
		if(!(root.c == '^') && !(root == null)) {
			codes.put(root.c, bits);
		} else {
			String a = bits + "0";
			String b = bits + "1";
			preorder(a, root.left);
			preorder(b, root.right);
		}
		
		System.out.println(codes);
		
	}
	/**
	 * Method used to add to list of codes used in sequence with helper method.
	 */
	public void order() {
		 preorder("", root);
		
	}
	
	private class Node implements Comparable<Node>{
		/**
		 * The node character.
		 */
		Character c;
		/**
		 * The frequency count for the character.
		 */
		int frequency;
		/**
		 * A node connection of lesser weight.
		 */
		Node left;
		/**
		 * A node connection of greater weight.
		 */
		Node right;
		
		/**
		 * Constructs the node
		 * 
		 * @param ch the char that is tracked
		 * @param frequency the char frequency set
		 */
		
		public Node(Character ch, int frequency){
			c = ch;
			this.frequency = frequency;
			left = null;
			right = null;
		}
		
		
		/**
		 * An empty constructor containing a symbol that doesnt exist within the text.
		 */
		public Node() {
			c = '^';
		}
		/**
		 * A method used to compare nodes by frequency
		 */
		@Override
		public int compareTo(Node o) {
			return frequency - o.frequency;
		}
	}


}
