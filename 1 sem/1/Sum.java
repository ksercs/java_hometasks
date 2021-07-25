
public class Sum {
    public static void main(String[] args) {
        int ans = 0;
        for (int i = 0; i < args.length; ++i){
            String str = args[i];
            int FirstNum = -1;
            for (int j = 0; j < str.length(); ++j) {
                char smb = str.charAt(j);
                if (Character.isDigit(smb) || smb == '-'){
                    if (FirstNum == -1) FirstNum = j;
                }
                else if (FirstNum != -1){
                        ans += Integer.parseInt(str.substring(FirstNum, j));
                        FirstNum = -1;
                    }
            }
            if (FirstNum != -1)
                ans += Integer.parseInt(str.substring(FirstNum));
            }
        System.out.println(ans);
        }    
}