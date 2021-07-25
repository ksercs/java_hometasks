import java.util.*;
import java.io.*;

public class WordStatIndex {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Need 2 arguments");
            return;
        }
        Map<String, ArrayList<Integer>> words = new LinkedHashMap<String, ArrayList<Integer>>();
        try (Scanner in = new Scanner(new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "utf-8")))) {
            int k = 0;
            in.useDelimiter("[^\\p{L}\\p{Pd}']+");
            while (in.hasNext()) {
                String temp = in.next().toLowerCase();
                ++k;
                if (words.containsKey(temp)) {
                    words.get(temp).add(k);
                } else {
                    words.put(temp, new ArrayList<Integer>());
                    words.get(temp).add(k);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find input file");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding(utf-8)");
        }
        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(args[1]), "utf-8")) {
            for (Map.Entry<String, ArrayList<Integer>> entry : words.entrySet()) {
                out.write(entry.getKey() + " " + entry.getValue().size());
                for (int k = 0; k < entry.getValue().size(); ++k) {
                    out.write(" " + entry.getValue().get(k));
                }
                out.write("\n");
            }
        } catch (FileNotFoundException e) {
            System.err.println("Cannot open output file");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding(utf-8)");
        } catch (IOException e) {
            System.err.println("IOException!");
        }
    }
}