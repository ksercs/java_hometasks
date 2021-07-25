import java.util.*;
 
public class Reverse2 {
    public static void main(String[] args) {
        ArrayDeque <String> lines = new ArrayDeque <String>();
        Scanner in = new Scanner(System.in);
        
        while ( in.hasNextLine() ) {
            String s = in.nextLine();
            lines.addFirst(s);
        }
        
        while (!lines.isEmpty()) {
            String[] number = lines.pollFirst().split("\\p{javaWhitespace}+");
        
            for (int j = number.length-1; j >= 0; --j){
                System.out.print(number[j] + " ");
            }
            System.out.println();
        }
    }    
}