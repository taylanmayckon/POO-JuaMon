package modelo;

/**
 * Subclasse concreta de Item utilizada para restaurar HP durante a batalha.
 * Participa do ITEM 7.3 (Subtipagem) e do ITEM 7.4 (Polimorfismo Paramétrico).
 */
public class Pocao extends Item {

    // ITEM 1.1 - ENCAPSULAMENTO
    private int pontosCura;

    public Pocao() {
        this(35);
    }

    public Pocao(int pontosCura) {
        super("Poção de Vida", "Restaura " + pontosCura + " pontos de HP do Pokémon ativo.");
        this.pontosCura = pontosCura;
    }

    @Override
    public String aplicarEfeito(Pokemon alvo) {
        int hpAntes = alvo.getHpAtual();
        alvo.curar(pontosCura);
        int curado = alvo.getHpAtual() - hpAntes;
        return "Você usou " + getNome() + " em " + alvo.getNome() + " e recuperou " + curado + " de HP!";
    }

    public int getPontosCura() {
        return pontosCura;
    }

    public void setPontosCura(int pontosCura) {
        this.pontosCura = pontosCura;
    }
}