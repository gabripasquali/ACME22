package com.acme.bank.generated;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.5.3
 * 2022-08-26T09:02:18.775+02:00
 * Generated source version: 3.5.3
 *
 */
@WebServiceClient(name = "BankService",
                  wsdlLocation = "file:/C:/Users/Admin/Desktop/IngSw/ACME22/ACME22/BankService/src/main/resources/bankService.wsdl",
                  targetNamespace = "it.unibo.wsdl")
public class BankService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("it.unibo.wsdl", "BankService");
    public final static QName BankServicePort = new QName("it.unibo.wsdl", "BankServicePort");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/Users/Admin/Desktop/IngSw/ACME22/ACME22/BankService/src/main/resources/bankService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(BankService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/C:/Users/Admin/Desktop/IngSw/ACME22/ACME22/BankService/src/main/resources/bankService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public BankService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public BankService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public BankService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public BankService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public BankService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public BankService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns Bank
     */
    @WebEndpoint(name = "BankServicePort")
    public Bank getBankServicePort() {
        return super.getPort(BankServicePort, Bank.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns Bank
     */
    @WebEndpoint(name = "BankServicePort")
    public Bank getBankServicePort(WebServiceFeature... features) {
        return super.getPort(BankServicePort, Bank.class, features);
    }

}
