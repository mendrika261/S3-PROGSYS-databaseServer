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

            if (command.equals("STOP")) {
                ClosedListener closedListener = new ClosedListener(serverSocket, userListener);
                closedListener.start();
                System.out.println("\tEn attente des utilisateurs: "+ userListener.statusOfClients());
            } else if(command.equals("FORCE STOP")) {
                System.exit(0);
                } else System.out.println(Color.RED + "\tCommande invalide" + Color.RESET);

            if(userListener.activeClients()==0) break;
        }
    }
}