package jogo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import modelo.CalculadoraDano;
import modelo.Jogador;
import modelo.Pokemon;
import modelo.PokemonInicial;
import modelo.PokemonSelvagem;

public class GerenciadorArquivo {

    /*
     * ITEM 13 - ARQUIVO: GRAVAR, RECUPERAR E UTILIZAR
     *
     * Este arquivo armazena informações reais do jogador.
     *
     * São salvos:
     * - nome;
     * - posição X;
     * - posição Y;
     * - quantidade de Pokébolas;
     * - quantidade de Poções;
     * - Pokémon capturados.
     *
     * Posteriormente os dados são recuperados e utilizados
     * para reconstruir o estado real do jogador.
     */
    private static final String PASTA_SAVES = "saves";

    private static final String NOME_ARQUIVO = "save_jogador.txt";


    /*
     * ITEM 13 - PARTE 1: GRAVAÇÃO
     *
     * Recebe o jogador atual e grava seus dados
     * em um arquivo de texto.
     */
    public static void salvarJogador(Jogador jogador) {

        // Garante que o diretório saves/ exista antes de salvar
        File pasta = new File(PASTA_SAVES);

        if (!pasta.exists()) {

            pasta.mkdirs();
        }


        File arquivo = new File(pasta, NOME_ARQUIVO);


        try (BufferedWriter escritor =
                new BufferedWriter(
                        new FileWriter(arquivo))) {

            // -------------------------
            // DADOS DO JOGADOR
            // -------------------------

            escritor.write(
                    "nome=" + jogador.getNome()
            );
            escritor.newLine();


            escritor.write(
                    "x=" + jogador.getX()
            );
            escritor.newLine();


            escritor.write(
                    "y=" + jogador.getY()
            );
            escritor.newLine();


            escritor.write(
                    "pokebolas=" + jogador.getPokebolas()
            );
            escritor.newLine();


            escritor.write(
                    "pocoes=" + jogador.getPocoes()
            );
            escritor.newLine();


            // -------------------------
            // POKÉMONS CAPTURADOS
            // -------------------------

            /*
             * A equipe sempre começa com o Pokémon inicial.
             *
             * Por isso o índice começa em 1.
             *
             * Índice 0 = Pokémon inicial.
             * Índice 1 em diante = Pokémon capturados.
             */
            ArrayList<Pokemon> equipe =
                    jogador.getEquipe();


            for (int i = 1;
                    i < equipe.size();
                    i++) {

                Pokemon pokemon =
                        equipe.get(i);


                /*
                 * Cada Pokémon capturado será armazenado assim:
                 *
                 * pokemon=ID,HP_ATUAL
                 *
                 * Exemplo:
                 *
                 * pokemon=2,17
                 * pokemon=7,8
                 */
                escritor.write(
                        "pokemon="
                        + pokemon.getId()
                        + ","
                        + pokemon.getHpAtual()
                );

                escritor.newLine();
            }


            System.out.println(
                    "Jogo salvo com sucesso em "
                    + arquivo.getPath()
            );


            System.out.println(
                    "Pokémons capturados salvos: "
                    + Math.max(
                            0,
                            equipe.size() - 1
                    )
            );


        } catch (IOException e) {

            System.out.println(
                    "Erro ao salvar o jogo: "
                    + e.getMessage()
            );
        }
    }


