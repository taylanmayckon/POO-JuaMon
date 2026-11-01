package jogo;

import javax.swing.SwingUtilities;

/*
 * ITEM 9 - PACOTES
 *
 * O projeto JuaMon está organizado em exatamente dois pacotes:
 *
 * jogo   -> contém interface, mapa, controle e execução do jogo.
 * modelo -> contém entidades e tipos pertencentes ao domínio.
 *
 * O projeto deve permanecer com exatamente esses dois pacotes.
 */
public class JuaMon {

    /*
     * ÚNICO MÉTODO MAIN DO PROJETO.
     *
     * O método main é responsável somente por iniciar
     * a aplicação.
     *
     * ATENÇÃO:
     * Este método NÃO está sendo utilizado para atender
     * ao requisito de método static do professor, pois
     * esse requisito exige outro método static além do main.
     */
    public static void main(String[] args) {

        /*
         * SwingUtilities.invokeLater garante que a interface
         * gráfica seja criada na thread apropriada do Swing.
         */
        SwingUtilities.invokeLater(() -> {

            JanelaJogo janela = new JanelaJogo();

            janela.setVisible(true);
        });
    }
}
