package com.jackie.dm.switchif;

public class TestSwitchIf {


    public static void main(String[] args) {

    }

    private void testSwitch(int num) {
        switch (num) {
            case 10:
                System.out.println("5");
            case 20:
                System.out.println("15");
            case 30:
                System.out.println("25");
            case 40:
                System.out.println("35");
            case 50:
                System.out.println("45");
            case 60:
                System.out.println("55");
            case 80:
                System.out.println("65");
            default:
                break;
        }
    }


    private void testIf(int number) {

        if (number > 0 && number < 10) {
            System.out.println("5");
        } else if (number > 10 && number < 20) {
            System.out.println("15");
        } else if (number > 30 && number < 40) {
            System.out.println("35");
        } else if (number > 40 && number < 50) {
            System.out.println("45");
        } else if (number > 50 && number < 60) {
            System.out.println("55");
        } else if (number > 60 && number < 70) {
            System.out.println("65");
        } else if (number > 70 && number < 80) {
            System.out.println("75");
        } else if (number > 80 && number < 90) {
            System.out.println("85");
        }


    }

}
