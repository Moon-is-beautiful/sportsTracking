import os

from flask import Flask, jsonify, request, send_from_directory
from flask_cors import CORS

from backend import cipher, comparison, database

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
@app.route("/createAccount", methods=["POST"])
def createAccount():
    newUserInfo = request.get_json()

    name = newUserInfo["name"]
    password = newUserInfo["loginPassword"]
    username = newUserInfo["loginUsername"]

    password = cipher.encrypt_password(password)

    database.createUser(name, username, password)

    return jsonify({"message": True}), 201


# Function that triggers when API for logging in gets called
# authenticates user information by checking if username password are in the database
@app.route("/login", methods=["POST"])
def login():
    userInfo = request.get_json()

    name = userInfo["name"]
    username = userInfo["loginUsername"]
    password = userInfo["loginPassword"]

    # right now deciding not to encrypt username
    # username = cipher.encrypt_user_id(username)
    password = cipher.encrypt_password(password)

    authentication = database.authenticateLogin(name, username, password)
    return jsonify({"authentication": authentication}), 201


# Function that will get the routes located in our database and load it into the front-end
@app.route("/getFootballRoute", methods=["GET"])
def get_football_route():
    route_name = request.args.get("routeName")  # getting the slant route data
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
@app.route("/startRoute", methods=["POST"])
def startRoute():
    data = request.get_json()

    # get data from the request
    time = data.get("timestamp")
    x = data.get("x_coordinates")
    y = data.get("y_coordinates")

    if not all([time is not None, x, y]):
        return jsonify({"error": "Missing data"}), 400

    # need to update the database createplay function to return the play number that it created
    # right now hard coding name but eventually we'll be pulling the name of the person
    database.createPlay("Jack")
    database.updatePlay("Jack", 1, time, x, y)

    return jsonify({"message": "Data received successfully"}), 200


@app.route("/calculateAccuracy", methods=["POST"])
def calculateAccuracy():
    # need to get the data document holding all the location information from the front-end
    data = request.get_json()

    route_name = data.get("routeName")

    # need to separate the data into two lists: x-coordinates and y-coordinates
    user_x_coordinates = data.get("x_coordinates", [])
    user_y_coordinates = data.get("y_coordinates", [])
    # need to get the ideal location from the back-end
    routeData = database.getFootballRouteData(route_name)
    if not routeData:
        return jsonify({"error": f"No data found for route '{route_name}'"}), 404

    ideal_x_coordinates = routeData.get("x-coordinates", [])
    ideal_y_coordinates = routeData.get("y-coordinates", [])
    # need to call the Compare Function
    score = comparison.compareRoutes(
        user_x_coordinates, user_y_coordinates, ideal_x_coordinates, ideal_y_coordinates
    )
    return jsonify({"Accuracy Score": score}), 200


if __name__ == "__main__":
    app.run(host="0.0.0.0", debug=False, port=os.environ.get("PORT", 80))
