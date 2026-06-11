package checkout;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/*
 * Subclasse que representa produtos pereciveis.
 * O desconto depende da proximidade do vencimento.
 */
public class ProdutoPerecivel extends Produto {
    private LocalDate dataVencimento;
    private int diasParaVencer;

    public ProdutoPerecivel(String codigo, String nome, double precoBase, LocalDate dataVencimento) {
        this(codigo, nome, precoBase, 0, dataVencimento);
    }

    public ProdutoPerecivel(String codigo, String nome, double precoBase, int estoque, LocalDate dataVencimento) {
        super(codigo, nome, precoBase, estoque);
        this.dataVencimento = dataVencimento;
        this.diasParaVencer = (int) ChronoUnit.DAYS.between(LocalDate.now(), dataVencimento);
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public int getDiasParaVencer() {
        return diasParaVencer;
    }

    @Override
    public double calcularPrecoFinal() {
        System.out.println("   > Produto perecivel identificado: " + getNome());
        System.out.println("   > Data de vencimento: " + dataVencimento);
        System.out.println("   > Dias para vencer: " + diasParaVencer);

        double desconto = 0.0;

        if (diasParaVencer < 3) {
            desconto = 0.30;
            System.out.println("   > Menos de 3 dias para vencer. Aplicando 30% de desconto.");
        } else {
            System.out.println("   > Produto ainda nao esta tao proximo do vencimento. Sem desconto de perecivel.");
        }

        double precoFinal = getPrecoBase() * (1 - desconto);
        System.out.println("   > Preco final unitario do perecivel: " + formatarMoeda(precoFinal));
        return precoFinal;
    }

    @Override
    public String getTipo() {
        return "PERECIVEL";
    }
}
