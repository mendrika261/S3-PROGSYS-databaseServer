package server;

import display.Color;
import display.Console;
import file.FileManager;
import interpreter.Query;
import object.Database;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.Instant;

public class UserSession extends Thread {
    Socket socket;
    Database database;

    public UserSession(Socket socket) {
        setSocket(socket);
        setName("Session of " + getSocket().getInetAddress().getHostName());
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(getSocket().getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(getSocket().getOutputStream());

            setDatabase(new Database("test"));
            Query query = new Query(getDatabase());
            String clientMessage;

            while (!getSocket().isClosed()) {
                String result = null;
                try {
                    clientMessage = dataInputStream.readUTF();

                    if (clientMessage.equals("EXIT")) {
                        FileManager.writeLog("LOGOUT - [" + Timestamp.from(Instant.now()) +"] " + getSocket().getInetAddress().getHostName() + ": I'm out now");
                        dataOutputStream.writeUTF("EXIT");
                        getSocket().close();
                        return;
                    }

                    query.setQuery(clientMessage);
                    while (query.getNbSubQuery() != 0) query.executeSubQuery();
                    result = Console.print(query.execute());
                    query.getDatabase().clearSubQueryTable();

                    FileManager.writeLog("QUERY - ["+ Timestamp.from(Instant.now()) + "] " + getSocket().getInetAddress().getHostName() + ": " + query.getQuery());
                } catch (Exception e) {
                    result = Color.RED + e.getMessage() + Color.RESET;

                    FileManager.writeLog("ERROR - ["+ Timestamp.from(Instant.now()) +"] "+ getSocket().getInetAddress().getHostName() +": " + query.getQuery());
                } finally {
                    if (result != null) dataOutputStream.writeUTF(result);
                }
            }
        } catch (Exception ignored) {}
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
}
