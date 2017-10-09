package Challenge1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Anagram {

    private Scanner scanner;

    private final String[] languages = new String[] {
            "english", "german", "english-obscure", "spanish",
            "esperanto", "french", "italian", "latin", "latin",
            "portuguese", "swedish", "names"
    };
    private final String[] validLanguages = new String[] {
            "English", "Deutsch (German)", "English with obscure words",
            "Español (Spanish)", "Esperanto", "Français (French)",
            "Italiano (Italian)", "Latin", "Nederlands (Dutch)",
            "Português (Portuguese)", "Svenska (Swedish)", "Names (proper names only)"
    };

    private String word = "testing";
    private int languagePos = 0;
    private int maxAnagrams = 500;
    private int maxNumWords = 20;
    private String mustInclude = "";
    private String mustExclude = "";
    private int minLetters = 1;
    private int maxLetters = 10;
    private boolean repeatOccurrences = true;

    public static void main(String[] args) {
        new Anagram().findAnagrams();
    }

    public Anagram() {
        scanner = new Scanner(System.in);
    }

    public void findAnagrams() {

        System.out.println("Welcome to the anagram generator");
        String repeat = "Y";
        while (repeat.equalsIgnoreCase("Y")) {
            changeOptions();

            System.out.print("Do you want to find a new anagram (Y/N) :");
            repeat = scanner.nextLine();
            System.out.println();
        }

        System.out.println("Thank you for using the anagram creator");
    }

    private void printOptions() {
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

    private void changeOptions() {

        int input = -1;
        do {
            printOptions();
            System.out.print("Please enter the number to change an option :");
            String userInput = scanner.nextLine();
            System.out.println();

            try {
                input = Integer.parseInt(userInput);
            } catch(NumberFormatException e) {
                System.out.println("Please enter a number");
                continue;
            }

            switch(input) {
                case(1):
                    System.out.print("Please enter the word to anagram :");
                    word = scanner.nextLine();
                    System.out.println();
                    break;

                case(2):
                    int pos = 1;
                    for (String language : validLanguages) {
                        System.out.println(pos++ + ". " + language);
                    }

                    System.out.print("Please choose which language :");
                    userInput = scanner.nextLine();

                    try {
                        languagePos = Integer.parseInt(userInput)-1;

                        if (languagePos < 0 || languagePos >= validLanguages.length) {
                            languagePos = 1;
                            System.out.println("Invalid language position");
                        }
                    } catch(NumberFormatException e) {
                        System.out.println("Pleas enter a number");
                    }

                    break;

                case(3):
                    System.out.print("Max number of words in anagram :");
                    userInput = scanner.nextLine();
                    System.out.println();
                    try {
                        maxNumWords = Integer.parseInt(userInput);
                        if (maxNumWords < 1) {
                            System.out.println("Please enter a positive number");
                            maxNumWords = 20;
                        }
                    } catch(NumberFormatException e) {
                        System.out.println("Please enter a number");
                    }

                    break;

                case(4):
                    System.out.print("A word that must be included :");
                    mustInclude = scanner.nextLine();
                    System.out.println();
                    break;

                case(5):
                    System.out.print("A words that must be excluded :");
                    mustExclude = scanner.nextLine();
                    System.out.println();
                    break;

                case(6):
                    System.out.print("The min number of letters per word :");
                    userInput = scanner.nextLine();
                    System.out.println();

                    try {
                        minLetters = Integer.parseInt(userInput);

                        if (minLetters < maxLetters && minLetters > 0) {
                            System.out.println("Please enter a positive number or less than max letters");
                            minLetters = 1;
                        }
                    } catch(NumberFormatException e) {
                        System.out.println("Please enter a number");
                    }
                    break;

                case(7):
                    System.out.print("The max number of letters per word :");
                    userInput = scanner.nextLine();
                    System.out.println();

                    try {
                        maxLetters = Integer.parseInt(userInput);

                        if (minLetters > maxLetters || maxLetters < 1 || maxLetters > 20) {
                            System.out.println("Please enter a positive number less than 20 and more than min letters");
                            maxLetters = 20;
                        }
                    } catch(NumberFormatException e) {
                        System.out.println("Please enter a number");
                    }
                    break;

                case(8):
                    System.out.print("Do you want to have repeat occurrences of words (Y/N) :");
                    userInput = scanner.nextLine();
                    System.out.println();

                    if (userInput.equalsIgnoreCase("Y")) {
                        repeatOccurrences = true;
                    } else if (userInput.equalsIgnoreCase("N")) {
                        repeatOccurrences = false;
                    } else {
                        System.out.println("Please enter Y or N");
                    }
                    break;

                case(0):
                    if (word.equals("")) {
                        System.out.println("You must enter a word to search");
                    } else {
                        ArrayList<String> anagrams = getAnagrams();
                        System.out.println("List of anagrams");
                        if (anagrams.size() == 0) {
                            System.out.println("No anagram were generated");
                        } else {
                            for (String anagram : anagrams) {
                                System.out.println(anagram);
                            }
                        }
                    }
                    break;

                default:
                    System.out.println("Please enter on of the valid option numbers");
                    break;
            }
        } while (input != 0);
    }


    private ArrayList<String> getAnagrams() {
        ArrayList<String> anagrams = new ArrayList<>();

        String webpage = getWebPage();
        String[] lines = webpage.split("<br>");

        for (int pos = 6; pos < lines.length-2; pos++) {
            anagrams.add(lines[pos]);
        }
        return anagrams;
    }

    private String getWebPage() {

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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return webpage.toString();
    }

}
