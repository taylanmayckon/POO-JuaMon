package jogo;

import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

/**
 * Gerenciador central de audio do JuaMon.
 * Suporta trilhas em loop continuo e efeitos pontuais em formato .wav (PCM 16-bit).
 * Possui tratamento silencioso para que o jogo continue funcionando normalmente 
 * caso os arquivos de audio ainda nao estejam presentes.
 */
public class GerenciadorAudio {

    // ITEM 1.1 - ENCAPSULAMENTO
    private static Clip clipMusicaFundo;
    private static String caminhoMusicaAtual = "";

    // Constantes com os caminhos padronizados dos audios do jogo
    public static final String PASTA_GRITOS = "/assets/audio/grito/";
    public static final String MUSICA_EXPLORACAO = "/assets/audio/music/music_exploration.wav";
    public static final String MUSICA_BATALHA = "/assets/audio/music/music_battle.wav";

    /**
     * Inicia uma musica em loop continuo.
     * Se a musica requisitada ja estiver tocando, mantem a execucao.
     */
    public static void iniciarMusicaEmLoop(String caminhoRecursoWav, float reducaoDecibeis) {
        if (caminhoRecursoWav == null || caminhoRecursoWav.equals(caminhoMusicaAtual)) {
            return;
        }

        pararMusicaFundo();

        try {
            InputStream is = GerenciadorAudio.class.getResourceAsStream(caminhoRecursoWav);
            if (is == null) {
                return; // Fallback: segue sem tocar caso o arquivo ainda nao exista
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            clipMusicaFundo = AudioSystem.getClip();
            clipMusicaFundo.open(audioStream);

            ajustarVolume(clipMusicaFundo, reducaoDecibeis);

            caminhoMusicaAtual = caminhoRecursoWav;
            clipMusicaFundo.loop(Clip.LOOP_CONTINUOUSLY);
            clipMusicaFundo.start();

        } catch (Exception ignored) {
        }
    }

    /**
     * Interrompe a musica que estiver em execucao.
     */
    public static void pararMusicaFundo() {
        try {
            if (clipMusicaFundo != null) {
                if (clipMusicaFundo.isRunning()) {
                    clipMusicaFundo.stop();
                }
                clipMusicaFundo.close();
                clipMusicaFundo = null;
            }
            caminhoMusicaAtual = "";
        } catch (Exception ignored) {
        }
    }

    /**
     * Toca um efeito sonoro curto uma unica vez.
     */
    public static void tocarEfeito(String caminhoRecursoWav) {
        try {
            InputStream is = GerenciadorAudio.class.getResourceAsStream(caminhoRecursoWav);
            if (is == null) {
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            Clip clipEfeito = AudioSystem.getClip();
            clipEfeito.open(audioStream);
            clipEfeito.start();

        } catch (Exception ignored) {
        }
    }

    /**
     * Dispara o grito especifico do Pokemon baseado em seu ID (/assets/audio/grito/[ID].wav).
     */
    public static void tocarGritoPokemon(int idPokemon) {
        tocarEfeito(PASTA_GRITOS + idPokemon + ".wav");
    }

    /**
     * Sobrecarga de conveniencia que dispara o grito do Pokemon padrao (ID 1 - Pikachu).
     */
    public static void tocarGritoPokemon() {
        tocarGritoPokemon(1);
    }

    private static void ajustarVolume(Clip clip, float decibeis) {
        try {
            if (clip != null && clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                gainControl.setValue(decibeis);
            }
        } catch (Exception ignored) {
        }
    }
}