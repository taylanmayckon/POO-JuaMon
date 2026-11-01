package modelo;

/*
 * ITEM 10.1 - CLASSES ABSTRATAS (Classe Abstrata 2 de 2)
 * Modela a estrutura base de um dos 4 ataques de um Pokémon.
 * Como cada ataque pode ser de Dano (3 ataques) ou de Buff (1 ataque),
 * o método aplicarGolpe é abstrato.
 */
public abstract class Golpe {

    // ITEM 1.1 - ENCAPSULAMENTO
    private String nome;
    private TipoPokemon tipo;

    public Golpe(String nome, TipoPokemon tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public String getNome() {
        return nome;
    }

    public TipoPokemon getTipo() {
        return tipo;
    }

    // Método abstrato obrigatório (não é construtor, get ou set)
    public abstract String aplicarGolpe(Pokemon usuario, Pokemon alvo);
}
