package com.mycompany.aubeeluckprjes3pokemon;

/**
 * La classe {@code Pokemon} rappresenta un generico Pokémon
 * con punti ferita (PF), energia e due attacchi disponibili.
 * Può essere estesa da sottoclassi concrete (es. {@code Pikachu},
 * {@code Charmander}, ecc.) per definire mosse personalizzate.
 *
 * <p>Ogni Pokémon dispone di:
 * <ul>
 *   <li>Un nome identificativo.</li>
 *   <li>Punti ferita massimi e correnti.</li>
 *   <li>Punti energia massimi e correnti.</li>
 *   <li>Due attacchi con nome, costo energetico e danno massimo.</li>
 * </ul>
 *
 * @author Aubeeluck Aditya
 */
public class Pokemon {

    /** Nome del Pokémon */
    private String nome;

    /** Punti ferita correnti */
    private int pf;

    /** Energia corrente */
    private int e = 0;

    /** Nome del primo attacco */
    private String atk1;

    /** Costo energetico del primo attacco */
    private int eatk1;

    /** Danno massimo infliggibile dal primo attacco */
    private int maxatk1;

    /** Nome del secondo attacco */
    private String atk2;

    /** Costo energetico del secondo attacco */
    private int eatk2;

    /** Danno massimo infliggibile dal secondo attacco */
    private int maxatk2;

    /**
     * Costruisce un nuovo {@code Pokemon} con le caratteristiche specificate.
     *
     * @param nome Nome del Pokémon
     * @param pf Punti ferita iniziali
     * @param e Energia iniziale
     * @param atk1 Nome del primo attacco
     * @param eatk1 Energia richiesta dal primo attacco
     * @param maxatk1 Danno massimo infliggibile dal primo attacco
     * @param atk2 Nome del secondo attacco
     * @param eatk2 Energia richiesta dal secondo attacco
     * @param maxatk2 Danno massimo infliggibile dal secondo attacco
     */
    public Pokemon(String nome, int pf, int eatk1, int maxatk1, int eatk2, int maxatk2, String atk1, String atk2) {
        this.nome = nome;
        this.pf = pf;
        this.atk1 = atk1;
        this.eatk1 = eatk1;
        this.maxatk1 = maxatk1;
        this.atk2 = atk2;
        this.eatk2 = eatk2;
        this.maxatk2 = maxatk2;
    }

    /**
     * Esegue il primo attacco se l'energia disponibile è sufficiente.
     * Il danno inflitto è generato casualmente tra 0 e {@code maxatk1}.
     *
     * @return Danno inflitto (0 se l'attacco non è disponibile)
     */
    public int attacco1() {
        return (int) (Math.random() * (getMaxatk1() + 1));
    }

    /**
     * Esegue il secondo attacco se l'energia disponibile è sufficiente.
     * Il danno inflitto è generato casualmente tra 0 e {@code maxatk2}.
     *
     * @return Danno inflitto (0 se l'attacco non è disponibile)
     */
    public int attacco2() {
        return (int) (Math.random() * (getMaxatk1() + 1));
    }

    /** @return Nome del Pokémon */
    public String getNome() { 
        return nome; 
    }

    /** @return Punti ferita correnti */
    public int getPf() { 
        return pf; 
    }

    /** Imposta i punti ferita correnti */
    public void setPf(int pf) { 
        this.pf = pf;
    }

    /** @return Energia corrente */
    public int getE() { 
        return e;
    }

    /** Imposta l'energia corrente */
    public void setE(int e) {
        this.e = e; 
    }

    /** @return energia necessaria per il primo attacco */    
    public int getEatk1() {
        return eatk1;
    }

    /** Imposta l'energia massima per il primo attacco */    
    public void setEatk1(int eatk1) {
        this.eatk1 = eatk1;
    }
    
    /** @return danno massimo del primo attacco */
    public int getMaxatk1() {
        return maxatk1;
    }

    /** Imposta i danni massimi del primo attacco */
    public void setMaxatk1(int maxatk1) {
        this.maxatk1 = maxatk1;
    }

    /** @return energia necessaria per il secondo attacco */
    public int getEatk2() {
        return eatk2;
    }

    /** Imposta i danni massimi del secondo attacco */    
    public void setEatk2(int eatk2) {
        this.eatk2 = eatk2;
    }

    /** @return danno massimo del secondo attacco */
    public int getMaxatk2() {
        return maxatk2;
    }

    /** Imposta l'energia massima per il secondo attacco */        
    public void setMaxatk2(int maxatk2) {
        this.maxatk2 = maxatk2;
    }
    

    /** @return Nome del primo attacco */
    public String getAtk1() { 
        return atk1;
    }

    /** @return Nome del secondo attacco */
    public String getAtk2() { 
        return atk2; 
    }
}
