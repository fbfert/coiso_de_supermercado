package checkout;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*
 * Gera uma carga inicial ficticia para teste do sistema.
 * A saida e deterministica para manter consistencia entre os arquivos TXT.
 */
public class SeedDados {
    private static final Path DIRETORIO_DADOS = Paths.get("dados");
    private static final Path ARQUIVO_PRODUTOS = DIRETORIO_DADOS.resolve("produtos.txt");
    private static final Path ARQUIVO_VENDAS = DIRETORIO_DADOS.resolve("vendas.txt");
    private static final Path ARQUIVO_ITENS_VENDA = DIRETORIO_DADOS.resolve("itens_venda.txt");
    private static final Path ARQUIVO_DESCONTOS = DIRETORIO_DADOS.resolve("descontos.txt");
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final String[] NOMES_PERECIVEIS = {
            "Iogurte Natural", "Queijo Minas", "Leite Integral", "Presunto Fatiado", "Pao de Forma",
            "Suco de Laranja", "Manteiga Cremosa", "Requeijao", "Ovos Caipiras", "Iogurte Grego"
    };
    private static final String[] NOMES_LIMPEZA = {
            "Detergente Neutro", "Sabao em Po", "Desinfetante", "Agua Sanitaria", "Amaciante",
            "Limpador Multiuso", "Esponja Multiuso", "Lustra Moveis", "Sabonete Liquido", "Alcool Gel"
    };
    private static final String[] FRAGRANCIAS = {
            "Limao", "Lavanda", "Eucalipto", "Tradicional", "Floral",
            "Citronela", "Baunilha", "Menta", "Algodao", "Aloe Vera"
    };

    private SeedDados() {
    }

    public static void executar() {
        System.out.println("Gerando carga inicial de dados...");

        garantirEstrutura();

        System.out.println("Criando 100 produtos...");
        List<ProdutoSeed> produtos = gerarProdutos();
        escreverArquivo(ARQUIVO_PRODUTOS, gerarLinhasProdutos(produtos));

        System.out.println("Criando 30 vendas...");
        System.out.println("Criando 150 itens de venda...");
        System.out.println("Criando 20 descontos aplicados...");
        GeracaoVendas resultado = gerarVendas(produtos);
        escreverArquivo(ARQUIVO_VENDAS, resultado.linhasVendas);
        escreverArquivo(ARQUIVO_ITENS_VENDA, resultado.linhasItensVenda);
        escreverArquivo(ARQUIVO_DESCONTOS, resultado.linhasDescontos);

        System.out.println("Arquivos TXT atualizados com sucesso.");
        System.out.println("Seed finalizado.");
    }

    private static void garantirEstrutura() {
        try {
            Files.createDirectories(DIRETORIO_DADOS);
        } catch (IOException e) {
            throw new RuntimeException("Nao foi possivel criar a pasta dados.", e);
        }
    }

    private static List<ProdutoSeed> gerarProdutos() {
        List<ProdutoSeed> produtos = new ArrayList<>();

        for (int i = 1; i <= 50; i++) {
            String codigo = String.format(Locale.US, "P%03d", i);
            String nome = NOMES_PERECIVEIS[(i - 1) % NOMES_PERECIVEIS.length] + " " + String.format(Locale.US, "%02d", i);
            BigDecimal precoBase = gerarPrecoBase(i);
            int estoque = gerarEstoque(i);
            LocalDate vencimentoBase = LocalDate.of(2026, 6, 11);
            LocalDate vencimento = (i <= 10)
                    ? vencimentoBase.plusDays(1 + ((i - 1) % 2))
                    : vencimentoBase.plusDays(5 + ((i - 11) % 20));

            produtos.add(new ProdutoSeed(codigo, "PERECIVEL", nome, precoBase, estoque, vencimento.toString(), ""));
        }

        for (int i = 51; i <= 100; i++) {
            int indiceLimpeza = i - 50;
            String codigo = String.format(Locale.US, "P%03d", i);
            String nome = NOMES_LIMPEZA[(indiceLimpeza - 1) % NOMES_LIMPEZA.length] + " " + String.format(Locale.US, "%02d", indiceLimpeza);
            BigDecimal precoBase = gerarPrecoBase(i);
            int estoque = gerarEstoque(i);
            String fragrancia = FRAGRANCIAS[(indiceLimpeza - 1) % FRAGRANCIAS.length];

            produtos.add(new ProdutoSeed(codigo, "LIMPEZA", nome, precoBase, estoque, "", fragrancia));
        }

        return produtos;
    }

    private static List<String> gerarLinhasProdutos(List<ProdutoSeed> produtos) {
        List<String> linhas = new ArrayList<>();
        for (ProdutoSeed produto : produtos) {
            linhas.add(produto.codigo + ";" + produto.tipo + ";" + produto.nome + ";" + formatar(produto.precoBase) + ";" + produto.estoque + ";" + produto.dataVencimento + ";" + produto.fragrancia);
        }
        return linhas;
    }

