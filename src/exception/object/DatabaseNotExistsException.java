package exception.object;

public class DatabaseNotExistsException extends Exception {
    public DatabaseNotExistsException(String database) {
        super("\nLa base de donnée \""+database+"\" n'existent pas!");
        setStackTrace(new StackTraceElement[]{});
    }
}
