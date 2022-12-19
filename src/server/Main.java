package server;

import display.Color;
import exception.consoleApp.ArgumentMissingException;
import exception.consoleApp.InvalidPortException;
import file.FileManager;

import java.net.ServerSocket;
import java.util.Scanner;

public class Main {
    public static String DATABASE = "database.db";
    public static UserCommit AUTO_COMMIT_USER;

    public static void main(String[] args) throws Exception {
        if(args.length!=1)
            throw new ArgumentMissingException();

        verifyArgs(args[0]);

        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Debut ---");

        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
        UserListener userListener = new UserListener(serverSocket);
        userListener.start();

        // For autocommit query
        setAutoCommitUser(new UserCommit(userListener));

        System.out.println(Color.YELLOW + "Le serveur est demarré sur le port "+ serverSocket.getLocalPort() + Color.RESET);

        FileManager.writeLog("SERVER - [" + java.time.LocalDateTime.now() + "] Le serveur est demarré sur le port " + serverSocket.getLocalPort());

        while (true) {
            System.out.print("Command >> ");
            String command = scanner.nextLine();

            switch (command) {
                case "STOP" -> {
                    new ClosedListener(serverSocket, userListener).start();
                    System.out.println("\tEn attente des utilisateurs: " + userListener.statusOfClients());
                }
                case "FORCE STOP" -> System.exit(0);
                case "STATUS" -> System.out.println("\tStatus: " + userListener.statusOfClients());
                default -> System.out.println(Color.RED + "\tCommande invalide, see README.md!" + Color.RESET);
            }
        }
    }

    static void verifyArgs(String port) throws InvalidPortException {
        if(!isValidNumber(port, 4) && !isValidNumber(port, 5)) throw new InvalidPortException();
    }

    static boolean isValidNumber(String port, int length) {
        if(port.length() != length) return false;
        try {
            Integer.parseInt(port);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    public static UserCommit getAutoCommitUser() {
        return AUTO_COMMIT_USER;
    }

    public static void setAutoCommitUser(UserCommit autoCommitUser) {
        AUTO_COMMIT_USER = autoCommitUser;
    }

}