package server;

import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));

        System.out.println("-- Serveur --");

        while (true) {
            Socket socket = serverSocket.accept();
            SessionUser sessionUser = new SessionUser(socket);
            sessionUser.start();
        }

        //serverSocket.close();

    }
}