package server;

import display.Color;
import display.Console;
import interpreter.Query;
import object.Database;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.time.Instant;
import java.util.Date;

public class SessionUser extends Thread {
    Socket socket;

    public SessionUser(Socket socket) {
        setSocket(socket);
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
                    if (clientMessage.equals("exit")) {
                        dataOutputStream.writeUTF("exit");
                        break;
                    }
                    query.setQuery(clientMessage);
                    while (query.getNbSubQuery() != 0) query.executeSubQuery();
                    result = Console.print(query.execute());
                    query.getDatabase().clearSubQueryTable();
                } catch (Exception e) {
                    result = Color.RED + e.getMessage() + Color.RESET;
                } finally {
                    if (result != null) dataOutputStream.writeUTF(result);
                    System.out.println(Date.from(Instant.now()) + ": " + query.getQuery());
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
