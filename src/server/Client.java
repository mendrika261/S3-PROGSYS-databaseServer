package server;

import display.Color;
import display.Console;
import interpreter.Query;
import object.Database;

import java.util.Scanner;

public class Client extends Thread {
    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);

        String dbName = "test";
        Database database = null;
        try {
            database = new Database(dbName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Query query = new Query(database);
        long start= 0L, end= 0L;

        System.out.println("Bienvenue, vous utilisez la base test");
        System.out.println();

        String text = "";
        while(true) {
            System.out.print("Requete >> ");
            try {
                text = scanner.nextLine();
                start = System.currentTimeMillis();
                query.setQuery(text);
                while(query.getNbSubQuery()!=0) {
                    query.executeSubQuery();
                    System.out.println(query.getQuery());
                }
                Console.print(query.execute());
                query.getDatabase().clearSubQueryTable();
                end = System.currentTimeMillis();
                System.out.println("Time: "+(end-start)+"ms");
            } catch (Exception e) {
                System.out.println(Color.RED + e.getMessage()  + Color.RESET);
            }
            System.out.println();
        }
    }
}
