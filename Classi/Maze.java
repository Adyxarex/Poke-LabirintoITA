package com.mycompany.aubeeluckprjes3pokemon;

import java.util.Random;

/**
 * La classe {@code Maze} rappresenta un labirinto bidimensionale generato
 * casualmente tramite un algoritmo di backtracking ricorsivo.
 *
 * <p>La struttura interna è una matrice di interi:
 * <ul>
 *   <li>{@code 1} = muro</li>
 *   <li>{@code 0} = corridoio percorribile</li>
 * </ul>
 *
 * <p>Caratteristiche principali:
 * <ul>
 *   <li>Entrata fissa in alto a sinistra</li>
 *   <li>Uscita generata automaticamente sul bordo opposto</li>
 *   <li>Almeno 3 biforcazioni casuali collegate al percorso principale</li>
 * </ul>
 *
 * <p>Include metodi per stampare il labirinto con numeri o simboli
 * e per ottenere e impostare i parametri interni.
 * 
 * @author Aubeeluck Aditya
 */
public class Maze {
    /** Matrice interna che rappresenta il labirinto. */
    private int[][] maze;

    /** Numero di righe del labirinto. */
    private int row;

    /** Numero di colonne del labirinto. */
    private int col;

    /** Flag che indica se l'uscita è già stata creata. */
    private boolean exitCreated = false;

    /** Coordinate dell'entrata del labirinto. */
    private int entranceX;
    private int entranceY;

    /** Coordinate dell'uscita del labirinto. */
    private int exitX;
    private int exitY;

    /** Generatore di numeri casuali per percorsi e biforcazioni. */
    private Random rand = new Random();

    /**
     * Costruisce un nuovo labirinto di dimensioni prestabilite.
     **/
    public Maze() {
        this.row = 20;
        this.col = 20;
        maze = new int[row][col];
        generateMaze();
    }        
    
    /**
     * Costruisce un nuovo labirinto di dimensioni specificate.
     * @param row numero di righe
     * @param col numero di colonne
     */
    public Maze(int row, int col) {
        if (row > 0 && col > 0) {
            this.row = row;
            this.col = col;
            maze = new int[row][col];
            generateMaze();
        } else {
            System.out.println("ERRORE: dimensioni labirinto non valide.");
        }
    }

 /**
 * Genera il labirinto impostando tutti i muri inizialmente e creando:
 * <ul>
 *   <li>Entrata casuale su uno dei quattro lati</li>
 *   <li>Percorso principale tramite backtracking ricorsivo</li>
 *   <li>Almeno 3 biforcazioni collegate al percorso principale</li>
 *   <li>Uscita posizionata in un punto valido del bordo</li>
 * </ul>
 */
    private void generateMaze() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                maze[i][j] = 1;
            }
        }

        // Scegli lato casuale: 0=alto, 1=basso, 2=sinistra, 3=destra
        int side = rand.nextInt(4);

        switch (side) {
            case 0: // alto
                entranceX = 0;
                entranceY = rand.nextInt(col - 2) + 1; // evitiamo angoli
                maze[entranceX][entranceY] = 0;
                maze[entranceX+1][entranceY] = 0;
                generateMainPath(entranceX+1, entranceY);
                break;
            case 1: // basso
                entranceX = row - 1;
                entranceY = rand.nextInt(col - 2) + 1;
                maze[entranceX][entranceY] = 0;
                maze[entranceX-1][entranceY] = 0;
                generateMainPath(entranceX-1, entranceY);
                break;
            case 2: // sinistra
                entranceX = rand.nextInt(row - 2) + 1;
                entranceY = 0;
                maze[entranceX][entranceY] = 0;
                maze[entranceX][entranceY+1] = 0;
                generateMainPath(entranceX, entranceY+1);
                break;
            case 3: // destra
                entranceX = rand.nextInt(row - 2) + 1;
                entranceY = col - 1;
                maze[entranceX][entranceY] = 0;
                maze[entranceX][entranceY-1] = 0;
                generateMainPath(entranceX, entranceY-1);
                break;
        }

        int biforcazioni = 0;
        for (int i = 1; i < row-1 && biforcazioni < 3; i++) {
            for (int j = 1; j < col-1 && biforcazioni < 3; j++) {
                if (maze[i][j] == 0 && rand.nextBoolean()) {
                    generateBranch(i, j);
                    biforcazioni++;
                }
            }
        }
    }


