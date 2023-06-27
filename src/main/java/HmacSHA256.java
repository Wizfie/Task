import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * HmacSHA256
 *
 * @author Owen.Yuan
 * @since 1.0
 */
public class HmacSHA256 {

    private static final HmacSHA256 instance = new HmacSHA256();

    private static final char[] hexDigits = new char[] {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    public static HmacSHA256 getInstance() {
        return instance;
    }

    public String apply(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        final String algorithm = "HmacSHA256";
        Mac mac = Mac.getInstance(algorithm);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, algorithm);
        mac.init(secretKeySpec);
        byte[] result = mac.doFinal(data);
        return hex(result);
    }

    public final String hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            sb.append(hexDigits[(b >> 4) & 0xf]).append(hexDigits[b & 0xf]);
        }
        return sb.toString();
    }
}