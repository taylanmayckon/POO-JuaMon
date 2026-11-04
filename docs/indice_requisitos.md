# 📑 Índice Oficial dos 22 Requisitos Obrigatórios — Projeto JuaMon

Este documento serve como **índice de consulta rápida** para a avaliação do código-fonte e como **roteiro de comentários** para o Diagrama de Classes UML. Todos os itens abaixo possuem marcação explícita `/* ITEM X.X ... */` diretamente nos arquivos `.java`.

---

## 🗺️ Tabela Resumo de Localização Rápida

| Item | Requisito do Edital | Classe(s) Principal(is) | Linha / Método / Atributo Exato |
| :---: | :--- | :--- | :--- |
| **1.1** | Encapsulamento (`private`) | Todas as classes (`Pokemon`, `Jogador`, `Mapa`, etc.) | Todos os atributos de instância declarados como `private`. |
| **1.2** | `String`, `int` e `float` na mesma classe | `modelo.Pokemon` | `String nome`, `int id / hpMaximo`, `float multiplicadorAtaque`. |
| **1.3** | Atributo de Enumeração | `modelo.Jogador` e `modelo.Pokemon` | `private Direcao direcao` (`Direcao.java`) e `private TipoPokemon tipo` (`TipoPokemon.java`). |
| **1.4** | Atributo `ArrayList` | `modelo.Jogador`, `modelo.Pokemon`, `modelo.Inventario` | `ArrayList<Pokemon> equipe`, `ArrayList<Golpe> golpes`, `ArrayList<T> itensArmazenados`. |
| **1.5** | Constante primitiva usada noutra classe | `jogo.Mapa` $\rightarrow$ `jogo.PainelMapa` | `public static final int TAMANHO_TILE = 20;` definida em `Mapa` e usada em `PainelMapa`. |
| **2.1** | Método com *Varargs* e laço `for` | `modelo.Batalha` | `public void curarEquipeParaCombate(Pokemon... pokemons)` com `for (Pokemon p : pokemons)`. |
| **2.2** | Método `static` útil (não `main`) | `modelo.CalculadoraDano` | `public static float obterMultiplicadorElemental(...)` e `calcularDano(...)`. |
| **3.1** | Construtor em $\ge 2$ classes concretas | `Pokemon`, `Jogador`, `Item`, `Batalha`, `Mapa` | Construtores parametrizados explicitamente declarados e invocados. |
| **4.1** | Associação por Composição | `jogo.PainelMapa` $\rightarrow$ `jogo.Mapa` | `PainelMapa` (Todo) instancia `this.mapa = new Mapa();` (Parte) em seu construtor. |
| **5.1** | Associação por Agregação | `modelo.Jogador` $\diamond\rightarrow$ `modelo.PokemonInicial` | Construtor `public Jogador(String nome, int x, int y, PokemonInicial pokemonInicial)`. |
| **6.1** | Atributo `final` em Superclasse Concreta | `modelo.Pokemon` | `private final String CATEGORIA_CRIACAO = "Criatura JuaMon";` (diferente do item 1.5). |
| **6.2** | Acoplamento Dinâmico (3 classes) | `Pokemon` (Pai), `PokemonInicial` (Filha), `Batalha` (3ª) | `parceiroPikachu.emitirGrito()` chamado em `Batalha.obterAnuncioDeEntrada()`. |
| **7.1** | Polimorfismo de Sobrecarga | `modelo.Batalha` | `executarGolpeTurno(..., int indiceGolpe)` e `executarGolpeTurno(..., Golpe golpe)`. |
| **7.2** | Polimorfismo de Coerção (*Casting*) | `modelo.CalculadoraDano` | `int danoFinal = (int) danoFlutuante;` dentro do método `calcularDano(...)`. |
| **7.3** | Polimorfismo de Subtipagem | `modelo.Batalha` (`Item` $\rightarrow$ `Pokebola`/`Pocao`) | `utilizarItemPolimorfico(Item item, Pokemon alvo)` e `obterTextoStatus(Pokemon)`. |
| **7.4** | Polimorfismo Paramétrico (*Generics*) | `modelo.Inventario<T>` em `modelo.Jogador` | `Inventario<Pokebola>` e `Inventario<Pocao>` usando `armazenarItem(T)` e `retirarItem()`. |
| **8.1** | Interfaces (3 classes, 1 com 2 interfaces) | `PokemonSelvagem`, `PokemonInicial`, `Jogador` | `PokemonSelvagem` implementa `Combatente` e `Capturavel`; os outros 2 implementam `Combatente`. |
| **9.1** | Exatamente Dois Pacotes | ` package jogo;` e `package modelo;` | 100% dos arquivos `.java` divididos exclusivamente entre `jogo` e `modelo`. |
| **10.1** | Duas Classes Abstratas | `modelo.Criatura` e `modelo.Golpe` | Métodos abstratos `gerarResumoStatus()` e `aplicarGolpe(...)` implementados nas filhas. |
| **11.1** | Exceção criada, levantada e tratada | `CapturaInvalidaException`, `Batalha`, `TelaBatalha` | Criada no `modelo`, lançada com `throw` em `Batalha` e tratada com `try-catch` em `TelaBatalha`. |
| **12.1** | Entrada de Dados via Caixa de Diálogo | `jogo.TelaBatalha` | `JOptionPane.showInputDialog(...)` no método `abrirDialogoTrocaPokemon()`. |
| **13.1** | Arquivos (Salvar, recuperar e usar) | `jogo.GerenciadorArquivo` e `jogo.PainelMapa` | Salva com `F5` e recarrega automaticamente de `saves/save_jogador.txt` ao iniciar o jogo. |

