package server;

import display.Color;
import display.Console;
import file.FileManager;
import interpreter.Query;
import object.Database;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UserSession extends Thread {
    Socket socket;
    Database database;
    int commitOrder;
    UserListener userListener;
    List<String> history = new ArrayList<>();

    public UserSession(Socket socket, UserListener userListener) {
        setSocket(socket);
        setUserListener(userListener);
        setName("Session of " + getSocket().getInetAddress().getHostName());
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(getSocket().getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(getSocket().getOutputStream());

            setDatabase(new Database(Main.DATABASE));
            Query query = new Query(getDatabase());
            String clientMessage;

            while (!getSocket().isClosed()) {
                String result = null;
                try {
                    clientMessage = dataInputStream.readUTF();

                    if (clientMessage.equalsIgnoreCase("EXIT")) {
                        exit(dataOutputStream);
                        return;
                    } else if (clientMessage.equalsIgnoreCase("COMMIT") && getHistory().size()>0) {
                        UserCommit userCommit = new UserCommit(dataOutputStream, this, getUserListener());
                        userCommit.run();
                        dataOutputStream.writeUTF("Transaction: modification réussie!");
                    } else result = Console.print(query.resolve(clientMessage));

                    if(needToBeCommitQuery(clientMessage)) {
                        getHistory().add(clientMessage);
                        if(getHistory().size()==1)
                            setCommitOrder(getUserListener().getNextCommitRank());
                    } else if(clientMessage.equalsIgnoreCase("rollback")) {
                        getHistory().clear();
                        setCommitOrder(0);
                    }

                    FileManager.writeLog("SUCESS - ["+ Timestamp.from(Instant.now()) + "] " + getSocket().getInetAddress().getHostName() + ": " + query.getQuery());
                } catch (Exception e) {
                    result = Color.RED + e.getMessage() + Color.RESET;

                    FileManager.writeLog("ERROR - ["+ Timestamp.from(Instant.now()) +"] "+ getSocket().getInetAddress().getHostName() +": " + query.getQuery());
                } finally {
                    if (result != null) dataOutputStream.writeUTF(result);
                }
            }
        } catch (Exception ignored) {}
    }

    public void exit(DataOutputStream dataOutputStream) throws IOException {
        FileManager.writeLog("LOGOUT - [" + Timestamp.from(Instant.now()) +"] " + getSocket().getInetAddress().getHostName() + ": I'm out now");
        dataOutputStream.writeUTF("EXIT");
        getSocket().close();
    }

    public boolean needToBeCommitQuery(String query) {
        String command = query.split(" ")[0];
        String[] needCommitCommand = {"insert", "update", "delete"};
        for (String m:needCommitCommand) {
            if(m.equalsIgnoreCase(command)) return true;
        }
        return false;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public Database getDatabase() {
        return database;
    }

    public int getCommitOrder() {
        return commitOrder;
    }

    public void setCommitOrder(int commitOrder) {
        this.commitOrder = commitOrder;
    }

    public List<String> getHistory() {
        return history;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }

    public UserListener getUserListener() {
        return userListener;
    }

    public void setUserListener(UserListener userListener) {
        this.userListener = userListener;
    }
}
