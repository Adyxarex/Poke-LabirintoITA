/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.aubeeluckprjes3pokemon;
import java.util.Random;
import java.util.Scanner;

/**
 * La classe {@code Game} estende {@link Maze} e gestisce la logica
 * del gioco PokeLabirinto.
 *
 * <p>Caratteristiche principali:
 * <ul>
 *   <li>Posiziona un Pokémon del giocatore in un punto casuale del labirinto</li>
 *   <li>Gestisce il movimento del giocatore e del nemico</li>
 *   <li>Gestisce gli incontri e i combattimenti tra Pokémon</li>
 *   <li>Verifica la fine del gioco quando il giocatore raggiunge l'uscita
 *       o quando un Pokémon viene sconfitto</li>
 * </ul>
 *
 * <p>Simboli nel labirinto:
 * <ul>
 *   <li>P = Pokémon del giocatore</li>
 *   <li>X = Pokémon nemico</li>
 *   <li>U = uscita</li>
 *   <li>. = corridoio</li>
 *   <li># = muro</li>
 * </ul>
 * 
 * @author aubee
 */
public class Game extends Maze {

    private Pokemon player;   // Pokémon scelto dall'utente
    private int playerX;      // posizione X del Pokémon
    private int playerY;      // posizione Y del Pokémon
    private int previousPX;   //posizione X del Pokémon prima dei una mossa
    private int previousPY;   //posizione y del Pokémon prima dei una mossa
    
    private Pokemon enemy;
    private int enemyX;
    private int enemyY;
    private Random rand = new Random();

    /**
     * Costruisce un nuovo gioco PokeLabirinto con un labirinto e un Pokémon.
     * @param row righe del labirinto
     * @param col colonne del labirinto
     * @param player Pokémon scelto dal giocatore
     * @param enemy Pokemon nemico
     */
    public Game(int row, int col, Pokemon player, Pokemon enemy) {
        super(row, col);
        this.player = player;
        this.enemy = enemy;
        placePlayerRandomly();
        placeEnemyRandomly();
    }

 /**
 * Posiziona il Pokémon del giocatore in un corridoio casuale del labirinto.
 * Assicura che:
 * <ul>
 *   <li>Il nemico sia su un percorso percorribile (maze[][] == 0)</li>
 *   <li>Non sia sull'uscita</li>
 * </ul>
 **/
    private void placePlayerRandomly() {
        int[][] maze = getMaze();
        do {
            playerX = (int) (Math.random() * super.getRow());
            playerY = (int) (Math.random() * super.getCol());
            previousPX = playerX;
            previousPY = playerY;
        } while (maze[playerX][playerY] != 0 || (playerX == getExitX() && playerY == getExitY()));
    }
    
 /**
 * Posiziona il Pokémon nemico in un corridoio casuale del labirinto.
 * Assicura che:
 * <ul>
 *   <li>Il nemico sia su un percorso percorribile (maze[][] == 0)</li>
 *   <li>Non sia sulla stessa posizione del giocatore</li>
 *   <li>Non sia sull'uscita</li>
 * </ul>
 **/
    private void placeEnemyRandomly() {
        int[][] maze = getMaze();
        do {
            enemyX = (int) (Math.random() * super.getRow());
            enemyY = (int) (Math.random() * super.getCol());
        } while (maze[enemyX][enemyY] != 0 || (playerX == getExitX() && playerY == getExitY())
                || (enemyX == playerX && enemyY == playerY));
    }
    
