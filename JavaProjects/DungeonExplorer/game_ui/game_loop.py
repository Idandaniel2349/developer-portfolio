import tkinter as tk
from api_client import APIClient
from ui_components import PlayerStatsFrame, PlayerInventoryFrame, RoomFrame, LootLogFrame

class GameApp:
    def __init__(self, root):
        self.root = root
        self.root.title("Dungeon Explorer")

        self.api_client = APIClient()
        self.game_session_id = None
        self.player = None
        self.current_room = None

         # UI layout
        self.stats_frame = PlayerStatsFrame(root)
        self.stats_frame.grid(row=0, column=0, sticky="nsew", padx=10, pady=10)

        self.inventory_frame = PlayerInventoryFrame(root, on_use_item=self.use_item)
        self.inventory_frame.grid(row=1, column=0, sticky="nsew", padx=10, pady=10)

        self.room_frame = RoomFrame(root, on_move=self.move, on_fight=self.fight)
        self.room_frame.grid(row=0, column=1, rowspan=2, sticky="nsew", padx=10, pady=10)

        self.loot_log_frame = LootLogFrame(root)
        self.loot_log_frame.grid(row=2, column=0, columnspan=2, sticky="nsew", padx=10, pady=10)

        # Start new game
        self.start_new_game()

    def start_new_game(self):
        game_data = self.api_client.new_game("Player")
        self.game_session_id = game_data["sessionId"]
        self.player = game_data["player"]
        self.current_room = game_data["dungeon"]["roomById"][self.player["currentRoomId"]]
        self.update_ui()
        self.loot_log_frame.clear()

    def move(self, direction):
        response = self.api_client.move(self.game_session_id, direction)

        if response["moveResult"]["moveEnum"] == "FLOOR_BOSS_NOT_DEFEATED":
            self.show_message("Floor boss not defeated, can't move to next floor")

        
        ## player inventory and currentroom and current floor can change
        self.player = response["player"]

        ## update current room
        self.current_room = response["currentRoom"]

        if response["moveResult"]["moveEnum"] == "FLOOR_TRANSITION":
            self.show_message("Descending to floor " + str(self.player["currentFloorNumber"]))

        self.update_ui()

        self.updateLootLog(response["moveResult"]["lootReceived"])   

        ## check game won
        if response["moveResult"]["moveEnum"] == "GAME_WON":
            self.show_message_new_game("End of the Dungeon, you Won!")

    def fight(self, enemy_id):
        response = self.api_client.fight(self.game_session_id, enemy_id)

        fight_result = response["result"]
        enemy = response["enemy"]

        ## update player
        self.player = response["player"]

        ## update room enemies
        if fight_result["fightEnum"] == "ENEMY_DEFEATED":
            self.current_room["enemies"] = [e for e in self.current_room["enemies"] if e["id"] != enemy_id]
        else:
            for e in self.current_room["enemies"]:
                if e["id"] == enemy_id:
                    e["stats"]["health"] = enemy["stats"]["health"]

        self.update_ui()

        self.updateLootLog(response["result"]["lootReceived"])    
        
        ## check game over
        if fight_result["fightEnum"] == "PLAYER_DEFEATED":
            self.show_message_new_game("You died. Game over!")

    def use_item(self, item_template_id):
        response = self.api_client.use_item(self.game_session_id, item_template_id)

        ## update player
        self.player = response["player"]

        self.update_ui()

    def updateLootLog(self, lootList):
        for loot in lootList:
            itemName = loot["item"]["name"]
            msg = "Received " + itemName + " X" + str(loot["quantity"])
            self.loot_log_frame.add_message(msg)

    def update_ui(self):
        self.stats_frame.update(self.player)
        self.inventory_frame.update(self.player.get("inventory"))
        self.room_frame.update(self.current_room)

    def show_message(self, msg):
        popup = tk.Toplevel(self.root)
        tk.Label(popup, text=msg).pack(padx=10, pady=10)

        tk.Button(popup, text="Ok", command=popup.destroy).pack(pady=5)

    def show_message_new_game(self, msg):
        popup = tk.Toplevel(self.root)
        tk.Label(popup, text=msg).pack(padx=10, pady=10)

        def on_new_game():
            popup.destroy()
            self.start_new_game()

        tk.Button(popup, text="New Game", command=on_new_game).pack(pady=5)


if __name__ == "__main__":
    root = tk.Tk()
    app = GameApp(root)
    root.mainloop()



