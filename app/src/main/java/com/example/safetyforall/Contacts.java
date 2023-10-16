package com.example.safetyforall;

public class Contacts {

    String C1,C2,C3;

    public Contacts(){
        C1="";
        C2="";
        C3="";
    }

    public Contacts(String c1, String c2, String c3) {
        C1 = c1;
        C2 = c2;
        C3 = c3;
    }

    public void setC1(String c1) {
        C1=c1;
    }

    public void setC2(String c2) {
        C2=c2;
    }

    public void setC3(String c3) {
        C3=c3;
    }

    public String getC1() {
        return C1;
    }

    public String getC2() {
        return C2;
    }

    public String getC3() {
        return C3;
    }
}
