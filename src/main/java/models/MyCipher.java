package main.java.models;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MyCipher {
    //from https://stackoverflow.com/questions/20796042/aes-encryption-and-decryption-with-java/20796446#20796446
    private final byte[] thisismykey;
    private final SecretKey secKey;
    private final Cipher aesCipher;
    public MyCipher() throws NoSuchPaddingException, NoSuchAlgorithmException {
        //here you should write a key that is divisible by 16
        thisismykey = "0123456701234567".getBytes();
        secKey = new SecretKeySpec(thisismykey, "AES");
        aesCipher = Cipher.getInstance("AES");

    }

    public byte[] encrypt (String originaltext) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        //turn your original text to byte
        byte[] myoriginaltexttobyte =originaltext.getBytes();
        //activate the encrypt method
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        //encrypt the text and assign the encrypted text to a byte array
        byte[] bytecipheredoforgtext = aesCipher.doFinal(myoriginaltexttobyte);
        //change it to string with new string
        //String textA = new String(bytecipheredoforgtext);
        //return the byte array
        return bytecipheredoforgtext;
    }

    public byte[] decrypt (byte[] encryptedtext) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        //get the bytes of encrypted text and assign it to a byte array
        byte[] byteofencryptedtext = encryptedtext;
        //activate the decrypt mode of the cipher
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        //decrypt the encrypted text and assign it to a byte array
        byte[] byteofencryptedbacktonormaltext = aesCipher.doFinal(byteofencryptedtext);
        //returned the decrypted byte array
        return byteofencryptedbacktonormaltext;
    }
}
