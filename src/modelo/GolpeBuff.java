package modelo;

/*
 * Subclasse concreta de Golpe que representa o 4º ataque (Buff) do Pokémon.
 */
public class GolpeBuff extends Golpe {

    // ITEM 1.1 - ENCAPSULAMENTO
    private float incrementoMultiplicador;

    public GolpeBuff(String nome, TipoPokemon tipo, float incrementoMultiplicador) {
        super(nome, tipo);
        this.incrementoMultiplicador = incrementoMultiplicador;
    }

    @Override
    public String aplicarGolpe(Pokemon usuario, Pokemon alvo) {
        usuario.aplicarBuffAtaque(incrementoMultiplicador);
        return usuario.getNome() + " usou " + getNome() + "! Seu multiplicador de ataque subiu para "
                + String.format("%.2fx", usuario.getMultiplicadorAtaque()) + "!";
    }
}