    /*
     * ITEM 13 - PARTE 2 E 3:
     * RECUPERAÇÃO E UTILIZAÇÃO DOS DADOS
     *
     * Este método:
     *
     * 1. abre o arquivo salvo;
     * 2. recupera os dados;
     * 3. cria novamente o jogador;
     * 4. reconstrói os Pokémon capturados;
     * 5. devolve o jogador reconstruído para ser
     *    utilizado pelo jogo.
     */
    public static Jogador carregarJogador() {

        File arquivo =
                new File(PASTA_SAVES, NOME_ARQUIVO);


        /*
         * Se ainda não existir um arquivo salvo,
         * retorna null.
         *
         * O PainelMapa então cria um jogador novo.
         */
        if (!arquivo.exists()) {

            System.out.println(
                    "Nenhum arquivo de save encontrado em "
                    + arquivo.getPath()
            );

            return null;
        }


        // -------------------------
        // VALORES PADRÃO
        // -------------------------

        String nome = "Treinador";

        int x = 200;
        int y = 180;

        int pokebolas = 5;
        int pocoes = 3;


        /*
         * Estas listas guardam temporariamente
         * as informações dos Pokémon encontradas
         * no arquivo.
         */
        ArrayList<Integer> idsCapturados =
                new ArrayList<>();

        ArrayList<Integer> hpCapturados =
                new ArrayList<>();


        try (BufferedReader leitor =
                new BufferedReader(
                        new FileReader(arquivo))) {

            String linha;


            /*
             * Percorre todas as linhas do arquivo.
             */
            while ((linha =
                    leitor.readLine()) != null) {


                /*
                 * Cada linha possui:
                 *
                 * chave=valor
                 *
                 * Exemplo:
                 *
                 * x=250
                 *
                 * ou:
                 *
                 * pokemon=4,12
                 */
                String[] partes =
                        linha.split("=", 2);


                if (partes.length != 2) {

                    continue;
                }


                String chave =
                        partes[0];

                String valor =
                        partes[1];


                switch (chave) {

                    case "nome":

                        nome = valor;

                        break;


                    case "x":

                        x =
                                Integer.parseInt(
                                        valor
                                );

                        break;


                    case "y":

                        y =
                                Integer.parseInt(
                                        valor
                                );

                        break;


                    case "pokebolas":

                        pokebolas =
                                Integer.parseInt(
                                        valor
                                );

                        break;


                    case "pocoes":

                        pocoes =
                                Integer.parseInt(
                                        valor
                                );

                        break;


                    /*
                     * Recupera um Pokémon capturado.
                     *
                     * O valor possui:
                     *
                     * ID,HP_ATUAL
                     *
                     * Exemplo:
                     *
                     * 4,12
                     */
                    case "pokemon":

                        String[] dadosPokemon =
                                valor.split(",", 2);


                        if (dadosPokemon.length == 2) {

                            int id =
                                    Integer.parseInt(
                                            dadosPokemon[0]
                                    );

                            int hpAtual =
                                    Integer.parseInt(
                                            dadosPokemon[1]
                                    );


                            idsCapturados.add(id);

                            hpCapturados.add(
                                    hpAtual
                            );
                        }

                        break;


                    default:

                        break;
                }
            }


            // -------------------------
            // RECONSTRUÇÃO DO JOGADOR
            // -------------------------

            /*
             * Cria novamente o Pokémon inicial.
             */
            PokemonInicial pokemonInicial =
                    CalculadoraDano
                            .criarPikachuInicial();


            /*
             * ITEM 13 - UTILIZAÇÃO DOS DADOS
             *
             * Nome e posição recuperados do arquivo
             * são utilizados para reconstruir efetivamente
             * o jogador.
             */
            Jogador jogadorCarregado =
                    new Jogador(
                            nome,
                            x,
                            y,
                            pokemonInicial
                    );


            /*
             * Restaura as quantidades de itens.
             */
            jogadorCarregado.setPokebolas(
                    pokebolas
            );

            jogadorCarregado.setPocoes(
                    pocoes
            );


            // -------------------------
            // RECONSTRUÇÃO DOS POKÉMONS
            // -------------------------

            /*
             * Para cada ID encontrado no arquivo,
             * utiliza a fábrica já existente no projeto.
             *
             * Isso evita duplicar todos os dados
             * de ataque, defesa, tipo, golpes etc.
             */
            for (int i = 0;
                    i < idsCapturados.size();
                    i++) {

                int id =
                        idsCapturados.get(i);

                int hpAtual =
                        hpCapturados.get(i);


                /*
                 * Recria a espécie correta
                 * utilizando seu ID.
                 */
                PokemonSelvagem pokemonCapturado =
                        CalculadoraDano
                                .criarPokemonSelvagemPorId(
                                        id
                                );


                /*
                 * Recupera também o HP que o Pokémon
                 * possuía quando o jogo foi salvo.
                 */
                pokemonCapturado.setHpAtual(
                        hpAtual
                );


                /*
                 * O Pokémon reconstruído é novamente
                 * adicionado à equipe do jogador.
                 *
                 * Aqui ocorre a UTILIZAÇÃO efetiva
                 * das informações recuperadas.
                 */
                jogadorCarregado.adicionarPokemon(
                        pokemonCapturado
                );
            }


            // -------------------------
            // MENSAGENS DE TESTE
            // -------------------------

            System.out.println(
                    "Save carregado com sucesso de "
                    + arquivo.getPath()
            );


            System.out.println(
                    "Posição recuperada: "
                    + x
                    + ", "
                    + y
            );


            System.out.println(
                    "Pokémons capturados recuperados: "
                    + idsCapturados.size()
            );


            /*
             * Retorna o jogador reconstruído.
             *
             * O PainelMapa utilizará este objeto
             * como o jogador real da partida.
             */
            return jogadorCarregado;


        } catch (IOException
                | NumberFormatException e) {

            System.out.println(
                    "Erro ao carregar o jogo: "
                    + e.getMessage()
            );


            return null;
        }
    }
}