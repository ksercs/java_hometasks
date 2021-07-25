import java.util.*;
import java.io.*;

public class SumFile {
    public static void main (String [] argc) {
        int ans = 0;
        try {
            Scanner in = new Scanner (new InputStreamReader (new FileInputStream(argc[0]), "utf-8"));
            try {
                while (in.hasNextInt()) {
                    ans += in.nextInt();
                }
            }  catch (InputMismatchException e) {
                e.printStackTrace();
            }
            in.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        try {
            OutputStreamWriter out = new OutputStreamWriter (new FileOutputStream (argc[1]), "utf-8");
            out.write(ans+"");
            out.close();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}