package Labs2.Zadaca2.MatrixTester;

public class InvalidColumnNumberException extends Exception {
    private final String message;

    public InvalidColumnNumberException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
