package modelo;

public class Jogador {

    /*
     * ITEM 1.1 - ENCAPSULAMENTO
     *
     * Os atributos não estáticos são privados.
     *
     * A posição do jogador não pode ser alterada
     * diretamente por outras classes.
     */
    private int x;
    private int y;


    /*
     * ITEM 1.3 - ATRIBUTO DO TIPO ENUM
     *
     * direcao utiliza a enum Direcao.
     *
     * Esse atributo é necessário para controlar
     * para qual lado o personagem está olhando
     * e qual sprite deve ser exibido.
     */
    private Direcao direcao;


    /*
     * ITEM 3 - CONSTRUTOR EM CLASSE CONCRETA
     *
     * O construtor recebe a posição inicial do jogador
     * dentro do mapa.
     */
    public Jogador(int x, int y) {

        this.x = x;
        this.y = y;

        // O jogador inicia olhando para baixo.
        this.direcao = Direcao.BAIXO;
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public Direcao getDirecao() {
        return direcao;
    }


    public void setDirecao(Direcao direcao) {
        this.direcao = direcao;
    }


    /*
     * Atualiza a posição do jogador.
     *
     * Este método só é chamado pelo PainelMapa
     * depois que a classe Mapa verifica se a
     * nova posição não possui obstáculos.
     */
    public void mover(
            int deslocamentoX,
            int deslocamentoY) {

        x = x + deslocamentoX;
        y = y + deslocamentoY;
    }
}
