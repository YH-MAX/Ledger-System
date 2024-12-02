import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Register");
            System.out.println("Login");
            System.out.println("Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = sc.nextLine();

                    System.out.print("Enter email: ");
                    String email = sc.nextLine();

                    System.out.print("Enter password: ");
                    String password = sc.nextLine();

                    userManager.registerUser(username, email, password);
                    break;

                case 2:
                    System.out.print("Enter email: ");
                    email = sc.nextLine();

                    System.out.print("Enter password: ");
                    password = sc.nextLine();

                    userManager.loginUser(email, password);
                    break;

                case 3:
                    System.out.println("Exiting the system...");
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
