import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
	public static class Node{
		public String index;
		public double d;
		public Node pi;
		public Node() {
			index = "NA"; d = 0; pi = null;
		}
		public Node(String index) {
			this.index = index; this.d = 0; pi = null;
		}
		public Node(String index, double d, Node pi) {
			this.index = index; this.d = d; this.pi=pi;
		}
	}
	public static class Edge{
		Node v; //End of edge
		double w; //Weight between start node and v.
		public Edge(Node v, double w) {
			this.v = v; this.w=w;
		}
	}
	public static class Graph{
		Map<Node,List<Edge>> AdjacencyList = new HashMap<>();
		public Graph() {}
		public void addNode(Node u) { //Note: this also initializes a new adjacencyList.
			AdjacencyList.put(u, new LinkedList<Edge>());
		}
		public void addEdge(Node u, Node v, double w) { //w(u,v)
			if(!AdjacencyList.keySet().contains(u) || !AdjacencyList.keySet().contains(v)) {
				System.out.println("One of the Nodes is not in the NodeSpace. Please Add This Node Before Adding An Edge");
			}
			else if(AdjacencyList.keySet().contains(u) && AdjacencyList.keySet().contains(v)) {
				AdjacencyList.get(u).add(new Edge(v,w));
			}
		}
		public void printGraph() {

			for (Node u: AdjacencyList.keySet()) {
				System.out.println("Node:   " + u.index + ":");
				for(Main.Edge v : AdjacencyList.get(u)) {
					System.out.println("  ====>" + v.v.index + " ---- " + v.w);
				}
			}
			System.out.println();
		}
		public Node getNode(String index) {
			for(Node v: AdjacencyList.keySet()) {
				if (v.index.contentEquals(index)) return v;
			} System.out.println("Node not in graph."); return null;
		}
		
	}

	public static void Initialize_Single_Source(Graph G, Node s) {
		for(Node v:G.AdjacencyList.keySet()) {
			v.d = Integer.MAX_VALUE;
			v.pi = null;
		}
		s.d=0;
	}
	public static boolean RELAX(Node u, Node v, double w) { //w(u,v)
		if (v.d > u.d + w) {
			v.d = u.d + w;
			v.pi = u;
			return true;
		}
		return false;
	}
	
	public static Node ExtractMin(Queue<Node> Q) {
		Node min = Q.peek();
		for (Node v: Q) {
			if (min.d > v.d) {
				min = v;
			}
		}
		Q.remove(min);
		return min;
	}
	public static boolean Modified_Dijkstra(Graph G, Node s) {
		if(!G.AdjacencyList.keySet().contains(s)) {
			System.out.println("Source is not contained in graph.");
			return false;
		}
		Initialize_Single_Source(G,s);
		Queue<Node> Q = new LinkedList<Node>();
		Q.add(s);
		while (!Q.isEmpty()) {
			Node u = ExtractMin(Q);
			for (Edge v : G.AdjacencyList.get(u)) {
				if (RELAX(u,v.v,v.w)) {
					if (Q.contains(v.v)) {
						Q.remove(v.v);
					}
					Q.add(v.v);
				}
			}
		}
		return true;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Scanner sourceScanner = new Scanner(new File("Graph Data Algorithms Assignment 8 Ian Sinclair.txt"));
		Scanner fileScanner = new Scanner(new File("Graph Data Algorithms Assignment 8 Ian Sinclair.txt"));
		Scanner LineScanner = new Scanner(new File("Graph Data Algorithms Assignment 8 Ian Sinclair.txt"));
		
		Graph G = new Graph();
		
		while(fileScanner.hasNextLine()) {
			String index = fileScanner.next();
			G.addNode(new Node(index));
			fileScanner.nextLine();
		}
		fileScanner.close();
		
		while(LineScanner.hasNextLine()) {
			String Line = LineScanner.nextLine();
			Scanner edgeScanner = new Scanner(Line);
			
			String startNode = edgeScanner.next();
			String EndNode = edgeScanner.next();
			
			while(edgeScanner.hasNextDouble()) {
				G.addEdge(G.getNode(startNode), G.getNode(EndNode), edgeScanner.nextDouble());
				if(edgeScanner.hasNext()) EndNode = edgeScanner.next();
			}
			edgeScanner.close();
		}
		LineScanner.close();
		
		String source = sourceScanner.next();
		sourceScanner.close();
		Node s = G.getNode(source);
		
		System.out.println("Printing Adjacency List:");
		System.out.println();
		G.printGraph();
		System.out.println("_________________________________________________");
		
		Modified_Dijkstra(G,s);
		
		System.out.println("Modified Dijkstra's Algorithm Shortest Path: ");
		for(Node u : G.AdjacencyList.keySet()) {
			System.out.println(u.index + ":");
			System.out.println("     Distance:  " + u.d);
			if (u != s) System.out.println("     Predecessor:  " + u.pi.index);
		}
	}

}
