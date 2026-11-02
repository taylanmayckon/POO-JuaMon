package modelo;

/**
 * Superclasse concreta que representa um item consumível do inventário.
 * Participa do ITEM 7.3 (Polimorfismo de Subtipagem) como classe Pai concreta.
 */
public class Item {

    // ITEM 1.1 - ENCAPSULAMENTO
    private String nome;
    private String descricao;

    // ITEM 3.1 - Construtor declarado em classe concreta
    public Item(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    /**
     * Método sobrescrito pelas subclasses concretas (Pokebola e Pocao)
     * para demonstrar o Polimorfismo de Subtipagem (Item 7.3).
     */
    public String aplicarEfeito(Pokemon alvo) {
        return "O item " + nome + " foi utilizado em " + alvo.getNome() + "!";
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}