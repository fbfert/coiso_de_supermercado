package checkout;

import java.text.NumberFormat;
import java.util.Locale;

/*
 * Um item do carrinho junta um produto com a quantidade comprada.
 * Aqui calculamos os valores bruto, desconto e final de forma didatica.
 */
public class ItemCarrinho {
    private Produto produto;
    private int quantidade;
    private double subtotalBruto;
    private double descontoAplicado;
    private double subtotalFinal;
    private String tipoDescontoAplicado;

    public ItemCarrinho(Produto produto, int quantidade) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.tipoDescontoAplicado = "SEM_DESCONTO";
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getSubtotalBruto() {
        return subtotalBruto;
    }

    public double getDescontoAplicado() {
        return descontoAplicado;
    }

    public double getSubtotalFinal() {
        return subtotalFinal;
    }

    public String getTipoDescontoAplicado() {
        return tipoDescontoAplicado;
    }

    public double calcularSubtotal() {
        System.out.println(" - Calculando subtotal do item: " + produto.getNome());
        System.out.println(" - Quantidade comprada: " + quantidade);
        System.out.println(" - Referencia base Produto apontando para objeto real " + produto.getClass().getSimpleName() + ".");
        System.out.println(" - Chamando calcularPrecoFinal() de forma polimorfica...");

        double precoBase = produto.getPrecoBase();
        subtotalBruto = precoBase * quantidade;
        descontoAplicado = 0.0;
        tipoDescontoAplicado = "SEM_DESCONTO";

        double precoUnitarioFinal = produto.calcularPrecoFinal();

        if (produto instanceof ProdutoPerecivel) {
            double descontoUnitario = precoBase - precoUnitarioFinal;
            descontoAplicado = descontoUnitario * quantidade;
            subtotalFinal = precoUnitarioFinal * quantidade;

            if (descontoAplicado > 0) {
                tipoDescontoAplicado = "PERECIVEL_PROXIMO_VENCIMENTO";
                System.out.println(" - Desconto de perecivel aplicado porque faltam menos de 3 dias para vencer.");
            } else {
                System.out.println(" - Produto perecivel sem desconto adicional no item.");
            }
        } else if (produto instanceof ProdutoLimpeza) {
            System.out.println(" - Produto de limpeza identificado no item.");
            System.out.println(" - Verificando quantidade para desconto progressivo...");

            double percentualDesconto = 0.0;
            if (quantidade >= 3 && quantidade <= 5) {
                percentualDesconto = 0.05;
                tipoDescontoAplicado = "LIMPEZA_LOTE";
                System.out.println(" - Faixa de 3 a 5 unidades. Aplicando 5% de desconto.");
            } else if (quantidade >= 6 && quantidade <= 9) {
                percentualDesconto = 0.10;
                tipoDescontoAplicado = "LIMPEZA_LOTE";
                System.out.println(" - Faixa de 6 a 9 unidades. Aplicando 10% de desconto.");
            } else if (quantidade >= 10) {
                percentualDesconto = 0.15;
                tipoDescontoAplicado = "LIMPEZA_LOTE";
                System.out.println(" - 10 ou mais unidades. Aplicando 15% de desconto.");
            } else {
                System.out.println(" - Quantidade abaixo de 3 unidades. Sem desconto progressivo.");
            }

            subtotalFinal = subtotalBruto * (1 - percentualDesconto);
            descontoAplicado = subtotalBruto - subtotalFinal;

            if (percentualDesconto > 0) {
                System.out.println(" - Desconto progressivo de limpeza aplicado no ItemCarrinho.");
            }
        } else {
            subtotalFinal = subtotalBruto;
            System.out.println(" - Nao e produto de limpeza. Nenhum desconto progressivo adicional sera aplicado.");
        }

        System.out.println(" - Subtotal bruto: " + formatarMoeda(subtotalBruto));
        System.out.println(" - Desconto aplicado: " + formatarMoeda(descontoAplicado));
        System.out.println(" - Subtotal final do item: " + formatarMoeda(subtotalFinal));

        return subtotalFinal;
    }

    private String formatarMoeda(double valor) {
        NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatador.format(valor);
    }
}
