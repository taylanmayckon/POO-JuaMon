package modelo;

/*
 * ITEM 8.1 - INTERFACES (Interface 2 de 2)
 * Define o contrato exclusivo para criaturas selvagens que podem ser capturadas.
 * Não possui getters, setters nem construtores.
 */
public interface Capturavel {
    boolean tentarCaptura();
}
