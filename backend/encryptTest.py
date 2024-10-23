import sys

from cryptography.fernet import Fernet
sys.path.append("..")
import encrypt

# Test for successful encryption
def test_successful_encryption():
    # Generate a new encryption key
    key = encrypt.generate_key()
    if key is None:
        print("Failed to generate encryption key.")
        return

    # Data to be encrypted
    data = "This is a test message"
    # Encrypt the data using the generated key
    encrypted_data = encrypt.encrypt_data(data, key)
    if encrypted_data:
        # Print the encrypted data if successful
        print(f"Successful Encryption: {encrypted_data}")
    else:
        print("Encryption failed.")

# Test for unsuccessful encryption
def test_unsuccessful_encryption():
    # Use an invalid key to attempt encryption
    key = b"invalid_key"
    try:
        # Attempt to encrypt data with an invalid key
        encrypt.encrypt_data("data", key)
        print("Unsuccessful encryption did not raise an error.")
    except Exception as e:
        # Print the expected failure message
        print(f"Expected failure for unsuccessful encryption: {e}")

# Test for successful decryption
def test_successful_decryption():
    # Generate a new encryption key
    key = encrypt.generate_key()
    if key is None:
        print("Failed to generate encryption key.")
        return

    # Data to be encrypted and then decrypted
    data = "This is a test message"
    # Encrypt the data
    encrypted_data = encrypt.encrypt_data(data, key)
    # Decrypt the encrypted data using the same key
    decrypted_data = encrypt.decrypt_data(encrypted_data, key)
    if decrypted_data == data:
        # Print the decrypted data if successful
        print(f"Successful Decryption: {decrypted_data}")
    else:
        print("Decryption failed.")

# Test for unsuccessful decryption
def test_unsuccessful_decryption():
    # Generate a new encryption key
    key = encrypt.generate_key()
    if key is None:
        print("Failed to generate encryption key.")
        return

    # Data to be encrypted
    data = "This is a test message"
    # Encrypt the data
    encrypted_data = encrypt.encrypt_data(data, key)
    # Generate a different key for decryption
    wrong_key = encrypt.generate_key()
    try:
        # Attempt to decrypt the encrypted data with the wrong key
        decrypt_data = encrypt.decrypt_data(encrypted_data, wrong_key)
        print("Unsuccessful decryption did not raise an error.")
    except Exception as e:
        # Print the expected failure message
        print(f"Expected failure for unsuccessful decryption: {e}")

if __name__ == "__main__":
    # Run test for successful encryption
    print("Running test for successful encryption...")
    test_successful_encryption()
    # Run test for unsuccessful encryption
    print("\nRunning test for unsuccessful encryption...")
    test_unsuccessful_encryption()
    # Run test for successful decryption
    print("\nRunning test for successful decryption...")
    test_successful_decryption()
    # Run test for unsuccessful decryption
    print("\nRunning test for unsuccessful decryption...")
    test_unsuccessful_decryption()