package view.cli;

import java.util.function.Consumer;

class CommandParser{

    private Consumer<String> strings;
    private CLIDemo cliDemo;

    private State state;

    CommandParser(CLIDemo cliDemo){
        this.cliDemo = cliDemo;
        this.state = State.MAIN;
    }

    void bind(Consumer<String> strings){
        this.strings = strings;
    }

    void parseCommand(String str) {
        System.out.println("parsing\t" + str);
        switch (state) {
            case MAIN:
                if (str.equalsIgnoreCase("info")) {
                    state = State.INFO;
                    cliDemo.askInfo();
                    break;
                }
                if (str.equalsIgnoreCase("quit")) {
                    state = State.QUIT;
                    cliDemo.quitGame();
                    break;
                }
                strings.accept(str);
                /*
                if (strings != null) {
                    strings.accept(str);
                    strings = null;
                }
                */
                break;

            case INFO:
                switch (str) {
                    case "1":
                        state = State.CHOOSEPLAYER;
                        cliDemo.askPlayer();
                        break;
                    case "2":
                        state = State.CHOOSETILE;
                        cliDemo.askTile();
                        break;

                        default:
                            state = State.MAIN;
                            cliDemo.getPrintedMap();
                            System.out.println(cliDemo.pickStringMessage);
                            break;
                }
                break;

            case CHOOSEPLAYER:
                cliDemo.choosePlayer(str);
                state = State.MAIN;
                cliDemo.getPrintedMap();
                System.out.println(cliDemo.pickStringMessage);
                break;

            case CHOOSETILE:
                cliDemo.chooseTile(str);
                state = State.MAIN;
                cliDemo.getPrintedMap();
                System.out.println(cliDemo.pickStringMessage);
                break;

            case QUIT:
                switch (str) {
                    case "y":
                        cliDemo.confirmqQuit();
                        break;
                    case "n":
                    default:
                        state = State.MAIN;
                        cliDemo.getPrintedMap();
                        System.out.println(cliDemo.pickStringMessage);
                        break;
                }
                break;


            default:
                System.out.println("type info or quit");
                break;
        }
    }

    private enum State {
        MAIN, INFO, CHOOSEPLAYER, CHOOSETILE, QUIT
    }
}