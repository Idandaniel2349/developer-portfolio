from game.board import Board
import unittest


## to run test in root folder run: python -m unittest -v tests.test_board_integration
class TestBoardIntegration(unittest.TestCase):

    """Integration test for Board: simulates a full sequence of moves, 
       checking grid state and score consistency across steps.
    """

    def test_multiple_moves(self):
        ## init
        board = Board()
        board.grid = [
            [2,0,0,2],
            [0,0,0,0],
            [0,0,0,0],
            [0,0,0,0]
        ]

        ## move 1
        expected = [
            [4,0,0,0],
            [0,0,0,0],
            [0,0,0,0],
            [0,0,0,0]
        ]
        board.move_left()
        self.assertEqual(board.grid,expected)
        self.assertEqual(board.score,4)

        self.assertEqual(board.can_move(), True)

        board.grid[0][3] = 4

        ## move 2
        expected = [
            [0,0,0,8],
            [0,0,0,0],
            [0,0,0,0],
            [0,0,0,0]
        ]
        board.move_right()
        self.assertEqual(board.grid,expected)
        self.assertEqual(board.score,12)

        self.assertEqual(board.can_move(), True)

        board.grid[3][0] = 4

        ## move 3
        expected = [
            [0,0,0,0],
            [0,0,0,0],
            [0,0,0,0],
            [4,0,0,8]
        ]
        board.move_down()
        self.assertEqual(board.grid,expected)
        self.assertEqual(board.score,12)

        self.assertEqual(board.can_move(), True)

        board.grid[0][0] = 4

        ## move 4
        expected = [
            [8,0,0,8],
            [0,0,0,0],
            [0,0,0,0],
            [0,0,0,0]
        ]
        board.move_up()
        self.assertEqual(board.grid,expected)
        self.assertEqual(board.score,20)

        self.assertEqual(board.can_move(), True)

        board.grid[2][2] = 2

        ## move 5
        expected = [
            [0,0,0,16],
            [0,0,0,0],
            [0,0,0,2],
            [0,0,0,0]
        ]
        board.move_right()
        self.assertEqual(board.grid,expected)
        self.assertEqual(board.score,36)

        self.assertEqual(board.can_move(), True)

if __name__ == "__main__":
    unittest.main()



