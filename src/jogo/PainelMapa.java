package jogo;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import modelo.Direcao;
import modelo.Jogador;


/*
 * PainelMapa implementa KeyListener da biblioteca Swing
 * para receber os comandos do teclado.
 *
 * ATENÇÃO:
 * Esta implementação NÃO está sendo considerada como
 * atendimento completo ao requisito de interfaces do professor.
 *
 * O requisito de interfaces será implementado com
 * interfaces próprias do domínio do JuaMon.
 */
public class PainelMapa extends JPanel implements KeyListener {


    /*
     * ITEM 1.1 - ENCAPSULAMENTO
     *
     * Todos os atributos não estáticos da classe
     * são privados.
     *
     * O estado do mapa, sprites, jogador, câmera
     * e encontros é controlado internamente.
     */
    private Image imagemMapa;

    private Image jogadorFrente1;
    private Image jogadorFrente2;

    private Image jogadorCostas1;
    private Image jogadorCostas2;

    private Image jogadorEsquerda1;
    private Image jogadorEsquerda2;

    private Image jogadorDireita1;
    private Image jogadorDireita2;


    // Controla o quadro atual da animação.
    private int frameAtual = 1;


    private Jogador jogador;

    private Mapa mapa;

    private Random random = new Random();

    private boolean encontroPermitido = true;

    private TelaBatalha telaBatalha;


    // Posição atual da câmera.
    private int cameraX = 0;
    private int cameraY = 0;


    // Tamanho visual do sprite.
    private static final int TAMANHO_SPRITE = 32;

    // Tamanho utilizado para calcular colisões.
    private static final int TAMANHO_COLISAO = 20;


