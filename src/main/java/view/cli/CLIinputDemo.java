package view.cli;

import java.util.Scanner;

public class CLIinputDemo {
    static String hey="";
    static String heyOld="";
    static String text;
    static Scanner input = new Scanner(System.in);
    static boolean inputTake=true;
    public static void main(String[] str){
        text = "write something";
        new Thread(()-> {
            while (inputTake) {
                print(input.nextLine());
            }}).start();
        System.out.println(text+" prova stampa");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        print("Simulating print");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        inputTake=false;
        print("finished");
    }
    public static void print(String in){
        System.out.println("Ricevuto: "+in);
    }
}
