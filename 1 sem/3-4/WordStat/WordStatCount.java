import java.util.*;
import java.io.*;

public class WordStatCount {
    public static void main(String[] argc) {
        StringBuilder curString = new StringBuilder();
        Map<String, Integer> words = new LinkedHashMap<String, Integer>();
        Scanner in = null;
        try (InputStreamReader inputStr = new InputStreamReader(new FileInputStream(argc[0]), "utf-8")) {
            in = new Scanner(inputStr);
            in.useDelimiter("");
            while (in.hasNext()) {
                char c = in.next().charAt(0);
                if (Character.isLetter(c) || Character.getType(c) == Character.DASH_PUNCTUATION || c == '\'') {
                    curString.append(c);
                } else if (curString.length() != 0) {
                    String temp = curString.toString().toLowerCase();
                    if (words.containsKey(temp)) {
                        words.put(temp, words.get(temp) + 1);
                    } else {
                        words.put(temp, 1);
                    }
                    curString.setLength(0);
                }
            }
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Accessed with an illegal index");
        }
        List<Map.Entry<String, Integer>> answers = new ArrayList<Map.Entry<String, Integer>>(words.entrySet());
        Collections.sort(answers, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
                return a.getValue() - b.getValue();
            }
        });
        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(argc[1]), "utf-8")) {
            try {
                for (Map.Entry entry : answers) {
                    out.write(entry.getKey() + " " + entry.getValue() + "\n");
                }
            } catch (IOException e) {
                System.out.println("IOException");
            }
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Accessed with an illegal index");
        }
    }
}