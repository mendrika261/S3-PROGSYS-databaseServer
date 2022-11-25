package exception.query;

public class MissingParenthesesException extends Exception {
    public MissingParenthesesException(int open, int close) {
        super("Il y a "+open+" parenthese(s) ouvert(s) et "+close+" fermé(s) dans la requête");
    }
}
