package com.acme.bank.utils;

public class VerifyInfo {
    String sid;
    String token;
    int id_order;
    public VerifyInfo(String sid, String token, int id){
        this.id_order = id;
        this.token = token;
        this.sid = sid;
    }
}
