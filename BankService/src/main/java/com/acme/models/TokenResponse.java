package com.acme.models;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TokenResponse {
    public String token;
    public String user;
    public String status;
}
