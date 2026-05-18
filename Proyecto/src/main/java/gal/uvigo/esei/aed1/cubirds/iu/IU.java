package gal.uvigo.esei.aed1.cubirds.iu;

import java.util.Scanner;

import es.uvigo.esei.aed1.tads.list.List;
import gal.uvigo.esei.aed1.cubirds.core.TypeBird;

public class IU {

    /*
     * ⠀⠀⠀⠀⠀⠀⠀
     * ⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣿⣿⡟⠋⢻⣷⣄⡀⠀⠀⠀⠀
     * ⠀⠀⠀⠀⣤⣾⣿⣷⣿⣿⣿⣿⣿⣶⣾⣿⣿⠿⠿⠿⠶⠄
     * ⠀⠀⠀⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠉⠀⠀⠀⠀⠀⠀
     * ⠀⠀⢸⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀
     * ⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⠀⠀⠀⠀⠀⠀⠀
     * ⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⠀⠀⠀⠀⠀⠀⠀
     * ⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠃⠀⠀⠀⠀⠀⠀⠀⠀
     * ⠀⠀⢸⣿⣿⣿⣿⣿⣿⣿⣿⡟⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀
     * ⠀⠀⠈⣿⣿⣿⣿⣿⣿⠟⠻⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
     * ⠀⠀⣼⣿⣿⣿⣿⣿⣿⣆⣤⠿⢶⣦⡀⠀⠀⠀⠀⠀⠀⠀
     * ⠀⢰⣿⣿⣿⣿⣿⣿⣿⣿⡀⠀⠀⠀⠑⠀⠀⠀⠀⠀⠀⠀
     * ⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
     * ⠸⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
     * ⠀⠀⠀⠉⠉⠙⠛⠋⠉⠉
     * ⠀⠀⠀⠀⠀⠀⠀⠀
     */

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

    // Permite escoger un tipo de pájaro de una lista de tipos posibles.
    public TypeBird chooseBirdType(List<TypeBird> availableTypes) {
        int choice = -1;

        do {
            displayMessage("Escoge un tipo de pájaro válido:");
            for (int i = 0; i < availableTypes.size(); i++) {
                displayMessage((i + 1) + ". " + availableTypes.get(i));
            }
            choice = readNumber("");
        } while (choice < 1 || choice > availableTypes.size());

        return availableTypes.get(choice - 1);
    }

    // Permite escoger una fila de la mesa.
    public int chooseRow(int rowCount) {
        int choice = -1;

        do {
            displayMessage("Elige una fila (1-4):");
            for (int i = 0; i < rowCount; i++) {
                displayMessage((i + 1) + ". Fila " + (i + 1));
            }
            choice = readNumber("Fila: ");
        } while (choice < 1 || choice > rowCount);

        return choice - 1;
    }

    // Permite escoger un lado para colocar las cartas.
    public boolean chooseSide() {
        int choice = -1;

        do {
            choice = readNumber("En que lado quieres colocar las cartas? 1 izquierda, 2 derecha: ");
        } while (choice < 1 || choice > 2);

        return choice == 1;
    }

    // Permite escoger sí o no.
    public boolean chooseYesNo(String message) {
        int choice = -1;

        do {
            choice = readNumber(message + " (1 si, 2 no): ");
        } while (choice < 1 || choice > 2);

        return choice == 1;
    }

}
