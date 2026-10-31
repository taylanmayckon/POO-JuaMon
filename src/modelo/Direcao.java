package modelo;

/*
 * ITEM 1.3 - ENUMERAÇÃO
 *
 * Direcao representa o conjunto fechado de direções
 * permitidas ao personagem.
 *
 * A enumeração é utilizada pelo atributo "direcao"
 * da classe Jogador e também pelo PainelMapa para
 * selecionar o sprite correto.
 */
public enum Direcao {

    CIMA,
    BAIXO,
    ESQUERDA,
    DIREITA
}
