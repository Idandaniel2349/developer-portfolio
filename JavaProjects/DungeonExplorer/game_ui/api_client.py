import requests

BASE_URL = "http://localhost:8080/game"

"""
    API Client for interacting with the Dungeon Explorer backend.
"""
class APIClient:

    def new_game(self,player_name: str):
        data = {"playerName": player_name}
        response = requests.post(f"{BASE_URL}/new", json=data)
        game_data = response.json()
        return game_data
    
    def load_game(self, session_id):
        data = {"sessionId": session_id}
        response = requests.post(f"{BASE_URL}/load", json=data)
        return response.json()
    
    def move(self, session_id, direction):
        data = {"sessionId": session_id, "direction" : direction}
        response = requests.post(f"{BASE_URL}/move", json=data)
        return response.json()
    
    def fight(self, session_id, enemy_id):
        data = {"sessionId": session_id, "enemyId" : enemy_id}
        response = requests.post(f"{BASE_URL}/fight", json=data)
        return response.json()
    
    def use_item(self, session_id, item_template_id):
        data = {"sessionId": session_id, "itemTemplateId" : item_template_id}
        response = requests.post(f"{BASE_URL}/use-item", json=data)
        return response.json()