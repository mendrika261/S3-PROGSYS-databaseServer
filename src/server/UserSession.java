package server;

import display.Color;
import display.Console;
import interpreter.Query;
import object.Database;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class UserSession extends Thread {
    Socket socket;

    public UserSession(Socket socket) {
        setSocket(socket);
        setName("One session");
    }

    @Override
    public void run() {
        try {
            DataInputStream dataInputStream = new DataInputStream(getSocket().getInputStream());
            DataOutputStream dataOutputStream = new DataOutputStream(getSocket().getOutputStream());

            Database database = new Database("test");
            Query query = new Query(database);

            while (!getSocket().isClosed()) {
                String result = null;
                try {
                    String clientMessage = dataInputStream.readUTF();
                    if (clientMessage.equals("EXIT")) {
                        dataOutputStream.writeUTF("EXIT");
                        getSocket().close();
                    }
                    query.setQuery(clientMessage);
                    while (query.getNbSubQuery() != 0) query.executeSubQuery();
                    result = Console.print(query.execute());
                    query.getDatabase().clearSubQueryTable();
                } catch (Exception e) {
                    result = Color.RED + e.getMessage() + Color.RESET;
                } finally {
                    if (result != null) dataOutputStream.writeUTF(result);
                    // TODO log
                    //System.out.println(Date.from(Instant.now()) + ": " + query.getQuery());
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
}
