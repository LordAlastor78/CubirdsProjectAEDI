package gal.uvigo.esei.aed1.cubirds.iu;
/* 
Cubirds Project


░░░░░░░░▄▄▄▀▀▀▄▄███▄░░░░░░░░░░░░░░
░░░░░▄▀▀░░░░░░░▐░▀██▌░░░░░░░░░░░░░
░░░▄▀░░░░▄▄███░▌▀▀░▀█░░░░░░░░░░░░░
░░▄█░░▄▀▀▒▒▒▒▒▄▐░░░░█▌░░░░░░░░░░░░
░▐█▀▄▀▄▄▄▄▀▀▀▀▌░░░░░▐█▄░░░░░░░░░░░
░▌▄▄▀▀░░░░░░░░▌░░░░▄███████▄░░░░░░
░░░░░░░░░░░░░▐░░░░▐███████████▄░░░
░░░░░le░░░░░░░▐░░░░▐█████████████▄
░░░░toucan░░░░░░▀▄░░░▐█████████████▄ 
░░░░░░has░░░░░░░░▀▄▄███████████████ 
░░░░░arrived░░░░░░░░░░░░█▀██████░░




*/
import java.util.Scanner;

import es.uvigo.esei.aed1.tads.list.List;
import gal.uvigo.esei.aed1.cubirds.core.TypeBird;

public class IU {

    private final Scanner keyboard;

    public IU() {
        keyboard = new Scanner(System.in);
    }

    /**
     * Lee un num. de teclado
     *
     * @param msg El mensaje a visualizar.
     * @return El num., como entero
     */
    public int readNumber(String msg) {
        boolean repeat;
        int toret = 0;

        do {
            repeat = false;
            System.out.print(msg);
            try {
                toret = Integer.parseInt(keyboard.nextLine());
            } catch (NumberFormatException exc) {
                repeat = true;
            }
        } while (repeat);

        return toret;
    }

    public String readString(String msg) {
        String toret;
        System.out.print(msg);
        toret = keyboard.nextLine();
        return toret;
    }

    public void displayMessage(String msg) {
        System.out.println(msg);
    }

    // Pide al jugador a que escoja un tipo específico de la lista de tipos pasada
    // como parámetro.
    public TypeBird chooseBirdType(List<TypeBird> availableTypes) {
        int choice = -1;

        do {
            displayMessage("Escoge un tipo de pájaro válido:");
            for (int i = 0; i < availableTypes.size(); i++) {
                displayMessage((i + 1) + ". " + availableTypes.get(i));
                // display message con el número de opción y el tipo de pájaro correspondiente
            }
            choice = readNumber("");
        } while (choice < 1 || choice > availableTypes.size());

        return availableTypes.get(choice - 1);
    }

    // Pide al jugador a que escoja una de las filas de la mesa.
    public int chooseRow(int rowCount) {

        int choice = -1;

        do { // lógica para el tema del input, se repite hasta que el jugador elige un número
             // de fila válido.
            displayMessage("Elige una fila:");
            for (int i = 0; i < rowCount; i++) {
                displayMessage((i + 1) + ". Fila " + (i + 1));
            }
            choice = readNumber("Fila: ");
        } while (choice < 1 || choice > rowCount);

        return choice - 1;
    }

    // Pide al jugador a que escoja poner las cartas por la derecha o por la izquierda.
    public boolean chooseSide() {
        int choice = -1;

        do {
            choice = readNumber("En que lado quieres colocar las cartas? 1 izquierda, 2 derecha: ");
        } while (choice < 1 || choice > 2);

        return choice == 1;
    }

}
