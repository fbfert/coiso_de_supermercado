package checkout;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/*
 * Representa uma venda fechada no sistema.
 * Guarda os itens, totais e o cupom fiscal em texto.
 */
public class Venda {
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private int idVenda;
    private LocalDateTime dataHora;
    private List<ItemCarrinho> itens;
    private double totalBruto;
    private double totalDescontos;
    private double totalFinal;
    private String cupomFiscal;

    public Venda(int idVenda, LocalDateTime dataHora, List<ItemCarrinho> itens, double totalBruto, double totalDescontos, double totalFinal, String cupomFiscal) {
        this.idVenda = idVenda;
        this.dataHora = dataHora;
        this.itens = itens;
        this.totalBruto = totalBruto;
        this.totalDescontos = totalDescontos;
        this.totalFinal = totalFinal;
        this.cupomFiscal = cupomFiscal;
    }

    public int getIdVenda() {
        return idVenda;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public List<ItemCarrinho> getItens() {
        return itens;
    }

    public double getTotalBruto() {
        return totalBruto;
    }

    public double getTotalDescontos() {
        return totalDescontos;
    }

    public double getTotalFinal() {
        return totalFinal;
    }

    public String getCupomFiscal() {
        return cupomFiscal;
    }

    public String gerarCupomFiscal() {
        StringBuilder cupom = new StringBuilder();
        NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        cupom.append("=========== CUPOM FISCAL ===========\n");
        cupom.append("Venda: ").append(idVenda).append(" | Data: ").append(dataHora.format(FORMATO_DATA_HORA)).append('\n');
        cupom.append(String.format("%-18s %-8s %-15s %-15s%n", "PRODUTO", "QTD", "BRUTO", "FINAL"));
        cupom.append("------------------------------------\n");

        for (ItemCarrinho item : itens) {
            cupom.append(String.format("%-18s %-8d %-15s %-15s%n",
                    item.getProduto().getNome(),
                    item.getQuantidade(),
                    formatador.format(item.getSubtotalBruto()),
                    formatador.format(item.getSubtotalFinal())));
        }

        cupom.append("------------------------------------\n");
        cupom.append(String.format("%-18s %-8s %-15s %-15s%n", "TOTAL BRUTO", "", formatador.format(totalBruto), ""));
        cupom.append(String.format("%-18s %-8s %-15s %-15s%n", "DESCONTOS", "", formatador.format(totalDescontos), ""));
        cupom.append(String.format("%-18s %-8s %-15s %-15s%n", "TOTAL FINAL", "", "", formatador.format(totalFinal)));
        cupom.append("====================================\n");

        return cupom.toString();
    }
}
