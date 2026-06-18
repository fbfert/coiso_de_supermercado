package checkout;

public class QuantidadeInvalidaException extends SistemaCheckoutException {
    public QuantidadeInvalidaException(int quantidade) {
        super("Quantidade invalida: " + quantidade + ".");
    }
}
