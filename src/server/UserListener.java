package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class UserListener extends Thread {
    ServerSocket serverSocket;
    List<Socket> sockets = new ArrayList<>();

    public UserListener(ServerSocket serverSocket) {
        setServerSocket(serverSocket);
        setName("Multi user");
    }

    @Override
    public void run() {
        Socket socket;

        while (!getServerSocket().isClosed()) {
            try {
                socket = getServerSocket().accept();
            } catch (SocketException e) {
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if(socket==null) continue;
            getSockets().add(socket);
            UserSession userSession = new UserSession(socket);
            userSession.start();
        }
    }

    public int activeClients() {
        int active = 0;
        for (Socket socket:getSockets()) {
            if (!socket.isClosed()) active++;
        }
        return active;
    }

    public String statusOfClients() {
        return activeClients() + "/" + getSockets().size() + " client(s)";
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public List<Socket> getSockets() {
        return sockets;
    }

    public void setSockets(List<Socket> sockets) {
        this.sockets = sockets;
    }
}
