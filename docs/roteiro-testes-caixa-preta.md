# Roteiro de Testes de Caixa-Preta

## Objetivo
Validar o comportamento do sistema a partir da entrada e da saida observadas no console, sem depender da estrutura interna das classes.

## Cenarios
1. Tentar vender um produto inexistente e verificar `ProdutoNaoEncontradoException`.
2. Tentar vender quantidade zero ou negativa e verificar `QuantidadeInvalidaException`.
3. Tentar vender quantidade maior que o estoque e verificar `EstoqueInsuficienteException`.
4. Executar uma venda valida com `ProdutoPerecivel` e `ProdutoLimpeza` para observar polimorfismo e descontos.

## Comandos
```bash
javac -d out src/checkout/*.java
java -cp out checkout.RoteiroTestesEstresse
```

## Evidencias
O aluno deve tirar prints do terminal mostrando as excecoes aparecendo e a mensagem `PASSOU` nos cenarios corretos.


# Roteiro de Testes de Caixa-Preta

## Sistema de Checkout de Supermercado

Este documento apresenta o roteiro de testes de caixa-preta desenvolvido para validar o funcionamento do sistema de checkout de supermercado.

O objetivo dos testes é verificar se o sistema responde corretamente a cenários de falha e de sucesso, sem exigir análise interna do código durante a execução. O foco está no comportamento observado no terminal: entrada fornecida, resultado esperado e resposta do sistema.

Os testes foram organizados em uma classe específica chamada `RoteiroTestesEstresse`, com método `main` próprio, para separar o fluxo normal do sistema dos testes de validação. Assim, o programa principal continua sendo executado pela classe `Main`, enquanto os testes podem ser executados separadamente.

## Comando para compilar o projeto

Antes da execução dos testes, o projeto deve ser compilado com o seguinte comando:

```bash
javac -d out src/checkout/*.java
```

Resultado esperado:

```text
Compilação concluída sem erros.
```

## Comando para executar o sistema principal

Para executar o menu principal do sistema:

```bash
java -cp out checkout.Main
```

Resultado esperado:

```text
O sistema deve abrir o menu principal do checkout e permitir a interação normal com o usuário.
```

## Comando para executar o roteiro de testes

Para executar os testes de caixa-preta:

```bash
java -cp out checkout.RoteiroTestesEstresse
```

Resultado esperado:

```text
O terminal deve exibir os testes numerados, indicando o cenário testado, o resultado esperado e se o teste passou ou falhou.
```

---

# Testes Realizados

## TESTE 1 — Produto inexistente

### Objetivo

Verificar se o sistema impede a venda de um produto que não existe na lista de produtos cadastrados.

### Entrada simulada

```text
Código de produto inexistente.
```

### Resultado esperado

```text
ProdutoNaoEncontradoException
```

### Justificativa

Esse teste valida se o sistema consegue identificar que o produto solicitado não está cadastrado. A venda não deve continuar quando o produto não existe, pois isso geraria uma inconsistência no carrinho e nos registros da venda.

### Resultado obtido

```text
PASSOU
```

O sistema lançou a exceção customizada `ProdutoNaoEncontradoException` e continuou funcionando normalmente.

---

## TESTE 2 — Quantidade inválida

### Objetivo

Verificar se o sistema impede a venda quando a quantidade informada é zero ou negativa.

### Entrada simulada

```text
Quantidade: 0 ou quantidade negativa.
```

### Resultado esperado

```text
QuantidadeInvalidaException
```

### Justificativa

Uma venda só pode ser registrada com quantidade positiva. Quantidades iguais a zero ou negativas não representam uma compra válida e devem ser bloqueadas antes de o produto ser adicionado ao carrinho.

### Resultado obtido

```text
PASSOU
```

O sistema lançou a exceção customizada `QuantidadeInvalidaException` e impediu o prosseguimento da venda inválida.

---

## TESTE 3 — Estoque insuficiente

### Objetivo

Verificar se o sistema impede a venda de uma quantidade maior do que o estoque disponível.

### Entrada simulada

```text
Produto com estoque disponível menor do que a quantidade solicitada.
```

Exemplo:

```text
Estoque disponível: 4 unidades
Quantidade solicitada: 10 unidades
```

### Resultado esperado

```text
EstoqueInsuficienteException
```

### Justificativa

O sistema não pode permitir que uma venda seja concluída com quantidade superior ao estoque disponível. Essa regra é essencial para manter a consistência do controle de estoque.

### Resultado obtido

```text
PASSOU
```

O sistema lançou a exceção customizada `EstoqueInsuficienteException` e bloqueou a venda inválida.

---

## TESTE 4 — Venda válida com polimorfismo e descontos

### Objetivo

Verificar se o sistema realiza corretamente uma venda válida contendo produtos de tipos diferentes.

### Entrada simulada

```text
ProdutoPerecivel válido.
ProdutoLimpeza válido.
Quantidade dentro do estoque disponível.
```

### Resultado esperado

```text
Venda concluída com sucesso.
Cálculo de subtotal realizado.
Descontos aplicados quando cabíveis.
Demonstração de polimorfismo no método calcularPrecoFinal().
```

### Justificativa

Esse teste comprova que o sistema funciona corretamente em um cenário válido. Também demonstra o uso de polimorfismo, pois o carrinho trabalha com referências do tipo `Produto`, mas executa o método `calcularPrecoFinal()` conforme o tipo real do objeto: `ProdutoPerecivel` ou `ProdutoLimpeza`.

### Resultado obtido

```text
PASSOU
```

O sistema executou a venda corretamente, calculou os subtotais e demonstrou o comportamento polimórfico das subclasses de `Produto`.

---

# Evidências no Terminal

Durante a execução do roteiro de testes, o terminal deve mostrar mensagens semelhantes a:

```text
TESTE 1
ESPERADO: ProdutoNaoEncontradoException
RESULTADO: ProdutoNaoEncontradoException
PASSOU

TESTE 2
ESPERADO: QuantidadeInvalidaException
RESULTADO: QuantidadeInvalidaException
PASSOU

TESTE 3
ESPERADO: EstoqueInsuficienteException
RESULTADO: EstoqueInsuficienteException
PASSOU

TESTE 4
ESPERADO: Venda válida com ProdutoPerecivel e ProdutoLimpeza
RESULTADO: Venda concluída com sucesso
PASSOU
```

Essas mensagens devem ser capturadas por meio de prints do terminal e anexadas ao relatório ou entrega final.

---

# Conclusão

Os testes de caixa-preta demonstram que o sistema trata corretamente entradas inválidas e mantém o funcionamento estável diante de falhas esperadas.

Foram testados cenários de produto inexistente, quantidade inválida, estoque insuficiente e venda válida. As exceções customizadas permitiram que o sistema identificasse erros de regra de negócio de forma clara, sem quebrar a execução.

Com isso, o projeto atende ao requisito de robustez, tratamento de erros e validação por meio de testes de caixa-preta.
