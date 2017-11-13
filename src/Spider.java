import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import javax.swing.DefaultListModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Spider {

	private static final int MAX_DEPTH = 1;
	private int currentDepth = 0;
	private Set<String> pagesVisited = new HashSet<String>();
	private List<String> pagesToVisit = new LinkedList<String>();
	private List<String> forbiddenURLs = new ArrayList<String>();
	private DefaultListModel<String> model = new DefaultListModel<String>();
	private List<String> matchingSites = new LinkedList<String>();
	private Document doc;
	private Graph graph = new Graph();
	private FileOutputStream fOutStream;

	public Spider(List<String> startingPages, String keyword) throws Exception {
		pagesToVisit.addAll(startingPages);
		System.out.println(startingPages);
		setForbiddenURLs();
		crawl(keyword);
	}

	public void setForbiddenURLs() {
		forbiddenURLs.add("facebook.com");
		forbiddenURLs.add("google.com");
		forbiddenURLs.add("youtube.com");
	}

	public void crawl(String keyword) throws Exception {
		int urlsRemaining = pagesToVisit.size();
		SpiderLeg leg = new SpiderLeg();
		fOutStream = new FileOutputStream("hyperlinks.txt");

		matchingSites.addAll(pagesToVisit);
		PrintWriter pWriter = new PrintWriter(fOutStream);
		pWriter.println("Web Crawler Results");
		pWriter.println();
		pWriter.println("Main Link(s): ");
		
		for (String link : pagesToVisit) {
			pWriter.println("	" + link);
		}
		
		pWriter.println();
		pWriter.println("Keyword: " + keyword);
		pWriter.println();
		pWriter.println("Links Found: ");
		pWriter.println();
		
		Vertex v = null;
		while (!pagesToVisit.isEmpty()) {
			String currentURL = pagesToVisit.get(0);
			if (graph.getVertexByName(currentURL) == null) {
				v = new Vertex(currentURL);
				graph.addVertex(v);
			} else {
				v = graph.getVertexByName(currentURL);
			}

			System.out.println("Main Link: " + currentURL);
			List<String> subLinks = leg.getHyperlink(currentURL);

			for (String subLink : subLinks) {
				if (!pagesToVisit.contains(subLink)) {
					if (!pagesVisited.contains(subLink)) {
						if (!matchingSites.contains(subLink)) {
							if (searchKeyword(subLink, keyword)) {
								if (currentDepth < MAX_DEPTH) {
									pagesToVisit.add(subLink);
								}
								matchingSites.add(subLink);
								graph.addEdge(v, new Vertex(subLink));
								System.out.println("	Sublink: " + subLink);
							} else {
								// System.out.println(" Doesn't have keyword");
							}
						}
					} else {
						// System.out.println(" Page already visited");
					}
				} else {
					// System.out.println(" Page already queued");
				}
			}
			
			pagesVisited.add(currentURL);
			pagesToVisit.remove(0);
			urlsRemaining--;
			
			if (urlsRemaining == 0) {
				urlsRemaining = pagesToVisit.size();
				currentDepth++;
			}
		}
		System.out.println("Vertices: " + graph.vertexSet().size());
		System.out.println("Edges: " + graph.edgeSet().size());
		int[][] adjacencyMatrix = this.adjacencyMatrix();
		this.printMatrix(adjacencyMatrix);
		
		for (int i = 0; i < adjacencyMatrix.length; i++) {
			adjacencyMatrix[i][i] = 1;
		}
		
		System.out.println();
		this.printMatrix(adjacencyMatrix);
		double[] vVector = new double[adjacencyMatrix.length];
		double[] vPrevious = new double[adjacencyMatrix.length];
		System.out.print("[");
		
		for (int i = 0; i < vVector.length; i++) {
			vVector[i] = 1;
			System.out.print(" " + vVector[i]);
		}
		
		System.out.println(" ]");
		while (vectorsEqual(vPrevious, vVector)) {

		}
		vPrevious = vVector;
		this.matrixMultiply(adjacencyMatrix, vVector);
		pWriter.close();
	}

	public int[][] adjacencyMatrix() {
		int numberOfSites = graph.vertexSet().size();
		int[][] matrix = new int[numberOfSites][numberOfSites];
		int i = 0;
		int j = 0;

		List<String> vertices = new ArrayList<String>();
		for (Vertex v : graph.vertexSet()) {
			vertices.add(v.getVertexName());
		}

		for (String outer : matchingSites) {
			Vertex v = graph.getVertexByName(outer);
			for (String inner : matchingSites) {
				if (outer == inner) {
					matrix[i][j] = 0;
				} else {
					edgeLoop: for (Edge e : v.getEdges()) {
						if (e.endVertices()[1].getVertexName() == inner) {
							matrix[i][j] = 1;
							break edgeLoop;
						} else {
							matrix[i][j] = 0;
						}
					}
				}
				j++;
			}
			i++;
			j = 0;
		}
		return matrix;
	}

	public void printMatrix(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	// B is adjacencyMatrix A + identity matrix I
	// v is the vector we multiply to B
	public void matrixMultiply(int[][] B, double[] v) {

	}

	public boolean vectorsEqual(double[] x, double[] y) {
		return false;
	}

	public boolean searchKeyword(String url, String keyword) {
		try {
			doc = Jsoup.connect(url).get();
			Elements meta = doc.getElementsByTag("meta");

			for (Element tag : meta) {
				for (Attribute child : tag.attributes()) {
					String text = child.getValue();
					// System.out.println(text);
					if (text.contains(keyword)) {
						return true;
					}
				}

			}
			return false;
		} catch (Exception e) {
		}
		return false;
	}

	public boolean validURL(String url) {
		for (String currentURL : forbiddenURLs) {
			if (url.contains(currentURL)) {
				return false;
			}
		}
		return true;
	}

	public DefaultListModel<String> getModel() {
		return this.model;
	}
}
