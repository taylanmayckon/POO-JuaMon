package jogo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import modelo.Batalha;
import modelo.CapturaInvalidaException;
import modelo.Golpe;
import modelo.Jogador;
import modelo.Pokemon;
import modelo.PokemonSelvagem;

/**
 * Interface visual refinada do combate por turnos (800x600).
 * Modularizada para facilitar a inserção de sprites, fundos customizados e áudios.
 */
public class TelaBatalha extends JDialog {

    // === CORES TEMÁTICAS (Estilo Game Boy / Modern UI) ===
    private static final Color COR_FUNDO_PAINEL = new Color(24, 28, 36);
    private static final Color COR_CARD_HUD = new Color(35, 42, 54, 225);
    private static final Color COR_BORDA_CARD = new Color(74, 85, 104);
    private static final Color COR_TEXTO_LOG = new Color(230, 235, 245);
    private static final Color COR_FUNDO_LOG = new Color(18, 20, 26);
    private static final Color COR_HP_VERDE = new Color(46, 204, 113);
    private static final Color COR_HP_AMARELO = new Color(241, 196, 15);
    private static final Color COR_HP_VERMELHO = new Color(231, 76, 60);

    // ITEM 1.1 - ENCAPSULAMENTO
    private Batalha batalha;
    private Jogador jogador;
    private PokemonSelvagem inimigo;
    private Pokemon pokemonAtivoJogador;

    // Controle de fluxo e turnos
    private int contadorTurno = 1;
    private boolean turnoIniciado = false;
    private boolean combateEncerrado = false;

    // Componentes Visuais Principais
    private PainelArena painelArena;
    private JTextArea areaLog;
    private JPanel painelDireitaAcoes;
    private JPanel painelMenuPrincipal;
    private JPanel painelMenuAtaques;
    private JPanel painelMenuMochila;

    // Elementos do HUD
    private JLabel labelNomeInimigo;
    private JLabel labelTipoInimigo;
    private JProgressBar barraHpInimigo;
    private JLabel labelHpTextoInimigo;

    private JLabel labelNomeJogador;
    private JLabel labelTipoJogador;
    private JProgressBar barraHpJogador;
    private JLabel labelHpTextoJogador;

    // Botões Principais
    private JButton btnLutar;
    private JButton btnMochila;
    private JButton btnTrocar;
    private JButton btnFugir;
    private JButton btnUsarPokebola;
    private JButton btnUsarPocao;
    private JButton[] botoesGolpes;

    /**
     * ITEM 3.1 - Construtor declarado em classe concreta.
     */
    public TelaBatalha(JFrame janelaPai, Jogador jogador, PokemonSelvagem inimigo) {
        super(janelaPai, "JuaMon — Arena de Batalha", true);
        this.jogador = jogador;
        this.inimigo = inimigo;
        this.pokemonAtivoJogador = jogador.getPokemonAtivo();
        this.batalha = new Batalha(jogador, inimigo);

        configurarJanela();
        inicializarComponentes();
        atualizarHuds();

        // Anúncio inicial com grito do parceiro (Item 6.2 - Acoplamento Dinâmico)
        adicionarSeparadorTurno(1);
        adicionarLinhaLog("⚔️ " + batalha.obterAnuncioDeEntrada());
        adicionarLinhaLog("O que " + pokemonAtivoJogador.getNome() + " fará?");
    }

    private void configurarJanela() {
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    }

    private void inicializarComponentes() {
        // 1. ARENA DE BATALHA (Topo / Centro: 800x380)
        painelArena = new PainelArena();
        painelArena.setPreferredSize(new Dimension(800, 380));
        painelArena.setLayout(null);
        montarHudsArena();
        add(painelArena, BorderLayout.CENTER);

        // 2. PAINEL DE COMANDOS INFERIOR (800x220)
        JPanel painelComandos = new JPanel(new GridLayout(1, 2, 10, 0));
        painelComandos.setPreferredSize(new Dimension(800, 190));
        painelComandos.setBackground(COR_FUNDO_PAINEL);
        painelComandos.setBorder(BorderFactory.createEmptyBorder(10, 12, 12, 12));

        // 2.1. Caixa de Histórico de Batalha (Lado Esquerdo)
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setFont(new Font("Consolas", Font.BOLD, 12));
        areaLog.setForeground(COR_TEXTO_LOG);
        areaLog.setBackground(COR_FUNDO_LOG);
        areaLog.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createLineBorder(COR_BORDA_CARD, 2));
        painelComandos.add(scrollLog);

