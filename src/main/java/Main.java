import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static ArrayList<String> builtinList = new ArrayList<>(Arrays.asList(
            "echo", "exit", "type"
    ));

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        whileLabel:
        while (true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            if (input.isBlank()) {
                break;
            }

            String[] command = input.split(" ");
            switch (command[0]) {
                case "exit" -> {
                    if (command.length == 2) {
                        int status = Integer.parseInt(command[1]);
                        exit(status);
                        break whileLabel;
                    }
                }
                case "echo" -> {
                    echo(command);
                    continue;
                }
                case "type" -> {
                    if (command.length == 2 && builtinList.contains(command[0])) {
                        System.out.println(command[0] + " is a shell builtin");
                        continue;
                    }
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

    private static void echo(String[] command) {
        StringBuilder b = new StringBuilder();
        for (int i = 1 ; i < command.length ; i++) {
            b.append(command[i]).append(" ");
        }
        System.out.println(b.toString().trim());
    }
}
