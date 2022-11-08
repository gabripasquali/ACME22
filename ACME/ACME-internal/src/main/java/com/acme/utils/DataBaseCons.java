package com.acme.utils;

import java.util.ArrayList;
import java.util.List;
import java.io.*;
import com.acme.utils.models.DBOrder;
import com.acme.utils.models.OrderInfo;
import com.acme.utils.models.Status;

import camundajar.impl.com.google.gson.Gson;
import camundajar.impl.com.google.gson.stream.JsonReader;
import camundajar.impl.com.google.gson.stream.JsonWriter;

public class DataBaseCons {
    static String dbName = "consDB.json";
    public List<OrderInfo> orderInfoList;

    public DataBaseCons(){

        Gson g = new Gson();
        /**TRY READ FROM FILE**/
        try (JsonReader reader = new JsonReader(new FileReader(dbName))) {
            DBOrder db = g.fromJson(reader, DBOrder.class);
            this.orderInfoList = db.orderInfoList;
        } catch (Exception e) {
            /**INITILIAZE EMPTY FILE AND ARRAY**/
            File file = new File(dbName);
            try {
                file.createNewFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

            this.orderInfoList = new ArrayList<>();

            this.save();
            System.out.println("NON TROVO IL FILE");
        }
    }

    public void save() {
        Gson g = new Gson();
        try (JsonWriter jsonWriter = new JsonWriter(new FileWriter(dbName));){
            g.toJson(g.toJsonTree(new DBOrder(orderInfoList)), jsonWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public void addCosegna(OrderInfo info, DataBaseCons db) throws IOException {
        db.orderInfoList
                .add(info);
        db.save();
    }

    public int lastId(DataBaseCons db){
        return db.orderInfoList.size();
    }


    public void modifyStatus(int id, DataBaseCons db, Status newStatus){
        db.orderInfoList
                .stream()
                .filter(orderInfo -> id == orderInfo.id)
                .forEach(orderInfo -> orderInfo.status = newStatus);
        db.save();
    }
}