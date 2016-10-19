package pl.code_zone.praca_licencjacka.secure;

import android.util.Base64;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


/**
 * Created by MSI on 2016-10-19.
 */
public class RSA {

    //TODO Key must be in database!!!!
    // Keys stored in Base64
    private static final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApqMjhQPflyFqCjo5D+8AoyEkvUqZK8se436nvMob9O9s2homE1TN6pT0+tO6OjLWsryXrc1zTtDqjljbqhXm73v2B7j+hQSGsHep8wmIQyCg8l8/coBArm9jycR88c18vP2l7pwXnb2OlyYBa/GjWQZgAzn4CpaGXskXhE5LQfU3H+1ZjikAkIIz4oyLibU6e6D7TqR3c4YO9rzMR+opATCBbQKp250rUUcLL3cJz+pnoo1k4RM+nam+PRYB0eqrLhFVxXq5a6QvpU7OBfHgnRb5V5XH1mVGsdTLQzMgtyxjlpEDyww1+GjCsQODUnFWNZlbqe5ozbmGQU9yGklXdwIDAQAB";
    PublicKey publicKeyRSA;

    public RSA() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        publicKeyRSA = loadPublicKey();
    }

    private PublicKey loadPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, UnsupportedEncodingException {
        X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(Base64.decode(publicKey.getBytes("UTF-8"), Base64.NO_WRAP));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpecPublic);
    }

    public byte[] encrypt (String text) throws NoSuchPaddingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher rsa;
        rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsa.init(Cipher.ENCRYPT_MODE, publicKeyRSA);
        return rsa.doFinal(text.getBytes("UTF-8"));
    }

}
