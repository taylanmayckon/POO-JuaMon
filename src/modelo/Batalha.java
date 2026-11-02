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

        // REGRA #2 SEU COMBATE: Todos os Pokémons do treinador começam 100% curados!
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
     * ITEM 7.3 - POLIMORFISMO DE SUBTIPAGEM (Exemplo 1: Pokemon -> PokemonInicial / PokemonSelvagem)
     * Recebe a superclasse concreta Pokemon e opera tanto PokemonInicial quanto PokemonSelvagem.
     */
    public String obterTextoStatus(Pokemon qualquerPokemon) {
        return qualquerPokemon.gerarResumoStatus();
    }

    /*
     * ITEM 7.3 - POLIMORFISMO DE SUBTIPAGEM (Exemplo 2: Item -> Pokebola / Pocao)
     * Recebe a superclasse concreta Item e executa polimorficamente o método aplicarEfeito()
     * das subclasses concretas Pokebola ou Pocao em tempo de execução.
     */
    public String utilizarItemPolimorfico(Item item, Pokemon alvo) {
        return item.aplicarEfeito(alvo);
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
     * Consome um objeto Pokebola diretamente do Inventario<Pokebola> do Jogador (Item 7.4).
     */
    public boolean tentarCapturarSelvagem() throws CapturaInvalidaException {
        Pokebola pokebolaRetirada = jogador.retirarPokebolaDaMochila();
        if (pokebolaRetirada == null) {
            throw new CapturaInvalidaException("Voce nao possui mais Pokebolas no inventario!");
        }

        // Aciona o polimorfismo de subtipagem (Item 7.3)
        utilizarItemPolimorfico(pokebolaRetirada, pokemonSelvagem);

        if (pokemonSelvagem.tentarCaptura()) {
            jogador.adicionarPokemon(pokemonSelvagem);
            return true;
        }
        return false;
    }

    /**
     * Consome uma Pocao do Inventario<Pocao> (Item 7.4) e aplica via Subtipagem (Item 7.3).
     */
    public String usarPocaoNoPokemon(Pokemon pokemonAliado) throws CapturaInvalidaException {
        Pocao pocaoRetirada = jogador.retirarPocaoDaMochila();
        if (pocaoRetirada == null) {
            throw new CapturaInvalidaException("Voce nao possui mais Pocoes de Vida na mochila!");
        }
        return utilizarItemPolimorfico(pocaoRetirada, pokemonAliado);
    }
}