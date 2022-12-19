package exception.consoleApp;


import display.Color;

public class InvalidPortException extends Exception {
    public InvalidPortException() {
        super(Color.YELLOW + "\n\tLe port spécifié est invalide!" + Color.RESET);
        setStackTrace(new StackTraceElement[]{});
    }
}
