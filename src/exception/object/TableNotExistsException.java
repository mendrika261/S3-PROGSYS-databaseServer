package exception.object;

public class TableNotExistsException extends Exception {
    public TableNotExistsException(String database, String table) {
        super("\nIl n'y pas de table \""+table+"\" dans la base \""+database+"\"!");
        setStackTrace(new StackTraceElement[]{});
    }
}
