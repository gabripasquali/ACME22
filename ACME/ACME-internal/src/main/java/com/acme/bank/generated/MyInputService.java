package com.acme.bank.generated;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 3.5.3
 * 2022-08-25T16:20:10.294+02:00
 * Generated source version: 3.5.3
 *
 */
@WebServiceClient(name = "MyInputService",
                  wsdlLocation = "file:/C:/Users/Admin/Desktop/IngSw/ACME22/ACME22/BankService/src/main/resources/bankService.wsdl",
                  targetNamespace = "it.unibo.wsdl")
public class MyInputService extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("it.unibo.wsdl", "MyInputService");
    public final static QName MyInputServicePort = new QName("it.unibo.wsdl", "MyInputServicePort");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/Users/Admin/Desktop/IngSw/ACME22/ACME22/BankService/src/main/resources/bankService.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(MyInputService.class.getName())
                .log(java.util.logging.Level.INFO,
                     "Can not initialize the default wsdl from {0}", "file:/C:/Users/Admin/Desktop/IngSw/ACME22/ACME22/BankService/src/main/resources/bankService.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public MyInputService(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public MyInputService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public MyInputService() {
        super(WSDL_LOCATION, SERVICE);
    }

    public MyInputService(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    public MyInputService(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    public MyInputService(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }




    /**
     *
     * @return
     *     returns MyInput
     */
    @WebEndpoint(name = "MyInputServicePort")
    public MyInput getMyInputServicePort() {
        return super.getPort(MyInputServicePort, MyInput.class);
    }

    /**
     *
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns MyInput
     */
    @WebEndpoint(name = "MyInputServicePort")
    public MyInput getMyInputServicePort(WebServiceFeature... features) {
        return super.getPort(MyInputServicePort, MyInput.class, features);
    }

}
