import java.util.*;
import java.io.*;

public class SumAbcFile {
    public static void main(String[] argc) {
        int ans = 0;
        Scanner in = null;
        try {
            StringBuilder curString = new StringBuilder();
            in = new Scanner(new InputStreamReader(new FileInputStream(argc[0]), "utf-8"));
            in.useDelimiter("");
            try {
                while (in.hasNext()) {
                    char c = in.next().charAt(0);
                    if (Character.isLetter(c)) {
                        curString.append(Character.getNumericValue(c) - 10);
                    } else if (c == '-' && curString.length() == 0) {
                        curString.append(c);
                    } else {
                        if (curString.length() != 0) {
                            String temp = curString.toString();
                            ans += Integer.parseInt(temp);
                        }
                        curString.setLength(0);
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("It's wrong input :(");
            }
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Accessed with an illegal index");
        } finally {
            if (in != null) {
                in.close();
            }
        }
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(argc[1]), "utf-8");
            try {
                out.write(ans + "\n");
            } catch (IOException e) {
                System.out.println("IOException");
            } finally {
                out.close();
            }
        } catch (IOException e) {
            System.out.println("IOException");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Accessed with an illegal index");
        }
    }
}