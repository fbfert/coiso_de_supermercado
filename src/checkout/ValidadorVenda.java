package checkout;

import java.util.List;

public class ValidadorVenda {
    public Produto buscarProdutoOuFalhar(List<Produto> produtos, String codigo) {
        for (Produto produto : produtos) {
            if (produto.getCodigo().equalsIgnoreCase(codigo)) {
                return produto;
            }
        }

        throw new ProdutoNaoEncontradoException(codigo);
    }

    public void validarQuantidade(Produto produto, int quantidade) {
        if (quantidade <= 0) {
            throw new QuantidadeInvalidaException(quantidade);
        }

        if (quantidade > produto.getEstoque()) {
            throw new EstoqueInsuficienteException(produto.getNome(), produto.getEstoque(), quantidade);
        }
    }
}
