import java.util.*;
import java.lang.*;
import java.io.*;

public class ReverseSumFast {
    public static void main(String[] args) {
        FastScanner in = new FastScanner(System.in);
        PrintWriter out = new PrintWriter(System.out);

        List<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
        List<Integer> col = new ArrayList<Integer>();
        List<Integer> sumR = new ArrayList<Integer>();
        List<Integer> sumC = new ArrayList<Integer>();

        int m = 0;
        while (true) {
            String s = "";
            try {
                s = in.nextLine();
            } catch (IOException e) {

            }
            if (s != null) {
                matrix.add(new ArrayList<Integer>());
                String[] t = s.split("\\p{javaWhitespace}+");
                int sum = 0;
                for (int i = 0; i < t.length; ++i) {
                    String st = t[i];
                    if (st.length() > 0) {
                        int cur = Integer.parseInt(st);
                        sum += cur;
                        if (sumC.size() < i + 1) {
                            sumC.add(cur);
                        } else {
                            sumC.set(i, sumC.get(i) + cur);
                        }
                        matrix.get(matrix.size() - 1).add(cur);
                    }
                }
                col.add(t.length);
                sumR.add(sum);
            } else
                break;
        }

        for (int i = 0; i < matrix.size(); ++i) {
            for (int j = 0; j < matrix.get(i).size(); ++j) {
                out.print(sumR.get(i) + sumC.get(j) - matrix.get(i).get(j) + " ");
            }
            out.println();
        }
        out.close();
    }

    static class FastScanner {
        private BufferedReader in;

        public FastScanner(InputStream stream) {
            in = new BufferedReader(new InputStreamReader(stream));
        }

        String nextLine() throws IOException {
            return in.readLine();
        }
    }
}
        
