package checkout;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class RoteiroTestesEstresse {
    public static void main(String[] args) {
        ValidadorVenda validador = new ValidadorVenda();

        testarProdutoInexistente(validador);
        testarQuantidadeZeroOuNegativa(validador);
        testarEstoqueInsuficiente(validador);
        testarVendaValidaComPolimorfismo();
    }

    private static void testarProdutoInexistente(ValidadorVenda validador) {
        System.out.println("TESTE 1");
        System.out.println("ESPERADO: ProdutoNaoEncontradoException ao buscar um codigo inexistente.");

        try {
            validador.buscarProdutoOuFalhar(List.of(
                    new ProdutoPerecivel("A1", "Iogurte", 10.0, 10, LocalDate.now().plusDays(5))),
                    "XYZ");
            System.out.println("RESULTADO: nenhuma excecao foi lancada.");
            System.out.println("FALHOU");
        } catch (ProdutoNaoEncontradoException e) {
            System.out.println("RESULTADO: " + e.getMessage());
            System.out.println("PASSOU");
        } catch (SistemaCheckoutException e) {
            System.out.println("RESULTADO: excecao diferente da esperada -> " + e.getClass().getSimpleName());
            System.out.println("FALHOU");
        }

        System.out.println();
    }

    private static void testarQuantidadeZeroOuNegativa(ValidadorVenda validador) {
        System.out.println("TESTE 2");
        System.out.println("ESPERADO: QuantidadeInvalidaException para quantidade zero ou negativa.");

        try {
            Produto produto = new ProdutoLimpeza("B1", "Detergente", 5.0, 10, "Limao");
            validador.validarQuantidade(produto, 0);
            System.out.println("RESULTADO: nenhuma excecao foi lancada.");
            System.out.println("FALHOU");
        } catch (QuantidadeInvalidaException e) {
            System.out.println("RESULTADO: " + e.getMessage());
            System.out.println("PASSOU");
        } catch (SistemaCheckoutException e) {
            System.out.println("RESULTADO: excecao diferente da esperada -> " + e.getClass().getSimpleName());
            System.out.println("FALHOU");
        }

        System.out.println();
    }

    private static void testarEstoqueInsuficiente(ValidadorVenda validador) {
        System.out.println("TESTE 3");
        System.out.println("ESPERADO: EstoqueInsuficienteException quando a quantidade excede o estoque.");

        try {
            Produto produto = new ProdutoPerecivel("C1", "Queijo", 20.0, 2, LocalDate.now().plusDays(2));
            validador.validarQuantidade(produto, 5);
            System.out.println("RESULTADO: nenhuma excecao foi lancada.");
            System.out.println("FALHOU");
        } catch (EstoqueInsuficienteException e) {
            System.out.println("RESULTADO: " + e.getMessage());
            System.out.println("PASSOU");
        } catch (SistemaCheckoutException e) {
            System.out.println("RESULTADO: excecao diferente da esperada -> " + e.getClass().getSimpleName());
            System.out.println("FALHOU");
        }

        System.out.println();
    }

    private static void testarVendaValidaComPolimorfismo() {
        System.out.println("TESTE 4");
        System.out.println("ESPERADO: venda valida com ProdutoPerecivel e ProdutoLimpeza, mostrando polimorfismo e descontos.");

        try {
            Produto perecivel = new ProdutoPerecivel("D1", "Iogurte Natural", 8.50, 20, LocalDate.now().plusDays(1));
            Produto limpeza = new ProdutoLimpeza("D2", "Detergente Neutro", 4.99, 50, "Limao");

            CarrinhoDeCompras carrinho = new CarrinhoDeCompras();
            carrinho.adicionarItem(perecivel, 2);
            carrinho.adicionarItem(limpeza, 4);

            Venda venda = carrinho.fecharCompra(999, LocalDateTime.now());
            String cupom = venda.getCupomFiscal();

            if (cupom.contains("Iogurte Natural") && cupom.contains("Detergente Neutro") && venda.getTotalFinal() > 0) {
                System.out.println("RESULTADO: venda gerada com sucesso e cupom contendo os dois tipos de produto.");
                System.out.println("PASSOU");
            } else {
                System.out.println("RESULTADO: a venda foi gerada, mas o cupom nao demonstrou o comportamento esperado.");
                System.out.println("FALHOU");
            }
        } catch (SistemaCheckoutException e) {
            System.out.println("RESULTADO: excecao inesperada -> " + e.getMessage());
            System.out.println("FALHOU");
        }

        System.out.println();
    }
}
