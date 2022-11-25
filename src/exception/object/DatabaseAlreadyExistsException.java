package exception.object;

public class DatabaseAlreadyExistsException extends Exception {
    public DatabaseAlreadyExistsException(String name) {
        super("\nLa base de donnée \""+name+"\" existe déjà!");
        setStackTrace(new StackTraceElement[]{});
    }
}
