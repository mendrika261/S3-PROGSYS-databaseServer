package exception.query;

public class OperatorInvalidException extends Exception {
    public OperatorInvalidException(String comp) {
        super("\nL'operateur de comparaison est invalide ou inconnue dans \""+comp+"\"");
    }
}
