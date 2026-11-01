package modelo;

/*
 * ITEM 11.1 - EXCEÇÃO PRÓPRIA (Criada, levantada e tratada)
 * Levantada quando o jogador tenta capturar um Pokémon sem ter Pokébolas
 * disponíveis ou quando a ação de captura viola uma regra.
 */
public class CapturaInvalidaException extends Exception {
    public CapturaInvalidaException(String mensagem) {
        super(mensagem);
    }
}
