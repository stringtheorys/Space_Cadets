package Challenge1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

class Anagram {

  // Input class
  private final Scanner scanner;

  // Language using in URL
  private final String[] languages = new String[]{
      "english", "german", "english-obscure", "spanish",
      "esperanto", "french", "italian", "latin", "latin",
      "portuguese", "swedish", "names"
  };
  // Languages printed to screen
  private final String[] validLanguages = new String[]{
      "English", "Deutsch (German)", "English with obscure words",
      "Español (Spanish)", "Esperanto", "Français (French)",
      "Italiano (Italian)", "Latin", "Nederlands (Dutch)",
      "Português (Portuguese)", "Svenska (Swedish)", "Names (proper names only)"
  };

  // Url Options
  private String word = "";           // Anagramed Word
  private int languagePos = 0;        // Language Position
  private int maxNumWords = 20;       // Max number of words in anagram
  private String mustInclude = "";    // Words that must be included in the anagram
  private String mustExclude = "";    // Words that cant be included in the anagram
  private int minLetters = 1;         // Min number of letters in a word
  private int maxLetters = 10;        // Max number of letters in a word
  private boolean repeatOccurrences = true; // Repeat occurrences of words in anagram

  public static void main(String[] args) {
    new Anagram().findAnagrams(); // Runs program
  }

  private Anagram() {
    scanner = new Scanner(System.in); // Creates scanner
  }

  // Finds the anagrams
  private void findAnagrams() {

    System.out.println("Welcome to the anagram generator");
    // Runs the code which the user enter y or Y
    String repeat;
    do {
      // Prints the options to change
      changeOptions();

      // Gets if the user wants a new anagram
      System.out.print("Do you want to find a new anagram (Y/N) :");
      repeat = scanner.nextLine();
      System.out.println();

      // Loops while repeat equals y or Y
    } while (repeat.equalsIgnoreCase("Y"));

    System.out.println("Thank you for using the anagram creator");
  }

  // Prints the options from user to change
  private void printOptions() {

    // Prints the options
    System.out.println("\n");
    System.out.println("Options");
    System.out.println("1. Word :" + word);
    System.out.println("2. Language :" + validLanguages[languagePos]);
    System.out.println("3. Maximum Number of Word per Anagram :" + maxNumWords);
    System.out.println("4. Anagrams must include this word :" + mustInclude);
    System.out.println("5. Anagram must exclude this word :" + mustExclude);
    System.out.println("6. Minimum number of letters per word :" + minLetters);
    System.out.println("7. Maximum number of letters per word :" + maxLetters);
    System.out.println("8. Repeat Occurrences of a word :" + repeatOccurrences);
    System.out.println("0. Get Anagrams");
  }

