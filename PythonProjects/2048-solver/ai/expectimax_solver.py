from ai.heuristic_solver import HeuristicSolver
from game.board import Board

class ExpectimaxSolver:
    possible_moves = ["up","left","right","down"]

    def __init__(self, max_depth = 2):
        self.max_depth = max_depth

    def get_best_move(self, board):
        best_move = None
        best_move_score = -float("inf")

        ## check all moves and take move with best expectimax score
        for move in self.possible_moves:
            board_with_move, changed = self.simulate_move(move, board)
            curr_depth = 1
            ## we want to only return a move if it makes a change (the game logic will make sure it exists)
            if not changed:
                continue
            board_with_move_score = self.chance_expectation(board_with_move, curr_depth)
            if board_with_move_score > best_move_score:
                best_move_score = board_with_move_score
                best_move = move

        return best_move
    
    def simulate_move(self, move, board):
        # shallow copy of the grid
        grid_copy = [row[:] for row in board.grid]
    
        # create a new Board instance
        board_copy = Board(board.size)
        board_copy.grid = grid_copy
        board_copy.score = board.score
    
        # apply the move
        changed = board_copy.move(move)
    
        return board_copy, changed

    ## This represents the AI choosing the best move (maximizing score).
    def player_move(self,board,curr_depth):
        ## if we are on leaf or no more moves, evaluate the board using heuristic
        if curr_depth >= self.max_depth or not board.can_move():
            heuristic_solver_ = HeuristicSolver(board)
            evaluate_score = heuristic_solver_.evaluate_board_move(board)
            return evaluate_score
        
        best_score = -float("inf")
        
        ## check all possible moves and take the one with the best score
        for move in self.possible_moves:
            board_with_move, changed = self.simulate_move(move, board)
            ## only consider moves that change the board so only they spawn tiles
            if changed:
                score = self.chance_expectation(board_with_move, curr_depth+1)
                if score > best_score:
                    best_score = score

        return best_score

    ## This represents all possible random tile spawns and calculates the expected score.
    def chance_expectation(self,board,curr_depth):
        empty_cells = [(i, j) for i in range(board.size) for j in range(board.size) if board.grid[i][j] == 0]

        # If there are no empty cells, evaluate the board using heuristic and return the score
        if not empty_cells:
            return HeuristicSolver(board).evaluate_board_move(board)
    
        expected_score = 0

        # for each empty cell, simulate spawning a 2 (90% chance) and a 4 (10% chance)
        for (row,col) in empty_cells:
            # Simulate spawning a 2
            board.grid[row][col] = 2
            expected_score += 0.9 * self.player_move(board,curr_depth)

            # Simulate spawning a 4
            board.grid[row][col] = 4
            expected_score += 0.1 * self.player_move(board,curr_depth)

            # Reset the cell to empty
            board.grid[row][col] = 0

        return expected_score / len(empty_cells)
        

        