/**
 * Genera ricorsivamente il percorso principale del labirinto partendo da
 * una posizione specificata.
 * <p>
 * Durante la generazione controlla se è possibile creare l'uscita e
 * la imposta rispettando una distanza minima dall'entrata.
 * </p>
 * @param x coordinata X di partenza
 * @param y coordinata Y di partenza
 */
    private void generateMainPath(int x, int y) {
        int[] dx = {-2, 2, 0, 0};
        int[] dy = {0, 0, -2, 2};
        int[] dirs = {0, 1, 2, 3};
        shuffleArray(dirs);

        for (int i = 0; i < dirs.length; i++) {
            int dir = dirs[i];
            int newX = x + dx[dir];
            int newY = y + dy[dir];

            if (!exitCreated && isInsideBoundsWithExit(newX, newY) && maze[newX][newY] == 1) {
                maze[(x+newX)/2][(y+newY)/2] = 0;
                maze[newX][newY] = 0;

                // distanza minima richiesta (almeno 1/2 della dimensione del labirinto)
                int minDistance = Math.min(row, col) / 2;

                // --- USCITA ---
                if (newX == row-2 && newY > 0 && newY < col-1 &&
                    distance(newX+1, newY, entranceX, entranceY) >= minDistance) {
                    maze[newX+1][newY] = 0;
                    exitX = newX+1;
                    exitY = newY;
                    exitCreated = true;
                } else if (newY == col-2 && newX > 0 && newX < row-1 &&
                    distance(newX, newY+1, entranceX, entranceY) >= minDistance) {
                    maze[newX][newY+1] = 0;
                    exitX = newX;
                    exitY = newY+1;
                    exitCreated = true;
                } else if (newX == 1 && newY > 0 && newY < col-1 &&
                    distance(newX-1, newY, entranceX, entranceY) >= minDistance) {
                    maze[newX-1][newY] = 0;
                    exitX = newX-1;
                    exitY = newY;
                    exitCreated = true;
                } else if (newY == 1 && newX > 0 && newX < row-1 &&
                    distance(newX, newY-1, entranceX, entranceY) >= minDistance) {
                    maze[newX][newY-1] = 0;
                    exitX = newX;
                    exitY = newY-1;
                    exitCreated = true;
                }

                generateMainPath(newX, newY);
            }
        }
    }

/**
 * Calcola la distanza di Manhattan tra due punti.
 * @param x1 coordinata X del primo punto
 * @param y1 coordinata Y del primo punto
 * @param x2 coordinata X del secondo punto
 * @param y2 coordinata Y del secondo punto
 * @return distanza assoluta tra i due punti
 */
    private int distance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);   //valore assoluto
    }


/**
 * Genera ricorsivamente una biforcazione del percorso partendo da un
 * corridoio esistente, creando nuovi corridoi casuali.
 * @param x coordinata X di partenza
 * @param y coordinata Y di partenza
 **/
    private void generateBranch(int x, int y) {
        int[] dx = {-2, 2, 0, 0};
        int[] dy = {0, 0, -2, 2};
        int[] dirs = {0, 1, 2, 3};
        shuffleArray(dirs);

        for (int i = 0; i < dirs.length; i++) {
            int dir = dirs[i];
            int newX = x + dx[dir];
            int newY = y + dy[dir];

            if (isInsideBounds(newX, newY) && maze[newX][newY] == 1) {
                maze[(x+newX)/2][(y+newY)/2] = 0;
                maze[newX][newY] = 0;
                generateBranch(newX, newY);
            }
        }
    }

/**
 * Verifica se le coordinate specificate sono all'interno dei limiti
 * interni del labirinto (escludendo i bordi).
 * @param x coordinata X da controllare
 * @param y coordinata Y da controllare
 * @return true se la posizione è valida, false altrimenti
 **/    
    private boolean isInsideBounds(int x, int y) {
        return x > 0 && x < row-1 && y > 0 && y < col-1;
    }
    
/**
 * Verifica se le coordinate specificate sono valide per la generazione
 * del percorso principale e dell'uscita.
 * @param x coordinata X da controllare
 * @param y coordinata Y da controllare
 * @return true se la posizione è valida, false altrimenti
 **/
    private boolean isInsideBoundsWithExit(int x, int y) {
        return x > 0 && x <= row-2 && y > 0 && y <= col-2;
    }

/**
 * Mescola casualmente gli elementi di un array di interi.
 * @param array array da mescolare
 */
    private void shuffleArray(int[] array) {
        for (int i = array.length-1; i > 0; i--) {
            int j = rand.nextInt(i+1);
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
    }
    
    public void printMaze() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                System.out.print(this.maze[i][j] + "\t");
            }
            System.out.println();
        }
    }
    
    /**
     * Stampa il labirinto utilizzando simboli:
     * <ul>
     *   <li>E = entrata</li>
     *   <li>U = uscita</li>
     *   <li>. = corridoio</li>
     *   <li># = muro</li>
     * </ul>
     */
    public void printMazeSymbols() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (i == entranceX && j == entranceY) System.out.print("E ");
                else if (i == exitX && j == exitY) System.out.print("U ");
                else if (maze[i][j] == 0) System.out.print(". ");
                else System.out.print("# ");
            }
            System.out.println();
        }
    }

    // Getter e setter

    public int[][] getMaze() {
        return maze;
    }

    public void setMaze(int[][] maze) {
        this.maze = maze;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isExitCreated() {
        return exitCreated;
    }

    public void setExitCreated(boolean exitCreated) {
        this.exitCreated = exitCreated;
    }

    public int getEntranceX() {
        return entranceX;
    }

    public void setEntranceX(int entranceX) {
        this.entranceX = entranceX;
    }

    public int getEntranceY() {
        return entranceY;
    }

    public void setEntranceY(int entranceY) {
        this.entranceY = entranceY;
    }

    public int getExitX() {
        return exitX;
    }

    public void setExitX(int exitX) {
        this.exitX = exitX;
    }

    public int getExitY() {
        return exitY;
    }

    public void setExitY(int exitY) {
        this.exitY = exitY;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }
    
}
