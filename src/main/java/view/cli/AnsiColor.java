package view.cli;

import java.awt.*;

class AnsiColor {
    AnsiColor(){}

    static String getDefault() {
        return "\u001B[0m";
    }
    static String getAnsi(Color color) {
        if(color.equals(Color.pink))        return "\u001B[35m";
        if(color.equals(Color.white))       return "\u001B[37m";
        if(color.equals(Color.black))       return "\u001B[30m";
        if(color.equals(Color.red))         return "\u001B[31m";
        if(color.equals(Color.green))       return "\u001B[32m";
        if(color.equals(Color.yellow))      return "\u001B[33m";
        if(color.equals(Color.blue))        return "\u001B[36m";
        if(color.equals(Color.magenta))     return "\u001B[35m";
        if(color.equals(Color.darkGray))    return "\u001B[90m";


        return "\u001B[0m";
    }

    static String getColorName(Color color) {
        if(color.equals(Color.pink)) return "pink";
        if(color.equals(Color.white)) return "white";
        if(color.equals(Color.black)) return "black";
        if(color.equals(Color.red)) return "red";
        if(color.equals(Color.green)) return "green";
        if(color.equals(Color.yellow)) return "yellow";
        if(color.equals(Color.blue)) return "blue";
        if(color.equals(Color.magenta)) return "magenta";

        return "gray";
    }


}
