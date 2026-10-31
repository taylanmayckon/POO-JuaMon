package jogo;

public class Mapa {

    /*
     * Tipos utilizados pelo mapa lógico.
     *
     * 0 = região livre
     * 1 = obstáculo
     * 2 = grama alta
     */
    public static final int LIVRE = 0;
    public static final int OBSTACULO = 1;
    public static final int GRAMA = 2;


    /*
     * ITEM 1.5 - CONSTANTE DE TIPO PRIMITIVO
     *
     * TAMANHO_TILE é uma constante inteira definida
     * diretamente nesta classe concreta.
     *
     * Ela é necessária para converter posições em pixels
     * para células do mapa lógico.
     *
     * A constante também é utilizada pela classe PainelMapa
     * para desenhar a grade com exatamente o mesmo tamanho
     * utilizado pela lógica de colisões.
     */
    public static final int TAMANHO_TILE = 20;


    // Dimensões da matriz lógica.
    private static final int TOTAL_LINHAS = 57;
    private static final int TOTAL_COLUNAS = 44;


    /*
     * ITEM 1.1 - ENCAPSULAMENTO
     *
     * A matriz é privada.
     *
     * Nenhuma outra classe pode modificar diretamente
     * o conteúdo do mapa.
     *
     * Alterações e consultas são realizadas através
     * dos métodos disponibilizados pela classe Mapa.
     */
    private int[][] mapaLogico;


    /*
     * ITEM 3 - CONSTRUTOR EM CLASSE CONCRETA
     *
     * Mapa é uma classe concreta e possui construtor
     * explicitamente declarado.
     *
     * O construtor cria a matriz e configura as áreas
     * de obstáculos e grama.
     */
    public Mapa() {

        /*
         * Matrizes de int começam automaticamente preenchidas
         * com zero.
         *
         * Como LIVRE = 0, todas as regiões começam livres.
         */
        mapaLogico = new int[TOTAL_LINHAS][TOTAL_COLUNAS];

        configurarObstaculos();

        configurarGrama();
    }


    /*
     * Configura todas as áreas que o jogador
     * não poderá atravessar.
     */
    private void configurarObstaculos() {

        preencherRegiao(0, 56, 41, 43, OBSTACULO);
        preencherRegiao(51, 56, 0, 43, OBSTACULO);
        preencherRegiao(0, 56, 0, 2, OBSTACULO);
        preencherRegiao(15, 20, 36, 40, OBSTACULO);
        preencherRegiao(0, 4, 0, 43, OBSTACULO);
        preencherRegiao(38, 44, 36, 40, OBSTACULO);
        preencherRegiao(38, 44, 14, 20, OBSTACULO);
        preencherRegiao(45, 46, 23, 24, OBSTACULO);
        preencherRegiao(36, 44, 3, 9, OBSTACULO);
        preencherRegiao(28, 31, 3, 14, OBSTACULO);
        preencherRegiao(30, 34, 15, 26, OBSTACULO);
        preencherRegiao(17, 44, 27, 30, OBSTACULO);
        preencherRegiao(22, 29, 20, 26, OBSTACULO);
        preencherRegiao(22, 25, 15, 19, OBSTACULO);
        preencherRegiao(20, 21, 15, 16, OBSTACULO);
        preencherRegiao(20, 20, 13, 14, OBSTACULO);
        preencherRegiao(10, 19, 13, 14, OBSTACULO);
        preencherRegiao(5, 8, 3, 8, OBSTACULO);
        preencherRegiao(5, 18, 20, 21, OBSTACULO);
        preencherRegiao(7, 12, 26, 37, OBSTACULO);
        preencherRegiao(25, 34, 36, 37, OBSTACULO);
        preencherRegiao(10, 21, 7, 8, OBSTACULO);
    }


