import random
from multiprocessing import Pool
from game.board import Board
from ai.heuristic_solver import HeuristicSolver
from ai.expectimax_solver import ExpectimaxSolver

def run_single_game(args):
    solver_type, seed = args
    random.seed(seed)  # reproducible randomness

    try:
        board = Board()
        if solver_type == 'expectimax':
            solver = ExpectimaxSolver()
            while board.can_move():
                move = solver.get_best_move(board)
                if move is None:
                    break
                board.move(move)
                board.spawn_tile()
            score = board.score
            max_tile = max(max(row) for row in board.grid)

        elif solver_type == 'heuristic':
            solver = HeuristicSolver(board)
            while board.can_move():
                best_move = None
                best_score = -float('inf')
                for move in ["up", "left", "right", "down"]:
                    board_copy = Board(board.size)
                    board_copy.grid = [r[:] for r in board.grid]
                    board_copy.score = board.score
                    changed = board_copy.move(move)
                    if not changed:
                        continue
                    eval_score = solver.evaluate_board_move(board_copy)
                    if eval_score > best_score:
                        best_score = eval_score
                        best_move = move
                if best_move is None:
                    break
                board.move(best_move)
                board.spawn_tile()
            score = board.score
            max_tile = max(max(row) for row in board.grid)

        else:
            raise ValueError("Unknown solver type")

    except Exception as e:
        # Print error instead of silently returning zeros
        print(f"Error in game with seed {seed}: {e}")
        score, max_tile = 0, 0

    return score, max_tile


def batch_test(num_games=5, solver_type='expectimax', num_processes=None):
    seeds = [i for i in range(num_games)]
    args_list = [(solver_type, seed) for seed in seeds]

    with Pool(processes=num_processes) as pool:
        results = pool.map(run_single_game, args_list)

    scores, max_tiles = zip(*results)

    print(f"Solver: {solver_type}")
    print(f"Average score: {sum(scores)/len(scores):.2f}")
    print(f"Max score: {max(scores)}")
    print(f"Max tile: {max(max_tiles)}\n")


if __name__ == "__main__":
    # Run Expectimax solver
    batch_test(solver_type='expectimax', num_processes=None)

    # Run Heuristic solver
    batch_test(solver_type='heuristic', num_processes=None)