    public void printMazeSymbols(){
        super.printMazeSymbols();
        System.out.println(" 'E' corrisponde all'entrata che corrisponde al punto di generazione del labirinto");
    }

/**
 * Stampa il labirinto mostrando la posizione corrente del giocatore
 * e del nemico.
 * <ul>
 *   <li>P = Pokémon del giocatore</li>
 *   <li>X = Pokémon nemico</li>
 *   <li>U = uscita</li>
 *   <li>. = corridoio</li>
 *   <li># = muro</li>
 * </ul>
 **/
    public void printMazeWithPokemon() {
        int[][] maze = getMaze();
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (i == playerX && j == playerY) {
                    System.out.print("P ");
                } else if (i == enemyX && j == enemyY) {
                    System.out.print("X ");
                } else if (i == super.getExitX() && j == super.getExitY()) {
                    System.out.print("U ");
                } else if (maze[i][j] == 0 && !(i == super.getEntranceX() && j == super.getEntranceY())) {
                    System.out.print(". ");
                } else {
                    System.out.print("# ");
                }
            }
            System.out.println();
        }
    }


/**
 * Mostra le direzioni disponibili in cui il giocatore può muoversi
 * (evita muri e bordi del labirinto).
 */
    public void showAvailableMoves() {
        int[][] maze = getMaze();
        System.out.println("Puoi muoverti in:");
        if (playerX > 0 && maze[playerX-1][playerY] == 0)   //non uscire dai bordi
            System.out.println(" - su (W)");
        
        if (playerY > 0 && maze[playerX][playerY-1] == 0)
            System.out.println(" - sinistra (A)");
        
        if (playerX < maze.length-1 && maze[playerX+1][playerY] == 0)
            System.out.println(" - giù (S)");
        
        if (playerY < maze[0].length-1 && maze[playerX][playerY+1] == 0)
            System.out.println(" - destra (D)");
    }
    
/**
 * Calcola la nuova posizione di un Pokémon muovendosi in una
 * direzione specificata.
 * <p>
 * Blocca il movimento se la direzione è verso un muro o
 * verso l'entrata del labirinto.
 * </p>
 * @param x coordinata X corrente
 * @param y coordinata Y corrente
 * @param direction direzione di movimento ("W", "A", "S", "D")
 * @return array di due interi {newX, newY} con le nuove coordinate
 **/
    private int[] move(int x, int y, String direction) {
        int[][] maze = getMaze();
        int newX = x;
        int newY = y;

        switch (direction.toUpperCase()) {
            case "W":
                if (x > 0 && maze[x-1][y] == 0 
                        && !((x-1) == super.getEntranceX() && y == super.getEntranceY())) {
                    newX = x - 1;
                }
                break;
            case "S":
                if (x < maze.length-1 && maze[x+1][y] == 0 
                        && !((x+1) == super.getEntranceX() && y == super.getEntranceY())) {
                    newX = x + 1;
                }
                break;
            case "A":
                if (y > 0 && maze[x][y-1] == 0 
                        && !(x == super.getEntranceX() && (y-1) == super.getEntranceY())) {
                    newY = y - 1;
                }
                break;
            case "D":
                if (y < maze[0].length-1 && maze[x][y+1] == 0 
                        && !(x == super.getEntranceX() && (y+1) == super.getEntranceY())) {
                    newY = y + 1;
                }
                break;
            default:
                System.out.println("Errore. Mossa non valida");
        }

        return new int[]{newX, newY};
    }

/**
 * Muove il Pokémon del giocatore in base alla direzione
 * scelta dall'utente.
 * <p>
 * Se il giocatore entra nella stessa cella del nemico,
 * viene avviato un incontro.
 * </p>
 * @param direction direzione scelta dall'utente ("W", "A", "S", "D")
 **/
    public void movePokemon(String direction) {
        int[] newPos = move(playerX, playerY, direction.toUpperCase());
        if (newPos[0] != playerX || newPos[1] != playerY) {
            previousPX = playerX;
            previousPY = playerY;
            playerX = newPos[0];
            playerY = newPos[1];
        }

        if (playerX == enemyX && playerY == enemyY) {
            encounter();
        }
    }

 /**
 * Gestisce l'incontro tra il giocatore e il Pokémon nemico.
 * <p>
 * Chiede all'utente se combattere o tornare indietro.
 * </p>
 */
    private void encounter() {
        Scanner in = new Scanner(System.in);
        String choice;
        do{
            System.out.println("Hai incontrato un Pokémon selvatico: " + enemy.getNome() + "!");
            System.out.print("Vuoi combattere (C) o tornare indietro (T)? ");
            choice = in.nextLine();
            if(!choice.equalsIgnoreCase("C") && !choice.equalsIgnoreCase("T")){
                System.out.println("Errore. Scekta non valida");
            }else{
                if (choice.equalsIgnoreCase("C")) {
                    combattimento(in);
                } else if (choice.equalsIgnoreCase("T")) {
                    System.out.println("Sei tornato indietro!");
                    playerX = previousPX;
                    playerY = previousPY;
                }
            }
            
        }while(!choice.equalsIgnoreCase("C") && !choice.equalsIgnoreCase("T"));
        
    }
    
