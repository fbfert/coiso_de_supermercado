package checkout;

import java.time.LocalDate;
import java.util.Scanner;

/*
 * Leitura simples e repetitiva de dados no terminal.
 * A classe evita repeticao de tratamento de erro nas opcoes do menu.
 */
public class LeitorEntrada {
    private Scanner scanner;

    public LeitorEntrada() {
        this.scanner = new Scanner(System.in);
    }

    public String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    public int lerInteiro(String mensagem) {
        while (true) {
            try {
                String texto = lerTexto(mensagem);
                return Integer.parseInt(texto);
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Digite um numero inteiro.");
            }
        }
    }

    public double lerDouble(String mensagem) {
        while (true) {
            try {
                String texto = lerTexto(mensagem).replace(",", ".");
                return Double.parseDouble(texto);
            } catch (NumberFormatException e) {
                System.out.println("Valor invalido. Digite um numero decimal valido.");
            }
        }
    }

    public LocalDate lerData(String mensagem) {
        while (true) {
            try {
                String texto = lerTexto(mensagem);
                return LocalDate.parse(texto);
            } catch (Exception e) {
                System.out.println("Data invalida. Use o formato yyyy-MM-dd.");
            }
        }
    }
}
