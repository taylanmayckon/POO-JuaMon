package jogo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
 * Interface visual do sistema de combate em turnos (800x600).
 * Integra regras de negócio do pacote modelo com a interface gráfica Swing.
 */
public class TelaBatalha extends JDialog {

    // ITEM 1.1 - ENCAPSULAMENTO
    private Batalha batalha;
    private Jogador jogador;
    private PokemonSelvagem inimigo;
    private Pokemon pokemonAtivoJogador;

    // Componentes gráficos principais
    private PainelArena painelArena;
    private JTextArea areaLog;
    private JPanel painelAcoes;
    private JPanel painelQuatroAtaques;
    private JPanel painelOpcoesGerais;

    // Barras de vida e rótulos
    private JProgressBar barraHpJogador;
    private JProgressBar barraHpInimigo;
    private JLabel labelNomeJogador;
    private JLabel labelNomeInimigo;
    private JLabel labelHpTextoJogador;
    private JLabel labelHpTextoInimigo;

    // Botões
    private JButton btnAtacarMenu;
    private JButton btnCapturar;
    private JButton btnFugir;
    private JButton[] botoesGolpes;

    private boolean combateEncerrado = false;

    /**
     * ITEM 3.1 - Construtor declarado em classe concreta.
     * Constrói a janela modal bloqueante de 800x600 sobre a JanelaJogo.
     */
    public TelaBatalha(JFrame janelaPai, Jogador jogador, PokemonSelvagem inimigo) {
        super(janelaPai, "JuaMon — Batalha por Turnos", true);
        this.jogador = jogador;
        this.inimigo = inimigo;
        this.pokemonAtivoJogador = jogador.getPokemonAtivo();
        this.batalha = new Batalha(jogador, inimigo);

        configurarJanela();
        inicializarComponentes();
        atualizarInterface();

        // Anúncio inicial (Item 6.2 - Acoplamento Dinâmico em ação)
        adicionarLog(batalha.obterAnuncioDeEntrada());
    }

