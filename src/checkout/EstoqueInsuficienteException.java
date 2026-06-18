package checkout;

public class EstoqueInsuficienteException extends SistemaCheckoutException {
    public EstoqueInsuficienteException(String nomeProduto, int estoqueDisponivel, int quantidadeSolicitada) {
        super("Estoque insuficiente para " + nomeProduto + ". Estoque atual: " + estoqueDisponivel + ", quantidade solicitada: " + quantidadeSolicitada + ".");
    }
}