    /*
     * Configura as regiões de grama alta.
     *
     * Quando o jogador passa por essas áreas,
     * poderá ocorrer um encontro aleatório.
     */
    private void configurarGrama() {

        preencherRegiao(12, 23, 3, 6, GRAMA);
        preencherRegiao(11, 23, 9, 12, GRAMA);
        preencherRegiao(22, 23, 13, 14, GRAMA);
        preencherRegiao(11, 18, 16, 19, GRAMA);
        preencherRegiao(11, 17, 22, 25, GRAMA);
        preencherRegiao(18, 18, 24, 25, GRAMA);
        preencherRegiao(13, 18, 26, 26, GRAMA);
        preencherRegiao(13, 16, 27, 30, GRAMA);

        preencherRegiao(32, 35, 3, 14, GRAMA);
        preencherRegiao(36, 37, 10, 22, GRAMA);
        preencherRegiao(36, 36, 23, 24, GRAMA);
        preencherRegiao(36, 46, 25, 26, GRAMA);
        preencherRegiao(38, 46, 21, 22, GRAMA);
        preencherRegiao(38, 46, 12, 13, GRAMA);

        preencherRegiao(45, 48, 14, 20, GRAMA);
        preencherRegiao(45, 50, 3, 8, GRAMA);
        preencherRegiao(45, 48, 9, 9, GRAMA);
        preencherRegiao(45, 50, 36, 40, GRAMA);

        preencherRegiao(49, 50, 35, 35, GRAMA);

        preencherRegiao(47, 48, 27, 32, GRAMA);
        preencherRegiao(47, 47, 26, 26, GRAMA);
        preencherRegiao(45, 46, 27, 33, GRAMA);

        preencherRegiao(24, 44, 32, 33, GRAMA);
        preencherRegiao(24, 36, 34, 34, GRAMA);
        preencherRegiao(25, 35, 35, 35, GRAMA);
    }


    /*
     * Verifica se o jogador poderá ocupar
     * a nova posição.
     */
    public boolean podeMoverPara(
            int x,
            int y,
            int larguraJogador,
            int alturaJogador) {

        int esquerda = x;
        int direita = x + larguraJogador - 1;

        int cima = y;
        int baixo = y + alturaJogador - 1;

        /*
         * Os quatro cantos do jogador precisam
         * estar em posições que não sejam obstáculos.
         */
        return posicaoLivre(esquerda, cima)
                && posicaoLivre(direita, cima)
                && posicaoLivre(esquerda, baixo)
                && posicaoLivre(direita, baixo);
    }


    /*
     * Verifica se uma posição específica está livre.
     */
    private boolean posicaoLivre(int x, int y) {

        // Converte pixels para posição da matriz.
        int coluna = x / TAMANHO_TILE;
        int linha = y / TAMANHO_TILE;

        if (!posicaoValida(linha, coluna)) {

            return false;
        }

        return mapaLogico[linha][coluna] != OBSTACULO;
    }


    /*
     * Retorna o tipo de terreno presente
     * em determinada linha e coluna.
     */
    public int getTipoTerreno(int linha, int coluna) {

        if (!posicaoValida(linha, coluna)) {

            return OBSTACULO;
        }

        return mapaLogico[linha][coluna];
    }


    /*
     * Verifica se o jogador está em uma
     * região de grama alta.
     */
    public boolean ehGrama(int x, int y) {

        int coluna = x / TAMANHO_TILE;
        int linha = y / TAMANHO_TILE;

        if (!posicaoValida(linha, coluna)) {

            return false;
        }

        return mapaLogico[linha][coluna] == GRAMA;
    }


    /*
     * Verifica se determinada posição existe
     * dentro dos limites da matriz.
     */
    private boolean posicaoValida(int linha, int coluna) {

        return linha >= 0
                && linha < TOTAL_LINHAS
                && coluna >= 0
                && coluna < TOTAL_COLUNAS;
    }


    /*
     * ITEM 1.1 - ENCAPSULAMENTO
     *
     * A matriz mapaLogico não pode ser alterada
     * diretamente por outras classes.
     *
     * Este método controla internamente a alteração
     * de uma posição da matriz.
     */
    private void definirTerreno(
            int linha,
            int coluna,
            int tipo) {

        if (posicaoValida(linha, coluna)) {

            mapaLogico[linha][coluna] = tipo;
        }
    }


    /*
     * Preenche uma região retangular inteira
     * com o mesmo tipo de terreno.
     *
     * ATENÇÃO:
     * Apesar de possuir vários parâmetros e utilizar for,
     * este método NÃO atende ao requisito de Varargs do professor.
     */
    private void preencherRegiao(
            int linhaInicial,
            int linhaFinal,
            int colunaInicial,
            int colunaFinal,
            int tipo) {

        for (int linha = linhaInicial;
                linha <= linhaFinal;
                linha++) {

            for (int coluna = colunaInicial;
                    coluna <= colunaFinal;
                    coluna++) {

                definirTerreno(
                        linha,
                        coluna,
                        tipo
                );
            }
        }
    }
}
