import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public static void main(String[] args) {
    public class SHA256Sum {
        if (args.length == 0) {
            byte[] buffer = new byte[1024];
            try (DigestInputStream dis = new DigestInputStream(System.in, MessageDigest.getInstance("SHA-256"))) {
                while (dis.read(buffer) > -1) {
                }
                System.out.println(DatatypeConverter.printHexBinary(dis.getMessageDigest().digest()).toUpperCase() + " *-");
            } catch (IOException e) {
                System.err.println("IOException! " + e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                System.err.println("No such algorithm!\n");
            }
            return;
        }

        for (String p : args) {
            try {
                System.out.println(DatatypeConverter.printHexBinary(MessageDigest.getInstance("SHA-256")
                        .digest(Files.readAllBytes(Paths.get(p)))).toUpperCase() + " *" + Paths.get(p).getFileName().toString());
            } catch (InvalidPathException e) {
                System.err.println(e.getMessage());
            } catch (SecurityException e) {
                System.err.println("String cannot be converted to a Path!\n");
            } catch (IOException e) {
                System.err.println("IOException! " + e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                System.err.println("No such algorithm!\n");
            }
        }
    }
}