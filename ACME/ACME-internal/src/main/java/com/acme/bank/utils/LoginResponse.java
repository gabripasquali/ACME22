package com.acme.bank.utils;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    public boolean success;
    public String sid;
    public String url;
}
