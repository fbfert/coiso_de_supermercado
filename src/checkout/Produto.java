package checkout;

import java.text.NumberFormat;
import java.util.Locale;

/*
 * Classe abstrata base do sistema.
 * Ela representa qualquer produto que possa ser colocado no carrinho.
 */
public abstract class Produto {
    private String codigo;
    private String nome;
    private double precoBase;
    private int estoque;

    public Produto(String codigo, String nome, double precoBase) {
        this(codigo, nome, precoBase, 0);
    }

    public Produto(String codigo, String nome, double precoBase, int estoque) {
        this.codigo = codigo;
        this.nome = nome;
        this.precoBase = precoBase;
        this.estoque = estoque;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public double getPrecoBase() {
        return precoBase;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public boolean reduzirEstoque(int quantidade) {
        if (quantidade <= 0 || quantidade > estoque) {
            return false;
        }
        estoque -= quantidade;
        return true;
    }

    public void adicionarEstoque(int quantidade) {
        if (quantidade > 0) {
            estoque += quantidade;
        }
    }

    protected String formatarMoeda(double valor) {
        NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatador.format(valor);
    }

    /*
     * Metodo abstrato exigido pelo professor.
     * Cada tipo de produto vai calcular o preco final de um jeito diferente.
     */
    public abstract double calcularPrecoFinal();

    public abstract String getTipo();
}
