package jogo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import modelo.CalculadoraDano;
import modelo.Direcao;
import modelo.Jogador;
import modelo.PokemonSelvagem;


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

    // Controla a cadência de passos para a troca suave dos sprites.
    private int contadorPassosAnimacao = 0;


    // Controle contínuo das teclas pressionadas (WASD e Setas) para eliminar o delay do teclado.
    private boolean teclaCima = false;
    private boolean teclaBaixo = false;
    private boolean teclaEsquerda = false;
    private boolean teclaDireita = false;

    // Velocidade de deslocamento equilibrada em pixels por ciclo (tick).
    private static final int VELOCIDADE_PASSO = 2;

    // Rastreia o último tile de grama para evitar sorteios repetidos no mesmo bloco.
    private int ultimoTileGramaX = -1;
    private int ultimoTileGramaY = -1;


    private Jogador jogador;

    private Mapa mapa;

    private Random random = new Random();

    private boolean encontroPermitido = true;


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
     * e teclado.
     */
    public PainelMapa() {

        // Carrega a imagem do mapa.
        ImageIcon imagem = new ImageIcon(
                getClass().getResource(
                        "/assets/map/mapa_floresta.png"
                )
        );

        imagemMapa = imagem.getImage();


        // -------------------------
        // SPRITES - FRENTE
        // -------------------------

        jogadorFrente1 = new ImageIcon(
                getClass().getResource(
                        "/assets/player/jogador_frente_1.png"
                )
        ).getImage();

        jogadorFrente2 = new ImageIcon(
                getClass().getResource(
                        "/assets/player/jogador_frente_2.png"
                )
        ).getImage();


        // -------------------------
        // SPRITES - COSTAS
        // -------------------------

        jogadorCostas1 = new ImageIcon(
                getClass().getResource(
                        "/assets/player/jogador_costas_1.png"
                )
        ).getImage();

        jogadorCostas2 = new ImageIcon(
                getClass().getResource(
                        "/assets/player/jogador_costas_2.png"
                )
        ).getImage();


        // -------------------------
        // SPRITES - ESQUERDA
        // -------------------------

        jogadorEsquerda1 = new ImageIcon(
                getClass().getResource(
                        "/assets/player/jogador_esquerda_1.png"
                )
        ).getImage();

        jogadorEsquerda2 = new ImageIcon(
                getClass().getResource(
                        "/assets/player/jogador_esquerda_2.png"
                )
        ).getImage();


        // -------------------------
        // SPRITES - DIREITA
        // -------------------------

        jogadorDireita1 = new ImageIcon(
                getClass().getResource(
                        "/assets/player/jogador_direita_1.png"
                )
        ).getImage();

        jogadorDireita2 = new ImageIcon(
                getClass().getResource(
                        "/assets/player/jogador_direita_2.png"
                )
        ).getImage();


        // Cria o jogador inicialmente em região livre.
        //jogador = new Jogador(200, 180); //Desativei pois agora vou implementar o salvar/carregar
        
        /*
        * ITEM 13 - RECUPERAÇÃO E USO DO ARQUIVO
        *
        * Ao iniciar o mapa, o programa tenta recuperar
        * os dados salvos anteriormente.
        */
        jogador = GerenciadorArquivo.carregarJogador();


        /*
        * Caso ainda não exista arquivo de save,
        * cria um novo jogador normalmente.
        */
        if (jogador == null) {

            jogador = new Jogador(
                200,
                180
            );
        }
        

        // Cria o mapa lógico utilizado nas colisões.
        /*
        * 4.1. COMPOSIÇÃO:
        *
        * O Todo (PainelMapa) instancia a Parte (Mapa) em seu construtor
        */
        mapa = new Mapa(); // O Todo (PainelMapa) instancia a Parte (Mapa) em seu construtor


        // Permite que o painel receba comandos do teclado.
        setFocusable(true);

        addKeyListener(this);


        // Inicia a trilha sonora de exploracao em loop continuo
        GerenciadorAudio.iniciarMusicaEmLoop(GerenciadorAudio.MUSICA_EXPLORACAO, -8.0f);


        /*
         * Loop contínuo de atualização gráfica e física do mapa (50 FPS).
         * Elimina completamente o atraso inicial de resposta do sistema operacional.
         */
        Timer timerMovimento = new Timer(20, e -> processarMovimentacaoContinua());
        timerMovimento.start();
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
     * Processa a física de movimento, checagem de colisão e
     * cadência de animação a cada ciclo do loop do jogo.
     */
    private void processarMovimentacaoContinua() {

        int deslocamentoX = 0;
        int deslocamentoY = 0;
        boolean andando = false;


        // Leitura contínua das direções ativas
        if (teclaCima) {

            jogador.setDirecao(Direcao.CIMA);
            deslocamentoY -= VELOCIDADE_PASSO;
            andando = true;

        } else if (teclaBaixo) {

            jogador.setDirecao(Direcao.BAIXO);
            deslocamentoY += VELOCIDADE_PASSO;
            andando = true;

        } else if (teclaEsquerda) {

            jogador.setDirecao(Direcao.ESQUERDA);
            deslocamentoX -= VELOCIDADE_PASSO;
            andando = true;

        } else if (teclaDireita) {

            jogador.setDirecao(Direcao.DIREITA);
            deslocamentoX += VELOCIDADE_PASSO;
            andando = true;
        }


        if (andando) {

            int novoX = jogador.getX() + deslocamentoX;
            int novoY = jogador.getY() + deslocamentoY;


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


                // Alterna os quadros do passo com cadência calibrada ao novo ritmo (~180ms)
                contadorPassosAnimacao++;

                if (contadorPassosAnimacao >= 9) {

                    frameAtual = (frameAtual == 1) ? 2 : 1;
                    contadorPassosAnimacao = 0;
                }
            }


            /*
             * Verifica se o personagem entrou
             * em uma região de grama alta.
             */
            if (mapa.ehGrama(
                    jogador.getX(),
                    jogador.getY())) {

                int tileAtualX = jogador.getX() / Mapa.TAMANHO_TILE;
                int tileAtualY = jogador.getY() / Mapa.TAMANHO_TILE;

                // Só verifica o sorteio se ingressou em uma nova célula da grade
                if (tileAtualX != ultimoTileGramaX || tileAtualY != ultimoTileGramaY) {

                    ultimoTileGramaX = tileAtualX;
                    ultimoTileGramaY = tileAtualY;

                    System.out.println(
                            "Jogador esta na grama alta."
                    );

                    verificarEncontroAleatorio();
                }

            } else {

                encontroPermitido = true;
                ultimoTileGramaX = -1;
                ultimoTileGramaY = -1;
            }


            atualizarCamera();

            repaint();

        } else {

            // Quando parado, mantém o personagem com postura neutra
            frameAtual = 1;
            contadorPassosAnimacao = 0;
        }
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

            // Limpa as teclas ativas para não continuar caminhando dentro do diálogo
            teclaCima = false;
            teclaBaixo = false;
            teclaEsquerda = false;
            teclaDireita = false;

            // Sorteia um dos 10 Pokémons da pool
            PokemonSelvagem selvagem = CalculadoraDano.sortearPokemonSelvagem();

            // Abre a TelaBatalha modal 800x600 bloqueando o mapa
            JFrame janelaPai = (JFrame) SwingUtilities.getWindowAncestor(this);
            TelaBatalha telaBatalha = new TelaBatalha(janelaPai, jogador, selvagem);
            telaBatalha.setVisible(true);

            // Ao fechar a batalha, retoma a musica de exploracao e restaura o foco no teclado
            GerenciadorAudio.iniciarMusicaEmLoop(GerenciadorAudio.MUSICA_EXPLORACAO, -8.0f);
            this.requestFocusInWindow();
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

        int codigo = e.getKeyCode();
        
        /*
        * ITEM 13 - GRAVAÇÃO DO ARQUIVO
        *
        * Ao pressionar F5, os dados atuais do jogador
        * são gravados no arquivo de save.
        */
        if (codigo == KeyEvent.VK_F5) {

            GerenciadorArquivo.salvarJogador(
                jogador
            );

            System.out.println(
                "Posicao salva: "
                + jogador.getX()
                + ", "
                + jogador.getY()
            );

            return;
        }


        // Suporte a Setas e teclas WASD
        if (codigo == KeyEvent.VK_UP || codigo == KeyEvent.VK_W) {

            teclaCima = true;
        }

        if (codigo == KeyEvent.VK_DOWN || codigo == KeyEvent.VK_S) {

            teclaBaixo = true;
        }

        if (codigo == KeyEvent.VK_LEFT || codigo == KeyEvent.VK_A) {

            teclaEsquerda = true;
        }

        if (codigo == KeyEvent.VK_RIGHT || codigo == KeyEvent.VK_D) {

            teclaDireita = true;
        }
    }


    @Override
    public void keyReleased(KeyEvent e) {

        int codigo = e.getKeyCode();


        // Desativa a direção ao soltar a tecla correspondente
        if (codigo == KeyEvent.VK_UP || codigo == KeyEvent.VK_W) {

            teclaCima = false;
        }

        if (codigo == KeyEvent.VK_DOWN || codigo == KeyEvent.VK_S) {

            teclaBaixo = false;
        }

        if (codigo == KeyEvent.VK_LEFT || codigo == KeyEvent.VK_A) {

            teclaEsquerda = false;
        }

        if (codigo == KeyEvent.VK_RIGHT || codigo == KeyEvent.VK_D) {

            teclaDireita = false;
        }
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