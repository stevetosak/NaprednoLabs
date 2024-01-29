package Labs2.Zadaca2.MatrixTester;

public class InsufficientElementsException extends Throwable {
    private final String message;

    public InsufficientElementsException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
