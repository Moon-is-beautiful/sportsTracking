from flask import Flask, send_from_directory, request, jsonify
from flask_cors import CORS
import os
import cipher
import database

# Static folder holds the built React app directory
app = Flask(__name__, static_folder="build", static_url_path="")
CORS(app)

# Setting up initial functions we'll need

# Function that gets triggered at the start of opening up the android app
# displays initial home page
@app.route()
def home():



# Function that gets triggered when API for creating account is called
# takes information from the front-end and store it in the backend. Will call cipher.py to encrypt password and username
@app.route()
def createAccount():



# Function that triggers when API for logging in gets called
# authenticates user information by checking if username password are in the database
@app.route()
def login():



# Function that will get the routes located in our database and load it into the front-end
@app.route()
def getRoutes():



# Function that will pull more information from a specific route in the database and then return info to the front-end
@app.route()
def joinRoute():



# Function that will start the route for practice and start collecting location info from the phone
@app.route()
def startRoute():



if __name__ == '__main__':
    app.run(host="0.0.0.0", debug=False, port=os.environ.get("PORT", 80))