import unittest
import json
from backend.backend_app import app
from backend import database


class TestApp(unittest.TestCase):
    def setUp(self):
        # Set up the Flask test client
        self.app = app.test_client()
        self.app.testing = True

        # Set up test user data
        self.test_user = {
            'name': 'Jack',
            'username': 'jz500',
            'password': '789'
        }

        # # Clean up any existing test data
        # database.deleteUser(self.test_user['name'], self.test_user['username'])

    # def tearDown(self):
    #     # Clean up after each test
    #     database.deleteUser(self.test_user['name'], self.test_user['username'])

    def test_create_account(self):
        response = self.app.post('/createaccount', data=json.dumps(self.test_user), content_type='application/json')
        self.assertEqual(response.status_code, 201)

        # Verify that the user was added to the database
        user_exists = database.usernameExists(self.test_user['name'], self.test_user['username'])
        self.assertTrue(user_exists)

    def test_login_success(self):
        # First, create the user
        self.app.post('/createaccount', data=json.dumps(self.test_user), content_type='application/json')

        # Now, attempt to login
        login_data = {
            'name': self.test_user['name'],
            'loginUsername': self.test_user['username'],
            'loginPassword': self.test_user['password']
        }

        response = self.app.post('/login', data=json.dumps(login_data), content_type='application/json')
        self.assertEqual(response.status_code, 200)
        data = json.loads(response.data)
        self.assertTrue(data['authentication'])

    def test_get_football_route(self):
        # Assuming that there is a route named 'Slant' in the database
        response = self.app.get('/getFootballRoute', query_string={'routeName': 'Slant'})
        self.assertEqual(response.status_code, 200)
        data = json.loads(response.data)

        # Check that the data contains expected keys
        self.assertIn('time', data)
        self.assertIn('xCoordinates', data)
        self.assertIn('yCoordinates', data)
        self.assertIn('description', data)
        self.assertIn('additionalInformation', data)


if __name__ == '__main__':
    unittest.main()