    private void configurarJanela() {
        setSize(800, 600);
        setResizable(false);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE); // Impede fechar pelo X sem fugir ou finalizar
    }

    private void inicializarComponentes() {
        // 1. ZONA SUPERIOR E CENTRAL: ARENA VISUAL (420px de altura)
        painelArena = new PainelArena();
        painelArena.setPreferredSize(new Dimension(800, 390));
        painelArena.setLayout(null); // Posicionamento absoluto para HUDs clássicos
        adicionarHudsNaArena();
        add(painelArena, BorderLayout.CENTER);

        // 2. ZONA INFERIOR: PAINEL DE CONTROLE E LOGS (180px de altura)
        JPanel painelInferior = new JPanel(new GridLayout(1, 2, 8, 8));
        painelInferior.setPreferredSize(new Dimension(800, 180));
        painelInferior.setBorder(BorderFactory.createEmptyBorder(6, 8, 8, 8));
        painelInferior.setBackground(new Color(30, 30, 45));

        // 2.1. Caixa de Texto com Rolagem (Log de Mensagens)
        areaLog = new JTextArea();
        areaLog.setEditable(false);
        areaLog.setLineWrap(true);
        areaLog.setWrapStyleWord(true);
        areaLog.setFont(new Font("Monospaced", Font.BOLD, 13));
        areaLog.setBackground(new Color(245, 245, 240));
        areaLog.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        painelInferior.add(scrollLog);

        // 2.2. Painel Dinâmico de Ações (Direita)
        painelAcoes = new JPanel(new BorderLayout());
        painelAcoes.setOpaque(false);

        montarMenuPrincipal();
        montarMenuAtaques();

        // Começa exibindo o menu principal
        painelAcoes.add(painelOpcoesGerais, BorderLayout.CENTER);
        painelInferior.add(painelAcoes);

        add(painelInferior, BorderLayout.SOUTH);
    }

    private void adicionarHudsNaArena() {
        // --- HUD DO INIMIGO (Canto Superior Esquerdo) ---
        JPanel hudInimigo = criarPainelHud();
        hudInimigo.setBounds(40, 30, 280, 80);

        labelNomeInimigo = new JLabel(inimigo.getNome() + " Nv." + inimigo.getNivel());
        labelNomeInimigo.setFont(new Font("Arial", Font.BOLD, 14));
        labelNomeInimigo.setForeground(Color.WHITE);
        hudInimigo.add(labelNomeInimigo);

        barraHpInimigo = new JProgressBar(0, inimigo.getHpMaximo());
        barraHpInimigo.setValue(inimigo.getHpAtual());
        barraHpInimigo.setForeground(new Color(46, 204, 113));
        barraHpInimigo.setPreferredSize(new Dimension(250, 16));
        hudInimigo.add(barraHpInimigo);

        labelHpTextoInimigo = new JLabel();
        labelHpTextoInimigo.setFont(new Font("Arial", Font.PLAIN, 12));
        labelHpTextoInimigo.setForeground(Color.LIGHT_GRAY);
        hudInimigo.add(labelHpTextoInimigo);

        painelArena.add(hudInimigo);

        // --- HUD DO JOGADOR (Canto Inferior Direito) ---
        JPanel hudJogador = criarPainelHud();
        hudJogador.setBounds(470, 240, 290, 95);

        labelNomeJogador = new JLabel(pokemonAtivoJogador.getNome() + " Nv." + pokemonAtivoJogador.getNivel());
        labelNomeJogador.setFont(new Font("Arial", Font.BOLD, 14));
        labelNomeJogador.setForeground(Color.WHITE);
        hudJogador.add(labelNomeJogador);

        barraHpJogador = new JProgressBar(0, pokemonAtivoJogador.getHpMaximo());
        barraHpJogador.setValue(pokemonAtivoJogador.getHpAtual());
        barraHpJogador.setForeground(new Color(46, 204, 113));
        barraHpJogador.setPreferredSize(new Dimension(260, 16));
        hudJogador.add(barraHpJogador);

        labelHpTextoJogador = new JLabel();
        labelHpTextoJogador.setFont(new Font("Arial", Font.BOLD, 12));
        labelHpTextoJogador.setForeground(Color.WHITE);
        hudJogador.add(labelHpTextoJogador);

        painelArena.add(hudJogador);
    }

    private JPanel criarPainelHud() {
        JPanel painel = new JPanel();
        painel.setBackground(new Color(20, 20, 30, 210));
        painel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 2),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        return painel;
    }

    private void montarMenuPrincipal() {
        painelOpcoesGerais = new JPanel(new GridLayout(3, 1, 6, 6));
        painelOpcoesGerais.setOpaque(false);

        btnAtacarMenu = new JButton("⚔️ LUTAR / ATACAR");
        btnAtacarMenu.setFont(new Font("Arial", Font.BOLD, 14));
        btnAtacarMenu.setBackground(new Color(231, 76, 60));
        btnAtacarMenu.setForeground(Color.WHITE);
        btnAtacarMenu.setFocusable(false);
        btnAtacarMenu.addActionListener(e -> alternarParaMenuAtaques(true));

        btnCapturar = new JButton("🔴 LANÇAR POKÉBOLA (" + jogador.getPokebolas() + ")");
        btnCapturar.setFont(new Font("Arial", Font.BOLD, 14));
        btnCapturar.setBackground(new Color(52, 152, 219));
        btnCapturar.setForeground(Color.WHITE);
        btnCapturar.setFocusable(false);
        btnCapturar.addActionListener(e -> executarTentativaCaptura());

        btnFugir = new JButton("🏃 FUGIR DA BATALHA");
        btnFugir.setFont(new Font("Arial", Font.BOLD, 14));
        btnFugir.setBackground(new Color(149, 165, 166));
        btnFugir.setForeground(Color.BLACK);
        btnFugir.setFocusable(false);
        btnFugir.addActionListener(e -> fugirCombate());

        painelOpcoesGerais.add(btnAtacarMenu);
        painelOpcoesGerais.add(btnCapturar);
        painelOpcoesGerais.add(btnFugir);
    }

    private void montarMenuAtaques() {
        painelQuatroAtaques = new JPanel(new GridLayout(3, 2, 6, 6));
        painelQuatroAtaques.setOpaque(false);

        ArrayList<Golpe> golpes = pokemonAtivoJogador.getGolpes();
        botoesGolpes = new JButton[4];

        for (int i = 0; i < 4; i++) {
            final int indice = i;
            if (i < golpes.size()) {
                Golpe g = golpes.get(i);
                JButton btn = new JButton(g.getNome() + " (" + g.getTipo() + ")");
                btn.setFont(new Font("Arial", Font.BOLD, 12));
                btn.setFocusable(false);
                btn.setBackground(i == 3 ? new Color(241, 196, 15) : new Color(230, 126, 34)); // Destaque pro Buff
                btn.setForeground(Color.BLACK);
                btn.addActionListener(e -> executarTurnoJogador(indice));
                botoesGolpes[i] = btn;
                painelQuatroAtaques.add(btn);
            } else {
                JButton btnVazio = new JButton("-");
                btnVazio.setEnabled(false);
                painelQuatroAtaques.add(btnVazio);
            }
        }

        // Botão de retorno
        JButton btnVoltar = new JButton("⬅ Voltar");
        btnVoltar.setFont(new Font("Arial", Font.BOLD, 12));
        btnVoltar.setBackground(Color.LIGHT_GRAY);
        btnVoltar.setFocusable(false);
        btnVoltar.addActionListener(e -> alternarParaMenuAtaques(false));
        painelQuatroAtaques.add(btnVoltar);
    }

    private void alternarParaMenuAtaques(boolean mostrarAtaques) {
        painelAcoes.removeAll();
        if (mostrarAtaques) {
            painelAcoes.add(painelQuatroAtaques, BorderLayout.CENTER);
        } else {
            painelAcoes.add(painelOpcoesGerais, BorderLayout.CENTER);
        }
        painelAcoes.revalidate();
        painelAcoes.repaint();
    }

    private void executarTurnoJogador(int indiceGolpe) {
        if (combateEncerrado) return;

        // Turno do Jogador: executa o golpe (Usa Sobrecarga - Item 7.1)
        String logJogador = batalha.executarGolpeTurno(pokemonAtivoJogador, inimigo, indiceGolpe);
        adicionarLog(logJogador);
        atualizarInterface();

        // Checa se o inimigo foi derrotado
        if (inimigo.estaDesmaiado()) {
            adicionarLog("🏆 " + inimigo.getNome() + " selvagem desmaiou! Você venceu a batalha!");
            encerrarCombateComAviso(inimigo.getNome() + " selvagem desmaiou! Você venceu!");
            return;
        }

        // Volta a tela para o menu geral durante o contra-ataque
        alternarParaMenuAtaques(false);

        // Turno do Inimigo: ataca aleatoriamente
        desabilitarControles(true);
        SwingUtilities.invokeLater(() -> executarTurnoInimigo());
    }

    private void executarTurnoInimigo() {
        if (combateEncerrado) return;

        int totalGolpesInimigo = inimigo.getGolpes().size();
        int sorteioGolpe = (int) (Math.random() * totalGolpesInimigo);

        String logInimigo = batalha.executarGolpeTurno(inimigo, pokemonAtivoJogador, sorteioGolpe);
        adicionarLog("Inimigo: " + logInimigo);
        atualizarInterface();

        if (pokemonAtivoJogador.estaDesmaiado()) {
            adicionarLog("💀 Seu " + pokemonAtivoJogador.getNome() + " desmaiou!");
            encerrarCombateComAviso("Seu Pokémon desmaiou! A batalha terminou.");
        } else {
            desabilitarControles(false);
        }
    }

    /**
     * ITEM 11.1 - EXCEÇÃO PRÓPRIA TRATADA COM BLOCO TRY-CATCH
     * Invoca tentarCapturarSelvagem() de Batalha que lança CapturaInvalidaException.
     */
    private void executarTentativaCaptura() {
        if (combateEncerrado) return;

        try {
            adicionarLog("🔴 Você lançou uma Pokébola!");
            boolean capturado = batalha.tentarCapturarSelvagem();

            if (capturado) {
                adicionarLog("🎉 SUCESSO! " + inimigo.getNome() + " foi capturado e adicionado à sua equipe!");
                JOptionPane.showMessageDialog(this,
                        "Incrível! O " + inimigo.getNome() + " foi capturado com sucesso!",
                        "Captura Bem-Sucedida", JOptionPane.INFORMATION_MESSAGE);
                combateEncerrado = true;
                dispose();
            } else {
                adicionarLog("❌ A Pokébola quebrou! A vida do Pokémon precisa estar abaixo de 20%!");
                JOptionPane.showMessageDialog(this,
                        "O " + inimigo.getNome() + " escapou!\nDica: Reduza a vida dele para menos de 20%!",
                        "Falha na Captura", JOptionPane.WARNING_MESSAGE);
                atualizarInterface();
                executarTurnoInimigo();
            }
        } catch (CapturaInvalidaException ex) {
            // ITEM 11.1 - Bloco catch tratando a exceção sem quebrar a execução
            adicionarLog("⚠️ Erro: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "Estoque Esgotado", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void fugirCombate() {
        adicionarLog("💨 Você fugiu em segurança da batalha!");
        JOptionPane.showMessageDialog(this, "Você escapou da batalha!", "Fuga", JOptionPane.INFORMATION_MESSAGE);
        combateEncerrado = true;
        dispose();
    }

    private void encerrarCombateComAviso(String mensagem) {
        combateEncerrado = true;
        desabilitarControles(true);
        JOptionPane.showMessageDialog(this, mensagem, "Fim da Batalha", JOptionPane.INFORMATION_MESSAGE);
        dispose();
    }

    private void desabilitarControles(boolean desabilitar) {
        btnAtacarMenu.setEnabled(!desabilitar);
        btnCapturar.setEnabled(!desabilitar);
        btnFugir.setEnabled(!desabilitar);
    }

    private void atualizarInterface() {
        // Atualiza barras de HP
        barraHpJogador.setValue(pokemonAtivoJogador.getHpAtual());
        ajustarCorBarra(barraHpJogador, pokemonAtivoJogador.obterPorcentagemHp());
        labelHpTextoJogador.setText("HP: " + pokemonAtivoJogador.getHpAtual() + "/" + pokemonAtivoJogador.getHpMaximo()
                + String.format(" (Buff: %.2fx)", pokemonAtivoJogador.getMultiplicadorAtaque()));

        barraHpInimigo.setValue(inimigo.getHpAtual());
        ajustarCorBarra(barraHpInimigo, inimigo.obterPorcentagemHp());
        labelHpTextoInimigo.setText("HP: " + inimigo.getHpAtual() + "/" + inimigo.getHpMaximo()
                + String.format(" (%.1f%%)", inimigo.obterPorcentagemHp()));

        btnCapturar.setText("🔴 LANÇAR POKÉBOLA (" + jogador.getPokebolas() + ")");
        painelArena.repaint();
    }

    private void ajustarCorBarra(JProgressBar barra, float pct) {
        if (pct <= 20.0f) {
            barra.setForeground(new Color(231, 76, 60)); // Vermelho (Pronto para captura)
        } else if (pct <= 50.0f) {
            barra.setForeground(new Color(243, 156, 18)); // Amarelo
        } else {
            barra.setForeground(new Color(46, 204, 113)); // Verde
        }
    }

    private void adicionarLog(String texto) {
        areaLog.append(texto + "\n");
        areaLog.setCaretPosition(areaLog.getDocument().getLength());
    }

    /**
     * Painel personalizado que desenha o cenário da floresta e as sprites dos Pokémon.
     */
    private class PainelArena extends JPanel {

        private Image imgInimigo;
        private Image imgJogador;

        public PainelArena() {
            carregarImagens();
        }

        private void carregarImagens() {
            imgInimigo = carregarImagem(inimigo.getCaminhoSprite());
            imgJogador = carregarImagem(pokemonAtivoJogador.getCaminhoSprite());
        }

        private Image carregarImagem(String caminho) {
            URL url = getClass().getResource(caminho);
            if (url != null) {
                return new ImageIcon(url).getImage();
            }
            return null; // Caso não encontre a imagem, o painel desenha a forma geométrica reserva
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Fundo da Arena: Gradiente de Floresta
            g.setColor(new Color(45, 95, 60));
            g.fillRect(0, 0, getWidth(), getHeight());

            // Base de combate do Inimigo (elipse de chão no topo direito)
            g.setColor(new Color(30, 70, 45));
            g.fillOval(480, 150, 220, 60);

            // Base de combate do Jogador (elipse de chão na base esquerda)
            g.setColor(new Color(30, 70, 45));
            g.fillOval(100, 290, 240, 70);

            // Renderiza Sprite do Inimigo
            if (imgInimigo != null) {
                g.drawImage(imgInimigo, 510, 60, 160, 160, this);
            } else {
                g.setColor(new Color(220, 80, 80));
                g.fillOval(530, 70, 120, 120);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 14));
                g.drawString(inimigo.getNome(), 550, 135);
            }

            // Renderiza Sprite do Pokémon do Jogador
            if (imgJogador != null) {
                g.drawImage(imgJogador, 140, 190, 160, 160, this);
            } else {
                g.setColor(new Color(240, 200, 50));
                g.fillOval(160, 200, 120, 120);
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.BOLD, 14));
                g.drawString(pokemonAtivoJogador.getNome(), 180, 265);
            }
        }
    }
}