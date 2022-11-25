package server;

import display.Color;
import display.Console;
import interpreter.Query;
import object.Database;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    /**
     * CREATE TABLE table COLS col1,col2...
     * DROP TABLE table
     * SELECT col1,col2,col3... FROM table
     * SELECT col1,col2,col3... FROM table WHERE (1 condition)
     * INSERT INTO table VALUES val1,val2,val3...
     * DESC table / DESC (database)
     * COMMIT
     * ROLLBACK
     * SELECT * FROM table
     * UPDATE table SET col1=val1 WHERE (1 condition fac.)
     * DELETE FROM table WHERE (1 condition fac.)
     * SELECT * FROM table JOIN user ON id==id
     * SELECT * FROM table JOIN user ON id==id WHERE (1 condition)
     * (query1) UNION (query2)
     * (query1) INTERSECTS (query1)
     * DIFFERENCE table1 AND table2
     * DIVIDE table1 BY table2
     * */

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(1234);

        System.out.println("-- Serveur --");

        Socket socket = serverSocket.accept();
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        Database database = new Database("test");
        Query query = new Query(database);

        while(!socket.isClosed()) {
            String result = null;
            try {
                query.setQuery(dataInputStream.readUTF());
                while (query.getNbSubQuery() != 0)
                    query.executeSubQuery();
                result = Console.print(query.execute());
                query.getDatabase().clearSubQueryTable();
            } catch (Exception e) {
                result = Color.RED + e.getMessage() + Color.RESET;
            } finally {
                if (result != null)
                    dataOutputStream.writeUTF(result);
            }
        }

        serverSocket.close();

    }
}