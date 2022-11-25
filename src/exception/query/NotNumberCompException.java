package exception.query;

public class NotNumberCompException extends Exception {
    public NotNumberCompException(String operator, String col, String value) {
        super("\nLa comparaison avec l'operateur \""+operator+"\" n'est possible que pour les nombres:\n" +
                "\""+col+"\" et \""+value+"\" ne sont pas des nombres");
    }
}
