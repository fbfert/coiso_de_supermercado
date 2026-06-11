package checkout;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 * Classe que guarda os itens comprados e fecha a compra.
 * O uso de List demonstra a colecao pedida pelo professor.
 */
public class CarrinhoDeCompras {
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private List<ItemCarrinho> itens;

    public CarrinhoDeCompras() {
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(Produto produto, int quantidade) {
        System.out.println();
        System.out.println("Adicionando item ao carrinho...");
        System.out.println("Produto: " + produto.getNome());
        System.out.println("Quantidade: " + quantidade);
        itens.add(new ItemCarrinho(produto, quantidade));
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    public Venda fecharCompra(int idVenda, LocalDateTime dataHora) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("FECHANDO COMPRA E GERANDO CUPOM FISCAL");
        System.out.println("==================================================");

        StringBuilder cupom = new StringBuilder();
        double totalBruto = 0.0;
        double totalDescontos = 0.0;
        double totalFinal = 0.0;

        cupom.append("=========== CUPOM FISCAL ===========\n");
        cupom.append("Venda: ").append(idVenda).append(" | Data: ").append(dataHora.format(FORMATO_DATA_HORA)).append('\n');
        cupom.append(String.format("%-18s %-8s %-15s %-15s%n", "PRODUTO", "QTD", "BRUTO", "FINAL"));
        cupom.append("------------------------------------\n");

        for (ItemCarrinho item : itens) {
            System.out.println();
            System.out.println("Processando item com polimorfismo...");
            Produto produtoBase = item.getProduto();
            System.out.println("Referenca base: Produto -> objeto real: " + produtoBase.getClass().getSimpleName());

            double subtotalFinal = item.calcularSubtotal();
            totalBruto += item.getSubtotalBruto();
            totalDescontos += item.getDescontoAplicado();
            totalFinal += subtotalFinal;

            cupom.append(String.format("%-18s %-8d %-15s %-15s%n",
                    produtoBase.getNome(),
                    item.getQuantidade(),
                    formatarMoeda(item.getSubtotalBruto()),
                    formatarMoeda(subtotalFinal)));
        }

        cupom.append("------------------------------------\n");
        cupom.append(String.format("%-18s %-8s %-15s %-15s%n", "TOTAL BRUTO", "", formatarMoeda(totalBruto), ""));
        cupom.append(String.format("%-18s %-8s %-15s %-15s%n", "DESCONTOS", "", formatarMoeda(totalDescontos), ""));
        cupom.append(String.format("%-18s %-8s %-15s %-15s%n", "TOTAL FINAL", "", "", formatarMoeda(totalFinal)));
        cupom.append("====================================\n");

        System.out.println();
        System.out.println("Compra finalizada com sucesso.");
        System.out.println("Total bruto: " + formatarMoeda(totalBruto));
        System.out.println("Total de descontos: " + formatarMoeda(totalDescontos));
        System.out.println("Total final: " + formatarMoeda(totalFinal));

        return new Venda(idVenda, dataHora, new ArrayList<>(itens), totalBruto, totalDescontos, totalFinal, cupom.toString());
    }

    public Venda fecharCompra() {
        return fecharCompra(0, LocalDateTime.now());
    }

    private String formatarMoeda(double valor) {
        NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatador.format(valor);
    }
}
