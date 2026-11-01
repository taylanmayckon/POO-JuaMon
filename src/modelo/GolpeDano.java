package modelo;

/*
 * Subclasse concreta de Golpe que representa os 3 ataques ofensivos do Pokémon.
 */
public class GolpeDano extends Golpe {

    // ITEM 1.1 - ENCAPSULAMENTO
    private int poder;

    public GolpeDano(String nome, TipoPokemon tipo, int poder) {
        super(nome, tipo);
        this.poder = poder;
    }

    public int getPoder() {
        return poder;
    }

    @Override
    public String aplicarGolpe(Pokemon usuario, Pokemon alvo) {
        int danoCausado = CalculadoraDano.calcularDano(usuario, alvo, this);
        alvo.receberDano(danoCausado);

        float mult = CalculadoraDano.obterMultiplicadorElemental(this.getTipo(), alvo.getTipo());
        String msgEficacia = "";
        if (mult > 1.0f) {
            msgEficacia = " Foi super efetivo!";
        } else if (mult < 1.0f) {
            msgEficacia = " Não foi muito efetivo...";
        }

        return usuario.getNome() + " usou " + getNome() + " (" + getTipo() + ") e causou " + danoCausado + " de dano!" + msgEficacia;
    }
}
