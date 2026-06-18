# Requisitos do Sistema

## Requisitos de ambiente

- Java Development Kit (JDK) 17 ou superior
- Terminal ou prompt de comando
- Sistema operacional capaz de executar aplicações Java

## Requisitos do projeto

- Compilador `javac`
- Comando `java` disponível no PATH
- Acesso de leitura e escrita na pasta `dados/`

## Requisitos funcionais

- Cadastrar produtos perecíveis
- Cadastrar produtos de limpeza
- Listar produtos cadastrados
- Buscar produtos por nome parcial
- Iniciar uma nova venda
- Adicionar itens ao carrinho
- Aplicar regras de desconto por categoria
- Emitir cupom fiscal da venda
- Registrar vendas e descontos em arquivos TXT
- Exibir relatório de descontos

## Requisitos não funcionais

- O sistema deve ser executado via terminal
- A persistência deve ser feita em arquivos TXT, sem banco de dados
- O projeto deve ser compilado sem dependências externas
- O comportamento deve permanecer didático e simples para apresentação acadêmica

## Como executar

Compilação:

```bash
javac -d out src/checkout/*.java
```

Execução do sistema principal:

```bash
java -cp out checkout.Main
```

Execução dos testes de caixa-preta:

```bash
java -cp out checkout.RoteiroTestesEstresse
```
