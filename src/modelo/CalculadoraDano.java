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

        if (tipoAtaque == TipoPokemon.INSETO && (tipoDefensor == TipoPokemon.PLANTA || tipoDefensor == TipoPokemon.PSIQUICO || tipoDefensor == TipoPokemon.SOMBRIO)) return 1.5f;
        if (tipoAtaque == TipoPokemon.SOMBRIO && (tipoDefensor == TipoPokemon.PSIQUICO || tipoDefensor == TipoPokemon.FANTASMA)) return 1.5f;
        if (tipoAtaque == TipoPokemon.VENENOSO && (tipoDefensor == TipoPokemon.PLANTA || tipoDefensor == TipoPokemon.FADA)) return 1.5f;

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
     * ID 1 mantido como Pikachu (Inicial do Jogador).
     * IDs 2 a 10 atualizados para a Geração 5 (Unova).
     * Cada um já nasce com 3 Ataques de Dano + 1 Ataque de Buff!
     */
    public static PokemonInicial criarPikachuInicial() {
        PokemonInicial pikachu = new PokemonInicial(1, "Pikachu", TipoPokemon.ELETRICO, 100, 18, 10, 5);
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
                p = new PokemonSelvagem(1, "Pikachu", TipoPokemon.ELETRICO, 90, 16, 10, 4);
                p.adicionarGolpe(new GolpeDano("Investida", TipoPokemon.NORMAL, 10));
                p.adicionarGolpe(new GolpeDano("Faísca", TipoPokemon.ELETRICO, 16));
                p.adicionarGolpe(new GolpeDano("Onda de Choque", TipoPokemon.ELETRICO, 20));
                p.adicionarGolpe(new GolpeBuff("Agilidade (+ATQ)", TipoPokemon.NORMAL, 0.20f));
                break;
            case 2:
                // Tepig (Inicial de Fogo - Gen 5)
                p = new PokemonSelvagem(2, "Tepig", TipoPokemon.FOGO, 95, 17, 11, 5);
                p.adicionarGolpe(new GolpeDano("Investida", TipoPokemon.NORMAL, 12));
                p.adicionarGolpe(new GolpeDano("Brasas", TipoPokemon.FOGO, 18));
                p.adicionarGolpe(new GolpeDano("Carga de Chamas", TipoPokemon.FOGO, 22));
                p.adicionarGolpe(new GolpeBuff("Chute Flamejante (+ATQ)", TipoPokemon.FOGO, 0.25f));
                break;
            case 3:
                // Oshawott (Inicial de Água - Gen 5)
                p = new PokemonSelvagem(3, "Oshawott", TipoPokemon.AGUA, 98, 16, 12, 5);
                p.adicionarGolpe(new GolpeDano("Arranhão", TipoPokemon.NORMAL, 12));
                p.adicionarGolpe(new GolpeDano("Revólver d'Água", TipoPokemon.AGUA, 18));
                p.adicionarGolpe(new GolpeDano("Concha Cortante", TipoPokemon.AGUA, 22));
                p.adicionarGolpe(new GolpeBuff("Foco da Lâmina (+ATQ)", TipoPokemon.AGUA, 0.25f));
                break;
            case 4:
                // Snivy (Inicial de Planta - Gen 5)
                p = new PokemonSelvagem(4, "Snivy", TipoPokemon.PLANTA, 90, 15, 13, 5);
                p.adicionarGolpe(new GolpeDano("Investida", TipoPokemon.NORMAL, 11));
                p.adicionarGolpe(new GolpeDano("Chicote de Vinha", TipoPokemon.PLANTA, 17));
                p.adicionarGolpe(new GolpeDano("Tornado de Folhas", TipoPokemon.PLANTA, 21));
                p.adicionarGolpe(new GolpeBuff("Dança do Crescimento (+ATQ)", TipoPokemon.PLANTA, 0.25f));
                break;
            case 5:
                // Pidove (Pássaro regional - Gen 5)
                p = new PokemonSelvagem(5, "Pidove", TipoPokemon.VOADOR, 85, 15, 9, 4);
                p.adicionarGolpe(new GolpeDano("Bicada", TipoPokemon.VOADOR, 12));
                p.adicionarGolpe(new GolpeDano("Ataque Rápido", TipoPokemon.NORMAL, 15));
                p.adicionarGolpe(new GolpeDano("Lâmina de Ar", TipoPokemon.VOADOR, 20));
                p.adicionarGolpe(new GolpeBuff("Vento Impulsionador (+ATQ)", TipoPokemon.VOADOR, 0.20f));
                break;
            case 6:
                // Sewaddle (Inseto de floresta - Gen 5)
                p = new PokemonSelvagem(6, "Sewaddle", TipoPokemon.INSETO, 80, 13, 12, 3);
                p.adicionarGolpe(new GolpeDano("Investida", TipoPokemon.NORMAL, 10));
                p.adicionarGolpe(new GolpeDano("Picada de Inseto", TipoPokemon.INSETO, 16));
                p.adicionarGolpe(new GolpeDano("Folha Navalha", TipoPokemon.PLANTA, 19));
                p.adicionarGolpe(new GolpeBuff("Manto de Folhas (+ATQ)", TipoPokemon.INSETO, 0.20f));
                break;
            case 7:
                // Patrat (Normal de início de rota - Gen 5)
                p = new PokemonSelvagem(7, "Patrat", TipoPokemon.NORMAL, 85, 16, 9, 4);
                p.adicionarGolpe(new GolpeDano("Mordida", TipoPokemon.SOMBRIO, 13));
                p.adicionarGolpe(new GolpeDano("Hiper Presa", TipoPokemon.NORMAL, 19));
                p.adicionarGolpe(new GolpeDano("Golpe Baixo", TipoPokemon.LUTADOR, 17));
                p.adicionarGolpe(new GolpeBuff("Olhar Vigilante (+ATQ)", TipoPokemon.NORMAL, 0.25f));
                break;
            case 8:
                // Roggenrola (Pedra pura - Gen 5)
                p = new PokemonSelvagem(8, "Roggenrola", TipoPokemon.PEDRA, 110, 19, 18, 5);
                p.adicionarGolpe(new GolpeDano("Cabeçada", TipoPokemon.NORMAL, 12));
                p.adicionarGolpe(new GolpeDano("Lançamento de Rocha", TipoPokemon.PEDRA, 18));
                p.adicionarGolpe(new GolpeDano("Deslize de Pedras", TipoPokemon.PEDRA, 22));
                p.adicionarGolpe(new GolpeBuff("Fortalecer Carcaça (+ATQ)", TipoPokemon.PEDRA, 0.25f));
                break;
            case 9:
                // Venipede (Inseto/Venenoso - Gen 5)
                p = new PokemonSelvagem(9, "Venipede", TipoPokemon.VENENOSO, 85, 15, 13, 4);
                p.adicionarGolpe(new GolpeDano("Rolamento", TipoPokemon.PEDRA, 14));
                p.adicionarGolpe(new GolpeDano("Ferrão Venenoso", TipoPokemon.VENENOSO, 18));
                p.adicionarGolpe(new GolpeDano("Cauda Venenosa", TipoPokemon.VENENOSO, 20));
                p.adicionarGolpe(new GolpeBuff("Acelerar Veneno (+ATQ)", TipoPokemon.VENENOSO, 0.20f));
                break;
            default:
                // Purrloin (Sombrio rápido - Gen 5)
                p = new PokemonSelvagem(10, "Purrloin", TipoPokemon.SOMBRIO, 82, 17, 8, 4);
                p.adicionarGolpe(new GolpeDano("Arranhão", TipoPokemon.NORMAL, 12));
                p.adicionarGolpe(new GolpeDano("Golpe Noturno", TipoPokemon.SOMBRIO, 19));
                p.adicionarGolpe(new GolpeDano("Talho Sombrio", TipoPokemon.SOMBRIO, 22));
                p.adicionarGolpe(new GolpeBuff("Garras Afiadas (+ATQ)", TipoPokemon.SOMBRIO, 0.25f));
                break;
        }
        return p;
    }
}