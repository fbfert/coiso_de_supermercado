package checkout;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/*
 * Menu principal do sistema.
 * Centraliza a interacao com o usuario e coordena os repositorios.
 */
public class MenuConsole {
    private static final DateTimeFormatter FORMATO_DATA_HORA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private RepositorioProduto produtoRepository;
    private RepositorioVenda vendaRepository;
    private RelatorioDescontosService relatorioDescontosService;
    private LeitorEntrada leitorEntrada;
    private ValidadorVenda validadorVenda;

    public MenuConsole() {
        this.produtoRepository = new ProdutoRepository();
        this.vendaRepository = new VendaRepository();
        this.relatorioDescontosService = new RelatorioDescontosService();
        this.leitorEntrada = new LeitorEntrada();
        this.validadorVenda = new ValidadorVenda();
    }

    public void executar() {
        boolean continuar = true;

        while (continuar) {
            exibirMenu();
            int opcao = leitorEntrada.lerInteiro("Escolha uma opcao: ");

            switch (opcao) {
                case 1:
                    listarProdutos();
                    break;
                case 2:
                    cadastrarProdutoPerecivel();
                    break;
                case 3:
                    cadastrarProdutoLimpeza();
                    break;
                case 4:
                    iniciarNovaVenda();
                    break;
                case 5:
                    listarVendas();
                    break;
                case 6:
                    relatorioDescontosService.exibirRelatorio();
                    break;
                case 7:
                    confirmarEGerarSeed();
                    break;
                case 8:
                    buscarProdutoPorNome();
                    break;
                case 0:
                    continuar = false;
                    System.out.println("Encerrando o sistema.");
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    private void exibirMenu() {
        System.out.println();
        System.out.println("==============================================");
        System.out.println("SISTEMA DE AUTOMACAO DE CHECKOUT DE SUPERMERCADO");
        System.out.println("==============================================");
        System.out.println("1 - Listar produtos cadastrados");
        System.out.println("2 - Cadastrar produto perecivel");
        System.out.println("3 - Cadastrar produto de limpeza");
        System.out.println("4 - Iniciar nova venda");
        System.out.println("5 - Listar vendas realizadas");
        System.out.println("6 - Ver relatorio de descontos aplicados");
        System.out.println("7 - Gerar dados de teste");
        System.out.println("8 - Buscar produto por nome");
        System.out.println("0 - Sair");
    }

    private void listarProdutos() {
        List<Produto> produtos = produtoRepository.carregarTodosProdutos();

        System.out.println();
        System.out.println("LISTAGEM DE PRODUTOS CADASTRADOS");
        if (produtos.isEmpty()) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        exibirTabelaProdutos(produtos);
    }

    private void buscarProdutoPorNome() {
        System.out.println();
        System.out.println("BUSCA DE PRODUTO POR NOME");
        System.out.println("Buscando produtos cadastrados...");
        String termoBusca = leitorEntrada.lerTexto("Digite parte do nome do produto: ");
        System.out.println("Termo digitado: " + termoBusca);
        System.out.println("Comparando o termo com os nomes dos produtos...");

        List<Produto> encontrados = produtoRepository.buscarPorNomeParcial(termoBusca);

        if (encontrados.isEmpty()) {
            System.out.println("Nenhum produto encontrado com esse termo.");
            return;
        }

        System.out.println("Produtos encontrados:");
        exibirTabelaProdutos(encontrados);
    }

    private void cadastrarProdutoPerecivel() {
        System.out.println();
        System.out.println("CADASTRO DE PRODUTO PERECIVEL");
        String codigo = leitorEntrada.lerTexto("Codigo: ");
        String nome = leitorEntrada.lerTexto("Nome: ");
        double precoBase = leitorEntrada.lerDouble("Preco base: ");
        int estoque = leitorEntrada.lerInteiro("Estoque: ");
        LocalDate dataVencimento = leitorEntrada.lerData("Data de vencimento (yyyy-MM-dd): ");

        Produto produto = new ProdutoPerecivel(codigo, nome, precoBase, estoque, dataVencimento);
        produtoRepository.salvarProduto(produto);
    }

    private void cadastrarProdutoLimpeza() {
        System.out.println();
        System.out.println("CADASTRO DE PRODUTO DE LIMPEZA");
        String codigo = leitorEntrada.lerTexto("Codigo: ");
        String nome = leitorEntrada.lerTexto("Nome: ");
        double precoBase = leitorEntrada.lerDouble("Preco base: ");
        int estoque = leitorEntrada.lerInteiro("Estoque: ");
        String fragrancia = leitorEntrada.lerTexto("Fragrancia: ");

        Produto produto = new ProdutoLimpeza(codigo, nome, precoBase, estoque, fragrancia);
        produtoRepository.salvarProduto(produto);
    }

    private void iniciarNovaVenda() {
        List<Produto> produtos = new ArrayList<>(produtoRepository.carregarTodosProdutos());

        if (produtos.isEmpty()) {
            System.out.println("Nao ha produtos cadastrados para iniciar uma venda.");
            return;
        }

        int idVenda = vendaRepository.gerarProximoIdVenda();
        CarrinhoDeCompras carrinho = new CarrinhoDeCompras();
        boolean continuarAdicionando = true;

        System.out.println();
        System.out.println("INICIANDO NOVA VENDA.");
        System.out.println("Informe os produtos pelo codigo.");
        System.out.println("Se precisar consultar antes, use a opcao 8 para buscar produtos pelo nome.");

        while (continuarAdicionando) {
            String codigo = leitorEntrada.lerTexto("Digite o codigo do produto, BUSCAR para pesquisar pelo nome ou 0 para finalizar: ");
            if ("0".equals(codigo)) {
                continuarAdicionando = false;
                break;
            }

            if ("BUSCAR".equalsIgnoreCase(codigo)) {
                buscarProdutosDentroDaVenda();
                continue;
            }

            try {
                Produto produto = validadorVenda.buscarProdutoOuFalhar(produtos, codigo);

                System.out.println("Produto encontrado. Criando objeto da subclasse correta...");
                System.out.println("Referencia base Produto apontando para objeto real " + produto.getClass().getSimpleName() + ".");

                int quantidade = leitorEntrada.lerInteiro("Quantidade: ");
                validadorVenda.validarQuantidade(produto, quantidade);

                produto.reduzirEstoque(quantidade);
                carrinho.adicionarItem(produto, quantidade);
            } catch (SistemaCheckoutException e) {
                System.out.println(e.getMessage());
            }
        }

        if (carrinho.getItens().isEmpty()) {
            System.out.println("Venda cancelada porque nenhum item foi adicionado.");
            return;
        }

        LocalDateTime dataHora = LocalDateTime.now();
        Venda venda = carrinho.fecharCompra(idVenda, dataHora);

        System.out.println();
        System.out.println(venda.getCupomFiscal());

        vendaRepository.salvarVenda(venda);
        vendaRepository.salvarItensVenda(venda);
        vendaRepository.salvarDescontosVenda(venda);
        produtoRepository.salvarTodosProdutos(produtos);
    }

    private void listarVendas() {
        List<Venda> vendas = vendaRepository.listarVendasSalvas();

        System.out.println();
        System.out.println("LISTAGEM DE VENDAS REALIZADAS");
        if (vendas.isEmpty()) {
            System.out.println("Nenhuma venda salva ainda.");
            return;
        }

        System.out.printf("%-8s %-18s %-15s %-15s %-15s%n",
                "ID", "DATA/HORA", "BRUTO", "DESCONTOS", "FINAL");

        for (Venda venda : vendas) {
            System.out.printf("%-8d %-18s %-15s %-15s %-15s%n",
                    venda.getIdVenda(),
                    venda.getDataHora().format(FORMATO_DATA_HORA),
                    formatarMoeda(venda.getTotalBruto()),
                    formatarMoeda(venda.getTotalDescontos()),
                    formatarMoeda(venda.getTotalFinal()));
        }
    }

    private void buscarProdutosDentroDaVenda() {
        System.out.println();
        System.out.println("BUSCA DE PRODUTO POR NOME");
        System.out.println("Buscando produtos cadastrados...");
        String termoBusca = leitorEntrada.lerTexto("Digite parte do nome do produto: ");
        System.out.println("Termo digitado: " + termoBusca);
        System.out.println("Comparando o termo com os nomes dos produtos...");

        List<Produto> encontrados = produtoRepository.buscarPorNomeParcial(termoBusca);

        if (encontrados.isEmpty()) {
            System.out.println("Nenhum produto encontrado com esse termo.");
            return;
        }

        System.out.println("Produtos encontrados:");
        exibirTabelaProdutos(encontrados);
    }

    private void exibirTabelaProdutos(List<Produto> produtos) {
        System.out.printf("%-8s %-12s %-24s %-14s %-10s %-15s %-15s%n",
                "CODIGO", "TIPO", "NOME", "PRECO BASE", "ESTOQUE", "VENCIMENTO", "FRAGRANCIA");

        for (Produto produto : produtos) {
            String vencimento = "";
            String fragrancia = "";

            if (produto instanceof ProdutoPerecivel) {
                vencimento = ((ProdutoPerecivel) produto).getDataVencimento().toString();
            }

            if (produto instanceof ProdutoLimpeza) {
                fragrancia = ((ProdutoLimpeza) produto).getFragrancia();
            }

            System.out.printf("%-8s %-12s %-24s %-14s %-10d %-15s %-15s%n",
                    produto.getCodigo(),
                    produto.getTipo(),
                    produto.getNome(),
                    formatarMoeda(produto.getPrecoBase()),
                    produto.getEstoque(),
                    vencimento,
                    fragrancia);
        }
    }

    private void confirmarEGerarSeed() {
        System.out.println();
        System.out.println("ATENCAO: esta operacao vai substituir os dados atuais dos arquivos TXT.");
        String resposta = leitorEntrada.lerTexto("Deseja realmente gerar os dados de teste? S/N: ");

        if ("S".equalsIgnoreCase(resposta.trim())) {
            SeedDados.executar();
        } else {
            System.out.println("Geracao de dados de teste cancelada.");
        }
    }

    private String formatarMoeda(double valor) {
        NumberFormat formatador = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        return formatador.format(valor);
    }
}
