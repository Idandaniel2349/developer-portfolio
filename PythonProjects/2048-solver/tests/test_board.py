from game.board import Board
import unittest
import copy

## to run test in root folder run: python -m unittest -v tests.test_board
class TestBoard(unittest.TestCase):

    def test_move_left(self):
        board = Board()
        board.grid = [
            [2,0,0,2],
            [0,0,4,4],
            [0,4,8,0],
            [2,0,0,0]
        ]
        expected = [
            [4,0,0,0],
            [8,0,0,0],
            [4,8,0,0],
            [2,0,0,0]
        ]
        board.move_left()
        self.assertEqual(board.grid,expected)
        self.assertEqual(board.score,12)

    def test_move_right(self):
        board = Board()
        board.grid = [
            [2,0,0,2],
            [0,0,4,4],
            [0,4,8,0],
            [2,0,0,0]
        ]
        expected = [
            [0,0,0,4],
            [0,0,0,8],
            [0,0,4,8],
            [0,0,0,2]
        ]
        board.move_right()
        self.assertEqual(board.grid,expected)
        self.assertEqual(board.score,12)

    def test_move_up(self):
        board = Board()
        board.grid = [
            [2,0,0,2],
            [0,0,4,2],
            [0,2,0,0],
            [0,8,4,0]
        ]
        expected = [
            [2,2,8,4],
            [0,8,0,0],
            [0,0,0,0],
            [0,0,0,0]
        ]
        board.move_up()
        self.assertEqual(board.grid,expected)
        self.assertEqual(board.score,12)

    def test_move_down(self):
        board = Board()
        board.grid = [
            [2,0,0,2],
            [0,0,4,2],
            [0,2,0,0],
            [0,8,4,0]
        ]
        expected = [
            [0,0,0,0],
            [0,0,0,0],
            [0,2,0,0],
            [2,8,8,4]
        ]
        board.move_down()
        self.assertEqual(board.grid,expected)
        self.assertEqual(board.score,12)

    def test_move_left_complex(self):
        board = Board()
        board.grid = [
            [2,2,2,2],
            [4,4,4,4],
            [4,4,8,8],
            [2,2,32,32]
        ]
        expected = [
            [4,4,0,0],
            [8,8,0,0],
            [8,16,0,0],
            [4,64,0,0]
        ]
        board.move_left()
        self.assertEqual(board.grid,expected)
        self.assertEqual(board.score,116)
    
    def test_move_left_no_movements(self):
        board = Board()
        board.grid = [
            [2,0,0,0],
            [4,0,0,0],
            [8,0,0,0],
            [16,0,0,0]
        ]
        expected = copy.deepcopy(board.grid)
        board.move_left()
        self.assertEqual(board.grid,expected)
        self.assertEqual(board.score,0)


    def test_can_move(self):
        test_cases = [
            ("empty_cells", [
            [2,0,0,2],
            [0,0,4,2],
            [0,2,0,0],
            [0,8,4,0] 
            ], True),
            ("full_mergeable", [
            [2,4,2,4],
            [4,2,4,2],
            [2,4,2,4],
            [4,2,4,4]
            ], True),
            ("full_no_moves", [
            [2,4,2,4],
            [4,2,4,2],
            [2,4,2,4],
            [4,2,4,2]
            ], False)
        ]

        for name, grid, expected in test_cases:
            with self.subTest(name=name):
                 board = Board()
                 board.grid = grid
                 can_move = board.can_move()
                 self.assertEqual(can_move, expected)

    def test_spawn_tile(self):
        board = Board()
        board.grid = [
            [0,0,0,0],
            [0,0,0,0],
            [0,0,0,0],
            [0,0,0,0]
        ]

        for _ in range(10):
            # make a deep copy of grid before spawn
            prev_grid = copy.deepcopy(board.grid)

            board.spawn_tile()

            # Find the newly spawned tile
            new_tile_positions = [(r, c) 
                                  for r in range(board.size) 
                                  for c in range(board.size) 
                                  if board.grid[r][c] != 0 and prev_grid[r][c] == 0]
            
            self.assertEqual(len(new_tile_positions),1)

            row, col = new_tile_positions[0]
            self.assertIn(board.grid[row][col], [2,4])

if __name__ == "__main__":
    unittest.main()