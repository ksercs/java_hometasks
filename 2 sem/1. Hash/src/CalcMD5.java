import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;

public class CalcMD5 {
    public static void main(String[] args) {
        try {
            for (String p : Files.readAllLines(Paths.get(args[0]))) {
                try {
                    System.out.println(DatatypeConverter.printHexBinary(
                            MessageDigest.getInstance("MD5").digest(Files.readAllBytes(Paths.get(p)))
                    ).toUpperCase());
                } catch (InvalidPathException e) {
                    System.err.println(e.getMessage());
                } catch (SecurityException e) {
                    System.err.println("String cannot be converted to a Path!");
                } catch (IOException e) {
                    System.err.println("IOException! " + e.getMessage());
                } catch (NoSuchAlgorithmException e) {
                    System.err.println("No such algorithm!");
                }
            }
        } catch (InvalidPathException e) {
            System.err.println(e.getMessage());
        } catch (SecurityException e) {
            System.err.println("String cannot be converted to a Path!");
        } catch (IOException e) {
            System.err.println("IOException! " + e.getMessage());
        }
    }
}