import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CUISpiderCrawler {
	public static void main(String[] args) throws Exception{
		Scanner keyboard = new Scanner(System.in);
		List<String> urls = new LinkedList<String>();
		int confirmNo = 1;
		String keyword = "";
		int noOfURLs = 0;
		System.out.println("Web Crawler Application: ");
		System.out.print("\nHow many starting URLs you'll use?: ");
		noOfURLs = keyboard.nextInt();
		keyboard.nextLine();
		for(int i=0;i<noOfURLs;i++){
			System.out.print("Enter URL number "+(i+1)+": ");
			urls.add(keyboard.nextLine());
		}
		System.out.println();
		System.out.print("Keyword: ");
		keyword = keyboard.nextLine();
		
		Spider spider = new Spider(urls, keyword);
	}
}
