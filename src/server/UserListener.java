package server;

import file.FileManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UserListener extends Thread {
    ServerSocket serverSocket;
    List<UserSession> users = new ArrayList<>();

    public UserListener(ServerSocket serverSocket) {
        setServerSocket(serverSocket);
        setName("Listen for users");
    }

    @Override
    public void run() {
        Socket socket;

        while (!getServerSocket().isClosed()) {
            try {
                socket = getServerSocket().accept();

                FileManager.writeLog("LOGIN - ["+ Timestamp.from(Instant.now()) +"] "+ socket.getInetAddress().getHostName()
                        +": I'am here now");
            } catch (SocketException e) {
                return;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            UserSession userSession = new UserSession(socket);
            getUsers().add(userSession);
            userSession.start();
        }
    }

    public int activeClients() {
        int active = 0;
        for (UserSession userSession:getUsers()) {
            if (!userSession.getSocket().isClosed()) active++;
        }
        return active;
    }

    public String statusOfClients() {
        return activeClients() + " client(s) actif(s) et " + getUsers().size() + " client(s) traité(s)";
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void setUsers(List<UserSession> users) {
        this.users = users;
    }

    public List<UserSession> getUsers() {
        return users;
    }
}
