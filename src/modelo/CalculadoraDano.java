package modelo;

import java.util.Random;

public class CalculadoraDano {

    /*
     * ITEM 2.2 - MÉTODO ESTÁTICO
     * Método utilitário estático (não é o main) que calcula a vantagem/desvantagem
     * elemental baseando-se no TIPO DO ATAQUE contra o TIPO DO POKÉMON DEFENSOR.
     */
    public static float obterMultiplicadorElemental(TipoPokemon tipoAtaque, TipoPokemon tipoDefensor) {
        if (tipoAtaque == TipoPokemon.ELETRICO && (tipoDefensor == TipoPokemon.AGUA || tipoDefensor == TipoPokemon.VOADOR)) return 1.5f;
        if (tipoAtaque == TipoPokemon.ELETRICO && (tipoDefensor == TipoPokemon.PLANTA || tipoDefensor == TipoPokemon.TERRA || tipoDefensor == TipoPokemon.PEDRA)) return 0.5f;

        if (tipoAtaque == TipoPokemon.FOGO && (tipoDefensor == TipoPokemon.PLANTA || tipoDefensor == TipoPokemon.INSETO || tipoDefensor == TipoPokemon.GELO)) return 1.5f;
        if (tipoAtaque == TipoPokemon.FOGO && (tipoDefensor == TipoPokemon.AGUA || tipoDefensor == TipoPokemon.PEDRA)) return 0.5f;

        if (tipoAtaque == TipoPokemon.AGUA && (tipoDefensor == TipoPokemon.FOGO || tipoDefensor == TipoPokemon.PEDRA || tipoDefensor == TipoPokemon.TERRA)) return 1.5f;
        if (tipoAtaque == TipoPokemon.AGUA && (tipoDefensor == TipoPokemon.PLANTA || tipoDefensor == TipoPokemon.ELETRICO)) return 0.5f;

        if (tipoAtaque == TipoPokemon.PLANTA && (tipoDefensor == TipoPokemon.AGUA || tipoDefensor == TipoPokemon.PEDRA || tipoDefensor == TipoPokemon.TERRA)) return 1.5f;
        if (tipoAtaque == TipoPokemon.PLANTA && (tipoDefensor == TipoPokemon.FOGO || tipoDefensor == TipoPokemon.VOADOR || tipoDefensor == TipoPokemon.INSETO)) return 0.5f;

        if (tipoAtaque == TipoPokemon.VOADOR && (tipoDefensor == TipoPokemon.PLANTA || tipoDefensor == TipoPokemon.INSETO || tipoDefensor == TipoPokemon.LUTADOR)) return 1.5f;
        if (tipoAtaque == TipoPokemon.PEDRA && (tipoDefensor == TipoPokemon.FOGO || tipoDefensor == TipoPokemon.VOADOR || tipoDefensor == TipoPokemon.INSETO)) return 1.5f;

        return 1.0f;
    }

    /*
     * ITEM 7.2 - POLIMORFISMO DE COERÇÃO EXPLÍCITA (CASTING)
     * O cálculo utiliza float por conta do multiplicador de buff do atacante e do
     * multiplicador elemental (ex: 1.5f). O atributo/variável 'danoFinal' é do tipo int
     * e recebe a coerção explícita (int) a partir do float 'danoFlutuante', sem usar Math.round.
     */
    public static int calcularDano(Pokemon atacante, Pokemon defensor, GolpeDano golpe) {
        float multElemental = obterMultiplicadorElemental(golpe.getTipo(), defensor.getTipo());
        float multBuff = atacante.getMultiplicadorAtaque();

        float base = (atacante.getAtaque() + golpe.getPoder()) - (defensor.getDefesa() * 0.4f);
        if (base < 5.0f) {
            base = 5.0f;
        }

        float danoFlutuante = base * multBuff * multElemental;

        // ITEM 7.2 - Coerção explícita de float para int
        int danoFinal = (int) danoFlutuante;
        return Math.max(1, danoFinal);
    }

