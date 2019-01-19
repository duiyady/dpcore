package com.duiya;

public class Test {
    public static void main(String[] args) {
        Exceptio exceptio = new Exceptio();
        System.out.println(exceptio.get());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sfas");

    }
}

class Exceptio {

    public int get() {
        int i = 0;
        try {
            i = 10;
            int j = 10 / 0;

        } catch (Exception e) {
            throw new RuntimeException("chuyi0");
        } finally {
            try {
                int r = 9;
                int e = r / 0;
                System.out.println(e);
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
        return i/5;
    }
}
