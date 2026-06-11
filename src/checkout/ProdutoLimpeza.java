package checkout;

/*
 * Subclasse que representa produtos de limpeza.
 * O desconto progressivo depende da quantidade e sera calculado no ItemCarrinho.
 */
public class ProdutoLimpeza extends Produto {
    private String fragrancia;

    public ProdutoLimpeza(String codigo, String nome, double precoBase, String fragrancia) {
        this(codigo, nome, precoBase, 0, fragrancia);
    }

    public ProdutoLimpeza(String codigo, String nome, double precoBase, int estoque, String fragrancia) {
        super(codigo, nome, precoBase, estoque);
        this.fragrancia = fragrancia;
    }

    public String getFragrancia() {
        return fragrancia;
    }

    @Override
    public double calcularPrecoFinal() {
        System.out.println("   > Produto de limpeza identificado: " + getNome());
        System.out.println("   > Fragrancia: " + fragrancia);
        System.out.println("   > Neste tipo de produto, o desconto progressivo depende da quantidade e sera tratado no ItemCarrinho.");
        System.out.println("   > Preco final unitario base: " + formatarMoeda(getPrecoBase()));
        return getPrecoBase();
    }

    @Override
    public String getTipo() {
        return "LIMPEZA";
    }
}
