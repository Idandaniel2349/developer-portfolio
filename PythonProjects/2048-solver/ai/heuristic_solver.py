from game.board import Board
import copy

class HeuristicSolver:
    possible_moves = ["left","right","down","up"]

    def __init__(self, board, weight_score=0.5, weight_empty=3, weight_monotonicity=1.2, weight_merges=5, weight_corner=3):
        self.board = board
        self.weight_score = weight_score
        self.weight_empty = weight_empty
        self.weight_monotonicity = weight_monotonicity
        self.weight_merges = weight_merges
        self.weight_corner = weight_corner

    def get_best_move(self):
        best_move = None
        best_move_score = -float("inf")
        ## check all moves and take move with best heuristic score
        for move in self.possible_moves:
            board_with_move, changed = self.simulate_move(move)
            ## we want to only return a move if it makes a change (the game logic will make sure it exists)
            if not changed:
                continue
            board_with_move_score = self.evaluate_board_move(board_with_move)
            if board_with_move_score > best_move_score:
                best_move_score = board_with_move_score
                best_move = move

        return best_move


    def simulate_move(self, move):
        board_copy = copy.deepcopy(self.board)
        changed = board_copy.move(move)
        return board_copy, changed
    
    def evaluate_board_move(self,board):

        empty_cells = [(i, j) for i in range(board.size) for j in range(board.size) if board.grid[i][j] == 0]

        monotonicity_score = self.calculate_monotonicity_score(board)

        merges_score = self.calculate_possible_merge_score(board)

        evaluation_score = self.weight_score * board.score + self.weight_empty * len(empty_cells) + self.weight_monotonicity * monotonicity_score + self.weight_merges * merges_score

        # corner bonus: reward largest tile in top-left
        max_tile = max(max(row) for row in board.grid)
        if board.grid[0][0] == max_tile:
            evaluation_score += max_tile * self.weight_corner

        return evaluation_score 



    ## Higher values indicate the board is more ordered, which is good for guiding the AI.
    def calculate_monotonicity_score(self,board):
        # favor decreasing rows and columns from top-left corner
        total_score = 0
        for row in board.grid:
            for i in range(len(row)-1):
                if row[i] >= row[i+1]:
                    total_score += row[i+1]
        for col in range(board.size):
            for i in range(board.size-1):
                if board.grid[i][col] >= board.grid[i+1][col]:
                    total_score += board.grid[i+1][col]
        return total_score
    
    def calculate_possible_merge_score(self,board):
        count_merges = 0

        for row_num in range(board.size):
            col_num = 0
            while col_num < board.size-1:
                if board.grid[row_num][col_num]==board.grid[row_num][col_num+1]:
                    count_merges+=1
                    col_num+=2
                else:
                    col_num+=1
        
        for col_num in range(board.size):
            row_num = 0
            while row_num < board.size-1:
                if board.grid[row_num][col_num] == board.grid[row_num+1][col_num]:
                    count_merges+=1
                    row_num+=2
                else:
                    row_num+=1

        return count_merges
