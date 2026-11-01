package modelo;

/*
 * ITEM 8.1 - INTERFACES (Classe 2 de 3 - Implementa DUAS interfaces)
 * PokemonSelvagem implementa tanto Combatente quanto Capturavel.
 */
public class PokemonSelvagem extends Pokemon implements Combatente, Capturavel {

    public PokemonSelvagem(int id, String nome, TipoPokemon tipo, int hpMaximo, int ataque, int defesa, int nivel, int taxaCaptura, int velocidade) {
        super(id, nome, tipo, hpMaximo, hpMaximo, ataque, defesa, nivel, taxaCaptura, velocidade, true);
    }

    @Override
    public boolean temCondicaoDeLutar() {
        return !estaDesmaiado();
    }

    @Override
    public boolean tentarCaptura() {
        // Regra do MVP: 100% de chance se a vida estiver abaixo ou igual a 20% 
        return obterPorcentagemHp() <= 20.0f;
    }
}
