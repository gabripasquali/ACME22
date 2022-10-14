from textwrap import indent
from flask import Flask
from flask import jsonify, request, abort
import random


app = Flask(__name__)

@app.route('/')
def index():
    return "hello world"

@app.route('/getAvailability', methods=['POST'])
@app.route('/getAvailability', methods=['POST'])
def getAvailability():
    order = random.choices(["True","False"], weights=(9,1), k=1)
    result = {"disp" : order[0]}
    return jsonify(result)

@app.route('/abortOrder', methods=['GET'])
def abortOrder():
    return jsonify(status = 'OK')