    private static GeracaoVendas gerarVendas(List<ProdutoSeed> produtos) {
        Map<String, ProdutoSeed> mapaProdutos = new HashMap<>();
        for (ProdutoSeed produto : produtos) {
            mapaProdutos.put(produto.codigo, produto);
        }

        List<String> linhasVendas = new ArrayList<>();
        List<String> linhasItens = new ArrayList<>();
        List<String> linhasDescontos = new ArrayList<>();

        for (int idVenda = 1; idVenda <= 30; idVenda++) {
            LocalDateTime dataHora = LocalDateTime.of(2026, 6, 1, 8, 0)
                    .plusDays(idVenda - 1L)
                    .plusHours((idVenda * 3L) % 10L)
                    .plusMinutes((idVenda * 7L) % 60L);

            List<ItemCalculado> itensDaVenda = new ArrayList<>();

            for (int posicao = 1; posicao <= 5; posicao++) {
                ItemCalculado item = criarItemDaVenda(idVenda, posicao, mapaProdutos);
                itensDaVenda.add(item);
                linhasItens.add(item.gerarLinha(idVenda));
                if (item.descontoAplicado.compareTo(BigDecimal.ZERO) > 0) {
                    linhasDescontos.add(item.gerarLinhaDesconto(idVenda));
                }
            }

            BigDecimal totalBruto = BigDecimal.ZERO;
            BigDecimal totalDescontos = BigDecimal.ZERO;
            BigDecimal totalFinal = BigDecimal.ZERO;

            for (ItemCalculado item : itensDaVenda) {
                totalBruto = totalBruto.add(item.subtotalBruto);
                totalDescontos = totalDescontos.add(item.descontoAplicado);
                totalFinal = totalFinal.add(item.subtotalFinal);
            }

            linhasVendas.add(String.join(";",
                    String.valueOf(idVenda),
                    dataHora.format(FORMATO_DATA_HORA),
                    formatar(totalBruto),
                    formatar(totalDescontos),
                    formatar(totalFinal)));
        }

        return new GeracaoVendas(linhasVendas, linhasItens, linhasDescontos);
    }

    private static ItemCalculado criarItemDaVenda(int idVenda, int posicao, Map<String, ProdutoSeed> mapaProdutos) {
        String codigoProduto;
        int quantidade;
        BigDecimal descontoAplicado = BigDecimal.ZERO;
        String tipoDesconto = "";

        if (posicao == 1) {
            if (idVenda <= 10) {
                codigoProduto = String.format(Locale.US, "P%03d", idVenda);
                quantidade = 2;
                ProdutoSeed produto = mapaProdutos.get(codigoProduto);
                BigDecimal subtotalBruto = produto.precoBase.multiply(BigDecimal.valueOf(quantidade));
                descontoAplicado = subtotalBruto.multiply(BigDecimal.valueOf(0.30));
                tipoDesconto = "PERECIVEL_PROXIMO_VENCIMENTO";
                return montarItem(produto, quantidade, descontoAplicado, tipoDesconto);
            }

            if (idVenda <= 20) {
                codigoProduto = String.format(Locale.US, "P%03d", 50 + (idVenda - 10));
                quantidade = quantidadeLimpezaComDesconto(idVenda);
                ProdutoSeed produto = mapaProdutos.get(codigoProduto);
                BigDecimal subtotalBruto = produto.precoBase.multiply(BigDecimal.valueOf(quantidade));
                descontoAplicado = subtotalBruto.multiply(BigDecimal.valueOf(percentualLimpeza(quantidade)));
                tipoDesconto = "LIMPEZA_LOTE";
                return montarItem(produto, quantidade, descontoAplicado, tipoDesconto);
            }

            codigoProduto = String.format(Locale.US, "P%03d", 10 + ((idVenda - 21) % 40) + 1);
            ProdutoSeed produto = mapaProdutos.get(codigoProduto);
            quantidade = 1;
            return montarItem(produto, quantidade, BigDecimal.ZERO, "");
        }

        if (posicao == 2) {
            codigoProduto = String.format(Locale.US, "P%03d", 10 + ((idVenda - 1) % 40) + 1);
            ProdutoSeed produto = mapaProdutos.get(codigoProduto);
            quantidade = 1;
            return montarItem(produto, quantidade, BigDecimal.ZERO, "");
        }

        if (posicao == 3) {
            codigoProduto = String.format(Locale.US, "P%03d", 60 + ((idVenda - 1) % 20) + 1);
            ProdutoSeed produto = mapaProdutos.get(codigoProduto);
            quantidade = 1;
            return montarItem(produto, quantidade, BigDecimal.ZERO, "");
        }

        if (posicao == 4) {
            codigoProduto = String.format(Locale.US, "P%03d", 20 + ((idVenda - 1) % 30) + 1);
            ProdutoSeed produto = mapaProdutos.get(codigoProduto);
            quantidade = 2;
            return montarItem(produto, quantidade, BigDecimal.ZERO, "");
        }

        codigoProduto = String.format(Locale.US, "P%03d", 70 + ((idVenda - 1) % 20) + 1);
        ProdutoSeed produto = mapaProdutos.get(codigoProduto);
        quantidade = 2;
        return montarItem(produto, quantidade, BigDecimal.ZERO, "");
    }

