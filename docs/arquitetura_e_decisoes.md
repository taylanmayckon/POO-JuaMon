# 🧠 Arquitetura, Regras de Negócio e Decisões de Projeto — JuaMon

Este documento detalha as decisões arquiteturais tomadas pela equipe durante o desenvolvimento do MVP do **JuaMon**, justificando como os conceitos de Programação Orientada a Objetos foram integrados às mecânicas do jogo.

---

## 1. Filosofia do MVP (Minimum Viable Product)

Para garantir um produto coeso, livre de bugs de compilação e 100% aderente às 22 exigências da disciplina de POO, a equipe estabeleceu quatro pilares de design:

1. **Separação Estrita em Dois Pacotes (`jogo` vs `modelo`):**
   * Nenhuma classe do pacote `modelo` importa bibliotecas gráficas (`javax.swing` ou `java.awt`). Isso garante que toda a lógica de dano, captura, herança e inventário seja pura e independente da interface.
2. **Fábrica Centralizada de Criaturas (`CalculadoraDano`):**
   * Em vez de criar 10 classes `.java` quase idênticas (uma para cada espécie de Pokémon), que poluiriam o pacote `modelo` sem agregar comportamento novo de POO, optamos por diferenciar as classes pelo **papel de domínio**: `PokemonInicial` (parceiro fixo que implementa apenas `Combatente`) e `PokemonSelvagem` (criatura da floresta que implementa `Combatente` e `Capturavel`). A variedade de espécies (IDs 1 a 10 da geração Unova + Pikachu) é gerenciada por métodos estáticos de criação em `CalculadoraDano`.
3. **Resiliência de Recursos (Fallbacks Gráficos e Sonoros):**
   * Tanto o `PainelArena` (em `TelaBatalha.java`) quanto o `GerenciadorAudio.java` foram projetados com carregamento defensivo. Caso um arquivo `.png` ou `.wav` não seja encontrado no *Classpath*, o jogo jamais lança `NullPointerException`: na arena, desenha-se um avatar vetorial reserva; no áudio, a execução prossegue silenciosamente.
4. **Movimentação Contínua Baseada em Game Loop (`javax.swing.Timer`):**
   * Eventos `keyPressed` padrão do sistema operacional possuem um atraso inicial de repetição (~250ms a 500ms). Para eliminar esse *delay* e calibrar a velocidade de caminhada e a troca de quadros (*frames*) do personagem, implementamos flags booleanas de direção atualizadas por um `Timer` constante de 20ms (50 FPS).

---

## 2. Mecânicas de Combate e Regras de Negócio

### 2.1. Os 4 Movimentos de Cada Pokémon (Hierarquia Abstrata `Golpe`)
Todo Pokémon da Pool nasce com exatamente **4 movimentos** armazenados em `ArrayList<Golpe>`:
* **Movimentos 1, 2 e 3 (`GolpeDano`):** Causam dano direto ao alvo. O cálculo leva em consideração o Ataque do usuário, o Poder do golpe, a Defesa do alvo, o Multiplicador Elemental (`1.5f` para super efetivo, `0.5f` para não muito efetivo, `1.0f` neutro) e o Multiplicador de Buff acumulado.
* **Movimento 4 (`GolpeBuff`):** Movimento tático que incrementa o atributo `float multiplicadorAtaque` do próprio Pokémon em `+0.20f` ou `+0.25f`, permitindo derrotar oponentes mais resistentes (como *Roggenrola*).

### 2.2. Regra de Captura Determinística (Interface `Capturavel`)
Para tornar os testes e a apresentação em sala de aula claros e previsíveis, a implementação de `tentarCaptura()` em `PokemonSelvagem` segue a regra direta do MVP:
* **HP Atual $\le 20\%$ do HP Máximo:** **100% de chance de captura.** A barra de vida na `TelaBatalha` muda automaticamente para a cor **vermelha**, sinalizando visualmente ao jogador o momento exato de lançar a Pokébola.
* **HP Atual $> 20\%$:** **0% de chance de captura.** A Pokébola é consumida do `Inventario<Pokebola>`, mas o Pokémon escapa e contra-ataca no turno seguinte.

### 2.3. Restauração Pós-Combate (Varargs e Ciclo de Jogo)
* **Início da Batalha:** O construtor de `Batalha` aciona `curarEquipeParaCombate(Pokemon... pokemons)`, garantindo que toda a equipe do treinador inicie o confronto com 100% de HP e buffs zerados (`1.0f`).
* **Fim da Batalha:** O método sobrescrito `dispose()` da `TelaBatalha` restaura automaticamente o bolso de Poções (`jogador.setPocoes(3)`) para o próximo encontro, mantendo o consumo real das Pokébolas.

---

## 3. Formato de Persistência (`saves/save_jogador.txt`)

O arquivo gerado pela classe `GerenciadorArquivo` ao pressionar **`F5`** utiliza uma estrutura `chave=valor` legível por humanos:

```ini
nome=Treinador
x=320
y=240
pokebolas=4
pocoes=3
pokemon=2,95
pokemon=6,80
```

* As chaves `x` e `y` guardam a posição exata em pixels no mapa da floresta.
* As chaves `pokebolas` e `pocoes` reconstroem a quantidade exata de objetos dentro de `Inventario<Pokebola>` e `Inventario<Pocao>`.
* Cada linha `pokemon=ID,HP_ATUAL` instrui o `GerenciadorArquivo` a consultar a fábrica `CalculadoraDano.criarPokemonSelvagemPorId(ID)`, restaurar seu HP e adicioná-lo à equipe do jogador.