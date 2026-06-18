package checkout;

import java.util.List;

public interface RepositorioVenda {
    int gerarProximoIdVenda();

    void salvarVenda(Venda venda);

    void salvarItensVenda(Venda venda);

    void salvarDescontosVenda(Venda venda);

    List<Venda> listarVendasSalvas();
}
