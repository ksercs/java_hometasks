import java.util.*;     
 
public class Reverse {
    public static void main(String[] args) {
        ArrayList <String> lines = new ArrayList <String>();
        ArrayList <Integer> ansRow = new ArrayList <Integer> ();
        ArrayList <Integer> ansCol = new ArrayList <Integer> ();
        ArrayList <Integer> RowSZ = new ArrayList <Integer> ();
        
        Scanner in = new Scanner(System.in);
        
        while ( in.hasNextLine() ) {
            String s = in.nextLine();
            lines.add(s);
        }
        
        for (int i = 0; i < lines.size(); i++){
            if (lines.get(i) != null){
                Scanner inNum = new Scanner(lines.get(i));
                ArrayList <Integer> num = new ArrayList <Integer> ();
                while (inNum.hasNextInt())
                    num.add(inNum.nextInt()); 
                ansRow.add(0);
                int NumSZ = num.size();
                RowSZ.add(NumSZ);
                while (ansCol.size() < NumSZ)
                    ansCol.add(0);
                for (int j = 0; j < NumSZ; ++j){
                    int ch = num.get(j);
                    if (ch > ansRow.get(i))
                        ansRow.set(i, ch);
                    if (ch > ansCol.get(j))
                        ansCol.set(j, ch); 
                }
            }   
            else
                RowSZ.add(0);
        }
        
        for (int i = 0; i < lines.size(); ++i){
            if (RowSZ.get(i) != 0)
                for (int j = 0; j < RowSZ.get(i); j++){
                    System.out.print(Math.max(ansRow.get(i), ansCol.get(j)) + " ");
                }
            System.out.println();
        }
    }    
}