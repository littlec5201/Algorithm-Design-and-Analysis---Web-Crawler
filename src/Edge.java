
public class Edge {
	private Vertex vertex1, vertex2;
	
	public Edge(Vertex vertex1, Vertex vertex2) {
		this.vertex1 = vertex1;
		this.vertex2 = vertex2;
	}
	
	public Vertex[] endVertices() {
		Vertex[] vertices = (Vertex[]) (new Vertex[2]);// unchecked
		vertices[0] = this.vertex1;
		vertices[1] = this.vertex2;
		return vertices;
	}
}
