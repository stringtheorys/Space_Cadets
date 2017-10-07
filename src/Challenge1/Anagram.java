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

    Scanner scanner;

    String word = "Tom Marvolo Riddle";
    String language = "English";
    int maxAnagrams = 500;
    int maxNumWords = 3;
    String mustInclude = "Tom";
    String mustExclude = "HI";
    int minLetters = 1;
    int maxLetters = 10;
    boolean repeatOccurrences = true;
    boolean showCandidateWord = false;
    boolean showLineNumbers = true;
    int showLowerUpper = 1;

    public static void main(String[] args) {
        new Anagram();
    }

    public Anagram() {
        scanner = new Scanner(System.in);
    }

    public void generateAnagram() {
        String i = "https://new.wordsmith.org/anagram/anagram.cgi?anagram=tom+marvolo+riddle&language=english&t=500&d=3&include=tom&exclude=hi&n=1&m=10&a=y&l=n&q=y&k=1";

        word = getString("Please enter a word or sentence to generate angagrams", "Please enter something");

    }

    private void printAdvancedOptions() {
        System.out.println("Advanced Options");
        System.out.println("1. Language :" + language);
        System.out.println("2. Maximum Number of Word per Anagram :" + maxAnagrams);
        System.out.println("3. Anagrams must include this word :" + mustInclude);
        System.out.println("4. Anagram must exclude this word :" + mustExclude);
        System.out.println("5. Minimum number of letters per word :" + minLetters);
        System.out.println("6. Maximum number of letters per word :" + maxLetters);
        System.out.println("7. Repeat Occurrences of a word :" + repeatOccurrences);
        System.out.println("8. Back on main menu");

        int userInput = getInt("Please enter the number of an advanced option to change :", "Please etner a number", "Please enter a number between 1 and 8", 0, 9);

        switch(userInput) {
            case(1):
                // TODO
        }
    }

    private String getString(String request, String error) {
        String input = "";
        while (input.equals("")) {
            System.out.print(request);
            input = scanner.nextLine();
            System.out.println();

            if (input.equals("")) {
                System.out.println(error);
            }
        }
        return input;
    }

    private int getInt(String request, String error, String outsideBounds, int min, int max) {
        String input = "";
        int output = -1;

        while (min > output || max < output) {

            System.out.print(request);
            input = scanner.nextLine();
            System.out.println();

            try {
                output = Integer.parseInt(input);
            } catch(NumberFormatException e) {
                System.out.println(error);
            }

            if (min > output || max < output) {
                System.out.println(outsideBounds);
            }
        }
        return output;
    }

    public String getWebPage() {
        String searchurl = createUrl();
        String webpage = "";

        try {
            URL url = new URL(searchurl);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String line = "";
            while ((line = br.readLine()) != null) {
                webpage += line;
            }
            is.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return webpage;
    }
    public ArrayList<String> parseWebPage(String webpage) {
        ArrayList<String> anagrams = new ArrayList<>();
        String[] lines = webpage.split("\n");

        boolean foundStart = false;
        for (int pos = 0; pos < lines.length; pos++) {
            if (foundStart) {
                if (lines[pos].substring(0, 3).equals("<b>")) {
                    foundStart = true;
                    // Cheat as the next line doesnt have a <br> at the start
                    anagrams.add(lines[pos++]);
                }
            } else {
                if (lines[pos].equals("<br><br>")) {
                    break;
                } else {
                    anagrams.add(lines[pos].substring(4));
                }
            }
        }

        return anagrams;
    }

    public String createUrl() {
        return "";
    }
}
