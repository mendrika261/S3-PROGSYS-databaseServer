package server;

import java.io.IOException;
import java.net.ServerSocket;

public class ClosedListener extends Thread {
    ServerSocket serverSocket;
    UserListener userListener;

    public ClosedListener(ServerSocket serverSocket, UserListener userListener) {
        setServerSocket(serverSocket);
        setMultiUser(userListener);
    }

    @Override
    public void run() {
        try {
            getServerSocket().close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (getMultiUser().activeClients()!=0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("--- Fin ---");
        System.exit(0);
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public UserListener getMultiUser() {
        return userListener;
    }

    public void setMultiUser(UserListener userListener) {
        this.userListener = userListener;
    }
}
