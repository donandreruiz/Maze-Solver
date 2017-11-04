package sp3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;


public class Maze {
	private static final int DEFAULT_DIM = 10;
	
	private int dim;
	private Map<Position,Set<Position>> adjList;
	
	public static class Position {
		Integer x;
		Integer y;
		
		public Position(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		@Override
		public int hashCode() {
			return (2*x.hashCode()) ^ y.hashCode();
		}
		
		@Override
		public boolean equals(Object o) {
			if(!(o instanceof Position))
				return false;
			Position p = (Position) o;
			return x.equals(p.x) && y.equals(p.y);
		}
		
		@Override
		public String toString() {
			return "(" + x + "," + y + ")";
		}
	}
	
	private static class Edge {
		Position p1;
		Position p2;
		
		public Edge(int x1, int y1, int x2, int y2) {
			this.p1 = new Position(x1,y1);
			this.p2 = new Position(x2,y2);
		}
		
		@Override
		public int hashCode() {
			return p1.hashCode() + p2.hashCode();
		}
		
		@Override
		public boolean equals(Object o) {
			if(!(o instanceof Edge))
				return false;
			Edge e = (Edge) o;
			return (p1.equals(e.p1) && p2.equals(e.p2)) ||
					(p2.equals(e.p1) && p1.equals(e.p2));
		}
		@Override
		public String toString() {
			return "<" + p1 + "," + p2 + ">";
		}
	}
	
	public Maze() {
		dim = DEFAULT_DIM;
		generateMaze();
	}

	public Maze(int n) {
		dim = n;
		generateMaze();
	}
	
	public int getDimension() {
		return dim;
	}
	
	/*
	 * Returns the set of neighboring positions in the maze.
	 */
	public Set<Position> getNeighboringSpaces(Position p) {
		if (p.x < 0 || p.x >= dim || p.y < 0 || p.y >= dim)
			throw new IllegalArgumentException("The position " + p + " is outside of the maze dimensions " + dim + ".");
		return adjList.get(p);
	}
	
	/*
	 * Create a maze using Kruskal's algorithm to find a random minimum spanning tree of the maze's squares.
	 */
	private void generateMaze() {
		// Associate a random weight with each potential edge in the maze's graph
		Map<Edge,Integer> edgeWeights = new HashMap<Edge, Integer>();
		List<Edge> edges = new ArrayList<Edge>();
		UnionFind<Position> uf = new UnionFind<Position>();
		adjList = new HashMap<Position, Set<Position>>();
		Random random = new Random();
		
		// Generate the vertices (squares of the maze)
		//  and create a list of possible edges (adjacencies) in the final maze
		for (int i = 0; i < dim-1; i++) {
			for (int j = 0; j < dim-1; j++) {
				Edge e1 = new Edge(i,j,i+1,j);
				edgeWeights.put(e1, random.nextInt());
				edges.add(e1);
				Edge e2 = new Edge(i,j,i,j+1);
				edgeWeights.put(e2, random.nextInt());
				edges.add(e2);
				
				Position p = new Position(i,j);
				adjList.put(p, new HashSet<Position>());
				uf.addSet(p);
			}
			Edge e3 = new Edge(i,dim-1,i+1,dim-1);
			edgeWeights.put(e3, random.nextInt());
			edges.add(e3);
			Edge e4 = new Edge(dim-1,i,dim-1,i+1);
			edgeWeights.put(e4, random.nextInt());
			edges.add(e4);

			Position p1 = new Position(i,dim-1);
			adjList.put(p1, new HashSet<Position>());
			uf.addSet(p1);
			Position p2 = new Position(dim-1,i);
			adjList.put(p2, new HashSet<Position>());
			uf.addSet(p2);
		}
		adjList.put(new Position(dim - 1,dim - 1), new HashSet<Position>());
		uf.addSet(new Position(dim - 1,dim - 1));
		
		// Sort the edges by their random weights
		Comparator<Edge> comp = new Comparator<Edge>() {
			@Override
			public int compare(Edge e1, Edge e2) {
				return edgeWeights.get(e1).compareTo(edgeWeights.get(e2));
			}
		};
		Collections.sort(edges,comp);
		
		// Find the minimum spanning tree of the maze's graph
		for(Edge e : edges) {
			if (!(uf.find(e.p1).equals(uf.find(e.p2)))) {
				// Add the edge to the adjacency list in both directions
				adjList.get(e.p1).add(e.p2);
				adjList.get(e.p2).add(e.p1);
				uf.union(e.p1, e.p2);
			}
		}
	}
}
