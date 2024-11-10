from datetime import datetime

from pymongo import MongoClient
from pymongo.mongo_client import MongoClient
from pymongo.server_api import ServerApi, ServerApiVersion

uri = "mongodb+srv://admin:2Dumb2Live%21@cluster0.od1dgod.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"

# Create a new client and connect to the server
client = MongoClient(uri, server_api=ServerApi("1"))


# Create a player (collection) and add a time-stamped document with the coordinates
def createUser(name, username, password):
    # check if username and password are already in the database
    if usernameExists(name, username):
        print(f"Username '{username}' already exists in the database '{name}'.")
        return

    personDB = client[name]
    accountInfo = personDB["Account Information"]
    accountDocument = {
        "username": username,
        "password": password,
    }  # Create a document (record) to upload to the collection
    insertAccount = accountInfo.insert_one(
        accountDocument
    )  # Insert the document into the player's collection
    print(f"Account created for username '{username}' in the database '{name}'.")


# Check if the username already exists in the collection
def usernameExists(name, username):
    personDB = client[name]
    accountInfo = personDB["Account Information"]
    existing_user = accountInfo.find_one({"username": username})
    return existing_user is not None  # Returns True if the username exists


def authenticateLogin(name, username, password):
    personDB = client[name]
    # Check if the name and username exists in the database
    if usernameExists(name, username) is True:
        # Grab the collection holding account information and check if username and password matches
        accountInfo = personDB.get_collection("Account Information")
        myquery = {"username": username, "password": password}
        # If username and password don't match return false to fail the authentication
        if accountInfo.find_one(myquery) is None:
            return False
        return True
    return False


# Get the next Play collection name
def getNextPlayName(db):
    collections = db.list_collection_names()
    play_collections = [col for col in collections if col.startswith("Play")]
    next_index = len(play_collections) + 1
    return f"Play {next_index}"


# Create a new Play collection
def createPlay(name):
    personDB = client[name]
    play_collection_name = getNextPlayName(personDB)
    play_collection = personDB[play_collection_name]

    # Initialize a single document with empty arrays for time, x-coordinate, and y-coordinate
    play_collection.insert_one({"Time": [], "x-coordinate": [], "y-coordinate": []})
    print(f"Collection '{play_collection_name}' created under the database '{name}'.")


# Update the time, x coordinate, and y coordinate arrays in the current Play collection
def updatePlay(name, play_number, new_time, new_x, new_y):
    personDB = client[name]
    play_collection_name = f"Play {play_number}"
    play_collection = personDB[play_collection_name]

    # Push new values into the respective arrays in the single document
    play_collection.update_one(
        {}, {"$push": {"time": new_time, "x-coordinate": new_x, "y-coordinate": new_y}}
    )
    print(f"Updated Play {play_number} with time: {new_time}, x: {new_x}, y: {new_y}.")


# Get the ideal football route data
def getFootballRouteData(route_name):
    db = client["FootballRoutes"]
    collection = db[route_name]

    # Fetch the documents
    data_doc = collection.find_one({"Time": {"$exists": True}})
    info_doc = collection.find_one({"Description": {"$exists": True}})

    if data_doc and info_doc:
        route_data = {
            "time": data_doc.get("Time", []),
            "x-coordinates": data_doc.get("x-coordinates", []),
            "y-coordinates": data_doc.get("y-coordinates", []),
            "description": info_doc.get("Description", ""),
            "additionalInformation": info_doc.get("Additional Information", ""),
        }
        return route_data
    else:
        return None


# 10/28 - Soobin added code
# Update the most recently created Play collection with new time and coordinates
def updateLocation(name, new_time, new_x, new_y):
    personDB = client[name]
    # Get the list of Play collections sorted by name to find the most recent
    play_collections = [
        col for col in personDB.list_collection_names() if col.startswith("Play")
    ]

    if not play_collections:
        print(f"No Play collections found in the database '{name}'.")
        return

    # Sort the collections in descending order to get the latest one
    play_collections.sort(key=lambda x: int(x.split(" ")[1]), reverse=True)
    latest_play = play_collections[0]
    play_collection = personDB[latest_play]

    # Push new values into the respective arrays in the single document
    play_collection.update_one(
        {}, {"$push": {"time": new_time, "x-coordinate": new_x, "y-coordinate": new_y}}
    )
    print(
        f"Updated the latest collection '{latest_play}' with time: {new_time}, x: {new_x}, y: {new_y}."
    )


# Function to store accuracy for the most recently created Play collection in a separate document
def updateAccuracy(name, Accuracy):
    personDB = client[name]
    # Get the list of Play collections sorted by name to find the most recent
    play_collections = [
        col for col in personDB.list_collection_names() if col.startswith("Play")
    ]

    if not play_collections:
        print(f"No Play collections found in the database '{name}'.")
        return

    # Sort the collections in descending order to get the latest one
    play_collections.sort(key=lambda x: int(x.split(" ")[1]), reverse=True)
    latest_play = play_collections[0]
    play_collection = personDB[latest_play]

    # Insert a new document for accuracy within the same "Play" collection
    accuracy_document = {
        "Type": "Accuracy",  # Adding a "type" field to differentiate this document
        "Accuracy": Accuracy,
    }
    # Insert the accuracy document into the same "Play" collection
    play_collection.insert_one(accuracy_document)
    print(
        f"Accuracy for the latest play '{latest_play}' stored as a separate document in the collection under the database '{name}'."
    )


# Retrieve user information upon sign-in
def getUserInfo(name, username):
    personDB = client[name]
    accountInfo = personDB["Account Information"]

    # Find the user document with the provided username
    user_doc = accountInfo.find_one({"username": username})

    if user_doc:
        return {"name": name, "username": user_doc.get("username")}
    else:
        print(f"User '{username}' not found in the database '{name}'.")
        return None


# Delete a collection
def deleteCollection(name, collection_name):
    personDB = client[name]
    if collection_name in personDB.list_collection_names():
        personDB.drop_collection(collection_name)
        print(f"Collection '{collection_name}' deleted from the database '{name}'.")
    else:
        print(
            f"Collection '{collection_name}' does not exist in the database '{name}'."
        )
