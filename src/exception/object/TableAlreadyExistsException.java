package exception.object;

public class TableAlreadyExistsException extends Exception {
    public TableAlreadyExistsException(String database, String table) {
        super("\nLa table \""+table+"\" existe déjà dans la base \""+database+"\"!");
        setStackTrace(new StackTraceElement[]{});
    }
}
