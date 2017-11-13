package Challenge1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Wiki {

  private static final int maxSearchs = 6;
  private Scanner scanner;

  public static void main(String[] args) {
    new Wiki().searchWikipedia();
  }

  public Wiki() {
    scanner = new Scanner(System.in);
  }

  public void searchWikipedia() {

    String researchPages;

    do {
      int searchNum = 0;
      do {
        System.out.print("Please enter something to search on wikipedia :");
        String userInput = scanner.nextLine();
        System.out.println();
        String searchUrl = "https://en.wikipedia.org/w/index.php?search=" +
            userInput.replace(" ", "+") +
            "&title=Special:Search&profile=default&fulltext=1&searchToken=2dxrf0t4b14r825vf9b9a6cx6";

        HashMap<String, String> searchOptions = parseSearchWebpage(searchUrl);

        int pos = 1;
        System.out.println("Wiki pages to search");
        if (searchOptions.size() == 0) {
          System.out.println("No Wiki pages");
        } else {
          ArrayList<String> searchUrls = new ArrayList<>();

          for (String searchName : searchOptions.keySet()) {
            System.out.println(pos++ + ". " + searchName);
            searchUrls.add(searchOptions.get(searchName));
          }
          System.out.println("0. Change search name");

          System.out.print("Choose wiki page :");
          userInput = scanner.nextLine();
          System.out.println();

          try {
            searchNum = Integer.parseInt(userInput);

            if (searchNum > 0 && searchNum < pos) {
              printSearchWebpage(searchUrls.get(searchNum - 1));
            } else if (searchNum != 0) {
              System.out.println("Please enter one of the search options");
            }
          } catch (NumberFormatException e) {
            System.out.println("Please enter an integer");
          }
        }
      } while (searchNum == 0);

      System.out.print("Would you like to search another wiki page (Y/N) :");
      researchPages = scanner.nextLine();
      System.out.println();

    } while (researchPages.equalsIgnoreCase("y"));

    System.out.println("Thank you for using my program");
  }

  // Parse the search url to find all of the search options
  private HashMap<String, String> parseSearchWebpage(String searchUrl) {
    String webpage = getWebpage(searchUrl);

    // Find the part of the page between the search results
    String searchPages = webpage.substring(webpage.indexOf("mw-search-result-heading"),
        webpage.indexOf("mw-search-pager-bottom"));
    // Each search entry starts with <a
    String[] searchLines = searchPages.split("<a");

    int searchPos = 1;
    HashMap<String, String> wikiPages = new HashMap<>();

    // Find the max number of searchs or all search, which comes first
    while (searchPos < searchLines.length && searchPos < maxSearchs) {

      String line = searchLines[searchPos];

      String[] linesThings = line.split("\"");
      String url = "";
      String title = "";

      int pos = 0;
      while (pos < linesThings.length) {
        // Find the url title
        if (linesThings[pos].equals(" title=")) {
          // Set the title to the next element in array
          title = linesThings[pos + 1];
          // Break as we dont need any more info
          break;
        } else if (linesThings[pos].equals(" href=")) {
          url = linesThings[pos + 1];
          pos += 2;
        } else {
          pos += 1;
        }
      }

      if (url.substring(0, 6).equals("/wiki/")) {
        url = "https://en.wikipedia.org" + url;
      }

      wikiPages.put(title, url);
      searchPos++;
    }
    return wikiPages;
  }

  // Prints the information card from the right of the wiki page
  private void printSearchWebpage(String searchUrl) {

    // Gets the webpage
    String webpage = getWebpage(searchUrl);

    // Gets the start position of the webpage with the infobox
    int startPos = webpage.indexOf("<table class=\"infobox vcard\" style=\"width:22em\">");
    // If start position returns -1 then the search parse doesnt exist in the string
    if (startPos == -1) {
      System.out.println("Sorry this page has the wrong formatting");
      // End program
      return;
    }
    // Gets the end position
    int endPos = webpage.indexOf("</table>");
    // Reduces the size of webpage to get information to just the information card
    String infoCard = webpage.substring(startPos, endPos);

    // Each line of the information card is split by <tr>
    String[] infoLines = infoCard.split("<tr>");

    // Loops through each line
    for (int infoLinePos = 0; infoLinePos < infoLines.length; infoLinePos++) {

      String line = infoLines[infoLinePos];       // Gets the line
      StringBuilder info = new StringBuilder();   // String of info
      boolean capture = false;                    // If to capture text

      // Loop through the whole text
      for (int linePos = 0; linePos < line.length(); linePos++) {
        // Capture text which is between the <>
        if (line.charAt(linePos) == '>') {
          capture = true;
          if (info.length() > 0) {
            info.append(" ");
          }
        } else if (line.charAt(linePos) == '<') {
          capture = false;
        } else if (capture) {
          info.append(line.charAt(linePos));
        }
      }
      // Print the text
      System.out.println(info.toString());
    }
  }

  // Gets the webpage
  private String getWebpage(String webpageUrl) {
    StringBuilder webpage = new StringBuilder();

    try {
      URL url = new URL(webpageUrl);
      InputStream is = url.openStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));

      String line = "";
      while ((line = br.readLine()) != null) {
        webpage.append(line);
      }
      is.close();

    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return webpage.toString();
  }
}