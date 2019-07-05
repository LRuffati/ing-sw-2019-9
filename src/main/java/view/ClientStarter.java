package view;

import controller.ClientController;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

/**
 * Contstructor of the client.
 * This class is used to create {@link ClientController}, that is the main controller of the client.
 * It asks whether the game start with cli or gui and rmi or socket. It also asks for the required {@link controller.GameMode gameMode} and for the IP address of the server
 */
public class ClientStarter {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Benvenuto su Adrenalina!");
        String res = "";
        boolean socket = true;
        boolean cli = true;
        boolean normal = true;
        String networkAddress = "";
        do {
            System.out.print("Socket [0] o RMI [1]:\t");
            res = scanner.next();
            if(res.equals("0")) socket = true;
            if(res.equals("1")) socket = false;
        } while (!res.equals("0") && !res.equals("1"));
        do {
            System.out.print("CLI [0] o GUI[1]:\t");
            res = scanner.next();
            if(res.equals("0")) cli = true;
            if(res.equals("1")) cli = false;
        } while (!res.equals("0") && !res.equals("1"));

        System.out.print("Normale [0] o Dominazione [1]:\t");
        res = scanner.next();
        if(res.equals("1")) normal = false;


        System.out.print("Indirizzo di rete (premi invio per usare localhost):\t");
        scanner.nextLine();
        res = scanner.nextLine();
        networkAddress = res.equals("") ? "localhost" : res;

        try {
            new ClientController(socket, cli, networkAddress, normal);
        }
        catch (IOException | NotBoundException e) {
            e.printStackTrace();
        }
    }
}