/**
 * Avvia un combattimento tra il Pokémon del giocatore e il nemico.
 * Aggiorna PF ed energia e gestisce turni alternati.
 * @param in Scanner per l'input dell'utente
 */
    public void combattimento(Scanner in) {
        int flag;
        do {
            player.setE(player.getE() + (int) (Math.random() * 100 + 1));
            enemy.setE(enemy.getE() + (int) (Math.random() * 100 + 1));

            if (Math.random() < 0.5) {
                // turno player
                flag = attacco(player, in);
                if (flag != 0) 
                    danno(player, enemy, flag);
                else
                    System.out.println(player.getNome() + " non ha energia sufficiente!");

                // turno enemy
                if (enemy.getPf() > 0) {
                    flag = attacco(enemy, in);
                    if (flag != 0) danno(enemy, player, flag);
                    else System.out.println(enemy.getNome() + " non ha energia sufficiente!");
                }
            } else {
                // turno enemy
                flag = attacco(enemy, in);
                if (flag != 0) 
                    danno(enemy, player, flag);
                else 
                    System.out.println(enemy.getNome() + " non ha energia sufficiente!");

                // turno player
                if (player.getPf() > 0) {
                    flag = attacco(player, in);
                    if (flag != 0)
                        danno(player, enemy, flag);
                    else 
                        System.out.println(player.getNome() + " non ha energia sufficiente!");
                }
            }

            System.out.println(player.getNome() + "  PF: " + player.getPf() + ", Energia: " + player.getE());
            System.out.println(enemy.getNome() + "  PF: " + enemy.getPf() + ", Energia: " + enemy.getE());
            System.out.println("----------------------------------");

        } while (player.getPf() > 0 && enemy.getPf() > 0);

    }


/**
 * Gestisce la scelta dell'attacco del Pokémon durante un turno.
 * @param p Pokémon che deve attaccare
 * @param in Scanner per l'input dell'utente
 * @return intero che indica quale attacco è stato scelto (0 = energia insufficiente)
 */
    private int attacco(Pokemon p, Scanner in) {
        int choice = 0;
        System.out.println("Turno di " + p.getNome());
        System.out.println("1) " + p.getAtk1() + " (costo " + p.getEatk1() + " energia, massimo " + p.getMaxatk1() + " danni)");
        System.out.println("2) " + p.getAtk2() + " (costo " + p.getEatk2() + " energia, massimo " + p.getMaxatk2() + " danni)");
        int scelta = in.nextInt();

        if (scelta == 1 && p.getE() >= p.getEatk1()) 
            choice = 1;   // attacco 1
        if (scelta == 2 && p.getE() >= p.getEatk2()) 
            choice = 2;   // attacco 2
        
        return choice; // energia insufficiente
    }

/**
 * Applica il danno inflitto da un attaccante a un difensore
 * e sottrae l'energia necessaria.
 * @param attaccante Pokémon che attacca
 * @param difensore Pokémon che riceve il danno
 * @param flag indica quale attacco usare (1 = attacco1, 2 = attacco2)
 */
    private void danno(Pokemon attaccante, Pokemon difensore, int flag) {
        int danni;
        if (flag == 1) {
            danni = attaccante.attacco1();
            difensore.setPf(difensore.getPf() - danni);
            attaccante.setE(attaccante.getE() - attaccante.getEatk1());
            System.out.println(attaccante.getNome() + " usa " + attaccante.getAtk1() + " e infligge " + danni + " danni!");
        } else {
            danni = attaccante.attacco2();
            difensore.setPf(difensore.getPf() - danni);
            attaccante.setE(attaccante.getE() - attaccante.getEatk2());
            System.out.println(attaccante.getNome() + " usa " + attaccante.getAtk2() + " e infligge " + danni + " danni!");
        }
    }

