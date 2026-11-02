package modelo;

import java.util.ArrayList;

public class Pokemon extends Criatura {

    /*
     * ITEM 6.1 - ATRIBUTO FINAL EM SUPERCLASSE CONCRETA
     * Não é o mesmo atributo do item 1.5 (Mapa.TAMANHO_TILE).
     */
    private final String CATEGORIA_CRIACAO = "Criatura JuaMon";

    /*
     * ITEM 1.1 (Encapsulamento: todos private)
     * ITEM 1.2 (String, int e float não estáticos na mesma classe):
     * - String: nome
     * - int: id, hpMaximo, hpAtual, ataque, defesa, nivel, taxaCaptura, velocidade
     * - float: multiplicadorAtaque (usado pelo 4º ataque de Buff!)
     */
    private int id; // 1 a 10 (define qual sprite será carregada no combate)
    private String nome;
    private TipoPokemon tipo;
    private int hpMaximo;
    private int hpAtual;
    private int ataque;
    private int defesa;
    private int nivel;
    private int taxaCaptura;
    private int velocidade;
    private float multiplicadorAtaque;

    /*
     * ITEM 1.4 - ATRIBUTO ARRAYLIST
     * Lista que armazena os 4 ataques do Pokémon (3 de dano e 1 de buff).
     */
    private ArrayList<Golpe> golpes;

    public Pokemon(int id, String nome, TipoPokemon tipo, int hpMaximo, int hpAtual, int ataque, int defesa, int nivel, int taxaCaptura, int velocidade, boolean selvagem) {
        super(selvagem);
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.hpMaximo = hpMaximo;
        this.hpAtual = hpAtual;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = nivel;
        this.taxaCaptura = taxaCaptura;
        this.velocidade = velocidade;
        this.multiplicadorAtaque = 1.0f;
        this.golpes = new ArrayList<>();
    }

    /*
     * ITEM 6.2 - ACOPLAMENTO DINÂMICO (Parte 1: Método na classe Pai concreta)
     * Será chamado através de um objeto declarado e instanciado como PokemonInicial.
     */
    public String emitirGrito() {
        return this.nome + " [" + CATEGORIA_CRIACAO + "]: Pika-Pika! Pronto para o combate!";
    }

    @Override
    public String gerarResumoStatus() {
        return nome + " (Nv." + nivel + ") HP: " + hpAtual + "/" + hpMaximo;
    }

    public void adicionarGolpe(Golpe novoGolpe) {
        if (golpes.size() < 4) {
            golpes.add(novoGolpe);
        }
    }

    public void restaurarParaCombate() {
        this.hpAtual = this.hpMaximo;
        this.multiplicadorAtaque = 1.0f;
    }

    public void aplicarBuffAtaque(float bonus) {
        this.multiplicadorAtaque += bonus;
    }

    public float obterPorcentagemHp() {
        return ((float) hpAtual / (float) hpMaximo) * 100.0f;
    }

    public void receberDano(int dano) {
        hpAtual -= dano;
        if (hpAtual < 0) {
            hpAtual = 0;
        }
    }

    public void curar(int cura) {
        hpAtual += cura;
        if (hpAtual > hpMaximo) {
            hpAtual = hpMaximo;
        }
    }

    public boolean estaDesmaiado() {
        return hpAtual <= 0;
    }

    //Retorna o caminho relativo do sprite de frente (adversário selvagem).
    public String getSpriteFrente() {
        return "/assets/pokemon/" + this.id + "/front.png";
    }

    //Retorna o caminho relativo do sprite de costas (aliado do jogador).
    public String getSpriteCostas() {
        return "/assets/pokemon/" + this.id + "/back.png";
    }

    // Retorna o caminho da foto/sprite do Pokémon baseado no ID (1 a 10)
    public String getCaminhoSprite() {
        return getSpriteFrente();
    }

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public TipoPokemon getTipo() { return tipo; }
    public void setTipo(TipoPokemon tipo) { this.tipo = tipo; }
    public int getHpMaximo() { return hpMaximo; }
    public void setHpMaximo(int hpMaximo) { this.hpMaximo = hpMaximo; }
    public int getHpAtual() { return hpAtual; }
    public void setHpAtual(int hpAtual) { this.hpAtual = hpAtual; }
    public int getAtaque() { return ataque; }
    public void setAtaque(int ataque) { this.ataque = ataque; }
    public int getDefesa() { return defesa; }
    public void setDefesa(int defesa) { this.defesa = defesa; }
    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }
    public int getTaxaCaptura() { return taxaCaptura; }
    public void setTaxaCaptura(int taxaCaptura) { this.taxaCaptura = taxaCaptura; }
    public int getVelocidade() { return velocidade; }
    public void setVelocidade(int velocidade) { this.velocidade = velocidade; }
    public float getMultiplicadorAtaque() { return multiplicadorAtaque; }
    public ArrayList<Golpe> getGolpes() { return golpes; }
}
