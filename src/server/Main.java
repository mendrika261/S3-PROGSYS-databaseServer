package server;

import display.Color;

import java.net.ServerSocket;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Debut ---");

        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
        UserListener userListener = new UserListener(serverSocket);
        userListener.start();

        System.out.println(Color.YELLOW + "Le serveur est demarré sur le port "+ serverSocket.getLocalPort() + Color.RESET);

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
}