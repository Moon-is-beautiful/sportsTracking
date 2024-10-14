
import sys
import database
from datetime import datetime

# Add the parent directory to the system path
sys.path.append("..")


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
	database.updatePlay(user.name, playNumber, datetime.now(), x_coordinate, y_coordinate)


user1 = User("Soobin", "sk250", "123")
create_user_test(user1)
create_play_test(user1)
create_play_test(user1)
update_play_test(user1, 1, 50, 100)
update_play_test(user1, 1, 150, 200)
update_play_test(user1, 2, 60, 110)
authenticate_login_test(user1)


