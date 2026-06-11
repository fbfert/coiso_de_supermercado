package checkout;

/*
 * Classe principal do sistema.
 * Agora o fluxo inicial abre o menu interativo do mini backend.
 */
public class Main {
    public static void main(String[] args) {
        MenuConsole menu = new MenuConsole();
        menu.executar();
    }
}
