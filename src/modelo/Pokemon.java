
package modelo;
import modelo.TipoPokemon;

public class Pokemon {
    private String nome;
    private TipoPokemon tipo;
    private int hpMaximo;
    private int hpAtual;
    private int ataque; 
    private int defesa; 
    private int nivel;
    private int taxaCaptura; // de 1 a 100 quanto maior o valor mais fácil capturar
    private int velocidade; //vai servir para determinar qual pokemon começa a batalha primeiro

    public Pokemon(String nome, TipoPokemon tipo, int hpMaximo, int hpAtual, int ataque, int defesa, int nivel, int taxaCaptura, int velocidade) {
        this.nome = nome;
        this.tipo = tipo;
        this.hpMaximo = hpMaximo;
        this.hpAtual = hpAtual;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = nivel;
        this.taxaCaptura = taxaCaptura;
        this.velocidade = velocidade;
    }
    
    // Métodos basicos de todo pokemon
    public void receberDano(int dano){
        hpAtual -= dano;
        if (hpAtual < 0)
            hpAtual = 0;
    }
    
    public void curar(int cura){
        hpAtual += cura;
        if (hpAtual > hpMaximo)
            hpAtual = hpMaximo;
    }
    
    public boolean estaDesmaiado(){
        return hpAtual <= 0;
    }
    
    // Getters e setters

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoPokemon getTipo() {
        return tipo;
    }

    public void setTipo(TipoPokemon tipo) {
        this.tipo = tipo;
    }

    public int getHpMaximo() {
        return hpMaximo;
    }

    public void setHpMaximo(int hpMaximo) {
        this.hpMaximo = hpMaximo;
    }

    public int getAtaque() {
        return ataque;
    }

    public void setAtaque(int ataque) {
        this.ataque = ataque;
    }

    public int getDefesa() {
        return defesa;
    }

    public void setDefesa(int defesa) {
        this.defesa = defesa;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getTaxaCaptura() {
        return taxaCaptura;
    }

    public void setTaxaCaptura(int taxaCaptura) {
        this.taxaCaptura = taxaCaptura;
    }

    public int getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(int velocidade) {
        this.velocidade = velocidade;
    }
    
}
