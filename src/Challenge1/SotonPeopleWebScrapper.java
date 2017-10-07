package Challenge1;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Scanner;

public class SotonPeopleWebScrapper {

    Scanner scanner = new Scanner(System.in);

    private static final String sotonSearchUrl = "http://www.ecs.soton.ac.uk/people/";

    // Personal info
    private String name;

    private String[] options = new String[] {
            "Bio", "Research", "Professional", "Publications", "Contact"
    };

    public static void main(String[] args) {
        new SotonPeopleWebScrapper();
    }


    public SotonPeopleWebScrapper() {
        Document webpage = getPersonsWebPage();
        displayPersonsOptions(webpage);
    }

    private void displayPersonsOptions(Document webpage) {
        System.out.println("Please enter what details you would like to know about " + name);

    }

    private Document getPersonsWebPage() {
        System.out.print("Please enter a member of ecs's name :");
        String name = scanner.nextLine();
        System.out.println();
        try {
            Document webpage = Jsoup.connect(sotonSearchUrl + name).get();
            System.out.println("Thank you for entering a correct url");
            return webpage;
        } catch (IOException e) {
            System.out.println("An Error occurred, the name could be wrong");
            e.printStackTrace();
            return getPersonsWebPage();
        }
    }
}
