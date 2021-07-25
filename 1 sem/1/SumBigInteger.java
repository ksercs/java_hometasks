import java.math.*;
 
public class SumBigInteger {
    public static void main(String[] args) {
        BigInteger ans = BigInteger.valueOf(0);
        for (int i = 0; i < args.length; ++i){
            String str = args[i];
            int FirstNum = -1, 
                len = str.length(), 
                j = 0;
            while (j < len) {
                char smb = str.charAt(j);
                if (!Character.isWhitespace(smb)){
                    if (FirstNum == -1) FirstNum = j;
                }
                else if (FirstNum != -1){
                        ans = ans.add(new BigInteger(str.substring(FirstNum, j)));
                        FirstNum = -1;
                    }
                 ++j;
            }
            if (FirstNum != -1)
                ans = ans.add(new BigInteger(str.substring(FirstNum)));
        }
        System.out.println(ans);
    }    
}