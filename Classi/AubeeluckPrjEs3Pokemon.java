/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.aubeeluckprjes3pokemon;

import java.util.Scanner;

/**
 *
 * @author aubee
 */
public class AubeeluckPrjEs3Pokemon {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Pokemon player = new Charmander();
        Pokemon p2 = new Bulbasaur();
        Game a = new Game(15, 15, player, p2);
        String direction;
        while(!a.gameEnd()){
            a.printMazeWithPokemon();
            System.out.println("Seleziona dove vuoi muoverti:");
            a.showAvailableMoves();
            direction = in.nextLine();
            a.movePokemon(direction);
            a.moveEnemy();
        }
        
    }
}
