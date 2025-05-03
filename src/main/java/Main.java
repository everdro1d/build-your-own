import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            if (input.isBlank()) {
                break;
            }

            String[] command = input.split(" ");
            if (command[0].equals("exit")) {
                if (command.length == 2) {
                    int status = Integer.parseInt(command[1]);
                    exit(status);
                }
            }

            invalidCommand(input);
        }
    }

    private static void invalidCommand(String input) {
        System.out.println(input + ": command not found");
    }

    private static void exit(int status) {
        System.exit(status);
    }
}
