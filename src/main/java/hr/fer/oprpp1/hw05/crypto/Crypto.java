package hr.fer.oprpp1.hw05.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Objects;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Class Crypto provides static methods that digest, encrypt and decrypt files.
 *
 * @author MatijaPav
 */
public class Crypto {

    /**
     * Digests the given file.
     * @param inputFile file whose digest is being calculated.
     * @return digest
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String digest(String inputFile) throws NoSuchAlgorithmException, IOException {
        Objects.requireNonNull(inputFile, "Input file can't be null!");
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");

        try(InputStream is = new BufferedInputStream(Files.newInputStream(Path.of(inputFile)))){
            byte[] bytearr = new byte[4096];
            while(true){
                int i = is.read(bytearr);
                if (i == -1) break;
                sha256.update(bytearr, 0, i);
            }
        }

        return Util.bytetohex(sha256.digest());
    }

    /**
     * Encrypts or decrypts files.
     * @param inputFile File that is being processed.
     * @param outputFile Resulting file.
     * @param keyText key used for encryption/decryption.
     * @param ivSpec initialization vector.
     */

    private static void crypt(String inputFile, String outputFile, boolean encrypt, String keyText, String ivSpec) throws NoSuchPaddingException, NoSuchAlgorithmException,
        InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, IOException {
        Objects.requireNonNull(inputFile, "Input file can't be null!");
        Objects.requireNonNull(outputFile, "Output file can't be null!");
        Objects.requireNonNull(keyText, "Password can't be null!");
        Objects.requireNonNull(ivSpec, "Initializing vector can't be null!");

        try (InputStream is = new BufferedInputStream(Files.newInputStream(Path.of(inputFile)));
             OutputStream os = new BufferedOutputStream(Files.newOutputStream(Path.of(outputFile)))) {

            SecretKeySpec sks = new SecretKeySpec(Util.hextobyte(keyText), "AES");
            AlgorithmParameterSpec aps = new IvParameterSpec(Util.hextobyte(ivSpec));

            Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
            c.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, sks, aps);

            byte[] bytearray = new byte[4096];

            while(true){
                int i = is.read(bytearray);
                if(i == -1){
                    break;
                }
                os.write(c.update(bytearray, 0, i));
            }
            os.write(c.doFinal());
        }
    }

    /**
     * Command-line program used for checking file digest, encryption and decryption.
     *
     */
    public static void main(String[] args) {
        if(args.length < 2 || args.length > 3)
            throw new IllegalArgumentException("Program expected 2 or 3 arguments, but it received " + args.length + " arguments!");

        try{
            Scanner sc = new Scanner(System.in);
            if(args[0].equalsIgnoreCase("checksha")){
                if(args.length != 2 || !args[1].endsWith(".bin"))
                    throw new IllegalArgumentException("checksha command takes exactly one .bin file as argument!");
                System.out.println("Please provide expected sha-256 digest for " + args[1] + ":");
                String expected = sc.nextLine().trim();
                String digest = digest(args[1]);
                System.out.print("Digest completed! ");

                if(expected.equals(digest))
                    System.out.println("Digest of " + args[1] + " matches expected digest!");
                else
                    System.out.println("Digest of " + args[1] + " does not match expected digest! Digest was " + digest);

            } else if (args[0].equalsIgnoreCase("encrypt")){
                if(args.length != 3 || (!args[1].endsWith(".pdf") && !args[1].endsWith(".bin")))
                    throw new IllegalArgumentException("Incorrect files passed!");

                System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits): ");
                String pass = sc.nextLine().trim();
                if(pass.length() != 32)
                    throw new IllegalArgumentException("Password must be 32 chars long!");
                System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits): ");
                String initVector = sc.nextLine().trim();
                crypt(args[1], args[2], true, pass, initVector);
                System.out.println("Encryption completed. Generated file " + args[2] + " based on file " + args[1] + ".");
            } else if (args[0].equalsIgnoreCase("decrypt")){
                if(args.length != 3 || (!args[1].endsWith(".pdf") && !args[1].endsWith(".bin")))
                    throw new IllegalArgumentException("Decrypt takes exactly two pdf files!");

                System.out.println("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits): ");
                String pass = sc.nextLine();
                if(pass.length() != 32)
                    throw new IllegalArgumentException("Password must be 32 chars long!");
                System.out.println("Please provide initialization vector as hex-encoded text (32 hex-digits): ");
                String initVector = sc.nextLine();
                crypt(args[1], args[2], false, pass, initVector);
                System.out.println("Decryption completed. Generated file " + args[2] + " based on file " + args[1] + ".");

            } else {
                throw new IllegalArgumentException("Illegal command!");
            }

        } catch(Exception e){
            System.out.println(e.getMessage());
        }

    }
}
