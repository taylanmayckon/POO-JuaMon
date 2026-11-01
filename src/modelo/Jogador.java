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
    private int pokebolas;

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
        this.pokebolas = 5; // Regra do MVP: Começa com 5 Pokébolas
        this.equipe = new ArrayList<>();
        this.pokemonInicialParceiro = pokemonInicial;
        if (pokemonInicial != null) {
            this.equipe.add(pokemonInicial);
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

    public void gastarPokebola() {
        if (this.pokebolas > 0) {
            this.pokebolas--;
        }
    }

    public Pokemon getPokemonAtivo() {
        return equipe.get(0);
    }

    public PokemonInicial getPokemonInicialParceiro() {
        return pokemonInicialParceiro;
    }

    public ArrayList<Pokemon> getEquipe() { return equipe; }
    public int getPokebolas() { return pokebolas; }
    public void setPokebolas(int pokebolas) { this.pokebolas = pokebolas; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public int getX() { return x; }
    public int getY() { return y; }
    public Direcao getDirecao() { return direcao; }
    public void setDirecao(Direcao direcao) { this.direcao = direcao; }
}