    /*
     * POOL DE 10 POKÉMONS (IDs 1 a 10)
     * Cada um já nasce com 3 Ataques de Dano + 1 Ataque de Buff!
     */
    public static PokemonInicial criarPikachuInicial() {
        PokemonInicial pikachu = new PokemonInicial(1, "Pikachu", TipoPokemon.ELETRICO, 100, 18, 10, 5, 25);
        pikachu.adicionarGolpe(new GolpeDano("Ataque Rápido", TipoPokemon.NORMAL, 12));
        pikachu.adicionarGolpe(new GolpeDano("Choque do Trovão", TipoPokemon.ELETRICO, 18));
        pikachu.adicionarGolpe(new GolpeDano("Cauda de Ferro", TipoPokemon.ACO, 20));
        pikachu.adicionarGolpe(new GolpeBuff("Carga Elétrica (+ATQ)", TipoPokemon.ELETRICO, 0.25f));
        return pikachu;
    }

    public static PokemonSelvagem sortearPokemonSelvagem() {
        Random r = new Random();
        int idSorteado = r.nextInt(10) + 1; // Sorteia de 1 a 10
        return criarPokemonSelvagemPorId(idSorteado);
    }

    public static PokemonSelvagem criarPokemonSelvagemPorId(int id) {
        PokemonSelvagem p;
        switch (id) {
            case 1:
                p = new PokemonSelvagem(1, "Pikachu", TipoPokemon.ELETRICO, 90, 16, 10, 4, 30, 22);
                p.adicionarGolpe(new GolpeDano("Investida", TipoPokemon.NORMAL, 10));
                p.adicionarGolpe(new GolpeDano("Faísca", TipoPokemon.ELETRICO, 16));
                p.adicionarGolpe(new GolpeDano("Onda de Choque", TipoPokemon.ELETRICO, 20));
                p.adicionarGolpe(new GolpeBuff("Agilidade (+ATQ)", TipoPokemon.NORMAL, 0.20f));
                break;
            case 2:
                p = new PokemonSelvagem(2, "Charmander", TipoPokemon.FOGO, 95, 17, 10, 5, 30, 20);
                p.adicionarGolpe(new GolpeDano("Arranhão", TipoPokemon.NORMAL, 12));
                p.adicionarGolpe(new GolpeDano("Brasas", TipoPokemon.FOGO, 18));
                p.adicionarGolpe(new GolpeDano("Presa de Fogo", TipoPokemon.FOGO, 22));
                p.adicionarGolpe(new GolpeBuff("Dança do Dragão (+ATQ)", TipoPokemon.DRAGAO, 0.25f));
                break;
            case 3:
                p = new PokemonSelvagem(3, "Squirtle", TipoPokemon.AGUA, 100, 15, 14, 5, 30, 16);
                p.adicionarGolpe(new GolpeDano("Investida", TipoPokemon.NORMAL, 12));
                p.adicionarGolpe(new GolpeDano("Jato de Água", TipoPokemon.AGUA, 18));
                p.adicionarGolpe(new GolpeDano("Mordida", TipoPokemon.SOMBRIO, 19));
                p.adicionarGolpe(new GolpeBuff("Fluxo de Água (+ATQ)", TipoPokemon.AGUA, 0.25f));
                break;
            case 4:
                p = new PokemonSelvagem(4, "Bulbasaur", TipoPokemon.PLANTA, 105, 15, 13, 5, 30, 15);
                p.adicionarGolpe(new GolpeDano("Investida", TipoPokemon.NORMAL, 12));
                p.adicionarGolpe(new GolpeDano("Chicote de Vinha", TipoPokemon.PLANTA, 18));
                p.adicionarGolpe(new GolpeDano("Folha Navalha", TipoPokemon.PLANTA, 22));
                p.adicionarGolpe(new GolpeBuff("Crescimento (+ATQ)", TipoPokemon.NORMAL, 0.25f));
                break;
            case 5:
                p = new PokemonSelvagem(5, "Pidgey", TipoPokemon.VOADOR, 85, 14, 9, 4, 45, 19);
                p.adicionarGolpe(new GolpeDano("Bicada", TipoPokemon.VOADOR, 12));
                p.adicionarGolpe(new GolpeDano("Lufada de Vento", TipoPokemon.VOADOR, 16));
                p.adicionarGolpe(new GolpeDano("Ataque de Asa", TipoPokemon.VOADOR, 20));
                p.adicionarGolpe(new GolpeBuff("Vento Afiado (+ATQ)", TipoPokemon.VOADOR, 0.20f));
                break;
            case 6:
                p = new PokemonSelvagem(6, "Caterpie", TipoPokemon.INSETO, 75, 12, 8, 3, 60, 12);
                p.adicionarGolpe(new GolpeDano("Investida", TipoPokemon.NORMAL, 10));
                p.adicionarGolpe(new GolpeDano("Picada de Inseto", TipoPokemon.INSETO, 15));
                p.adicionarGolpe(new GolpeDano("Fio de Seda Cortante", TipoPokemon.INSETO, 18));
                p.adicionarGolpe(new GolpeBuff("Casulo Forte (+ATQ)", TipoPokemon.INSETO, 0.20f));
                break;
            case 7:
                p = new PokemonSelvagem(7, "Rattata", TipoPokemon.NORMAL, 80, 16, 8, 4, 50, 21);
                p.adicionarGolpe(new GolpeDano("Ataque Rápido", TipoPokemon.NORMAL, 13));
                p.adicionarGolpe(new GolpeDano("Hiper Presa", TipoPokemon.NORMAL, 19));
                p.adicionarGolpe(new GolpeDano("Mordida Sombria", TipoPokemon.SOMBRIO, 17));
                p.adicionarGolpe(new GolpeBuff("Foco deenergia (+ATQ)", TipoPokemon.NORMAL, 0.25f));
                break;
            case 8:
                p = new PokemonSelvagem(8, "Geodude", TipoPokemon.PEDRA, 110, 18, 16, 5, 35, 10);
                p.adicionarGolpe(new GolpeDano("Investida", TipoPokemon.NORMAL, 12));
                p.adicionarGolpe(new GolpeDano("Lançamento de Rocha", TipoPokemon.PEDRA, 18));
                p.adicionarGolpe(new GolpeDano("Tremor de Terra", TipoPokemon.TERRA, 21));
                p.adicionarGolpe(new GolpeBuff("Polimento de Rocha (+ATQ)", TipoPokemon.PEDRA, 0.25f));
                break;
            case 9:
                p = new PokemonSelvagem(9, "Zubate", TipoPokemon.VENENOSO, 85, 15, 9, 4, 45, 20);
                p.adicionarGolpe(new GolpeDano("Mordida", TipoPokemon.SOMBRIO, 14));
                p.adicionarGolpe(new GolpeDano("Presa Venenosa", TipoPokemon.VENENOSO, 18));
                p.adicionarGolpe(new GolpeDano("Cortador de Ar", TipoPokemon.VOADOR, 19));
                p.adicionarGolpe(new GolpeBuff("Grito Supersônico (+ATQ)", TipoPokemon.NORMAL, 0.20f));
                break;
            default:
                p = new PokemonSelvagem(10, "Abra", TipoPokemon.PSIQUICO, 80, 20, 8, 5, 25, 23);
                p.adicionarGolpe(new GolpeDano("Confusão", TipoPokemon.PSIQUICO, 17));
                p.adicionarGolpe(new GolpeDano("Feixe Psíquico", TipoPokemon.PSIQUICO, 22));
                p.adicionarGolpe(new GolpeDano("Esfera de Energia", TipoPokemon.PLANTA, 18));
                p.adicionarGolpe(new GolpeBuff("Mente Calma (+ATQ)", TipoPokemon.PSIQUICO, 0.30f));
                break;
        }
        return p;
    }
}
