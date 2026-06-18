package checkout;

public class SistemaCheckoutException extends RuntimeException {
    public SistemaCheckoutException(String message) {
        super(message);
    }

    public SistemaCheckoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
