package jogo;

import javax.swing.JFrame;

public class JanelaJogo extends JFrame {

    /*
     * ITEM 3 - CONSTRUTOR EM CLASSE CONCRETA
     *
     * JanelaJogo é uma classe concreta e possui um construtor
     * declarado explicitamente.
     *
     * O construtor é necessário para configurar a janela
     * principal e inserir nela o PainelMapa.
     */
    public JanelaJogo() {

        // Define o nome exibido na barra superior.
        setTitle("JuaMon");

        // Define o tamanho inicial da janela.
        setSize(800, 600);

        // Encerra o programa quando a janela é fechada.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Impede alteração manual do tamanho da janela.
        setResizable(false);

        /*
         * Cria o painel responsável pela exploração,
         * mapa, personagem e câmera.
         */
        PainelMapa painelMapa = new PainelMapa();

        // Adiciona o painel à janela.
        add(painelMapa);

        // Centraliza a janela no monitor.
        setLocationRelativeTo(null);
    }
}
