import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;

public class Vertex {
	private Set<Edge> edges;
	private String vertexName;
	
	public Vertex(String name) {
		edges = new HashSet<Edge>();
		vertexName = name;
	}
	
	public String getVertexName() {
		return this.vertexName;
	}
	
	public Set<Edge> getEdges() {
		return this.edges;
	}
	
	public void addEdge(Edge e) {
		edges.add(e);
	}
}
