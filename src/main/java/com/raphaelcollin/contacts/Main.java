/*
 * *
 *  @author <Raphael Collin> <rapphaelmanhaes2017@hotmail.com>
 *  @copyright (c) 2019
 * /
 */

package com.raphaelcollin.contacts;

/* This separate class is necessary to prevent a bug when the application is packaged. JDK 11 does not contain JavaFX
* and if the main class of the application extends Application, the executable file does not run */

public class Main{
    public static void main(String[] args) {
        FXMain.main(args);
    }
}
