# Sistema de Automacao de Checkout de Supermercado

## Objetivo
Este projeto implementa um mini backend de caixa de supermercado em Java, com menu no terminal e persistencia simples em arquivos TXT.

Ele foi mantido didatico para demonstrar os conceitos pedidos pelo professor:
- classe abstrata
- heranca
- polimorfismo
- colecoes `List`
- sobrescrita de metodos
- regras de desconto por categoria
- carrinho de compras
- cupom fiscal

## Estrutura geral
O sistema agora funciona como um fluxo interativo:
1. lista produtos cadastrados
2. cadastra produto perecivel
3. cadastra produto de limpeza
4. inicia uma nova venda
5. lista vendas salvas
6. mostra relatorio de descontos
7. gera dados ficticios de teste
8. busca produtos por parte do nome
0. encerra o sistema

## Classe abstrata `Produto`
A classe `Produto` continua sendo a base de todo o modelo.

Ela define:
- `codigo`
- `nome`
- `precoBase`
- `estoque`
- o metodo abstrato `calcularPrecoFinal()`
- o metodo abstrato `getTipo()`

As subclasses herdam essa estrutura e implementam o comportamento especifico de cada categoria.

## Heranca e sobrescrita
As subclasses principais sao:
- `ProdutoPerecivel`
- `ProdutoLimpeza`

Cada uma sobrescreve `calcularPrecoFinal()` para aplicar suas regras e manter o exemplo de polimorfismo visivel no terminal.

## Polimorfismo
O polimorfismo aparece quando o sistema trabalha com referencias do tipo `Produto`, mas o objeto real pode ser `ProdutoPerecivel` ou `ProdutoLimpeza`.

Exemplos de mensagens exibidas:
- `Referencia base Produto apontando para objeto real ProdutoPerecivel.`
- `Chamando calcularPrecoFinal() de forma polimorfica...`

## Descontos
As regras de desconto continuam separadas por categoria:

### Produto perecivel
Se faltam menos de 3 dias para vencer, o sistema aplica 30% de desconto.

### Produto de limpeza
O desconto progressivo depende da quantidade:
- 3 a 5 unidades: 5%
- 6 a 9 unidades: 10%
- 10 ou mais unidades: 15%

Os descontos sao registrados em `dados/descontos.txt`.

## Persistencia em TXT
Os arquivos ficam na pasta `dados/`.

Arquivos usados:
- `dados/produtos.txt`
- `dados/vendas.txt`
- `dados/itens_venda.txt`
- `dados/descontos.txt`

Se a pasta ou os arquivos nao existirem, o sistema cria automaticamente.

## Geracao de dados de teste
A opcao `7 - Gerar dados de teste` sobrescreve os arquivos TXT com uma carga ficticia para demonstracao.

Ela cria:
- 100 produtos
- 30 vendas
- 150 itens de venda
- 20 registros de descontos

Essa carga e util para testar:
- listagem de produtos
- vendas salvas
- itens vendidos
- relatorio de descontos

Antes de gerar, o sistema pede confirmacao no menu:
`Deseja realmente gerar os dados de teste? S/N`

### Formato de `produtos.txt`
`codigo;tipo;nome;precoBase;estoque;dataVencimento;fragrancia`

Exemplos:
```text
001;PERECIVEL;Iogurte Natural;8.50;20;2026-06-13;
002;LIMPEZA;Detergente Neutro;4.99;50;;Limao
```

### Formato de `vendas.txt`
`idVenda;dataHora;totalBruto;totalDescontos;totalFinal`

Exemplo:
```text
1;2026-06-11 13:40;173.30;7.89;165.41
```

### Formato de `itens_venda.txt`
`idVenda;codigoProduto;nomeProduto;quantidade;precoBase;subtotalBruto;descontoAplicado;subtotalFinal`

### Formato de `descontos.txt`
`idVenda;codigoProduto;nomeProduto;tipoDesconto;valorDesconto`

## Classes novas
Foram adicionadas as classes:
- `ProdutoRepository`
- `VendaRepository`
- `RelatorioDescontosService`
- `MenuConsole`
- `LeitorEntrada`
- `Venda`

### `ProdutoRepository`
Responsavel por:
- salvar produto
- carregar todos os produtos
- buscar produto por codigo
- buscar produto por nome parcial
- salvar a lista atualizada de produtos

## Busca por nome parcial
A opcao `8 - Buscar produto por nome` permite localizar produtos cadastrados digitando apenas uma parte do nome.

Exemplos:
- `det` encontra `Detergente Neutro`
- `leite` encontra `Leite Integral`
- `limp` encontra `Limpador Multiuso`

A busca ignora maiusculas e minusculas.

Durante a venda, o sistema tambem aceita o comando `BUSCAR` no lugar do codigo do produto para consultar a lista pelo nome e depois voltar ao fluxo normal.

### `VendaRepository`
Responsavel por:
- gerar o proximo ID da venda
- salvar venda
- salvar itens da venda
- salvar descontos da venda
- listar vendas salvas

### `RelatorioDescontosService`
Le o arquivo `descontos.txt` e mostra:
- descontos aplicados por venda
- produto com desconto
- tipo de desconto
- valor do desconto
- total geral concedido

### `MenuConsole`
Centraliza a interface do terminal e organiza as operacoes do sistema.

### `LeitorEntrada`
Facilita a leitura e validacao de texto, inteiro, decimal e data.

### `Venda`
Representa a venda fechada com:
- idVenda
- dataHora
- `List<ItemCarrinho>`
- totalBruto
- totalDescontos
- totalFinal

## Como executar no VSCode
1. Abra a pasta do projeto no VSCode.
2. Use o JDK configurado em `C:\Program Files\Eclipse Adoptium\jdk-17.0.19.10-hotspot`.
3. Execute a tarefa `Compilar e executar checkout` com `Ctrl+Shift+B`.
4. Ou rode pelo depurador usando a configuracao `Executar Checkout`.

Tambem e possivel compilar e executar manualmente no terminal:
```bash
javac -d out src/checkout/*.java
java -cp out checkout.Main
```

### Atalho de terminal
No PowerShell do Windows foi criado o comando `programa`.

Ele faz automaticamente:
- compila o projeto
- executa a aplicacao principal

Uso:
```powershell
programa
```

Se o comando nao estiver disponivel em uma janela nova, recarregue o perfil do PowerShell ou abra um novo terminal.

## Exemplo de execucao
```text
==============================================
SISTEMA DE AUTOMACAO DE CHECKOUT DE SUPERMERCADO
==============================================
1 - Listar produtos cadastrados
2 - Cadastrar produto perecivel
3 - Cadastrar produto de limpeza
4 - Iniciar nova venda
5 - Listar vendas realizadas
6 - Ver relatorio de descontos aplicados
7 - Gerar dados de teste
8 - Buscar produto por nome
0 - Sair
```

## Exemplo de cupom fiscal
```text
=========== CUPOM FISCAL ===========
Venda: 1 | Data: 2026-06-11T13:40
PRODUTO            QTD      BRUTO           FINAL
------------------------------------
Iogurte Natural    2        R$ 17,00        R$ 11,90
Detergente Neutro  4        R$ 19,96        R$ 18,96
------------------------------------
TOTAL BRUTO                 R$ 36,96
DESCONTOS                   R$ 6,10
TOTAL FINAL                 R$ 30,86
====================================
```

## Observacoes
O projeto continua simples e didatico, sem banco de dados e sem framework, para ficar adequado a uma apresentacao de POO com persistencia basica em arquivo.
