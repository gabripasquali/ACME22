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
    order = random.choice(["True","False"])
    return jsonify(disp = order)

@app.route('/abortOrder', methods=['GET'])
def abortOrder():
    return jsonify(status = 'OK')
