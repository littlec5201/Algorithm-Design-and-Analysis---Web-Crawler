import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.xml.ws.http.HTTPException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SpiderLeg {

	private Document doc;
	private List<String> links = new LinkedList<String>();

	public SpiderLeg(String url, String keyWord) {
//		getTitle(url);
//		getHyperlink(url);
	}

	public SpiderLeg(List<String> url, String keyword) {

	}

	public SpiderLeg() {

	}

	public void getTitle(String url) {
		System.out.println("getTitle method called!");
		try {
			doc = Jsoup.connect(url).get();
			String title = doc.title();
			System.out.println("Title of " + url + " is " + title);
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> getHyperlink(String url) {
		try {
			doc = Jsoup.connect(url).get();
			Elements hyperLinks = doc.select("a[href]");
			for (Element currentHyperLink : hyperLinks) {
				String toBeAdded = currentHyperLink.absUrl("abs:href");
				if (toBeAdded.contains("http://")) {
					this.links.add(toBeAdded);
				}
			}
			return this.links;
		} catch (Exception e) {
		}
		return null;
	}

	public boolean urlExists(String url) {
		try {
			doc = Jsoup.connect(url).get();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void getImages(String url) {
		try {
			doc = Jsoup.connect(url).get();
			Elements media = doc.select("[src]");
			for (Element src : media) {
				if (src.tagName().equals("img")) {
					String fileName = src.attr("abs:src");
					fileName = fileName.substring(fileName.lastIndexOf("/") + 1, fileName.length());
					System.out.println("IMAGE NAME: " + fileName + ", IMAGE WIDTH: " + src.attr("width")
							+ ", IMAGE HEIGHT: " + src.attr("height") + ", IMAGE ALT: " + src.attr("alt"));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getMeta(String url) {
		System.out.println("getMeta method called!");
		try {
			doc = Jsoup.connect(url).get();
			Elements meta = doc.getElementsByTag("meta");

			for (Element tag : meta) {
				System.out.println(tag);
				String keyword = meta.attr("keyword");
				System.out.println(keyword);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<String> getLinks() {
		return this.links;
	}

	public static void main(String[] args) {
		String url = "http://www.facebook.com";
		SpiderLeg s = new SpiderLeg(url,
				"Hello");
		s.getMeta(url);

	}

}
