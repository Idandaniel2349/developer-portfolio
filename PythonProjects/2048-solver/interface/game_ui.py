from ai.expectimax_solver import ExpectimaxSolver
from game.board import Board
import tkinter as tk
import math


class GameUI:
    def __init__(self,board,tile_size=100):
        self.board = board
        self.tile_size=100
        self.ai_solving = False

        # Main window
        self.window = tk.Tk()
        self.window.title("2048")

        #Score Label at the top
        self.score_label =tk.Label(self.window, text=f"Score: {self.board.score}",font=("Helvetica", 24, "bold"))
        self.score_label.pack(pady=10)

        # restart button
        self.restart_button = tk.Button(self.window, text="Restart", font=("Helvetica", 18), command=self.restart_game)
        self.restart_button.pack(pady=10)

         # AI single move button
        self.ai_move_button = tk.Button(self.window, text="AI Move", font=("Helvetica", 18),command=self.ai_move)
        self.ai_move_button.pack(pady=5)

        # AI solve board button
        self.ai_solve_button = tk.Button(self.window, text="Solve Board", font=("Helvetica", 18),command=self.ai_solve)
        self.ai_solve_button.pack(pady=5)

        # Board canvas
        self.canvas = tk.Canvas(self.window, width = (board.size * self.tile_size), height = (board.size * self.tile_size), bg="white")
        self.canvas.pack()

        # setup key binding
        self.setup_key_bindings()

        # draw starting board
        self.update_ui()


    # input and move handling
         
    def setup_key_bindings(self):
        self.window.bind("<Left>", lambda e: self.make_move("left"))
        self.window.bind("<Right>", lambda e: self.make_move("right"))
        self.window.bind("<Up>", lambda e: self.make_move("up"))
        self.window.bind("<Down>", lambda e: self.make_move("down"))

    def make_move(self, direction):
        ## handle the move
        changed = self.board.move(direction)
        if changed:
            self.board.spawn_tile()
        self.update_ui()
        
        # check if over and handle game over
        if not self.board.can_move():
                self.show_game_over()

    def restart_game(self):
        self.ai_solving = False  # stop any running AI
        self.board = Board()
        self.update_ui()

    # update the ui
    def update_ui(self):
        # board draw
        self.canvas.delete("all")
        for row in range(self.board.size):
            for col in range(self.board.size):
                self.draw_tile(row,col, self.board.grid[row][col])
        # score draw
        self.score_label.config(text=f"Score: {self.board.score}")

    # drawing functions
    def get_tile_color(self, value):
        """Dynamic tile color: bigger numbers → darker tiles"""
        if value == 0:
            return "#f2e6d9" # empty tile color
        exponent = int(math.log2(value))

         # Start from light orange for exponent=1, subtract a fixed amount per step
        r = 255
        g = max(180 - (exponent - 1) * 20, 50)  # reduces green, makes darker
        b = max(120 - (exponent - 1) * 10, 30)  # reduces blue
        return f'#{r:02x}{g:02x}{b:02x}'

    def draw_tile(self, row, col, value):
        x1 = col * self.tile_size
        y1 = row * self.tile_size
        x2 = x1 + self.tile_size
        y2 = y1 + self.tile_size

        color = self.get_tile_color(value)
        self.canvas.create_rectangle(x1, y1, x2, y2, fill=color, outline="#bbada0")

        if value != 0:
            self.canvas.create_text(
                (x1 + x2) // 2, (y1 + y2) // 2,
                text=str(value),
                font=("Helvetica", 24, "bold"),
                fill="black"
            )

    ## game over overlay
    def show_game_over(self):
        center_x = self.tile_size * self.board.size //2
        center_y = self.tile_size * self.board.size //2

        self.canvas.create_rectangle(
            0, center_y - 60,
            self.tile_size * self.board.size,
            center_y + 60,
            fill="white",
            stipple="gray25"
        )

        self.canvas.create_text(
            center_x, center_y - 20,
            text="GAME OVER!",
            font=("Helvetica", 32, "bold"),
            fill="red"
        )

        self.canvas.create_text(
            center_x, center_y + 20,
            text=f"Final Score: {self.board.score}",
            font=("Helvetica", 24, "bold"),
            fill="black"
        )

    # ai functions
    def ai_move(self):
        solver = ExpectimaxSolver()
        best_move = solver.get_best_move(self.board)
        self.make_move(best_move)

    def solve_ai_step(self):
        if not self.ai_solving:
            return
        
        if self.board.can_move():
            best_move = self.expectimax_solver.get_best_move(self.board)

            self.make_move(best_move)

            ## after 100 milliseconds call again
            self.window.after(100, self.solve_ai_step)
        else:
            self.show_game_over()
            self.ai_solving = False

    def ai_solve(self):
        self.ai_solving = True
        self.expectimax_solver = ExpectimaxSolver()
        self.solve_ai_step()

    ## run loop
    def run(self):
        self.window.mainloop()



    
         