---

## 📝 Textos Prontos para Colar nas Notas (Post-its) do Diagrama de Classes UML

Ao montar o Diagrama de Classes no *Draw.io / Lucidchart*, você pode copiar e colar exatamente estes blocos de comentários apontando para as classes correspondentes:

1. **Nota apontando para `Pokemon`:**
   > **ITEM 1.1 (Encapsulamento):** Todos os atributos `private`.
   > **ITEM 1.2 (String, int, float):** `String nome`, `int hpMaximo`, `float multiplicadorAtaque`.
   > **ITEM 1.4 (ArrayList):** `private ArrayList<Golpe> golpes`.
   > **ITEM 6.1 (Atributo final em Superclasse Concreta):** `private final String CATEGORIA_CRIACAO = "Criatura JuaMon"`.
   > **ITEM 6.2 - Parte 1 (Acoplamento Dinâmico):** Superclasse concreta que define `emitirGrito()`.

2. **Nota apontando para `PokemonInicial` e `Batalha`:**
   > **ITEM 6.2 - Partes 2 e 3 (Acoplamento Dinâmico):** `PokemonInicial` herda de `Pokemon` e NÃO sobrescreve `emitirGrito()`. A 3ª classe (`Batalha`) possui o atributo `private PokemonInicial parceiroPikachu` (instanciado como `PokemonInicial`) e invoca `parceiroPikachu.emitirGrito()`.

3. **Nota apontando para `PainelMapa` e `Mapa`:**
   > **ITEM 4.1 (Composição):** O Todo (`PainelMapa`) instancia a Parte (`this.mapa = new Mapa()`) diretamente em seu construtor.
   > **ITEM 1.5 (Constante Primitiva):** `Mapa` define `public static final int TAMANHO_TILE = 20`, consumida por `PainelMapa`.

4. **Nota apontando para `Jogador` e `PokemonInicial`:**
   > **ITEM 5.1 (Agregação):** `Jogador` recebe a instância externa de `PokemonInicial` em seu construtor `Jogador(String, int, int, PokemonInicial)`.
   > **ITEM 1.3 (Enum):** `Jogador` possui `private Direcao direcao`.

5. **Nota apontando para `Inventario<T>` e `Jogador`:**
   > **ITEM 7.4 (Polimorfismo Paramétrico):** Classe concreta genérica `Inventario<T>`. O tipo é parametrizado explicitamente fora do construtor nos atributos `Inventario<Pokebola>` e `Inventario<Pocao>` de `Jogador` e manipulado via `armazenarItem(T)` e `T retirarItem()`.

6. **Nota apontando para `Item`, `Pokebola`, `Pocao` e `Batalha`:**
   > **ITEM 7.3 (Polimorfismo de Subtipagem):** `Item` (pai concreto) é sobrescrito por `Pokebola` e `Pocao` (filhos concretos). `Batalha.utilizarItemPolimorfico(Item, Pokemon)` opera polimorficamente qualquer subtipo.
   > **ITEM 3.1 (Construtores):** `Item`, `Jogador`, `Pokemon`, `Batalha` e `Mapa` possuem construtores explícitos.

7. **Nota apontando para `Batalha` e `CalculadoraDano`:**
   > **ITEM 2.1 (Varargs com for):** `Batalha.curarEquipeParaCombate(Pokemon... pokemons)` percorre o vetor com `for`.
   > **ITEM 7.1 (Sobrecarga):** Duas assinaturas de `executarGolpeTurno(...)` em `Batalha`.
   > **ITEM 2.2 (Método Estático) e ITEM 7.2 (Coerção/Casting):** `CalculadoraDano.calcularDano(...)` é estático e realiza `int danoFinal = (int) danoFlutuante;`.

8. **Nota apontando para `Combatente` e `Capturavel`:**
   > **ITEM 8.1 (Interfaces):** 3 classes implementam interfaces: `Jogador` (`Combatente`), `PokemonInicial` (`Combatente`) e `PokemonSelvagem` (**duas interfaces**: `Combatente` e `Capturavel`).

9. **Nota apontando para `Criatura` e `Golpe`:**
   > **ITEM 10.1 (Classes Abstratas):** `Criatura` (método abstrato `gerarResumoStatus()`) e `Golpe` (método abstrato `aplicarGolpe()`, implementado por `GolpeDano` e `GolpeBuff`).

10. **Nota apontando para `CapturaInvalidaException`, `Batalha` e `TelaBatalha`:**
    > **ITEM 11.1 (Exceção):** Criada em `CapturaInvalidaException`, levantada com `throw` em `Batalha` e tratada com `try-catch` em `TelaBatalha`.
    > **ITEM 12.1 (Caixa de Diálogo):** `TelaBatalha.abrirDialogoTrocaPokemon()` captura entrada do usuário via `JOptionPane.showInputDialog`.

11. **Nota apontando para `GerenciadorArquivo`:**
    > **ITEM 13.1 (Arquivos):** Grava os dados do jogador em `saves/save_jogador.txt` (tecla `F5`) e reconstrói o objeto `Jogador` e sua equipe ao iniciar a partida.