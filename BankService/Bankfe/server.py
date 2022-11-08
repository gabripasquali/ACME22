from flask import Flask, render_template, jsonify, request, json
import requests
import xml.etree.ElementTree as ET


app = Flask(__name__)

@app.route('/')
def index():
    bill = request.args.get('bill')
    to_user = request.args.get('to')
    id_order = request.args.get('order')
    return render_template('login.html', bill = bill, to_user = to_user, id_order = id_order)
    
@app.route('/login-request', methods=['POST'])
def login():
    #read json body and get username, password and bill to complete soap request 
    reqData = json.loads(request.data)
    usr = reqData["username"]
    psw = reqData["password"]
    #check for presence of bill
    if "bill" in reqData:
        bill = reqData["bill"]
        to_user = reqData["to_user"]
        id_order = reqData["id_order"]
    
    
        body = """<?xml version="1.0" encoding="utf-8"?>
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                <soap:Body>
                <xsd1:login xmlns:xsd1='BankService.xds'>
                    <username>"""+usr+"""</username>
                    <password>"""+psw+"""</password>
                    <bill>"""+str(bill)+"""</bill>
                    <order_id>"""+str(id_order)+"""</order_id>
                    <to_user>"""+str(to_user)+"""</to_user>
                </xsd1:login>
                </soap:Body>
                </soap:Envelope>"""
    else:
        body = """<?xml version="1.0" encoding="utf-8"?>
                <soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
                <soap:Body>
                <xsd1:login xmlns:xsd1='BankService.xds'>
                    <username>"""+usr+"""</username>
                    <password>"""+psw+"""</password>
                </xsd1:login>
                </soap:Body>
                </soap:Envelope>"""

    headers = {"Content-Type": "text/xml; charset=utf-8"}
    
    response = requests.request("POST", "http://bank:8000/login",
                             headers = headers,
                             data = body)
    #parse xml response to get succesfull login and, in that case, sid
    #return info with json
    tree = ET.fromstring(response.text)
    for n in tree.iter('loginResponse'):
      success = n.find('success').text

    if success == 'true':
        for n in tree.iter('loginResponse'):
            sid = n.find('sid').text
            url = n.find('url')
        if url is None:
            response = {"success" : True, "sid" : sid}
        else :
            response = {"success" : True, "sid" : sid, "url": url.text}
    else :
        response = {"success" : False}
    
    return json.dumps(response)    

@app.route('/payment-page')
def bank_page():
    sid = request.args.get('sid')
    bill = request.args.get('bill')
    return render_template('bank.html', bill = bill, sid = sid)
    
@app.route('/payed')
def payed_page():
    return "already payed"
    
@app.route('/pay-request', methods=['POST'])
def pay_request():
    reqData = json.loads(request.data)
    sid = reqData["sid"]
    body = """<?xml version="1.0" encoding="utf-8"?>
                <s11:Envelope xmlns:s11='http://schemas.xmlsoap.org/soap/envelope/'>
                  <s11:Body>
                    <xsd1:pay xmlns:xsd1='BankService.xsd'>
                      <sid>"""+ sid +"""</sid>
                    </xsd1:pay>
                  </s11:Body>
                </s11:Envelope>"""
    headers = {"Content-Type": "text/xml; charset=utf-8"}
    response = requests.request("POST", "http://bank:8000/pay",
                             headers = headers,
                             data = body)
    success = 'false'
    tree = ET.fromstring(response.text)
    for n in tree.iter('payResponse'):
      success = n.find('success').text
    
    if success == 'true':
        for n in tree.iter('payResponse'):
            token = n.find('token').text
        response = {"success" : True, "token" : token}
    else:
        response = {"success" : False}
        
    return json.dumps(response)
    
@app.route('/verifyToken', methods=['POST'])
def verifyToken():
    reqData = json.loads(request.data)
    sid = reqData["sid"]
    token = reqData["token"]
    id_order = reqData["id_order"]
    body = """<s11:Envelope xmlns:s11='http://schemas.xmlsoap.org/soap/envelope/'>
            <s11:Body>
                <xsd1:verifyToken xmlns:xsd1='BankService.xsd'>
                  <sid>""" + sid + """</sid>
                  <order_id>""" + str(id_order) + """</order_id>
                  <token>""" + str(token) + """</token>
                </xsd1:verifyToken>
              </s11:Body>
            </s11:Envelope>"""  
    headers = {"Content-Type": "text/xml; charset=utf-8"}
    response = requests.request("POST", "http://bank:8000/verifyToken",
                             headers = headers,
                             data = body)
    
    success = 'false'
    tree = ET.fromstring(response.text)
    for n in tree.iter('verifyTokenResponse'):
        success = n.find('success').text
    
    if success == 'true':
        response = {"success" : True}
    else:
        response = {"success" : False}
        
    return json.dumps(response)
    
@app.route('/refound', methods=['POST'])
def refound():
    reqData = json.loads(request.data)
    sid = reqData["sid"]
    id_order = reqData["id_order"]
    body = """<s11:Envelope xmlns:s11='http://schemas.xmlsoap.org/soap/envelope/'>
            <s11:Body>
                <xsd1:refound xmlns:xsd1='BankService.xsd'>
                  <sid>""" + sid + """</sid>
                  <order_id>""" + str(id_order) + """</order_id>
                </xsd1:refound>
              </s11:Body>
            </s11:Envelope>"""  
    headers = {"Content-Type": "text/xml; charset=utf-8"}
    response = requests.request("POST", "http://bank:8000/verifyToken",
                             headers = headers,
                             data = body)
    success = 'false'
    
    tree = ET.fromstring(response.text)
    for n in tree.iter('refoundResponse'):
        success = n.find('success').text
    
    if success == 'true':
        response = {"success" : True}
    else:
        response = {"success" : False}
        
    return json.dumps(response)
    
@app.route('/confirm', methods=['POST'])
def confirm():
    reqData = json.loads(request.data)
    sid = reqData["sid"]
    id_order = reqData["id_order"]
    body = """<s11:Envelope xmlns:s11='http://schemas.xmlsoap.org/soap/envelope/'>
            <s11:Body>
                <xsd1:confirm xmlns:xsd1='BankService.xsd'>
                  <sid>""" + sid + """</sid>
                  <order_id>""" + str(id_order) + """</order_id>
                </xsd1:confirm>
              </s11:Body>
            </s11:Envelope>"""  
    headers = {"Content-Type": "text/xml; charset=utf-8"}
    response = requests.request("POST", "http://bank:8000/verifyToken",
                             headers = headers,
                             data = body)
    success = 'false'
    
    tree = ET.fromstring(response.text)
    for n in tree.iter('refoundResponse'):
        success = n.find('success').text
    
    if success == 'true':
        response = {"success" : True}
    else:
        response = {"success" : False}
        
    return json.dumps(response)