/**
     * Determina il vincitore della battaglia tra il Pokémon del giocatore e il nemico.
     * <p>
     * Il metodo controlla i Punti Ferita (PF) residui di entrambi i Pokémon:
     * <ul>
     *   <li>Se il giocatore ha ancora PF > 0 e il nemico ha PF ≤ 0,
     *       il giocatore viene dichiarato vincitore.</li>
     *   <li>Se il nemico ha ancora PF > 0 e il giocatore ha PF ≤ 0,
     *       il nemico viene dichiarato vincitore.</li>
     *   <li>In tutti gli altri casi (entrambi ancora in vita o entrambi a 0),
     *       non viene determinato un vincitore immediato.</li>
     * </ul>
     *
     * @param player Pokémon controllato dal giocatore
     * @param enemy Pokémon avversario
     * @return {@code true} se il giocatore è vincitore o entrambi sono ancora in vita,
     *         {@code false} se il nemico vince
     */
    private boolean vincitore(Pokemon player, Pokemon enemy) {
        boolean flag = true;
        if (player.getPf() > 0 && enemy.getPf() < 0) {
            System.out.println(player.getNome() + " vince la battaglia!");
        } else if (player.getPf() < 0 && enemy.getPf() > 0){
            System.out.println(enemy.getNome() + " vince la battaglia!");
            flag = false;
        }
        
        return flag;
    }
    
/**
 * Controlla se il gioco è terminato.
 * <ul>
 *   <li>Il giocatore ha trovato l'uscita</li>
 *   <li>Il giocatore o il nemico è stato sconfitto</li>
 * </ul>
 * @return true se il gioco è terminato, false altrimenti
 */
    public boolean gameEnd(){
        boolean flag = false;
        if(playerX == super.getExitX() && playerY == super.getExitY()){
            System.out.println("Uscita dal labirinto trovata !\n Hai vinto il gioco");
            flag = true;
        }else{
            boolean isAlive;
            isAlive = vincitore(player, enemy);
            if(!isAlive){
                flag = true;
            }
        }
        return flag;
    }
    

/**
 * Muove il Pokémon nemico in una direzione casuale valida.
 * Se il nemico finisce nella stessa cella del giocatore,
 * viene avviato un incontro.
 */
    public void moveEnemy() {
        String[] directions = new String[]{"W", "S", "A", "D"};
        int[][] maze = getMaze();

        // tenta finché non trova una direzione valida
        boolean moved = false;
        while (!moved) {
            String dir = directions[rand.nextInt(4)];
            int[] newPos = move(enemyX, enemyY, dir);
            if (newPos[0] != enemyX || newPos[1] != enemyY) {
                enemyX = newPos[0];
                enemyY = newPos[1];
                moved = true;
            }
        }

        if (enemyX == playerX && enemyY == playerY) {
            encounter();
        }
    }



    public Pokemon getPlayer() {
        return player;
    }

    public void setPlayer(Pokemon player) {
        this.player = player;
    }

    public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    public int getPreviousPX() {
        return previousPX;
    }

    public void setPreviousPX(int previousPX) {
        this.previousPX = previousPX;
    }

    public int getPreviousPY() {
        return previousPY;
    }

    public void setPreviousPY(int previousPY) {
        this.previousPY = previousPY;
    }

    public int getEnemyX() {
        return enemyX;
    }

    public void setEnemyX(int enemyX) {
        this.enemyX = enemyX;
    }

    public int getEnemyY() {
        return enemyY;
    }

    public void setEnemyY(int enemyY) {
        this.enemyY = enemyY;
    }

}

