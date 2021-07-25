import java.util.*;
import java.lang.*;
import java.io.*;

public class ReverseSum {
    public static void main(String[] args) {
        FastScanner in = new FastScanner(System.in);

        List<ArrayList<Integer>> matrix = new ArrayList<ArrayList<Integer>>();
        List<Integer> sumR = new ArrayList<Integer>();
        List<Integer> sumC = new ArrayList<Integer>();

        int m = 0;
        while (true) {
            try {
                String s = in.nextLine();
                if (s != null) {
                    matrix.add(new ArrayList<Integer>());
                    String[] t = s.split("\\p{javaWhitespace}+");
                    int sum = 0;
                    for (int i = 0; i < t.length; ++i) {
                        if (t[i].length() > 0) {
                            int cur = new Integer(t[i]);
                            sum += cur;
                            if (sumC.size() < i + 1) {
                                sumC.add(cur);
                            } else {
                                sumC.set(i, sumC.get(i) + cur);
                            }
                            matrix.get(matrix.size() - 1).add(cur);
                        }
                    }
                    sumR.add(sum);
                } else
                    break;
            } catch (IOException e) {
                System.out.println("IOException");
            }
        }

        for (int i = 0; i < matrix.size(); ++i) {
            for (int j = 0; j < matrix.get(i).size(); ++j) {
                System.out.print(sumR.get(i) + sumC.get(j) - matrix.get(i).get(j) + " ");
            }
            System.out.println();
        }
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
        
