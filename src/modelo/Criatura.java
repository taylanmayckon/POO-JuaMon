package modelo;

/*
 * ITEM 10.1 - CLASSES ABSTRATAS (Classe Abstrata 1 de 2)
 * Representa a base abstrata de qualquer criatura viva do universo JuaMon.
 * Possui um método abstrato que não é get, set ou construtor.
 */
public abstract class Criatura {

    // ITEM 1.1 - ENCAPSULAMENTO
    private boolean selvagem;

    public Criatura(boolean selvagem) {
        this.selvagem = selvagem;
    }

    public boolean isSelvagem() {
        return selvagem;
    }

    public void setSelvagem(boolean selvagem) {
        this.selvagem = selvagem;
    }

    // Método abstrato obrigatório para cumprir o Item 10.1
    public abstract String gerarResumoStatus();
}