    /*
     * ITEM 3 - CONSTRUTOR EM CLASSE CONCRETA
     *
     * O construtor configura todos os elementos
     * necessários para iniciar a exploração:
     *
     * mapa,
     * sprites,
     * jogador,
     * batalha provisória
     * e teclado.
     */
    public PainelMapa() {

        // Carrega a imagem do mapa.
        ImageIcon imagem = new ImageIcon(
                getClass().getResource(
                        "/imagens/mapa_floresta.png"
                )
        );

        imagemMapa = imagem.getImage();


        // -------------------------
        // SPRITES - FRENTE
        // -------------------------

        jogadorFrente1 = new ImageIcon(
                getClass().getResource(
                        "/imagens/jogador_frente_1.png"
                )
        ).getImage();

        jogadorFrente2 = new ImageIcon(
                getClass().getResource(
                        "/imagens/jogador_frente_2.png"
                )
        ).getImage();


        // -------------------------
        // SPRITES - COSTAS
        // -------------------------

        jogadorCostas1 = new ImageIcon(
                getClass().getResource(
                        "/imagens/jogador_costas_1.png"
                )
        ).getImage();

        jogadorCostas2 = new ImageIcon(
                getClass().getResource(
                        "/imagens/jogador_costas_2.png"
                )
        ).getImage();


        // -------------------------
        // SPRITES - ESQUERDA
        // -------------------------

        jogadorEsquerda1 = new ImageIcon(
                getClass().getResource(
                        "/imagens/jogador_esquerda_1.png"
                )
        ).getImage();

        jogadorEsquerda2 = new ImageIcon(
                getClass().getResource(
                        "/imagens/jogador_esquerda_2.png"
                )
        ).getImage();


        // -------------------------
        // SPRITES - DIREITA
        // -------------------------

        jogadorDireita1 = new ImageIcon(
                getClass().getResource(
                        "/imagens/jogador_direita_1.png"
                )
        ).getImage();

        jogadorDireita2 = new ImageIcon(
                getClass().getResource(
                        "/imagens/jogador_direita_2.png"
                )
        ).getImage();


        // Cria o jogador inicialmente em região livre.
        jogador = new Jogador(200, 180);


        // Cria o mapa lógico utilizado nas colisões.
        /*
        * 4.1. COMPOSIÇÃO:
        *
        * O Todo (PainelMapa) instancia a Parte (Mapa) em seu construtor
        */
        mapa = new Mapa(); // O Todo (PainelMapa) instancia a Parte (Mapa) em seu construtor


        // Tela provisória de batalha.
        telaBatalha = new TelaBatalha();


        // Permite que o painel receba comandos do teclado.
        setFocusable(true);

        addKeyListener(this);
    }


    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);


        // Desenha o mapa considerando a posição da câmera.
        g.drawImage(
                imagemMapa,
                -cameraX,
                -cameraY,
                this
        );


        // Desenha o personagem.
        desenharJogador(g);


        /*
         * Grade utilizada somente durante o desenvolvimento.
         *
         * Para visualizar novamente, basta remover "//".
         */
        // desenharGrade(g);
    }


    /*
     * Realiza o sorteio do encontro aleatório.
     */
    private void verificarEncontroAleatorio() {

        if (!encontroPermitido) {

            return;
        }


        // Número aleatório entre 0 e 99.
        int numero = random.nextInt(100);


        // Aproximadamente 15% de chance.
        if (numero < 15) {

            encontroPermitido = false;

            System.out.println(
                    "Um JuaMon selvagem apareceu!"
            );

            telaBatalha.iniciarBatalha();
        }
    }


    /*
     * Desenha a grade utilizada para auxiliar
     * no mapeamento das colisões.
     */
    private void desenharGrade(Graphics g) {

        g.setColor(Color.BLACK);


        int larguraMapa = imagemMapa.getWidth(this);
        int alturaMapa = imagemMapa.getHeight(this);


        /*
         * ITEM 1.5 - USO DA CONSTANTE DE OUTRA CLASSE
         *
         * PainelMapa utiliza Mapa.TAMANHO_TILE.
         *
         * Isso garante que a grade visual utilize
         * exatamente o mesmo tamanho das células
         * utilizadas pela lógica do mapa.
         */


        // Linhas verticais.
        for (int x = 0;
                x <= larguraMapa;
                x += Mapa.TAMANHO_TILE) {

            int xTela = x - cameraX;

            g.drawLine(
                    xTela,
                    -cameraY,
                    xTela,
                    alturaMapa - cameraY
            );
        }


        // Linhas horizontais.
        for (int y = 0;
                y <= alturaMapa;
                y += Mapa.TAMANHO_TILE) {

            int yTela = y - cameraY;

            g.drawLine(
                    -cameraX,
                    yTela,
                    larguraMapa - cameraX,
                    yTela
            );
        }


        int linhas =
                alturaMapa / Mapa.TAMANHO_TILE + 1;

        int colunas =
                larguraMapa / Mapa.TAMANHO_TILE + 1;


        // Identifica linha, coluna e tipo de terreno.
        for (int linha = 0;
                linha < linhas;
                linha++) {

            for (int coluna = 0;
                    coluna < colunas;
                    coluna++) {

                int xTela =
                        coluna * Mapa.TAMANHO_TILE
                        - cameraX
                        + 5;

                int yTela =
                        linha * Mapa.TAMANHO_TILE
                        - cameraY
                        + 15;


                int tipo =
                        mapa.getTipoTerreno(
                                linha,
                                coluna
                        );


                String texto =
                        linha
                        + ","
                        + coluna
                        + " ["
                        + tipo
                        + "]";


                g.drawString(
                        texto,
                        xTela,
                        yTela
                );
            }
        }
    }


    /*
     * Escolhe e desenha o sprite de acordo
     * com a direção e o quadro da animação.
     */
    private void desenharJogador(Graphics g) {

        Image imagemAtual;


        switch (jogador.getDirecao()) {

            case CIMA:

                if (frameAtual == 1) {

                    imagemAtual = jogadorCostas1;

                } else {

                    imagemAtual = jogadorCostas2;
                }

                break;


            case BAIXO:

                if (frameAtual == 1) {

                    imagemAtual = jogadorFrente1;

                } else {

                    imagemAtual = jogadorFrente2;
                }

                break;


            case ESQUERDA:

                if (frameAtual == 1) {

                    imagemAtual = jogadorEsquerda1;

                } else {

                    imagemAtual = jogadorEsquerda2;
                }

                break;


            case DIREITA:

                if (frameAtual == 1) {

                    imagemAtual = jogadorDireita1;

                } else {

                    imagemAtual = jogadorDireita2;
                }

                break;


            default:

                imagemAtual = jogadorFrente1;

                break;
        }


        /*
         * Converte a posição do mundo para
         * posição visual considerando a câmera.
         */
        g.drawImage(
                imagemAtual,
                jogador.getX() - cameraX,
                jogador.getY() - cameraY,
                TAMANHO_SPRITE,
                TAMANHO_SPRITE,
                this
        );
    }


    @Override
    public void keyPressed(KeyEvent e) {

        int velocidade = 10;

        int deslocamentoX = 0;
        int deslocamentoY = 0;

        boolean tentouMover = false;


        // -------------------------
        // CIMA
        // -------------------------

        if (e.getKeyCode() == KeyEvent.VK_UP) {

            jogador.setDirecao(
                    Direcao.CIMA
            );

            deslocamentoY = -velocidade;

            tentouMover = true;
        }


        // -------------------------
        // BAIXO
        // -------------------------

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {

            jogador.setDirecao(
                    Direcao.BAIXO
            );

            deslocamentoY = velocidade;

            tentouMover = true;
        }


        // -------------------------
        // ESQUERDA
        // -------------------------

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {

            jogador.setDirecao(
                    Direcao.ESQUERDA
            );

            deslocamentoX = -velocidade;

            tentouMover = true;
        }


        // -------------------------
        // DIREITA
        // -------------------------

        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

            jogador.setDirecao(
                    Direcao.DIREITA
            );

            deslocamentoX = velocidade;

            tentouMover = true;
        }


        // Ignora outras teclas.
        if (!tentouMover) {

            return;
        }


        /*
         * Calcula a posição que o jogador
         * pretende ocupar.
         */
        int novoX =
                jogador.getX()
                + deslocamentoX;

        int novoY =
                jogador.getY()
                + deslocamentoY;


        /*
         * Consulta Mapa antes de realizar
         * o movimento.
         */
        if (mapa.podeMoverPara(
                novoX,
                novoY,
                TAMANHO_COLISAO,
                TAMANHO_COLISAO)) {


            jogador.mover(
                    deslocamentoX,
                    deslocamentoY
            );


            // Alterna os quadros da caminhada.
            if (frameAtual == 1) {

                frameAtual = 2;

            } else {

                frameAtual = 1;
            }
        }


        /*
         * Verifica se o personagem entrou
         * em uma região de grama alta.
         */
        if (mapa.ehGrama(
                jogador.getX(),
                jogador.getY())) {


            System.out.println(
                    "Jogador está na grama alta."
            );


            verificarEncontroAleatorio();

        } else {

            encontroPermitido = true;
        }


        atualizarCamera();


        // Solicita novo desenho da tela.
        repaint();
    }


    @Override
    public void keyReleased(KeyEvent e) {

        /*
         * Quando a tecla é solta,
         * retorna ao primeiro quadro.
         */
        frameAtual = 1;

        repaint();
    }


    @Override
    public void keyTyped(KeyEvent e) {

        // Não utilizado.
    }


    /*
     * Atualiza a câmera para acompanhar
     * a posição do jogador.
     */
    private void atualizarCamera() {

        cameraX =
                jogador.getX()
                - getWidth() / 2;

        cameraY =
                jogador.getY()
                - getHeight() / 2;


        // Impede a câmera de ultrapassar o início.
        if (cameraX < 0) {

            cameraX = 0;
        }

        if (cameraY < 0) {

            cameraY = 0;
        }


        int larguraMapa =
                imagemMapa.getWidth(this);

        int alturaMapa =
                imagemMapa.getHeight(this);


        int maxCameraX =
                larguraMapa - getWidth();

        int maxCameraY =
                alturaMapa - getHeight();


        // Impede a câmera de ultrapassar o final.
        if (cameraX > maxCameraX) {

            cameraX =
                    Math.max(
                            0,
                            maxCameraX
                    );
        }


        if (cameraY > maxCameraY) {

            cameraY =
                    Math.max(
                            0,
                            maxCameraY
                    );
        }
    }
}
