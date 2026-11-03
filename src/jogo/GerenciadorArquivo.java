package jogo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import modelo.CalculadoraDano;
import modelo.Jogador;
import modelo.PokemonInicial;

public class GerenciadorArquivo {

    /*
     * ITEM 13 - ARQUIVO: GRAVAR, RECUPERAR E UTILIZAR
     *
     * Este arquivo armazena informações reais do jogador.
     *
     * Os dados são gravados durante o jogo e posteriormente
     * recuperados e utilizados para reconstruir seu estado.
     */
    private static final String NOME_ARQUIVO = "save_jogador.txt";


    /*
     * ITEM 13 - PARTE 1: GRAVAÇÃO
     *
     * Recebe o jogador atual e grava seus dados
     * em um arquivo de texto.
     */
    public static void salvarJogador(Jogador jogador) {

        try (BufferedWriter escritor =
                new BufferedWriter(
                        new FileWriter(NOME_ARQUIVO))) {

            // Salva o nome do jogador.
            escritor.write(
                    "nome=" + jogador.getNome()
            );
            escritor.newLine();


            // Salva a posição horizontal.
            escritor.write(
                    "x=" + jogador.getX()
            );
            escritor.newLine();


            // Salva a posição vertical.
            escritor.write(
                    "y=" + jogador.getY()
            );
            escritor.newLine();


            // Salva a quantidade de Pokébolas.
            escritor.write(
                    "pokebolas=" + jogador.getPokebolas()
            );
            escritor.newLine();


            // Salva a quantidade de poções.
            escritor.write(
                    "pocoes=" + jogador.getPocoes()
            );
            escritor.newLine();


            System.out.println(
                    "Jogo salvo com sucesso."
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
     * Este método lê o arquivo anteriormente gravado,
     * recupera seus valores e utiliza essas informações
     * para reconstruir o objeto Jogador.
     */
    public static Jogador carregarJogador() {

        File arquivo =
                new File(NOME_ARQUIVO);


        /*
         * Caso ainda não exista arquivo de save,
         * retorna null.
         *
         * O PainelMapa utilizará esse retorno para
         * criar um jogador novo normalmente.
         */
        if (!arquivo.exists()) {

            System.out.println(
                    "Nenhum arquivo de save encontrado."
            );

            return null;
        }


        /*
         * Valores padrão.
         *
         * São utilizados caso alguma informação
         * não esteja presente no arquivo.
         */
        String nome = "Treinador";

        int x = 200;
        int y = 180;

        int pokebolas = 5;
        int pocoes = 3;


        try (BufferedReader leitor =
                new BufferedReader(
                        new FileReader(arquivo))) {

            String linha;


            /*
             * Percorre todas as linhas do arquivo.
             */
            while ((linha = leitor.readLine()) != null) {

                /*
                 * Cada linha possui o formato:
                 *
                 * chave=valor
                 *
                 * Exemplo:
                 *
                 * x=250
                 */
                String[] partes =
                        linha.split("=", 2);


                /*
                 * Se a linha não possuir chave e valor,
                 * ela será ignorada.
                 */
                if (partes.length != 2) {

                    continue;
                }


                String chave =
                        partes[0];

                String valor =
                        partes[1];


                /*
                 * Identifica qual informação está
                 * sendo recuperada do arquivo.
                 */
                switch (chave) {

                    case "nome":

                        nome = valor;

                        break;


                    case "x":

                        x =
                                Integer.parseInt(valor);

                        break;


                    case "y":

                        y =
                                Integer.parseInt(valor);

                        break;


                    case "pokebolas":

                        pokebolas =
                                Integer.parseInt(valor);

                        break;


                    case "pocoes":

                        pocoes =
                                Integer.parseInt(valor);

                        break;


                    /*
                     * Caso futuramente o arquivo possua
                     * alguma informação desconhecida,
                     * ela será simplesmente ignorada.
                     */
                    default:

                        break;
                }
            }


            /*
             * Cria novamente o Pokémon inicial necessário
             * para construir o objeto Jogador.
             */
            PokemonInicial pokemonInicial =
                    CalculadoraDano.criarPikachuInicial();


            /*
             * ITEM 13 - UTILIZAÇÃO DOS DADOS RECUPERADOS
             *
             * Aqui os dados lidos do arquivo não são apenas
             * mostrados no console.
             *
             * Nome, posição X e posição Y são efetivamente
             * utilizados para reconstruir o jogador.
             */
            Jogador jogadorCarregado =
                    new Jogador(
                            nome,
                            x,
                            y,
                            pokemonInicial
                    );


            /*
             * As quantidades recuperadas do arquivo
             * também são aplicadas novamente ao jogador.
             */
            jogadorCarregado.setPokebolas(
                    pokebolas
            );

            jogadorCarregado.setPocoes(
                    pocoes
            );


            /*
             * A mensagem ajuda a comprovar durante
             * os testes que os dados foram recuperados.
             */
            System.out.println(
                    "Save carregado com sucesso. "
                    + "Posição recuperada: "
                    + x
                    + ", "
                    + y
            );


            /*
             * Retorna o jogador reconstruído.
             *
             * O PainelMapa passará a utilizar este
             * objeto como o jogador real do jogo.
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
