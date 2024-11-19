import java.util.InputMismatchException;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        int menuChoice = 0;
        while (menuChoice != 3){
            menuChoice = runMenu();
            System.out.println("You chose: " + menuChoice);
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

        return option;
    }
}
