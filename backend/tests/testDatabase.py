# import sys
from datetime import datetime

from backend import database

# Add the parent directory to the system path
# sys.path.append("..")


class User:
    def __init__(self, name, username, password):
        self.name = name
        self.username = username
        self.password = password


def create_user_test(user):
    database.createUser(user.name, user.username, user.password)


def authenticate_login_test(user):
    database.authenticateLogin(user.name, user.username, user.password)


def create_play_test(user):
    database.createPlay(user.name)


def update_play_test(user, playNumber, x_coordinate, y_coordinate):
    database.updatePlay(
        user.name, playNumber, datetime.now(), x_coordinate, y_coordinate
    )


user1 = User("Soobin", "sk250", "123")
create_user_test(user1)
create_play_test(user1)
create_play_test(user1)
update_play_test(user1, 1, 50, 100)
update_play_test(user1, 1, 150, 200)
update_play_test(user1, 2, 60, 110)
authenticate_login_test(user1)

# Testing the sequence of updating location and storing accuracy as separate documents:
# Step 1: Create a user and insert them into the database
database.createUser(
    "TestDB", "player1", "password123"
)  # First attempt to create database "TestDB"

# Step 2: Create a new Play collection
database.createPlay("TestDB")  # Creates "Play 1"

# Step 3: Update the time, x, and y coordinates for the latest Play collection
database.updateLocation(
    "TestDB", datetime.now(), 50, 100
)  # Update coordinates for "Play 1"
database.updateLocation(
    "TestDB", datetime.now(), 60, 110
)  # Add another entry to "Play 1"

# Step 4: Store the accuracy for the latest play as a separate document in the same collection
database.updateAccuracy("TestDB", 0.95)  # Store accuracy for "Play 1"

# Step 5: Create another Play collection and update its coordinates
database.createPlay("TestDB")  # Creates "Play 2"
database.updateLocation(
    "TestDB", datetime.now(), 150, 200
)  # Update coordinates for "Play 2"

# Step 6: Store the accuracy for the new latest play as a separate document
database.updateAccuracy("TestDB", 0.85)  # Store accuracy for "Play 2"

# Step 6: Delete the "Play 3" collection
database.deleteCollection(
    "TestDB", "Play 3"
)  # This will tell that "Play 3" does not exist

# Step 6: Delete the "Play 2" collection
database.deleteCollection(
    "TestDB", "Play 2"
)  # This will delete "Play 2" from the "TestDB" database
