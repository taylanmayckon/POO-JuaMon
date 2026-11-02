package modelo;

/**
 * Subclasse concreta de Item utilizada para capturar Pokémons selvagens.
 * Participa do ITEM 7.3 (Subtipagem) e do ITEM 7.4 (Polimorfismo Paramétrico).
 */
public class Pokebola extends Item {

    // ITEM 1.1 - ENCAPSULAMENTO
    private float bonusCaptura;

    public Pokebola() {
        super("Pokébola", "Dispositivo esférico para capturar criaturas com vida <= 20%.");
        this.bonusCaptura = 1.0f;
    }

    @Override
    public String aplicarEfeito(Pokemon alvo) {
        return "Você arremessou uma " + getNome() + " em direção a " + alvo.getNome() + "!";
    }

    public float getBonusCaptura() {
        return bonusCaptura;
    }

    public void setBonusCaptura(float bonusCaptura) {
        this.bonusCaptura = bonusCaptura;
    }
}