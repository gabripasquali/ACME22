package com.acme.utils.models;

public class SendOrderContent {
    public String info;
    public String bank_url;

    public SendOrderContent(String info, String bank_url, Double total_price, int id) {
        this.info = info;
        this.bank_url = bank_url + "?bill="+ total_price + "&to=2&order="+ id ;
    }
}
