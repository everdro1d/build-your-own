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
                    break;
                }
            } else if (command[0].equals("echo")) {
                echo(command);
                break;
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

    private static void echo(String[] command) {
        StringBuilder b = new StringBuilder();
        for (int i = 1 ; i < command.length ; i++) {
            b.append(command[i]).append(" ");
        }
        System.out.println(b.toString().trim());
    }
}
