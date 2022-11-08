package com.acme.utils.models;

import java.io.Serializable;
import java.util.List;

public class DBOrder implements Serializable {
    public List<OrderInfo> orderInfoList;

    public DBOrder(List<OrderInfo> orderInfoList) {
        this.orderInfoList = orderInfoList;
        
    }
}
