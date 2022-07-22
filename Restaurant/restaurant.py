from flask import Flask
from flask import jsonify, request, abort
import random


app = Flask(__name__)

@app.route('/')
def index():
    return "hello world"

@app.route('/getAvailability', methods=['POST'])
def getAvailability():
    #lettura del body
    #controlli sull'ordine
    order['status'] = random.choice([True, False])
    return jsonify(order)

@app.route('/abortOrder', methods=['PUT'])
def abortOrder():
    return jsonify(status = 'OK')