        // 2.2. Painel Dinâmico de Ações (Lado Direito)
        painelDireitaAcoes = new JPanel(new BorderLayout());
        painelDireitaAcoes.setOpaque(false);

        construirMenuPrincipal();
        construirMenuAtaques();
        construirMenuMochila();

        painelDireitaAcoes.add(painelMenuPrincipal, BorderLayout.CENTER);
        painelComandos.add(painelDireitaAcoes);

        add(painelComandos, BorderLayout.SOUTH);
    }

    private void montarHudsArena() {
        // --- HUD DO INIMIGO (Superior Esquerdo) ---
        JPanel cardInimigo = criarCardHud();
        cardInimigo.setBounds(35, 25, 290, 85);
        cardInimigo.setLayout(new GridLayout(3, 1, 2, 2));

        JPanel linha1Inimigo = new JPanel(new BorderLayout());
        linha1Inimigo.setOpaque(false);
        labelNomeInimigo = new JLabel(inimigo.getNome());
        labelNomeInimigo.setFont(new Font("SansSerif", Font.BOLD, 15));
        labelNomeInimigo.setForeground(Color.WHITE);
        labelTipoInimigo = new JLabel("[" + inimigo.getTipo() + "]");
        labelTipoInimigo.setFont(new Font("SansSerif", Font.PLAIN, 11));
        labelTipoInimigo.setForeground(new Color(180, 190, 205));
        linha1Inimigo.add(labelNomeInimigo, BorderLayout.WEST);
        linha1Inimigo.add(labelTipoInimigo, BorderLayout.EAST);
        cardInimigo.add(linha1Inimigo);

        barraHpInimigo = new JProgressBar(0, inimigo.getHpMaximo());
        barraHpInimigo.setValue(inimigo.getHpAtual());
        barraHpInimigo.setPreferredSize(new Dimension(270, 14));
        barraHpInimigo.setBorderPainted(false);
        cardInimigo.add(barraHpInimigo);

        labelHpTextoInimigo = new JLabel("", SwingConstants.RIGHT);
        labelHpTextoInimigo.setFont(new Font("SansSerif", Font.BOLD, 11));
        labelHpTextoInimigo.setForeground(new Color(200, 210, 220));
        cardInimigo.add(labelHpTextoInimigo);

        painelArena.add(cardInimigo);

        // --- HUD DO JOGADOR (Inferior Direito) ---
        JPanel cardJogador = criarCardHud();
        cardJogador.setBounds(475, 235, 290, 85);
        cardJogador.setLayout(new GridLayout(3, 1, 2, 2));

        JPanel linha1Jogador = new JPanel(new BorderLayout());
        linha1Jogador.setOpaque(false);
        labelNomeJogador = new JLabel(pokemonAtivoJogador.getNome());
        labelNomeJogador.setFont(new Font("SansSerif", Font.BOLD, 15));
        labelNomeJogador.setForeground(Color.WHITE);
        labelTipoJogador = new JLabel("[" + pokemonAtivoJogador.getTipo() + "]");
        labelTipoJogador.setFont(new Font("SansSerif", Font.PLAIN, 11));
        labelTipoJogador.setForeground(new Color(180, 190, 205));
        linha1Jogador.add(labelNomeJogador, BorderLayout.WEST);
        linha1Jogador.add(labelTipoJogador, BorderLayout.EAST);
        cardJogador.add(linha1Jogador);

        barraHpJogador = new JProgressBar(0, pokemonAtivoJogador.getHpMaximo());
        barraHpJogador.setValue(pokemonAtivoJogador.getHpAtual());
        barraHpJogador.setPreferredSize(new Dimension(270, 14));
        barraHpJogador.setBorderPainted(false);
        cardJogador.add(barraHpJogador);

        labelHpTextoJogador = new JLabel("", SwingConstants.RIGHT);
        labelHpTextoJogador.setFont(new Font("SansSerif", Font.BOLD, 11));
        labelHpTextoJogador.setForeground(Color.WHITE);
        cardJogador.add(labelHpTextoJogador);

        painelArena.add(cardJogador);
    }

    private JPanel criarCardHud() {
        JPanel painel = new JPanel();
        painel.setBackground(COR_CARD_HUD);
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COR_BORDA_CARD, 2, true),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        return painel;
    }

    private void construirMenuPrincipal() {
        painelMenuPrincipal = new JPanel(new GridLayout(2, 2, 8, 8));
        painelMenuPrincipal.setOpaque(false);

        btnLutar = criarBotaoEstilizado("⚔️ LUTAR", new Color(220, 53, 69));
        btnLutar.addActionListener(e -> alternarMenu(true));

        btnMochila = criarBotaoEstilizado("🎒 MOCHILA", new Color(40, 167, 69));
        btnMochila.addActionListener(e -> alternarParaMenuMochila());

        btnTrocar = criarBotaoEstilizado("🔄 POKÉMON", new Color(23, 162, 184));
        btnTrocar.addActionListener(e -> abrirDialogoTrocaPokemon());

        btnFugir = criarBotaoEstilizado("🏃 FUGIR", new Color(108, 117, 125));
        btnFugir.addActionListener(e -> fugirCombate());

        painelMenuPrincipal.add(btnLutar);
        painelMenuPrincipal.add(btnMochila);
        painelMenuPrincipal.add(btnTrocar);
        painelMenuPrincipal.add(btnFugir);
    }

    private void construirMenuAtaques() {
        painelMenuAtaques = new JPanel(new GridLayout(3, 2, 6, 6));
        painelMenuAtaques.setOpaque(false);

        ArrayList<Golpe> golpes = pokemonAtivoJogador.getGolpes();
        botoesGolpes = new JButton[4];

        for (int i = 0; i < 4; i++) {
            final int indice = i;
            if (i < golpes.size()) {
                Golpe g = golpes.get(i);
                // Destaque dourado para o 4º ataque (Buff)
                Color corBotao = (i == 3) ? new Color(212, 160, 23) : new Color(225, 112, 85);
                JButton btn = criarBotaoEstilizado(g.getNome() + "\n[" + g.getTipo() + "]", corBotao);
                btn.setFont(new Font("SansSerif", Font.BOLD, 11));
                btn.addActionListener(e -> processarTurnoJogador(indice));
                botoesGolpes[i] = btn;
                painelMenuAtaques.add(btn);
            } else {
                JButton btnVazio = new JButton("-");
                btnVazio.setEnabled(false);
                painelMenuAtaques.add(btnVazio);
            }
        }

        JButton btnVoltar = criarBotaoEstilizado("⬅ VOLTAR", new Color(99, 110, 114));
        btnVoltar.addActionListener(e -> alternarMenu(false));
        painelMenuAtaques.add(btnVoltar);
    }

    /**
     * Constrói o submenu visual do Inventário Genérico (Item 7.4) diretamente no painel direito.
     */
    private void construirMenuMochila() {
        painelMenuMochila = new JPanel(new GridLayout(3, 1, 6, 6));
        painelMenuMochila.setOpaque(false);

        btnUsarPokebola = criarBotaoEstilizado(
                "🔴 LANÇAR POKÉBOLA (Estoque: " + jogador.getPokebolas() + ")",
                new Color(225, 65, 65)
        );
        btnUsarPokebola.addActionListener(e -> {
            alternarMenu(false);
            executarTentativaCaptura();
        });

        btnUsarPocao = criarBotaoEstilizado(
                "🧪 USAR POÇÃO +35 HP (Estoque: " + jogador.getPocoes() + ")",
                new Color(46, 160, 110)
        );
        btnUsarPocao.addActionListener(e -> {
            alternarMenu(false);
            executarUsoDePocao();
        });

        JButton btnVoltarMochila = criarBotaoEstilizado("⬅ VOLTAR AO MENU", new Color(99, 110, 114));
        btnVoltarMochila.addActionListener(e -> alternarMenu(false));

        painelMenuMochila.add(btnUsarPokebola);
        painelMenuMochila.add(btnUsarPocao);
        painelMenuMochila.add(btnVoltarMochila);
    }

    private void alternarParaMenuMochila() {
        if (combateEncerrado) return;
        atualizarBotoesMochila();
        painelDireitaAcoes.removeAll();
        painelDireitaAcoes.add(painelMenuMochila, BorderLayout.CENTER);
        painelDireitaAcoes.revalidate();
        painelDireitaAcoes.repaint();
    }

    private void atualizarBotoesMochila() {
        if (btnUsarPokebola != null) {
            btnUsarPokebola.setText("<html><center>🔴 LANÇAR POKÉBOLA (Estoque: " + jogador.getPokebolas() + ")</center></html>");
        }
        if (btnUsarPocao != null) {
            btnUsarPocao.setText("<html><center>🧪 USAR POÇÃO +35 HP (Estoque: " + jogador.getPocoes() + ")</center></html>");
        }
    }

    private JButton criarBotaoEstilizado(String texto, Color corFundo) {
        JButton btn = new JButton("<html><center>" + texto.replace("\n", "<br>") + "</center></html>");
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setBackground(corFundo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(corFundo.darker(), 2, true));
        return btn;
    }

    private void alternarMenu(boolean exibirAtaques) {
        painelDireitaAcoes.removeAll();
        if (exibirAtaques) {
            painelDireitaAcoes.add(painelMenuAtaques, BorderLayout.CENTER);
        } else {
            painelDireitaAcoes.add(painelMenuPrincipal, BorderLayout.CENTER);
        }
        painelDireitaAcoes.revalidate();
        painelDireitaAcoes.repaint();
    }

    private void processarTurnoJogador(int indiceGolpe) {
        if (combateEncerrado) return;

        contadorTurno++;
        adicionarSeparadorTurno(contadorTurno);

        // Turno do Jogador: usa Sobrecarga (Item 7.1)
        String logJogador = batalha.executarGolpeTurno(pokemonAtivoJogador, inimigo, indiceGolpe);
        adicionarLinhaLog("▶ " + logJogador);
        atualizarHuds();

        // Checagem de vitória
        if (inimigo.estaDesmaiado()) {
            adicionarLinhaLog("🏆 O " + inimigo.getNome() + " selvagem desmaiou!");
            adicionarLinhaLog("Parabéns! Você venceu o confronto!");
            finalizarComDialogo("Vitória! " + inimigo.getNome() + " selvagem foi derrotado!");
            return;
        }

        // Volta ao menu inicial e executa a resposta do adversário
        alternarMenu(false);
        bloquearControles(true);
        SwingUtilities.invokeLater(this::processarTurnoInimigo);
    }

    private void processarTurnoInimigo() {
        if (combateEncerrado) return;

        int qtdGolpes = inimigo.getGolpes().size();
        int golpeSorteado = (int) (Math.random() * qtdGolpes);

        String logInimigo = batalha.executarGolpeTurno(inimigo, pokemonAtivoJogador, golpeSorteado);
        adicionarLinhaLog("⚠️ Inimigo: " + logInimigo);
        atualizarHuds();

        if (pokemonAtivoJogador.estaDesmaiado()) {
            adicionarLinhaLog("💀 " + pokemonAtivoJogador.getNome() + " desmaiou!");
            
            // Verifica se o jogador possui outro Pokémon com vida para lutar
            if (jogador.temCondicaoDeLutar()) {
                adicionarLinhaLog("Escolha outro Pokémon para continuar!");
                JOptionPane.showMessageDialog(this,
                        pokemonAtivoJogador.getNome() + " desmaiou! Escolha outro companheiro.",
                        "Troca Obrigatória", JOptionPane.WARNING_MESSAGE);
                abrirDialogoTrocaPokemon();
                bloquearControles(false);
            } else {
                adicionarLinhaLog("Todos os seus Pokémon desmaiaram...");
                finalizarComDialogo("Você não tem mais Pokémon para lutar! A batalha terminou.");
            }
        } else {
            bloquearControles(false);
        }
    }

    /**
     * Permite trocar o Pokémon ativo no meio da batalha por outro membro vivo da equipe.
     */
    private void abrirDialogoTrocaPokemon() {
        ArrayList<Pokemon> equipe = jogador.getEquipe();
        ArrayList<Pokemon> aptos = new ArrayList<>();
        ArrayList<String> nomesOpcoes = new ArrayList<>();

        for (Pokemon p : equipe) {
            if (p != pokemonAtivoJogador && !p.estaDesmaiado()) {
                aptos.add(p);
                nomesOpcoes.add(p.getNome() + " (HP: " + p.getHpAtual() + "/" + p.getHpMaximo() + ")");
            }
        }

        if (aptos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Você não possui outro Pokémon apto para entrar na batalha!",
                    "Sem Opções de Troca", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String escolha = (String) JOptionPane.showInputDialog(
                this,
                "Selecione o Pokémon que entrará no combate:",
                "Troca de Pokémon",
                JOptionPane.QUESTION_MESSAGE,
                null,
                nomesOpcoes.toArray(),
                nomesOpcoes.get(0)
        );

        if (escolha != null) {
            int indiceEscolhido = nomesOpcoes.indexOf(escolha);
            Pokemon anterior = pokemonAtivoJogador;
            pokemonAtivoJogador = aptos.get(indiceEscolhido);

            contadorTurno++;
            adicionarSeparadorTurno(contadorTurno);
            adicionarLinhaLog("🔄 Você recolheu " + anterior.getNome() + " e enviou " + pokemonAtivoJogador.getNome() + "!");

            // Reconstrói o menu de golpes para o novo Pokémon
            construirMenuAtaques();
            atualizarHuds();
            painelArena.recarregarSprites();

            // A troca gasta o turno do jogador; o inimigo ataca em seguida!
            bloquearControles(true);
            SwingUtilities.invokeLater(this::processarTurnoInimigo);
        }
    }

    /**
     * Executa o uso de uma Poção de Vida retirada do Inventario<Pocao> (Itens 7.3, 7.4 e 11.1).
     */
    private void executarUsoDePocao() {
        if (combateEncerrado) return;

        contadorTurno++;
        adicionarSeparadorTurno(contadorTurno);

        try {
            String resultadoCura = batalha.usarPocaoNoPokemon(pokemonAtivoJogador);
            adicionarLinhaLog("🧪 " + resultadoCura);
            atualizarHuds();

            // Usar item na batalha consome o turno do jogador; o inimigo ataca em seguida
            bloquearControles(true);
            SwingUtilities.invokeLater(this::processarTurnoInimigo);
        } catch (CapturaInvalidaException ex) {
            // ITEM 11.1 - Registro explícito no console/terminal
            System.err.println("\n[TERMINAL - EXCECAO TRATADA] " + ex.getClass().getSimpleName() + ": " + ex.getMessage());

            adicionarLinhaLog("⚠️ Inventário vazio: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Mochila — Sem Poções", JOptionPane.ERROR_MESSAGE);
        }
    }

    /*
     * ITEM 11.1 - EXCEÇÃO PRÓPRIA (Tratada com bloco try-catch)
     *
     * Justificativa: A classe CapturaInvalidaException (criada no pacote modelo herdando de Exception)
     * é levantada com o comando 'throw' em Batalha.java quando o método tentarCapturarSelvagem() 
     * detecta que o estoque de Pokébolas do jogador se esgotou (pokebolas <= 0).
     *
     * Aqui na TelaBatalha.java, o bloco try-catch intercepta a exceção e impede que a aplicação 
     * apresente erros não tratados em tempo de execução, notificando o usuário por meio de uma
     * caixa de diálogo de erro amigável.
     */
    private void executarTentativaCaptura() {
        if (combateEncerrado) return;

        contadorTurno++;
        adicionarSeparadorTurno(contadorTurno);

        try {
            adicionarLinhaLog("🔴 Você arremessou uma Pokébola!");
            boolean capturado = batalha.tentarCapturarSelvagem();

            if (capturado) {
                adicionarLinhaLog("🎉 SUCESSO! " + inimigo.getNome() + " selvagem foi capturado!");
                adicionarLinhaLog(inimigo.getNome() + " foi adicionado à sua equipe!");
                atualizarHuds();
                JOptionPane.showMessageDialog(this,
                        "Incrível! O " + inimigo.getNome() + " foi capturado com sucesso e entrou na sua equipe!",
                        "Captura Bem-Sucedida", JOptionPane.INFORMATION_MESSAGE);
                combateEncerrado = true;
                dispose();
            } else {
                adicionarLinhaLog("❌ A Pokébola quebrou! " + inimigo.getNome() + " escapou!");
                adicionarLinhaLog("💡 Dica: Reduza a vida dele para 20% ou menos para ter 100% de chance!");
                atualizarHuds();
                JOptionPane.showMessageDialog(this,
                        "O " + inimigo.getNome() + " escapou da Pokébola!\n"
                        + "Dica: Reduza o HP dele para menos de 20% para garantir a captura!",
                        "Captura Falhou", JOptionPane.WARNING_MESSAGE);

                // Como a captura falhou, o turno é consumido e o inimigo ataca em seguida
                bloquearControles(true);
                SwingUtilities.invokeLater(this::processarTurnoInimigo);
            }
        } catch (CapturaInvalidaException ex) {
            // ITEM 11.1 - Registro explícito no console/terminal
            System.err.println("\n=======================================================");
            System.err.println("[TERMINAL - EXCECAO CAPTURADA E TRATADA COM TRY-CATCH]");
            System.err.println("Classe da Excecao: " + ex.getClass().getName());
            System.err.println("Mensagem de Erro : " + ex.getMessage());
            System.err.println("=======================================================\n");

            // ITEM 11.1 - Tratamento da exceção: exibe mensagem explicativa sem encerrar o jogo
            adicionarLinhaLog("⚠️ Erro de Captura: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    ex.getMessage() + "\nVocê precisa de Pokébolas na mochila para capturar!",
                    "Atenção — Sem Pokébolas", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fugirCombate() {
        adicionarLinhaLog("💨 Você escapou com sucesso da batalha!");
        JOptionPane.showMessageDialog(this, "Você fugiu da batalha em segurança!", "Fuga", JOptionPane.INFORMATION_MESSAGE);
        combateEncerrado = true;
        dispose();
    }

    /**
     * Sobrescreve o fechamento da janela de batalha para garantir que,
     * independentemente do desfecho (vitória, derrota, fuga ou captura),
     * o estoque de Poções do jogador seja restaurado para 3 unidades.
     */
    @Override
    public void dispose() {
        if (jogador != null) {
            jogador.setPocoes(3);
        }
        super.dispose();
    }

    private void finalizarComDialogo(String msg) {
        combateEncerrado = true;
        bloquearControles(true);
        JOptionPane.showMessageDialog(this, msg, "Fim de Batalha", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private void bloquearControles(boolean bloquear) {
        btnLutar.setEnabled(!bloquear);
        btnMochila.setEnabled(!bloquear);
        btnTrocar.setEnabled(!bloquear);
        btnFugir.setEnabled(!bloquear);
    }

    private void atualizarHuds() {
        // Atualiza Informações do Inimigo
        labelNomeInimigo.setText(inimigo.getNome());
        labelTipoInimigo.setText("[" + inimigo.getTipo() + "]");
        barraHpInimigo.setValue(inimigo.getHpAtual());
        ajustarGradienteBarra(barraHpInimigo, inimigo.obterPorcentagemHp());
        labelHpTextoInimigo.setText("HP: " + inimigo.getHpAtual() + " / " + inimigo.getHpMaximo()
                + String.format(" (%.0f%%)", inimigo.obterPorcentagemHp()));

        // Atualiza Informações do Jogador
        labelNomeJogador.setText(pokemonAtivoJogador.getNome());
        labelTipoJogador.setText("[" + pokemonAtivoJogador.getTipo() + "]");
        barraHpJogador.setValue(pokemonAtivoJogador.getHpAtual());
        ajustarGradienteBarra(barraHpJogador, pokemonAtivoJogador.obterPorcentagemHp());

        String buffTexto = (pokemonAtivoJogador.getMultiplicadorAtaque() > 1.0f)
                ? String.format(" [ATQ: %.2fx]", pokemonAtivoJogador.getMultiplicadorAtaque()) : "";
        labelHpTextoJogador.setText("HP: " + pokemonAtivoJogador.getHpAtual() + " / " + pokemonAtivoJogador.getHpMaximo() + buffTexto);

        atualizarBotoesMochila();
        painelArena.repaint();
    }

    private void ajustarGradienteBarra(JProgressBar barra, float pct) {
        if (pct <= 20.0f) {
            barra.setForeground(COR_HP_VERMELHO); // Alerta / Ponto de Captura
        } else if (pct <= 50.0f) {
            barra.setForeground(COR_HP_AMARELO);
        } else {
            barra.setForeground(COR_HP_VERDE);
        }
    }

    private void adicionarSeparadorTurno(int turno) {
        areaLog.append("\n================ [ TURNO " + turno + " ] ================\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    private void adicionarLinhaLog(String texto) {
        areaLog.append("  " + texto + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    /**
     * Arena de Batalha modularizada com posições constantes para renderização de sprites e planos de fundo.
     */
    private class PainelArena extends JPanel {

        // Constantes visuais para calibrar facilmente as posições e proporções das bases
        private static final int INIMIGO_X = 520;
        private static final int INIMIGO_Y = 55;
        private static final int JOGADOR_X = 135;
        private static final int JOGADOR_Y = 180;
        private static final int TAMANHO_SPRITE = 170;

        // =========================================================================
        // AJUSTE MANUAL DAS SPRITES (OFFSETS X e Y / CONTROLE ABSOLUTO)
        // Eixo X: valores positivos movem para a DIREITA, negativos para a ESQUERDA
        // Eixo Y: valores positivos movem para BAIXO, negativos para CIMA
        // =========================================================================
        private static final int DESLOCAMENTO_X_INIMIGO = 0;   // Ajuste horizontal do adversário
        private static final int DESLOCAMENTO_Y_INIMIGO = 48;  // Ajuste vertical do adversário

        private static final int DESLOCAMENTO_X_JOGADOR = 0;   // Ajuste horizontal do parceiro
        private static final int DESLOCAMENTO_Y_JOGADOR = 48;  // Ajuste vertical do parceiro

        private Image imgInimigo;
        private Image imgJogador;

        public PainelArena() {
            recarregarSprites();
        }

        public void recarregarSprites() {
            // Inimigo selvagem é visto de frente; jogador é visto de costas
            imgInimigo = carregarImagemComFallback(
                    inimigo.getSpriteFrente(),
                    "/imagens/pokemon_" + inimigo.getId() + ".png"
            );
            imgJogador = carregarImagemComFallback(
                    pokemonAtivoJogador.getSpriteCostas(),
                    pokemonAtivoJogador.getSpriteFrente() // Se não tiver a de costas, usa a de frente como reserva
            );
            repaint();
        }

        /**
         * Carrega a imagem do Classpath com suporte a caminho alternativo de contingência.
         */
        private Image carregarImagemComFallback(String caminhoPrincipal, String caminhoAlternativo) {
            URL url = getClass().getResource(caminhoPrincipal);
            if (url == null && caminhoAlternativo != null) {
                url = getClass().getResource(caminhoAlternativo);
            }
            if (url != null) {
                return new ImageIcon(url).getImage();
            }
            return null; // Fallback automático para o avatar circular caso a imagem não exista
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            // Ativa renderização precisa e preserva o aspecto nítido de pixel art
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

            // 1. Cenário de Fundo (Floresta)
            g2.setColor(new Color(40, 85, 55));
            g2.fillRect(0, 0, getWidth(), getHeight());

            // 2. Bases / Plataformas Elípticas de Combate
            g2.setColor(new Color(25, 60, 40));
            g2.fillOval(INIMIGO_X - 25, INIMIGO_Y + 120, 220, 60);
            g2.fillOval(JOGADOR_X - 25, JOGADOR_Y + 125, 230, 65);

            // 3. Renderização do Inimigo Selvagem (Visão Frontal)
            if (imgInimigo != null) {
                g2.drawImage(
                        imgInimigo,
                        INIMIGO_X + DESLOCAMENTO_X_INIMIGO,
                        INIMIGO_Y + DESLOCAMENTO_Y_INIMIGO,
                        TAMANHO_SPRITE,
                        TAMANHO_SPRITE,
                        this
                );
            } else {
                g2.setColor(new Color(231, 76, 60));
                g2.fillOval(INIMIGO_X + 25, INIMIGO_Y + 20, 120, 120);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                g2.drawString(inimigo.getNome(), INIMIGO_X + 45, INIMIGO_Y + 85);
            }

            // 4. Renderização do Pokémon do Jogador (Visão Traseira / Costas)
            if (imgJogador != null) {
                g2.drawImage(
                        imgJogador,
                        JOGADOR_X + DESLOCAMENTO_X_JOGADOR,
                        JOGADOR_Y + DESLOCAMENTO_Y_JOGADOR,
                        TAMANHO_SPRITE,
                        TAMANHO_SPRITE,
                        this
                );
            } else {
                g2.setColor(new Color(241, 196, 15));
                g2.fillOval(JOGADOR_X + 25, JOGADOR_Y + 20, 120, 120);
                g2.setColor(Color.BLACK);
                g2.setFont(new Font("SansSerif", Font.BOLD, 13));
                g2.drawString(pokemonAtivoJogador.getNome(), JOGADOR_X + 45, JOGADOR_Y + 85);
            }
        }
    }
}