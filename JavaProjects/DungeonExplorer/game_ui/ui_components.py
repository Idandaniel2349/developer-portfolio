import tkinter as tk
from tkinter import ttk

class PlayerStatsFrame(tk.Frame):
    def __init__(self, master):
        # Displays all player stat labels
        super().__init__(master,relief=tk.RAISED, borderwidth=2)
        self.hp_label = tk.Label(self, text="Health: ")
        self.hp_label.pack(anchor="w")
        self.attack_label = tk.Label(self, text="Attack: ")
        self.attack_label.pack(anchor="w")
        self.defense_label = tk.Label(self, text="Defense: ")
        self.defense_label.pack(anchor="w")
        self.xp_label = tk.Label(self, text="XP: ")
        self.xp_label.pack(anchor="w")
        self.level_label = tk.Label(self, text="Level: ")
        self.level_label.pack(anchor="w")
        self.floor_label = tk.Label(self, text="Floor: ")
        self.floor_label.pack(anchor="w")

    def update(self, player):
        # Update displayed stats
        self.hp_label.config(text=f"HP: {player['stats']['health']}")
        self.attack_label.config(text=f"Attack: {player['stats']['attack']}")
        self.defense_label.config(text=f"Defense: {player['stats']['defense']}")
        self.xp_label.config(text=f"XP: {player['xp']}")
        self.level_label.config(text=f"Level: {player['level']}")
        self.floor_label.config(text=f"Floor: {player['currentFloorNumber']}")

class PlayerInventoryFrame(tk.Frame):
    def __init__(self, master, on_use_item):
        # Inventory UI with a callback for using items 
        super().__init__(master, relief=tk.RAISED, borderwidth=2)
        self.on_use_item = on_use_item
        self.items_frame = tk.Frame(self)
        self.items_frame.pack()
        self.title = tk.Label(self, text="Inventory")
        self.title.pack(anchor="w")

    def update(self, inventory):
        # Clear previous buttons
        for widget in self.items_frame.winfo_children():
            widget.destroy()

        # Add buttons for each item in inventory as buttons
        inventoryItems = inventory.get("items", {})
        for stack in inventoryItems.values():  # iterate over values of the map
            item = stack['item']
            quantity = stack.get('quantity', 1)
            btn = tk.Button(
                self.items_frame, 
                text=f"Use {item['name']} x{quantity}", 
                command=lambda tid=item['templateId']: self.on_use_item(tid)
            )
            btn.pack(side="left", padx=2)
        

class RoomFrame(tk.Frame):
    def __init__(self, master, on_move, on_fight):
        # Displays current room info, exits, and enemies
        super().__init__(master, relief=tk.SUNKEN, borderwidth=2)
        self.room_label = tk.Label(self, text="Room: ")
        self.room_label.pack()
        self.exits_frame = tk.Frame(self)
        self.exits_frame.pack()
        self.enemies_frame = tk.Frame(self)
        self.enemies_frame.pack()
        self.on_move = on_move
        self.on_fight = on_fight


    def update(self, room):
        # Update room ID
        self.room_label.config(text=f"Room: {room['id']}")

         # clear frames
        for f in [self.exits_frame, self.enemies_frame]:
            for w in f.winfo_children():
                w.destroy()

        # Add move buttons
        for direction, room_id in room.get('exits', {}).items():
            btn = tk.Button(self.exits_frame, text=f"Go {direction}", command=lambda dir=direction: self.on_move(dir))
            btn.pack(side="left", padx=2)

        # Add fight buttons for enemies
        for enemy in room.get('enemies', []):
            hp = enemy['stats']['health']
            attack = enemy['stats']['attack']
            name = enemy['name']
            if enemy['boss']:
                name = 'Boss: ' + name
            btn = tk.Button(self.enemies_frame, text=f"{name} (HP: {hp}, ATK: {attack})", command=lambda eid=enemy['id']: self.on_fight(eid))
            btn.pack(side="left", padx=2)

class LootLogFrame(tk.Frame):
    def __init__(self, master):
        # Log for loot events
        super().__init__(master, relief=tk.SUNKEN, borderwidth=2)
        self.title = tk.Label(self, text="Loot Log")
        self.title.pack(anchor='w')

        self.log_frame = tk.Frame(self)
        self.log_frame.pack()

    def add_message(self, msg):
        # Append message
        label = tk.Label(self.log_frame, text=msg, anchor="w")
        label.pack(fill="x")
        
        # keep last 10 entries
        if len(self.log_frame.winfo_children()) > 10:
            self.log_frame.winfo_children()[0].destroy()

    def clear(self):
        # Clear the log
        for widget in self.log_frame.winfo_children():
            widget.destroy()
    
