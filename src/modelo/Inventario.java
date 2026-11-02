package modelo;

import java.util.ArrayList;

/**
 * ITEM 7.4 - POLIMORFISMO PARAMÉTRICO (Generics)
 *
 * Nome do Item: 7.4. Polimorfismo paramétrico.
 * Justificativa: Inventario<T> é uma classe concreta parametrizada que permite
 * criar compartimentos de mochila fortemente tipados para diferentes categorias de itens
 * (como Inventario<Pokebola> e Inventario<Pocao>) sem duplicar a lógica de armazenamento,
 * contagem e remoção. O parâmetro genérico <T> não é recebido no construtor, sendo
 * manipulado explicitamente nos métodos armazenarItem(T) e retirarItem().
 */
public class Inventario<T> {

    // ITEM 1.1 (Encapsulamento) e ITEM 1.4 (Atributo ArrayList)
    private String nomeCompartimento;
    private ArrayList<T> itensArmazenados;

    // Construtor NÃO recebe o tipo genérico T, cumprindo rigorosamente a exigência do item 7.4
    public Inventario(String nomeCompartimento) {
        this.nomeCompartimento = nomeCompartimento;
        this.itensArmazenados = new ArrayList<T>();
    }

    /**
     * ITEM 7.4 - Uso explícito do parâmetro genérico T fora do construtor.
     */
    public void armazenarItem(T novoItem) {
        if (novoItem != null) {
            this.itensArmazenados.add(novoItem);
        }
    }

    /**
     * ITEM 7.4 - Retorno tipado pelo parâmetro genérico T ao consumir um item do estoque.
     */
    public T retirarItem() {
        if (this.itensArmazenados.isEmpty()) {
            return null;
        }
        return this.itensArmazenados.remove(0);
    }

    public T inspecionarTopo() {
        if (this.itensArmazenados.isEmpty()) {
            return null;
        }
        return this.itensArmazenados.get(0);
    }

    public int obterQuantidade() {
        return this.itensArmazenados.size();
    }

    public boolean estaVazio() {
        return this.itensArmazenados.isEmpty();
    }

    public String getNomeCompartimento() {
        return nomeCompartimento;
    }

    public void setNomeCompartimento(String nomeCompartimento) {
        this.nomeCompartimento = nomeCompartimento;
    }

    public ArrayList<T> getItensArmazenados() {
        return itensArmazenados;
    }
}