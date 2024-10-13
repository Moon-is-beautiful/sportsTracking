from werkzeug.security import generate_password_hash, check_password_hash
import datetime


def encrypt_user_id(username):
    return generate_password_hash(username + datetime.datetime.utcnow().isoformat())

def encrypt_password(password):
    return generate_password_hash(password)

def verify_password(encrypted_password, password):
    return check_password_hash(encrypted_password, password)
