import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class EncryptDecrypt {
    public  String encrypt(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(input.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    
    public  boolean decrypt(String input, String hash) throws NoSuchAlgorithmException {
        return encrypt(input).equals(hash);
    }
}