  // Changes the options
  private void changeOptions() {

    int input = -1;
    // Runs while input is not equal to 0 as zero gets the anagrams
    do {
      // Prints the options and gets the option to change
      printOptions();
      System.out.print("Please enter the number to change an option :");
      String userInput = scanner.nextLine();
      System.out.println();

      // Tries to get the integer version
      try {
        input = Integer.parseInt(userInput);
      } catch (NumberFormatException e) {
        System.out.println("Please enter a number");
        // Continue just goes to the start of the loop again skip the rest of the loop code
        continue;
      }

      // Switch statement for the user input
      switch (input) {
        // Change the word
        case (1):
          System.out.print("Please enter the word to anagram :");
          word = scanner.nextLine();
          System.out.println();
          break;

        // Change the language
        case (2):
          // Prints all of the languages
          int pos = 1;
          for (String language : validLanguages) {
            System.out.println(pos++ + ". " + language);
          }

          // Get user input for language number
          System.out.print("Please choose which language :");
          userInput = scanner.nextLine();

          try {
            languagePos = Integer.parseInt(userInput) - 1;

            // Check if in bounds
            if (languagePos < 0 || languagePos >= validLanguages.length) {
              languagePos = 1;
              System.out.println("Invalid language position");
            }
          } catch (NumberFormatException e) {
            System.out.println("Pleas enter a number");
          }
          break;

        // Change the max number of words in anagram
        case (3):
          System.out.print("Max number of words in anagram :");
          userInput = scanner.nextLine();
          System.out.println();

          try {
            maxNumWords = Integer.parseInt(userInput);

            // Check if in bounds
            if (maxNumWords < 1) {
              System.out.println("Please enter a positive number");
              maxNumWords = 20;
            } else if (maxNumWords > 20) {
              System.out.println("Please enter a number less than 20");
              maxNumWords = 20;
            }
          } catch (NumberFormatException e) {
            System.out.println("Please enter a number");
          }
          break;

        // Words that must be included
        case (4):
          System.out.print("A word that must be included :");
          mustInclude = scanner.nextLine();
          System.out.println();
          break;

        // Words that must be excluded
        case (5):
          System.out.print("A words that must be excluded :");
          mustExclude = scanner.nextLine();
          System.out.println();
          break;

        // Change the min input of letters per word
        case (6):
          System.out.print("The min number of letters per word :");
          userInput = scanner.nextLine();
          System.out.println();

          try {
            minLetters = Integer.parseInt(userInput);

            // Check if in bounds
            if (minLetters < maxLetters && minLetters > 0) {
              System.out.println("Please enter a positive number or less than max letters");
              minLetters = 1;
            }
          } catch (NumberFormatException e) {
            System.out.println("Please enter a number");
          }
          break;

        // Change the max number of letters per word
        case (7):
          System.out.print("The max number of letters per word :");
          userInput = scanner.nextLine();
          System.out.println();

          try {
            maxLetters = Integer.parseInt(userInput);

            // Check if in bounds
            if (minLetters > maxLetters || maxLetters < 1 || maxLetters > 20) {
              System.out
                  .println("Please enter a positive number less than 20 and more than min letters");
              maxLetters = 20;
            }
          } catch (NumberFormatException e) {
            System.out.println("Please enter a number");
          }
          break;

        // Change if repeat occurrences of words are allowed
        case (8):
          System.out.print("Do you want to have repeat occurrences of words (Y/N) :");
          userInput = scanner.nextLine();
          System.out.println();

          // Bounds checking
          if (userInput.equalsIgnoreCase("Y")) {
            repeatOccurrences = true;
          } else if (userInput.equalsIgnoreCase("N")) {
            repeatOccurrences = false;
          } else {
            System.out.println("Please enter Y or N");
          }
          break;

        // Get the anagrams
        case (0):
          if (word.equals("")) {
            System.out.println("You must enter a word to search");
          } else {
            // Get an arraylist of anagrams
            ArrayList<String> anagrams = getAnagrams();
            if (anagrams.size() == 0) {
              System.out.println("No anagram were generated");
            } else {
              System.out.println("List of anagrams");
              for (String anagram : anagrams) {
                System.out.println(anagram);
              }
            }
          }
          break;

        // Unknown switch input
        default:
          System.out.println("Please enter on of the valid option numbers");
          break;
      }
    } while (input != 0);
  }

  // Gets anagrams
  private ArrayList<String> getAnagrams() {
    ArrayList<String> anagrams = new ArrayList<>();

    // Get the webpage
    String webpage = getWebPage();

    // Split the webpage into arrays of all locations with <br>
    String[] lines = webpage.split("<br>");

    // Skip the first 6 and last 2 elements in array are parts we dont want them
    anagrams.addAll(Arrays.asList(lines).subList(6, lines.length - 2));
    return anagrams;
  }

  // Get the webpage
  private String getWebPage() {

    // Get the search Url
    int maxAnagrams = 500;
    String searchUrl = "https://new.wordsmith.org/anagram/anagram.cgi?anagram=+"
        + word.replace(" ", "+")
        + "&language=" + languages[languagePos]       // Language
        + "&t=" + maxAnagrams           // Max number of anagrams
        + "&d=" + maxNumWords           // Max Number of words
        + "&include=" + mustInclude     // Must Include
        + "&exclude=" + mustExclude      // Must Exclude
        + "&n=" + minLetters            // Min Letters in a word
        + "&m=" + maxLetters            // Max Letters in a word
        + "&a=y"                        // Repeat occurrences
        + "&l=n"                        // Cadidate word list
        + "&q=n"                        // Line Numbers
        + "&k=1";                       // Anagram case

    StringBuilder webpage = new StringBuilder();

    try {
      URL url = new URL(searchUrl);
      InputStream is = url.openStream();
      BufferedReader br = new BufferedReader(new InputStreamReader(is));

      String line = "";
      while ((line = br.readLine()) != null) {
        webpage.append(line);
      }
      is.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

    return webpage.toString();
  }
}
