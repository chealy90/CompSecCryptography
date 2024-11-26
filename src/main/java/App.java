import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.*;

public class App {
    private static String salt = "TestingSalt12345";
    public static void main(String[] args) {
        //encryptAES("Lorem ipsum dolor sit amet");
        generateKey();

        int menuChoice = 0;
        while (menuChoice != 3){
            menuChoice = runMenu();
            switch (menuChoice) {
                case 1:
                    runEncryptFile();
                    break;
                case 2:
                    runDecryptFile();
                case 3:
                    System.out.println("--Thank you for using the encryption tool--");
                    break;
            }
        }
    }

    //APP RUNNING METHODS
    public static int runMenu(){
        Scanner kb = new Scanner(System.in);
        System.out.println("--- Please select an option ---");
        System.out.println("1: Encrypt a file.\n2: Decrypt a file.\n3: Quit.");
        int option = 0;
        try {
            System.out.print("Your choice:");
            option = kb.nextInt();
            kb.nextLine();
        } catch (InputMismatchException e){
            System.out.println("--invalid choice--");
            runMenu();
        }
        return option;
    }

    public static void runEncryptFile(){
        //get file data
        Scanner kb = new Scanner(System.in);
        System.out.println("--Encrypting--");
        System.out.print("--Filename:");
        String filename = kb.nextLine();
        String filePText = readFileContents(filename);


        if (filePText == null){
            return;
        }
        try {
            String key = generateKey();
            System.out.println("--Your Key is:"+key);
            String cText = encryptAES(filePText, key);
            writeToFile("ciphertext.txt", cText);

        } catch (Exception e){
            System.out.println("--Error--");
        }
    }

    public static void runDecryptFile(){
        Scanner kb = new Scanner(System.in);
        System.out.println("--Decrypting--");
        System.out.print("Filename:");
        String filename = kb.nextLine();
        String fileCText = readFileContents(filename);
        if (fileCText == null){
            return;
        }
        try {
            System.out.println("Enter your key:");
            String key = kb.next();
            kb.nextLine();
            String pText = decryptAES(fileCText, key);
            System.out.println("Ptext: " + pText);
            writeToFile("plaintext.txt", pText);
            //System.out.println(pText);

        } catch (Exception e){
            System.out.println("Error");
            e.printStackTrace();
        }
    }


    //ENCRYPTION AND DECRYPTION METHODS
    public static String generateKey(){
        SecureRandom sr = new SecureRandom();
        byte[] bytes = new byte[16];
        sr.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }


    public static String encryptAES(String stringToEncrypt, String initialSecretKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException
    {
        //default byte array
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        //create keyspec
        KeySpec spec = new PBEKeySpec(
                initialSecretKey.toCharArray(), App.salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
        System.out.println(secretKey.getEncoded());

        // Return encrypted string
        return Base64.getEncoder().encodeToString(
                cipher.doFinal(stringToEncrypt.getBytes(
                        StandardCharsets.UTF_8)));
    }


    public static String decryptAES(String stringToDecrypt, String initialSecretKey) throws NoSuchAlgorithmException,
            InvalidKeySpecException,
            NoSuchPaddingException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException
    {
        byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0 };
        // Create IvParameterSpec object and assign with
        // constructor
        IvParameterSpec ivspec = new IvParameterSpec(iv);

        // Create SecretKeyFactory Object
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        // Create KeySpec object and assign with
        // constructor
        KeySpec spec = new PBEKeySpec(initialSecretKey.toCharArray(), App.salt.getBytes(), 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
        // Return decrypted string
        return new String(cipher.doFinal(Base64.getDecoder().decode(stringToDecrypt)));
    }






    //FILE IO METHODS
    public static String readFileContents(String filename){
        try {
            String path = "./src/assets/" + filename;
            FileReader fr = new FileReader(path);
            String contents = "";
            int i;
            while ((i=fr.read()) != -1){
                contents += String.valueOf((char) i);
            }
            fr.close();
            return contents;
        }
        catch (FileNotFoundException e){
            System.out.println("--error: file not found--");
            return null;
        } catch (IOException e){
            System.out.println("--error: a problem occurred while reading the file");
            return null;
        }
    }

    public static void writeToFile(String filename, String contents) throws FileNotFoundException, IOException{
        String path = "./src/assets/" + filename;
        FileWriter fw = new FileWriter(path, false);
        for (int i=0;i<contents.length();i++){
            fw.write(contents.charAt(i));
        }
        System.out.println("--Written successfully");

        fw.close();
    }

}
