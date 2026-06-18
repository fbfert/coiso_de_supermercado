package checkout;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/*
 * Responsavel pela persistencia simples de produtos em arquivo TXT.
 */
public class ProdutoRepository implements RepositorioProduto {
    private static final Path DIRETORIO_DADOS = Paths.get("dados");
    private static final Path ARQUIVO_PRODUTOS = DIRETORIO_DADOS.resolve("produtos.txt");

    public ProdutoRepository() {
        garantirEstrutura();
    }

    private void garantirEstrutura() {
        try {
            Files.createDirectories(DIRETORIO_DADOS);
            if (Files.notExists(ARQUIVO_PRODUTOS)) {
                Files.createFile(ARQUIVO_PRODUTOS);
            }
        } catch (IOException e) {
            throw new RuntimeException("Nao foi possivel preparar a pasta dados.", e);
        }
    }

    public void salvarProduto(Produto produto) {
        System.out.println("Salvando produto no arquivo produtos.txt...");
        List<Produto> produtos = carregarTodosProdutos();

        boolean substituiu = false;
        for (int i = 0; i < produtos.size(); i++) {
            if (produtos.get(i).getCodigo().equalsIgnoreCase(produto.getCodigo())) {
                produtos.set(i, produto);
                substituiu = true;
                break;
            }
        }

        if (!substituiu) {
            produtos.add(produto);
        }

        salvarTodosProdutos(produtos);
        System.out.println("Produto salvo com sucesso no arquivo produtos.txt.");
    }

    public void salvarTodosProdutos(List<Produto> produtos) {
        List<String> linhas = new ArrayList<>();
        for (Produto produto : produtos) {
            linhas.add(formatarLinhaProduto(produto));
        }

        try {
            Files.write(ARQUIVO_PRODUTOS, linhas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar produtos.", e);
        }
    }

    public List<Produto> carregarTodosProdutos() {
        System.out.println("Carregando produtos cadastrados do arquivo produtos.txt...");
        garantirEstrutura();

        List<Produto> produtos = new ArrayList<>();
        List<String> linhas;

        try {
            linhas = Files.readAllLines(ARQUIVO_PRODUTOS, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler produtos.txt.", e);
        }

        for (String linha : linhas) {
            if (linha == null || linha.trim().isEmpty()) {
                continue;
            }

            Produto produto = criarProdutoDaLinha(linha);
            if (produto != null) {
                produtos.add(produto);
            }
        }

        return produtos;
    }

    public Produto buscarProdutoPorCodigo(String codigo) {
        for (Produto produto : carregarTodosProdutos()) {
            if (produto.getCodigo().equalsIgnoreCase(codigo)) {
                return produto;
            }
        }
        return null;
    }

    public List<Produto> buscarPorNomeParcial(String termoBusca) {
        List<Produto> resultados = new ArrayList<>();

        if (termoBusca == null || termoBusca.trim().isEmpty()) {
            return resultados;
        }

        String termoNormalizado = termoBusca.trim().toLowerCase();
        List<Produto> produtos = carregarTodosProdutos();

        for (Produto produto : produtos) {
            String nomeNormalizado = produto.getNome().toLowerCase();

            if (nomeNormalizado.contains(termoNormalizado)) {
                resultados.add(produto);
            }
        }

        return resultados;
    }

    private Produto criarProdutoDaLinha(String linha) {
        try {
            String[] partes = linha.split(";", -1);
            if (partes.length < 7) {
                System.out.println("Linha invalida em produtos.txt: " + linha);
                return null;
            }

            String codigo = partes[0].trim();
            String tipo = partes[1].trim();
            String nome = partes[2].trim();
            double precoBase = Double.parseDouble(partes[3].trim());
            int estoque = Integer.parseInt(partes[4].trim());
            String dataVencimento = partes[5].trim();
            String fragrancia = partes[6].trim();

            if ("PERECIVEL".equalsIgnoreCase(tipo)) {
                return new ProdutoPerecivel(codigo, nome, precoBase, estoque, LocalDate.parse(dataVencimento));
            }

            if ("LIMPEZA".equalsIgnoreCase(tipo)) {
                return new ProdutoLimpeza(codigo, nome, precoBase, estoque, fragrancia);
            }

            System.out.println("Tipo de produto desconhecido na linha: " + linha);
            return null;
        } catch (Exception e) {
            System.out.println("Linha invalida em produtos.txt: " + linha);
            return null;
        }
    }

    private String formatarLinhaProduto(Produto produto) {
        String dataVencimento = "";
        String fragrancia = "";

        if (produto instanceof ProdutoPerecivel) {
            dataVencimento = ((ProdutoPerecivel) produto).getDataVencimento().toString();
        }

        if (produto instanceof ProdutoLimpeza) {
            fragrancia = ((ProdutoLimpeza) produto).getFragrancia();
        }

        return String.join(";",
                produto.getCodigo(),
                produto.getTipo(),
                produto.getNome(),
                String.format(java.util.Locale.US, "%.2f", produto.getPrecoBase()),
                String.valueOf(produto.getEstoque()),
                dataVencimento,
                fragrancia);
    }
}
