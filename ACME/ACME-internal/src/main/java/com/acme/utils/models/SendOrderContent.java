package com.acme.utils.models;

public class SendOrderContent {
    public String info;
    public String bank_url;
    public Double total_price;

    public SendOrderContent(String info,String bank_url, Double total_price) {
        this.info = info;
        this.bank_url = bank_url;
        this.total_price = total_price;
    }

}
