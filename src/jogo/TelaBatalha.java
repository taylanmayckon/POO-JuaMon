package jogo;

import javax.swing.JOptionPane;

public class TelaBatalha {

    /*
     * Tela provisória utilizada enquanto o sistema
     * completo de batalha ainda está sendo integrado.
     */
    public void iniciarBatalha() {

        /*
         * ATENÇÃO:
         *
         * showMessageDialog apenas mostra uma informação.
         *
         * Portanto, este trecho NÃO está sendo utilizado
         * para atender ao requisito de entrada por caixa
         * de diálogo do professor.
         *
         * O requisito será atendido posteriormente com
         * showInputDialog no login, cadastro ou escolha
         * do JuaMon inicial.
         */
        JOptionPane.showMessageDialog(
                null,
                "Um JuaMon selvagem apareceu!",
                "Batalha",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
