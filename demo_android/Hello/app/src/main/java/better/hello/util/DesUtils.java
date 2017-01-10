package better.hello.util;

import android.annotation.SuppressLint;
import android.util.Base64;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;


public class DesUtils {
    private static final String MCRYPT_TRIPLEDES = "DESede";
    private static final String TRANSFORMATION = "DESede/CBC/PKCS5Padding";
    private final static String encoding = "utf-8";

    private final static String key = "qwertyuhjklmngffdddlohbcasj";

    private final static String iv = "12345678";

    public static String decrypt(String encryptStr) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(key.getBytes(encoding));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(MCRYPT_TRIPLEDES);
        SecretKey sec = keyFactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec IvParameters = new IvParameterSpec(iv.getBytes(encoding));
        cipher.init(Cipher.DECRYPT_MODE, sec, IvParameters);
        return new String(cipher.doFinal(Base64.decode(encryptStr, Base64.DEFAULT)), encoding);
    }

    @SuppressLint("TrulyRandom")
    public static String encrypt(String plainText) throws Exception {
        DESedeKeySpec spec = new DESedeKeySpec(key.getBytes(encoding));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        SecretKey sec = keyFactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        IvParameterSpec IvParameters = new IvParameterSpec(iv.getBytes(encoding));
        cipher.init(Cipher.ENCRYPT_MODE, sec, IvParameters);
        return new String(Base64.encode(cipher.doFinal(plainText.getBytes(encoding)), Base64.DEFAULT));
    }

    public static byte[] generateSecretKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance(MCRYPT_TRIPLEDES);
        return keygen.generateKey().getEncoded();
    }

}
