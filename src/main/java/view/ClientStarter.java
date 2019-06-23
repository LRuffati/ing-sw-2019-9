package view;

import controller.ClientController;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class ClientStarter {
    public static void main(String[] args) {
        try {
            new ClientController(true, true, "localhost");
        }
        catch (IOException | NotBoundException e) {
            e.printStackTrace();
        }
        /*
        Scanner scanner = new Scanner(System.in);
        System.out.println("Benvenuto su Adrenalina!");
        String res = "";
        boolean socket = true;
        boolean cli = true;
        String networkAddress = "";
        do {
            System.out.print("Socket [0] o RMI [1]:\t");
            res = scanner.next();
            if(res.equals("0")) socket = true;
            if(res.equals("1")) socket = false;
        } while (!res.equals("0") && !res.equals("1"));
        do {
            System.out.print("view.cli [0] o GUI[1]:\t");
            res = scanner.next();
            if(res.equals("0")) cli = true;
            if(res.equals("1")) cli = false;
        } while (!res.equals("0") && !res.equals("1"));

        System.out.print("Indirizzo di rete ('' per usare localhost):\t");
        scanner.nextLine();
        res = scanner.nextLine();
        networkAddress = res.equals("") ? "localhost" : res;

        try {
            new ClientController(socket, cli, networkAddress);
        }
        catch (IOException | NotBoundException e) {
            e.printStackTrace();
        }
        */
    }
}
