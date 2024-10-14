from flask import Flask, send_from_directory, request, jsonify
from flask_cors import CORS
import os
from backend import cipher
from backend import database

# Static folder holds the built React app directory
app = Flask(__name__, static_folder="build", static_url_path="")
CORS(app)


# Setting up initial functions we'll need
# Function that gets triggered at the start of opening up the android app
# displays initial home page
@app.route("/")
def home():
    return "this is the home page"


# Function that gets triggered when API for creating account is called
# takes information from the front-end and store it in the backend. Will call cipher.py to encrypt password and username
@app.route("/createaccount", methods=["POST"])
def createAccount():
    newUserInfo = request.get_json()  # parse incoming json request data and return it

    name = newUserInfo['name']  # get the information from the key name
    password = newUserInfo['password']  # get the information from the key password
    username = newUserInfo['username']  # get the information from the key username

    # might change so that database uses the cipher.py functions instead of backend_app.py
    # username = cipher.encrypt(username, 3, 2) # encrypt the username
    password = cipher.encrypt_password(password)  # encrypt the password

    database.createUser(name, username, password)  # put the user in our database

    return 'Done', 201  # postman to check route integrity


# Function that triggers when API for logging in gets called
# authenticates user information by checking if username password are in the database
@app.route("/login", methods=["POST"])
def login():
    userInfo = request.get_json()

    name = userInfo['name']
    username = userInfo['loginUsername']
    password = userInfo['loginPassword']

    # right now deciding not to encrypt username
    # username = cipher.encrypt_user_id(username)
    password = cipher.encrypt_password(password)

    authentication = database.authenticateLogin(name, username, password)
    return jsonify({"authentication": authentication})


# Function that will get the routes located in our database and load it into the front-end
@app.route("/getFootballRoute", methods=["GET"])
def get_football_route():
    route_name = request.args.get('routeName')  # getting the slant route data
    if not route_name:
        return jsonify({"error": "routeName parameter is required"}), 400

    route_data = database.getFootballRouteData(route_name)
    if route_data:
        return jsonify(route_data), 200
    else:
        return jsonify({"error": "Route not found"}), 404


# # Function that will pull more information from a specific route in the database and then return info to the front-end
# @app.route()
# def joinRoute():
#
#
#
# # Function that will start the route for practice and start collecting location info from the phone
@app.route("/startRoute", methods = ["POST"])
def startRoute():
    data = request.get_json()
    
    # get data from the request
    time = data.get('timestamp')
    x = data.get('longitude')
    y = data.get('latitude')

    if not all([time is not None, x, y]):
        return jsonify({"error": "Missing data"}), 400
    
    # need to update the database createplay function to return the play number that it created
    # right now hard coding name but eventually we'll be pulling the name of the person
    database.createPlay("Jack")
    database.updatePlay("Jack", 1, time, x, y)

    return jsonify({"message": "Data received successfully"}), 200


if __name__ == '__main__':
    app.run(host="0.0.0.0", debug=False, port=os.environ.get("PORT", 80))
