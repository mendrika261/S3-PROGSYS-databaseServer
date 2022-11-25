package exception.query;

public class SyntaxIncompleteException extends Exception {
    public SyntaxIncompleteException(String query) {
        super("\nRequête incomplet dans:\n>> "+query+" (Suite manquante ici...)");
        setStackTrace(new StackTraceElement[]{});
    }
}
