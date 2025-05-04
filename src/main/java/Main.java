import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static final ArrayList<String> builtinList = new ArrayList<>(Arrays.asList(
            "echo", "exit", "type", "pwd", "cd"
    ));

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);
        whileLabel:
        while (true) {
            System.out.print("$ ");
            String input = scanner.nextLine();
            if (input.isBlank()) {
                continue;
            }

            if (!input.trim().contains(" ")) {
                if (!builtinList.contains(input) && !isExecInPath(input)) {
                    invalidCommand(input);
                    continue;
                }
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
                    if (command.length == 2 && builtinList.contains(command[1])) {
                        System.out.println(command[1] + " is a shell builtin");
                    } else if (!builtinList.contains(command[1])) {
                        if (isExecInPath(command[1])) {
                            System.out.println(command[1] + " is " + getExecPathInBin(command[1]));
                            continue;
                        }
                        System.out.println(command[1] + ": not found");
                    }
                    continue;
                }
                case "pwd" -> {
                    System.out.println(getWorkingDirectory());
                    continue;
                }
                case "cd" -> {
                    if (command.length == 2) {
                        changeDirectory(command[1]);
                    }
                    continue;
                }
                default -> {
                    if (isExecInPath(command[0])) {
                        runExecutable(command);
                        continue;
                    }
                    invalidCommand(command[0]);
                }
            }

            invalidCommand(command[0]);
        }
    }

    private static void invalidCommand(String input) {
        System.out.println(input + ": command not found");
    }

    private static void exit(int status) {
        System.exit(status);
    }

    private static void echo(String[] command) {
        System.out.println(getArgsAsString(command));
    }

    private static String getWorkingDirectory() {
        return System.getProperty("user.dir");
    }

    private static void changeDirectory(String path) {
        File f = new File(path);
        if (!f.exists()) {
            System.out.println("cd: " + path + ": No such file or directory");
            return;
        }

        if (!f.isDirectory()) {
            f = f.getParentFile();
        }

        System.setProperty("user.dir", f.getAbsolutePath());
    }


    private static String getArgsAsString(String[] command) {
        StringBuilder b = new StringBuilder();
        for (int i = 1; i < command.length ; i++) {
            b.append(command[i]).append(" ");
        }
        return b.toString().trim();
    }

    private static boolean isExecInPath(String execName) {
        return (getExecPathInBin(execName) != null);
    }

    private static String getExecPathInBin(String execName) {
        String path = System.getenv("PATH");
        if (path == null) {
            System.err.println("PATH is null");
            return null;
        }

        String[] bins;
        if (path.contains(":")) {
            bins = path.split(":");
        } else {
            bins = new String[] { path };
        }

        for (String bin : bins) {
            File f = new File(bin);
            File[] execFiles = f.listFiles(File::canExecute);
            if (execFiles == null) {
                continue;
            }

            for (File file : execFiles) {
                if (file.getName().equals(execName)) {
                    return file.getAbsolutePath();
                }
            }
        }

        return null;
    }

    private static void runExecutable(String[] execWithArgs) {
        ProcessBuilder pb = new ProcessBuilder(new ArrayList<>(Arrays.asList(execWithArgs)));
        pb.redirectInput(ProcessBuilder.Redirect.PIPE);
        try {
            Process p = pb.start();
            Scanner i = new Scanner(p.getInputStream());
            while (i.hasNextLine()) {
                System.out.println(i.nextLine());
            }

            p.waitFor();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
