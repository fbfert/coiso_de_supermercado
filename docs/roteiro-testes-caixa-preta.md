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
