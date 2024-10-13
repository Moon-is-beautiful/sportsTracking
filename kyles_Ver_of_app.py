from flask import Flask, request, jsonify
from flask_pymongo import PyMongo
from werkzeug.security import generate_password_hash, check_password_hash
from bson import ObjectId
import jwt
import datetime
from functools import wraps
from pymongo.mongo_client import MongoClient
from pymongo.server_api import ServerApi

app = Flask(__name__)




# Configuring the MongoDB database
uri = "mongodb+srv://admin:2Dumb2Live%21@cluster0.od1dgod.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
client = MongoClient(uri, server_api=ServerApi('1'))

app.config['SECRET_KEY'] = '5555'



def createUser(username, encryptedUserID, encryptedPassword):
    db = client.get_database("UserLoginCredentials")
    collection = db[username]
    userDocument = {
        "userID": encryptedUserID,
        "password": encryptedPassword,
        "dateCreated": datetime.datetime.now(),
    }
    collection.insert_one(userDocument)

# Function to determine if username exists or not in the database 
def getUser(username):
    db = client.get_database("UserLoginCredentials")
    ListOfCollections = db.list_collection_names()
    return username in ListOfCollections





# Authentication decorator
def token_required(f):
    @wraps(f)
    def decorated(*args, **kwargs):
        token = request.headers.get('x-access-token')
        if not token:
            return jsonify({'message': 'Token is missing!'}), 401
        try:
            data = jwt.decode(token, app.config['SECRET_KEY'], algorithms=['HS256'])
            db = client.get_database("UserLoginCredentials")
            current_user = db[data['username']].find_one({'userID': data['user_id']})
            if not current_user:
                return jsonify({'message': 'User not found!'}), 401
        except jwt.ExpiredSignatureError:
            return jsonify({'message': 'Token has expired!'}), 401
        except jwt.InvalidTokenError:
            return jsonify({'message': 'Invalid token!'}), 401
        return f(current_user, *args, **kwargs)
    return decorated

# User registration endpoint
@app.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    username = data.get('username')
    password = data.get('password')

    if not username or not password:
        return jsonify({'message': 'Username and password are required!'}), 400

    if getUser(username):
        return jsonify({'message': 'User already exists!'}), 400

    encryptedUserID = generate_password_hash(username + datetime.datetime.utcnow().isoformat())
  
    encryptedPassword = generate_password_hash(password)

    createUser(username, encryptedUserID, encryptedPassword)

    return jsonify({'message': 'User registered successfully!'}), 201

# User login endpoint
@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    username = data.get('username')
    password = data.get('password')

    db = client.get_database("UserLoginCredentials")
    user = db[username].find_one()
    if not user or not check_password_hash(user['password'], password):
        return jsonify({'message': 'Invalid username or password!'}), 401

    token = jwt.encode({'username': username, 'user_id': user['userID'], 'exp': datetime.datetime.utcnow() + datetime.timedelta(hours=1)}, app.config['SECRET_KEY'], algorithm='HS256')

    return jsonify({'token': token}), 200

# Protected route example
@app.route('/protected', methods=['GET'])
@token_required
def protected_route(current_user):
    return jsonify({'message': f'Welcome {current_user["username"]}! This is a protected route.'}), 200

# Error handling for common issues
@app.errorhandler(404)
def not_found(error):
    return jsonify({'message': 'Resource not found!'}), 404

@app.errorhandler(500)
def internal_error(error):
    return jsonify({'message': 'An internal error occurred!'}), 500

# Function to create a user (collection) with user info (document) into the userlogincredential (database)

if __name__ == '__main__':
    app.run(debug=True)
