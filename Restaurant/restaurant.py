from textwrap import indent
from flask import Flask
from flask import jsonify, request, abort
import random


app = Flask(__name__)

@app.route('/')
def index():
    return "hello world"

@app.route('/getAvailability', methods=['POST'])
def getAvailability():
    order = random.choices(["True","False"], weights=(9,1), k=1)
    return jsonify(disp = order)

@app.route('/abortOrder', methods=['GET'])
def abortOrder():
    return jsonify(status = 'OK')
