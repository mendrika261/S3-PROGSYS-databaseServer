package exception.query;

public class SyntaxInvalidException extends Exception {
    public SyntaxInvalidException(String lastWord, String query) {
        super("\nSyntaxe invalide ou inconnue \""+lastWord+"\" dans:\n>> "+query);
        setStackTrace(new StackTraceElement[]{});
    }
}
