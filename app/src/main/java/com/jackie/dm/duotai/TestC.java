package com.jackie.dm.duotai;

public class TestC implements TestA,TestB {
    @Override
    public void test() {

    }

    @Override
    public void skip() {

    }

    @Override
    public void fly() {

    }


    public String getName(){
        return "=================";
    }

    public static void main(String[] args) {
        TestC testC = new TestC();
        testC.test();
        System.out.println(testC.getName());
    }
}
