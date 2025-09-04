
import unittest
from game.board import Board
from ai.expectimax_solver import ExpectimaxSolver

## to run test in root folder run: python -m unittest -v tests.test_expectimax_solver
class TestExpectimaxSolver(unittest.TestCase):

    def test_expectimax_merge_choice(self):
        board = Board()
        board.grid = [[2, 2, 0, 0],
                  [0, 0, 0, 0],
                  [0, 0, 0, 0],
                  [0, 0, 0, 0]]
        solver = ExpectimaxSolver()
        best_move = solver.get_best_move(board)
        assert best_move == "left"

    def test_expectimax_best_move_merge(self):
        board = Board()
        board.grid = [[0,   0,   0,   0 ],
                      [ 512,   0,   0,   0 ],
                      [ 512,   2,   2,  32 ],
                      [  16,  16,   4,   2 ]
        ]

        solver = ExpectimaxSolver()
        best_move = solver.get_best_move(board)

        assert best_move == "up"


