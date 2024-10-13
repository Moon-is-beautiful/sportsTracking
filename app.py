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
@app.route("/")
def home():
	return "this is the home page"


# Function that gets triggered when API for creating account is called
# takes information from the front-end and store it in the backend. Will call cipher.py to encrypt password and username
@app.route("/createaccount", methods = ["POST"])
def createAccount():
	newUserInfo = request.get_json() # parse incoming json request data and return it	

	name  = newUserInfo['name'] # get the information from the key name
	password = newUserInfo['password'] # get the information from the key password
	username = newUserInfo['username'] # get the information from the key username
	
	#might change so that database uses the cipher.py functions instead of app.py
	username = cipher.encrypt(username, 3, 2) # encrypt the username
	password = cipher.encripy(password, 4, 2) # encrypt the password

	database.createUser(name, userID, password) # put the user in our database
	
	return 'Done', 201 # postman to check route integrity


# Function that triggers when API for logging in gets called
# authenticates user information by checking if username password are in the database
@app.route("/login", methods=["POST"])
def login():
	userInfo = request.get_json()
	username = userInfo['loginUsername'] 
	password = userInfo['loginPassword']
	
	username = cipher.encrypt(username, 3, 2)
	password = cipher.encrypt(username, 4, 2)
	
	authentication = database.authenticateLogin(username, password)
	return jsonify({"authentication": authentication})


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
