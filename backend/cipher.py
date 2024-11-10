# from cryptography.fernet import Fernet, InvalidToken
from werkzeug.security import generate_password_hash, check_password_hash
import datetime

# # Generate a key for encryption and decryption
# # You must store this key securely to be able to decrypt the data later
# def generate_key():
#     try:
#         key = Fernet.generate_key()
#         return key
#     except Exception as e:
#         print(f"Error generating key: {e}")
#         return None
#
# # Encrypt data using the generated key
# def encrypt_password(data, key):
#     try:
#         fernet = Fernet(key)
#         encrypted_data = fernet.encrypt(data.encode())
#         return encrypted_data
#     except TypeError as e:
#         print(f"Type Error: {e} - Make sure the input data is a string and key is valid.")
#     except Exception as e:
#         print(f"Error encrypting data: {e}")
#     return None
#
# # Decrypt data using the generated key
# def decrypt_password(encrypted_data, key):
#     try:
#         fernet = Fernet(key)
#         decrypted_data = fernet.decrypt(encrypted_data).decode()
#         return decrypted_data
#     except InvalidToken as e:
#         print(f"Invalid Token Error: {e} - The key used for decryption does not match the one used for encryption.")
#     except TypeError as e:
#         print(f"Type Error: {e} - Make sure the encrypted data and key are valid.")
#     except Exception as e:
#         print(f"Error decrypting data: {e}")
#     return None
#
# # Example usage
# if __name__ == "__main__":
#     # Generate and store the key securely
#     key = generate_key()
#     if key is None:
#         print("Failed to generate encryption key.")
#     else:
#         print(f"Encryption Key: {key}\n")
#
#         # Encrypt a sample username and password
#         username = "kl125"
#         password = "456"
#
#         encrypted_username = encrypt_data(username, key)
#         encrypted_password = encrypt_data(password, key)
#
#         if encrypted_username and encrypted_password:
#             print(f"Encrypted Username: {encrypted_username}")
#             print(f"Encrypted Password: {encrypted_password}\n")
#
#             # Decrypt the data
#             decrypted_username = decrypt_data(encrypted_username, key)
#             decrypted_password = decrypt_data(encrypted_password, key)
#
#             if decrypted_username and decrypted_password:
#                 print(f"Decrypted Username: {decrypted_username}")
#                 print(f"Decrypted Password: {decrypted_password}")
#             else:
#                 print("Failed to decrypt the data.")
#         else:
#             print("Failed to encrypt username or password.")


def encrypt_user_id(username):
    return generate_password_hash(username + datetime.datetime.utcnow().isoformat(), method='pbkdf2:sha256')


def encrypt_password(password):
    return generate_password_hash(password, method='pbkdf2:sha256')


def verify_password(encrypted_password, password):
    return check_password_hash(encrypted_password, password)