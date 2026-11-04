package modelo;

/*
 * ITEM 6.2 - ACOPLAMENTO DINÂMICO (Parte 2: Classe Filha)
 * PokemonInicial herda de Pokemon e NÃO possui o método emitirGrito() reescrito.
 *
 * ITEM 8.1 - INTERFACES (Classe 1 de 3)
 * Implementa a interface Combatente.
 */
public class PokemonInicial extends Pokemon implements Combatente {

    public PokemonInicial(int id, String nome, TipoPokemon tipo, int hpMaximo, int ataque, int defesa, int nivel) {
        super(id, nome, tipo, hpMaximo, hpMaximo, ataque, defesa, nivel, false);
    }

    @Override
    public boolean temCondicaoDeLutar() {
        return !estaDesmaiado();
    }
}