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

## Decisões de Design e Tratamento de Erros
1. `Produto` foi definida como classe abstrata porque existe um comportamento comum a todas as categorias, mas o preco final e o tipo do produto dependem da subclasse. Uma interface nao guardaria estado compartilhado como `codigo`, `nome`, `precoBase` e `estoque`.
2. Em `ItemCarrinho.calcularSubtotal()`, o sistema trabalha com uma referencia do tipo `Produto`, mas o objeto real pode ser `ProdutoPerecivel` ou `ProdutoLimpeza`. Quando `calcularPrecoFinal()` e chamado, o Java usa o metodo da subclasse concreta, e isso permite aplicar regras diferentes sem mudar o carrinho.
3. `EstoqueInsuficienteException` e disparada quando a quantidade solicitada e maior que o estoque do produto. Ela e necessaria porque a regra de negocio nao permite vender mais unidades do que existem, e o tratamento por excecao evita seguir o fluxo com um estado invalido.

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

## Decisões de Design e Tratamento de Erros

Esta seção apresenta as principais decisões de design adotadas no desenvolvimento do sistema de checkout de supermercado, especialmente em relação ao uso de classe abstrata, polimorfismo e exceções customizadas.

### 1. Por que a classe `Produto` foi definida como abstrata e não como interface?

A classe `Produto` foi definida como abstrata porque representa uma base comum para todos os produtos do sistema. Todo produto, independentemente de sua categoria, possui informações compartilhadas, como código, nome, preço base e estoque.

Esses dados são atributos internos do objeto e precisam ser reaproveitados pelas classes filhas. Por isso, uma classe abstrata é mais adequada do que uma interface nesse caso. Uma interface serviria principalmente para definir métodos obrigatórios, mas não seria a melhor escolha para centralizar atributos e comportamentos comuns.

No projeto, `Produto` possui atributos privados, métodos públicos de acesso e métodos comuns, como controle de estoque e formatação de moeda. Ao mesmo tempo, ela mantém métodos abstratos, como `calcularPrecoFinal()` e `getTipo()`, obrigando cada subclasse a implementar sua própria regra.

As classes `ProdutoPerecivel` e `ProdutoLimpeza` herdam de `Produto`, mas cada uma calcula o preço final de maneira diferente. Assim, a classe abstrata permite reaproveitamento de código e, ao mesmo tempo, garante especialização nas subclasses.

### 2. Como funciona o polimorfismo no método `ItemCarrinho.calcularSubtotal()`?

O polimorfismo aparece no método `calcularSubtotal()` da classe `ItemCarrinho`.

A classe `ItemCarrinho` possui um atributo do tipo `Produto`. Isso significa que ela trabalha com a referência genérica da classe abstrata, sem precisar saber inicialmente se o objeto real é um `ProdutoPerecivel` ou um `ProdutoLimpeza`.

Durante a execução, o método chama:

```java
produto.calcularPrecoFinal();
```

Mesmo a variável sendo do tipo `Produto`, o Java identifica em tempo de execução qual é a classe real do objeto. Se o produto adicionado ao carrinho for um `ProdutoPerecivel`, será executado o método `calcularPrecoFinal()` da classe `ProdutoPerecivel`. Se for um `ProdutoLimpeza`, será executado o método sobrescrito em `ProdutoLimpeza`.

Esse comportamento demonstra polimorfismo porque o mesmo comando gera comportamentos diferentes, dependendo do tipo real do objeto.

O fluxo pode ser explicado assim:

1. O usuário adiciona um produto ao carrinho.
2. O carrinho cria um `ItemCarrinho` com uma referência do tipo `Produto`.
3. Ao calcular o subtotal, `ItemCarrinho` chama `produto.calcularPrecoFinal()`.
4. Em tempo de execução, o Java decide qual versão do método será executada.
5. O preço final é calculado conforme a regra específica da subclasse.

Com isso, o carrinho não precisa conhecer todos os detalhes das categorias de produto. Ele apenas chama o método definido na classe abstrata, e cada subclasse responde de acordo com sua própria regra de negócio.

### 3. Cenário de teste que dispara a exceção customizada `EstoqueInsuficienteException`

A exceção customizada `EstoqueInsuficienteException` é disparada quando o usuário tenta vender uma quantidade maior do que o estoque disponível de determinado produto.

Exemplo de cenário:

* Produto selecionado: `Detergente Neutro`
* Estoque disponível: `4 unidades`
* Quantidade solicitada na venda: `10 unidades`
* Resultado esperado: disparo da exceção `EstoqueInsuficienteException`

Esse erro representa uma regra de negócio importante. Um sistema de supermercado não pode permitir que uma venda seja concluída com quantidade superior ao estoque disponível.

Por isso, foi criada uma exceção customizada específica para esse caso. Essa exceção deixa o erro mais claro, melhora a organização do código e evita que o sistema trate todos os problemas como erros genéricos.

No fluxo da venda, a validação é feita antes de o produto ser adicionado ao carrinho. Quando a quantidade informada é maior do que o estoque, o sistema lança a exceção customizada. Em seguida, o `MenuConsole` captura essa exceção por meio da classe base `SistemaCheckoutException`, exibe uma mensagem ao usuário e permite que o sistema continue funcionando normalmente.

Esse tratamento é importante porque demonstra robustez. O sistema não quebra diante de uma entrada inválida, mas também não permite que uma venda incorreta seja registrada.

Além da `EstoqueInsuficienteException`, o projeto também possui outras exceções customizadas, como `ProdutoNaoEncontradoException` e `QuantidadeInvalidaException`, usadas para tratar outros cenários de falha no fluxo de venda.

Dessa forma, o tratamento de erros foi implementado de maneira orientada a objetos, separando as regras de validação da interface do menu e tornando o código mais organizado, legível e seguro.
