package checkout;

public class ProdutoNaoEncontradoException extends SistemaCheckoutException {
    public ProdutoNaoEncontradoException(String codigo) {
        super("Produto nao encontrado: " + codigo);
    }
}
