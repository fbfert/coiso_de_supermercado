package checkout;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 * Responsavel pela persistencia de vendas, itens e descontos.
 */
public class VendaRepository implements RepositorioVenda {
    private static final Path DIRETORIO_DADOS = Paths.get("dados");
    private static final Path ARQUIVO_VENDAS = DIRETORIO_DADOS.resolve("vendas.txt");
    private static final Path ARQUIVO_ITENS = DIRETORIO_DADOS.resolve("itens_venda.txt");
    private static final Path ARQUIVO_DESCONTOS = DIRETORIO_DADOS.resolve("descontos.txt");
    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public VendaRepository() {
        garantirEstrutura();
    }

    private void garantirEstrutura() {
        try {
            Files.createDirectories(DIRETORIO_DADOS);
            if (Files.notExists(ARQUIVO_VENDAS)) {
                Files.createFile(ARQUIVO_VENDAS);
            }
            if (Files.notExists(ARQUIVO_ITENS)) {
                Files.createFile(ARQUIVO_ITENS);
            }
            if (Files.notExists(ARQUIVO_DESCONTOS)) {
                Files.createFile(ARQUIVO_DESCONTOS);
            }
        } catch (IOException e) {
            throw new RuntimeException("Nao foi possivel preparar a pasta dados.", e);
        }
    }

    public int gerarProximoIdVenda() {
        int maiorId = 0;
        try {
            List<String> linhas = Files.readAllLines(ARQUIVO_VENDAS, StandardCharsets.UTF_8);
            for (String linha : linhas) {
                if (linha == null || linha.trim().isEmpty()) {
                    continue;
                }

                String[] partes = linha.split(";", -1);
                if (partes.length > 0) {
                    try {
                        maiorId = Math.max(maiorId, Integer.parseInt(partes[0].trim()));
                    } catch (NumberFormatException e) {
                        System.out.println("Linha invalida em vendas.txt ao gerar o proximo ID: " + linha);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler vendas.txt.", e);
        }
        return maiorId + 1;
    }

    public void salvarVenda(Venda venda) {
        String linha = String.join(";",
                String.valueOf(venda.getIdVenda()),
                venda.getDataHora().format(FORMATO_DATA),
                formatarNumero(venda.getTotalBruto()),
                formatarNumero(venda.getTotalDescontos()),
                formatarNumero(venda.getTotalFinal()));

        try {
            Files.write(ARQUIVO_VENDAS, List.of(linha), StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
            System.out.println("Venda salva com sucesso no arquivo vendas.txt.");
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar venda.", e);
        }
    }

    public void salvarItensVenda(Venda venda) {
        List<String> linhas = new ArrayList<>();
        for (ItemCarrinho item : venda.getItens()) {
            linhas.add(String.join(";",
                    String.valueOf(venda.getIdVenda()),
                    item.getProduto().getCodigo(),
                    item.getProduto().getNome(),
                    String.valueOf(item.getQuantidade()),
                    formatarNumero(item.getProduto().getPrecoBase()),
                    formatarNumero(item.getSubtotalBruto()),
                    formatarNumero(item.getDescontoAplicado()),
                    formatarNumero(item.getSubtotalFinal())));
        }

        try {
            Files.write(ARQUIVO_ITENS, linhas, StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
            System.out.println("Itens da venda salvos no arquivo itens_venda.txt.");
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar itens da venda.", e);
        }
    }

    public void salvarDescontosVenda(Venda venda) {
        List<String> linhas = new ArrayList<>();
        for (ItemCarrinho item : venda.getItens()) {
            if (item.getDescontoAplicado() > 0) {
                linhas.add(String.join(";",
                        String.valueOf(venda.getIdVenda()),
                        item.getProduto().getCodigo(),
                        item.getProduto().getNome(),
                        item.getTipoDescontoAplicado(),
                        formatarNumero(item.getDescontoAplicado())));
            }
        }

        if (linhas.isEmpty()) {
            return;
        }

        try {
            Files.write(ARQUIVO_DESCONTOS, linhas, StandardCharsets.UTF_8, java.nio.file.StandardOpenOption.APPEND);
            System.out.println("Desconto registrado no arquivo descontos.txt.");
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar descontos.", e);
        }
    }

    public List<Venda> listarVendasSalvas() {
        List<Venda> vendas = new ArrayList<>();
        try {
            List<String> linhas = Files.readAllLines(ARQUIVO_VENDAS, StandardCharsets.UTF_8);
            for (String linha : linhas) {
                if (linha == null || linha.trim().isEmpty()) {
                    continue;
                }

                String[] partes = linha.split(";", -1);
                if (partes.length < 5) {
                    System.out.println("Linha invalida em vendas.txt: " + linha);
                    continue;
                }

                int idVenda = Integer.parseInt(partes[0].trim());
                LocalDateTime dataHora = LocalDateTime.parse(partes[1].trim(), FORMATO_DATA);
                double totalBruto = Double.parseDouble(partes[2].trim());
                double totalDescontos = Double.parseDouble(partes[3].trim());
                double totalFinal = Double.parseDouble(partes[4].trim());

                vendas.add(new Venda(idVenda, dataHora, List.of(), totalBruto, totalDescontos, totalFinal, ""));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler vendas.txt.", e);
        }
        return vendas;
    }

    private String formatarNumero(double valor) {
        return String.format(Locale.US, "%.2f", valor);
    }
}
