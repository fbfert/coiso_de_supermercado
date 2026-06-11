package checkout;

import java.io.IOException;
import java.text.NumberFormat;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;

/*
 * Lendo descontos.txt e exibindo um relatorio simples no terminal.
 */
public class RelatorioDescontosService {
    private static final Path DIRETORIO_DADOS = Paths.get("dados");
    private static final Path ARQUIVO_DESCONTOS = DIRETORIO_DADOS.resolve("descontos.txt");

    public RelatorioDescontosService() {
        garantirEstrutura();
    }

    private void garantirEstrutura() {
        try {
            Files.createDirectories(DIRETORIO_DADOS);
            if (Files.notExists(ARQUIVO_DESCONTOS)) {
                Files.createFile(ARQUIVO_DESCONTOS);
            }
        } catch (IOException e) {
            throw new RuntimeException("Nao foi possivel preparar o arquivo de descontos.", e);
        }
    }

    public void exibirRelatorio() {
        System.out.println();
        System.out.println("==============================================");
        System.out.println("RELATORIO DE DESCONTOS APLICADOS");
        System.out.println("==============================================");
        System.out.println("Lendo descontos.txt...");

        List<String> linhas;
        try {
            linhas = Files.readAllLines(ARQUIVO_DESCONTOS, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler descontos.txt.", e);
        }

        if (linhas.isEmpty()) {
            System.out.println("Nenhum desconto registrado ainda.");
            return;
        }

        System.out.printf("%-8s %-15s %-24s %-32s %-12s%n",
                "VENDA", "CODIGO", "PRODUTO", "TIPO DESCONTO", "VALOR");
        System.out.println("--------------------------------------------------------------------------------");

        double totalGeral = 0.0;

        for (String linha : linhas) {
            if (linha == null || linha.trim().isEmpty()) {
                continue;
            }

            String[] partes = linha.split(";", -1);
            if (partes.length < 5) {
                System.out.println("Linha invalida em descontos.txt: " + linha);
                continue;
            }

            String idVenda = partes[0].trim();
            String codigoProduto = partes[1].trim();
            String nomeProduto = partes[2].trim();
            String tipoDesconto = partes[3].trim();
            double valorDesconto = Double.parseDouble(partes[4].trim());

            totalGeral += valorDesconto;

            System.out.printf("%-8s %-15s %-24s %-32s %-12s%n",
                    idVenda,
                    codigoProduto,
                    nomeProduto,
                    tipoDesconto,
                    formatarMoeda(valorDesconto));
        }

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Total geral de descontos concedidos: " + formatarMoeda(totalGeral));
    }

    private String formatarMoeda(double valor) {
        NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatador.format(valor);
    }
}
