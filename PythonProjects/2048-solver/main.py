from game.board import Board
from interface.game_ui import GameUI

if __name__ == "__main__":
    board = Board(size=4)
    ui = GameUI(board)
    ui.run()