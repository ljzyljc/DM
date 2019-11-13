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

    public static void main(String[] args) {
        TestC testC = new TestC();
        testC.test();

        TestA testA = new TestA() {

            @Override
            public void fly() {

            }
        };
    }
}