    private static ItemCalculado montarItem(ProdutoSeed produto, int quantidade, BigDecimal descontoAplicado, String tipoDesconto) {
        BigDecimal subtotalBruto = produto.precoBase.multiply(BigDecimal.valueOf(quantidade));
        descontoAplicado = descontoAplicado.setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotalFinal = subtotalBruto.subtract(descontoAplicado).setScale(2, RoundingMode.HALF_UP);

        return new ItemCalculado(
                produto.codigo,
                produto.nome,
                quantidade,
                produto.precoBase,
                subtotalBruto.setScale(2, RoundingMode.HALF_UP),
                descontoAplicado,
                subtotalFinal,
                tipoDesconto);
    }

    private static int quantidadeLimpezaComDesconto(int idVenda) {
        int[] quantidades = {3, 4, 5, 6, 7, 8, 10, 11, 12, 9};
        return quantidades[idVenda - 11];
    }

    private static double percentualLimpeza(int quantidade) {
        if (quantidade >= 3 && quantidade <= 5) {
            return 0.05;
        }
        if (quantidade >= 6 && quantidade <= 9) {
            return 0.10;
        }
        return 0.15;
    }

    private static BigDecimal gerarPrecoBase(int indice) {
        int centavos = 250 + ((indice * 73) % 9741);
        return BigDecimal.valueOf(centavos).movePointLeft(2).setScale(2, RoundingMode.HALF_UP);
    }

    private static int gerarEstoque(int indice) {
        return 10 + ((indice * 19) % 191);
    }

    private static String formatar(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    private static void escreverArquivo(Path arquivo, List<String> linhas) {
        try {
            Files.write(arquivo, linhas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao escrever o arquivo " + arquivo.getFileName(), e);
        }
    }

    private static class ProdutoSeed {
        private final String codigo;
        private final String tipo;
        private final String nome;
        private final BigDecimal precoBase;
        private final int estoque;
        private final String dataVencimento;
        private final String fragrancia;

        private ProdutoSeed(String codigo, String tipo, String nome, BigDecimal precoBase, int estoque, String dataVencimento, String fragrancia) {
            this.codigo = codigo;
            this.tipo = tipo;
            this.nome = nome;
            this.precoBase = precoBase;
            this.estoque = estoque;
            this.dataVencimento = dataVencimento;
            this.fragrancia = fragrancia;
        }
    }

    private static class ItemCalculado {
        private final String codigoProduto;
        private final String nomeProduto;
        private final int quantidade;
        private final BigDecimal precoBase;
        private final BigDecimal subtotalBruto;
        private final BigDecimal descontoAplicado;
        private final BigDecimal subtotalFinal;
        private final String tipoDesconto;

        private ItemCalculado(String codigoProduto, String nomeProduto, int quantidade, BigDecimal precoBase, BigDecimal subtotalBruto, BigDecimal descontoAplicado, BigDecimal subtotalFinal, String tipoDesconto) {
            this.codigoProduto = codigoProduto;
            this.nomeProduto = nomeProduto;
            this.quantidade = quantidade;
            this.precoBase = precoBase;
            this.subtotalBruto = subtotalBruto;
            this.descontoAplicado = descontoAplicado;
            this.subtotalFinal = subtotalFinal;
            this.tipoDesconto = tipoDesconto;
        }

        private String gerarLinha(int idVenda) {
            return String.join(";",
                    String.valueOf(idVenda),
                    codigoProduto,
                    nomeProduto,
                    String.valueOf(quantidade),
                    formatar(precoBase),
                    formatar(subtotalBruto),
                    formatar(descontoAplicado),
                    formatar(subtotalFinal));
        }

        private String gerarLinhaDesconto(int idVenda) {
            return String.join(";",
                    String.valueOf(idVenda),
                    codigoProduto,
                    nomeProduto,
                    tipoDesconto,
                    formatar(descontoAplicado));
        }
    }

    private static class GeracaoVendas {
        private final List<String> linhasVendas;
        private final List<String> linhasItensVenda;
        private final List<String> linhasDescontos;

        private GeracaoVendas(List<String> linhasVendas, List<String> linhasItensVenda, List<String> linhasDescontos) {
            this.linhasVendas = linhasVendas;
            this.linhasItensVenda = linhasItensVenda;
            this.linhasDescontos = linhasDescontos;
        }
    }
}
