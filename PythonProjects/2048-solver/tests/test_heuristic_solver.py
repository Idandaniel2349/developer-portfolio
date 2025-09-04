from ai.heuristic_solver import HeuristicSolver
from game.board import Board
import unittest

## to run test in root folder run: python -m unittest -v tests.test_heuristic_solver
class TestHeuristicSolver(unittest.TestCase):

    def test_empty_board_scoring_higher(self):
        board_empty = Board()
        solver = HeuristicSolver(board_empty)
        score_empty = solver.evaluate_board_move(board_empty)

        board_full = Board()
        board_full.grid = [[2, 4, 8, 16],
                       [32, 64, 128, 256],
                       [512, 1024, 2048, 4096],
                       [2, 4, 8, 16]]
        score_full = solver.evaluate_board_move(board_full)
        assert score_empty > score_full

    def test_monotonicity(self):
        board_bad = Board()
        board_bad.grid = [[2, 4, 8, 16],
                       [0, 0, 0, 0],
                       [0, 0, 0, 0],
                       [0, 0, 0, 0]]

        board_good = Board()
        board_good.grid = [[16, 8, 4, 2],
                      [0, 0, 0, 0],
                      [0, 0, 0, 0],
                      [0, 0, 0, 0]]

        solver = HeuristicSolver(board_good)
        assert solver.evaluate_board_move(board_good) > solver.evaluate_board_move(board_bad)

    def test_merge_preference(self):
        board_merge = Board()
        board_merge.grid = [[2, 2, 0, 0],
                        [0, 0, 0, 0],
                        [0, 0, 0, 0],
                        [0, 0, 0, 0]]

        board_no_merge = Board()
        board_no_merge.grid = [[2, 4, 0, 0],
                           [0, 0, 0, 0],
                           [0, 0, 0, 0],
                           [0, 0, 0, 0]]

        solver = HeuristicSolver(board_merge)
        assert solver.evaluate_board_move(board_merge) > solver.evaluate_board_move(board_no_merge)

    def test_corner_preference(self):
        board_corner = Board()
        board_corner.grid = [[2048, 0, 0, 0],
                         [0, 0, 0, 0],
                         [0, 0, 0, 0],
                         [0, 0, 0, 0]]

        board_non_corner = Board()
        board_non_corner.grid = [[0, 0, 0, 2048],
                            [0, 0, 0, 0],
                            [0, 0, 0, 0],
                            [0, 0, 0, 0]]

        solver = HeuristicSolver(board_corner)
        assert solver.evaluate_board_move(board_corner) > solver.evaluate_board_move(board_non_corner)

    def test_heuristic_best_move_merge(self):
        board = Board()
        board.grid = [[0,   0,   0,   0 ],
                      [ 512,   0,   0,   0 ],
                      [ 512,   2,   2,  32 ],
                      [  16,  16,   4,   2 ]
        ]

        solver = HeuristicSolver(board)
        best_move = solver.get_best_move()

        assert best_move == "up"


