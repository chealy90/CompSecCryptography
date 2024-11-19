import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
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
            }
        }
    }


    public static int runMenu(){
        Scanner kb = new Scanner(System.in);
        System.out.println("--- Please select an option ---");
        System.out.println("1: Encrypt a file.\n2: Decrypt a file.\n3: Quit.");
        int option = 0;
        try {
            System.out.print("Your choice:");
            option = kb.nextInt();
        } catch (InputMismatchException e){
            System.out.println("--invalid choice--");
            runMenu();
        }
        kb.close();
        return option;
    }

    public static void runEncryptFile(){
        Scanner kb = new Scanner(System.in);
        System.out.println("--Encrypting--");
        String filename = kb.nextLine();
        String filePText = readFileContents(filename);


    }

    public static void runDecryptFile(){
        System.out.println("Decrypting");
    }

    //file IO methods
    public static String readFileContents(String filename){
        return "";
    }

}
