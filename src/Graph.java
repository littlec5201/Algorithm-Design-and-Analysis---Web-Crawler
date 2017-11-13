import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Graph {
	private Set<Vertex> vertices;
	private Set<Edge> edges;
	private Map<Vertex, Set<Edge>> adjacencyLists;
	
	public Graph() {
		this.vertices = new HashSet<Vertex>();
		this.edges = new HashSet<Edge>();
		this.adjacencyLists = new HashMap<Vertex, Set<Edge>>();
	}
	
	public Set<Vertex> vertexSet() {
		return this.vertices;
	}
	
	public Set<Edge> edgeSet() {
		return this.edges;
	}
	
	public void addVertex(Vertex vertex) {
		vertices.add(vertex);
		adjacencyLists.put(vertex, new HashSet<Edge>());
	}
	
	public Vertex getVertexByName(String name) {
		for (Vertex v : vertices) {
			if (v.getVertexName() == name) {
				return v;
			}
		}
		return null;
	}
	
	public void addEdge(Vertex vertex1, Vertex vertex2) {
		if (!vertices.contains(vertex1)) {
			addVertex(vertex1);
		}
		if (!vertices.contains(vertex2)) {
			addVertex(vertex2);
		}
		Edge edge = new Edge(vertex1, vertex2);
		edges.add(edge);
		adjacencyLists.get(vertex1).add(edge);
		vertex1.addEdge(edge);
	}
	
	public Set<Edge> getEdges(Vertex vertex) {
		return adjacencyLists.get(vertex);
	}
}
