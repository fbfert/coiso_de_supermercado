package checkout;

import java.util.List;

public interface RepositorioProduto {
    void salvarProduto(Produto produto);

    void salvarTodosProdutos(List<Produto> produtos);

    List<Produto> carregarTodosProdutos();

    Produto buscarProdutoPorCodigo(String codigo);

    List<Produto> buscarPorNomeParcial(String termoBusca);
}
