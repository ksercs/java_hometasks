import java.util.*;
import java.io.*;

public class WordStatLineIndex2 {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Need 2 arguments");
            return;
        }
        Map<String, ArrayList<Integer>> words = new TreeMap<String, ArrayList<Integer>>();
        try {
            BufferedReader buf = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), "utf-8"));
            int s = 0;
            while (true) {
                try(String ss = buf.readline()){
                    if (ss == null)
                        break;
                    String[] str = ss.split("[^\\p{L}\\p{Pd}']+");
                    ++s;
                    if (str.length == 0) continue;
                    int c = 0;
                    for (int r = 0; r < str.length; ++r) {
                        if (str[r].length() == 0) {
                            continue;
                        }
                        ++c;
                        String temp = str[r].toLowerCase();
                        if (words.containsKey(temp)) {
                            words.get(temp).add(s);
                            words.get(temp).add(c);
                        } else {
                            words.put(temp, new ArrayList<Integer>());
                            words.get(temp).add(s);
                            words.get(temp).add(c);
                        }
                    }
                }catch (FileNotFoundException e) {
                    System.err.println("Cannot find input file");
                } catch (UnsupportedEncodingException e) {
                    System.err.println("Unsupported encoding(utf-8)");
        }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Cannot find input file");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Unsupported encoding(utf-8)");
        }
        try (OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(args[1]), "utf-8")) {
            for (Map.Entry<String, ArrayList<Integer>> entry : words.entrySet()) {
                out.write(entry.getKey() + " " + entry.getValue().size()/2);
                int k = 0;
                while (k < entry.getValue().size()){
                    out.write(" " + entry.getValue().get(k) + ":");
                    out.write("" + entry.getValue().get(k+1));
                    k += 2;
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