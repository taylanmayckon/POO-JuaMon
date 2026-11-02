package modelo;

import java.util.ArrayList;

/*
 * ITEM 8.1 - INTERFACES (Classe 3 de 3)
 * Jogador implementa Combatente para indicar se sua equipe ainda pode lutar.
 */
public class Jogador implements Combatente {

    // ITEM 1.1 - ENCAPSULAMENTO
    private String nome;
    private int x;
    private int y;
    private Direcao direcao;

    // ITEM 1.4 - ATRIBUTO ARRAYLIST
    private ArrayList<Pokemon> equipe;
    private PokemonInicial pokemonInicialParceiro;

    /*
     * ITEM 7.4 - POLIMORFISMO PARAMÉTRICO (Atributos parametrizados explicitamente)
     * Justificativa: O jogador possui compartimentos específicos na mochila tipados
     * explicitamente com <Pokebola> e <Pocao>, garantindo segurança de tipos em tempo de compilação.
     */
    private Inventario<Pokebola> compartimentoPokebolas;
    private Inventario<Pocao> compartimentoPocoes;

    // Construtor original mantido por compatibilidade, delegando para a Agregação
    public Jogador(int x, int y) {
        this("Treinador", x, y, CalculadoraDano.criarPikachuInicial());
    }

    /*
     * ITEM 5.1 - ASSOCIAÇÃO POR AGREGAÇÃO
     * O objeto PokemonInicial é instanciado fora de Jogador e injetado no construtor.
     */
    public Jogador(String nome, int x, int y, PokemonInicial pokemonInicial) {
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.direcao = Direcao.BAIXO;
        this.equipe = new ArrayList<>();
        this.pokemonInicialParceiro = pokemonInicial;
        if (pokemonInicial != null) {
            this.equipe.add(pokemonInicial);
        }

        // ITEM 7.4 - Instanciação e abastecimento usando métodos parametrizados fora do construtor de Inventario
        this.compartimentoPokebolas = new Inventario<Pokebola>("Bolso de Pokébolas");
        this.compartimentoPocoes = new Inventario<Pocao>("Bolso de Poções");

        // Regra do MVP: Começa com 5 Pokébolas e 3 Poções na mochila
        abastecerInventarioInicial(5, 3);
    }

    /**
     * Abastece os compartimentos genéricos chamando armazenarItem(T) fora do construtor.
     */
    public final void abastecerInventarioInicial(int qtdPokebolas, int qtdPocoes) {
        this.compartimentoPokebolas.getItensArmazenados().clear();
        for (int i = 0; i < qtdPokebolas; i++) {
            this.compartimentoPokebolas.armazenarItem(new Pokebola());
        }

        this.compartimentoPocoes.getItensArmazenados().clear();
        for (int i = 0; i < qtdPocoes; i++) {
            this.compartimentoPocoes.armazenarItem(new Pocao(35));
        }
    }

    @Override
    public boolean temCondicaoDeLutar() {
        for (Pokemon p : equipe) {
            if (!p.estaDesmaiado()) {
                return true;
            }
        }
        return false;
    }

    public void mover(int deslocamentoX, int deslocamentoY) {
        x = x + deslocamentoX;
        y = y + deslocamentoY;
    }

    public void adicionarPokemon(Pokemon capturado) {
        this.equipe.add(capturado);
    }

    public Pokebola retirarPokebolaDaMochila() {
        return this.compartimentoPokebolas.retirarItem();
    }

    public Pocao retirarPocaoDaMochila() {
        return this.compartimentoPocoes.retirarItem();
    }

    public void gastarPokebola() {
        this.compartimentoPokebolas.retirarItem();
    }

    public Pokemon getPokemonAtivo() {
        return equipe.get(0);
    }

    public PokemonInicial getPokemonInicialParceiro() {
        return pokemonInicialParceiro;
    }

    public Inventario<Pokebola> getCompartimentoPokebolas() {
        return compartimentoPokebolas;
    }

    public Inventario<Pocao> getCompartimentoPocoes() {
        return compartimentoPocoes;
    }

    public ArrayList<Pokemon> getEquipe() {
        return equipe;
    }

    public int getPokebolas() {
        return this.compartimentoPokebolas.obterQuantidade();
    }

    public void setPokebolas(int pokebolas) {
        this.compartimentoPokebolas.getItensArmazenados().clear();
        for (int i = 0; i < pokebolas; i++) {
            this.compartimentoPokebolas.armazenarItem(new Pokebola());
        }
    }

    public int getPocoes() {
        return this.compartimentoPocoes.obterQuantidade();
    }

    /**
     * Atualiza e recarrega o estoque de Poções de Vida no Inventario<Pocao>.
     * Utilizado para resetar as poções para a quantidade padrão (3) após os combates.
     */
    public void setPocoes(int pocoes) {
        this.compartimentoPocoes.getItensArmazenados().clear();
        for (int i = 0; i < pocoes; i++) {
            this.compartimentoPocoes.armazenarItem(new Pocao(35));
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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
}