import sys
from backend import cipher
from datetime import datetime

# Add the parent directory to the system path
sys.path.append("..")


class User:
    def __init__(self, name, username, password):
        self.name = name
        self.username = username
        self.password = password


def encrypt_user_info(user):
    encryptedUsername = cipher.encrypt_user_id(user.username)
    encryptedPassword = cipher.encrypt_password(user.password)
    print("Encrypted Username Hash:")
    print(encryptedUsername + "\n")
    print("Encrypted Password Hash:")
    print(encryptedPassword + "\n")


user1 = User("Kyle", "kl125", "456")
encrypt_user_info(user1)
