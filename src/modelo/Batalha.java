package modelo;

import java.util.List;

public class Batalha {

    // ITEM 1.1 - ENCAPSULAMENTO
    private Jogador jogador;
    private PokemonSelvagem pokemonSelvagem;

    /*
     * ITEM 6.2 - ACOPLAMENTO DINÂMICO (Parte 3: Terceira Classe)
     * Atributo declarado da classe FILHA (PokemonInicial) e instanciado como
     * da classe FILHA, que chama o método emitirGrito() exclusivo da classe PAI (Pokemon).
     */
    private PokemonInicial parceiroPikachu;

    public Batalha(Jogador jogador, PokemonSelvagem pokemonSelvagem) {
        this.jogador = jogador;
        this.pokemonSelvagem = pokemonSelvagem;
        this.parceiroPikachu = jogador.getPokemonInicialParceiro();

        // REGRA #2 DO SEU COMBATE: Todos os Pokémons do treinador começam 100% curados!
        List<Pokemon> equipe = jogador.getEquipe();
        curarEquipeParaCombate(equipe.toArray(new Pokemon[0]));
    }

    /*
     * ITEM 6.2 - ACOPLAMENTO DINÂMICO EM USO
     */
    public String obterAnuncioDeEntrada() {
        return parceiroPikachu.emitirGrito() + " Um " + pokemonSelvagem.getNome() + " selvagem surgiu!";
    }

    /*
     * ITEM 2.1 - MÉTODO COM VARARGS E FOR
     * Recebe uma lista de argumentos variados (Pokemon...) e usa um laço FOR
     * para deixar todos os Pokémons do treinador 100% curados no início do combate.
     */
    public void curarEquipeParaCombate(Pokemon... pokemons) {
        for (Pokemon p : pokemons) {
            if (p != null) {
                p.restaurarParaCombate();
            }
        }
    }

    /*
     * ITEM 7.3 - POLIMORFISMO DE SUBTIPAGEM (Pai e filhos concretos)
     * Recebe a superclasse concreta Pokemon e opera tanto PokemonInicial quanto PokemonSelvagem.
     */
    public String obterTextoStatus(Pokemon qualquerPokemon) {
        return qualquerPokemon.gerarResumoStatus();
    }

    /*
     * ITEM 7.1 - POLIMORFISMO DE SOBRECARGA (Versão 1: por índice do botão 0 a 3)
     */
    public String executarGolpeTurno(Pokemon atacante, Pokemon defensor, int indiceGolpe) {
        Golpe golpeEscolhido = atacante.getGolpes().get(indiceGolpe);
        return executarGolpeTurno(atacante, defensor, golpeEscolhido);
    }

    /*
     * ITEM 7.1 - POLIMORFISMO DE SOBRECARGA (Versão 2: recebendo o objeto Golpe)
     */
    public String executarGolpeTurno(Pokemon atacante, Pokemon defensor, Golpe golpe) {
        return golpe.aplicarGolpe(atacante, defensor);
    }

    /*
     * ITEM 11.1 - LEVANTANDO A EXCEÇÃO PRÓPRIA NA TENTATIVA DE CAPTURA
     */
    public boolean tentarCapturarSelvagem() throws CapturaInvalidaException {
        if (jogador.getPokebolas() <= 0) {
            throw new CapturaInvalidaException("Você não possui mais Pokébolas no inventário!");
        }
        jogador.gastarPokebola();
        if (pokemonSelvagem.tentarCaptura()) {
            jogador.adicionarPokemon(pokemonSelvagem);
            return true;
        }
        return false;
    }
}
