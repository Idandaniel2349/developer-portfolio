import random

class Board:
    ## init board
    def __init__(self, size=4):
        self.size = size
        self.grid = [[0] * size for _ in range(size)] # matrix
        self.score = 0
        self.spawn_tile()
        self.spawn_tile()


    def spawn_tile(self):
        empty_cells = [(i, j) for i in range(self.size) for j in range(self.size) if self.grid[i][j] == 0]

        # if there are no empty cells
        if not empty_cells:
            return

        i, j = random.choice(empty_cells)
        self.grid[i][j] = 2 if random.random() < 0.9 else 4
    
    ## print board and print score
    def print(self):
        for row in self.grid:
            print(row)

    def print_score(self):
        print(self.score)
    
    ## move methods
    def move(self, direction):
        grid_copy = [row[:] for row in self.grid]
        if direction == "left":
            self.move_left()
        elif direction == "right":
            self.move_right()
        elif direction == "up":
            self.move_up()
        elif direction == "down":
            self.move_down()
        else:
            raise ValueError("Invalid direction")
        
        # check if any changes in grid
        return grid_copy!=self.grid

    def move_left(self):
        for row in self.grid:
            self.move_row_left(row)

    def move_row_left(self, row):
        self.merge_left(row)
        self.slide_left(row)

    def merge_left(self, row):
        col_index = self.size - 1
        while col_index > 0:
            ## check if merge needed and do it if it is needed
            if row[col_index]!=0 and row[col_index]==row[col_index-1]:
                row[col_index] = 0
                row[col_index-1] = 2 * row[col_index-1]
                self.score += row[col_index-1]
                col_index -= 2
            elif row[col_index]!=0 and row[col_index-1]==0:
                row[col_index-1] = row[col_index]
                row[col_index] = 0
                col_index -= 1
            else:
                col_index-=1

    def slide_left(self, row):
        index = 0
        for col_index in range(self.size):
            if row[col_index]!=0:
                row[index] = row[col_index]
                if index!=col_index:
                    row[col_index] = 0
                index += 1

    def move_right(self):
        for row in self.grid:
            ## for each row reverse it, do left logic and revrse back, that way we will get right logic
            row.reverse()
            self.merge_left(row)
            self.slide_left(row)
            row.reverse()

    def move_up(self):
        for col_index in range(self.size):
            ## for each col, turn it to row, do left legic and return to col
            col_as_row = self.col_to_row(col_index)
            self.move_row_left(col_as_row)
            self.row_to_col(col_as_row,col_index)

    def move_down(self):
        for col_index in range(self.size):
            ## for each col, turn it to row, reverse it, do left legic, reverse back and return to col
            col_as_row = self.col_to_row(col_index)
            col_as_row.reverse()
            self.move_row_left(col_as_row)
            col_as_row.reverse()
            self.row_to_col(col_as_row,col_index)

    def col_to_row(self, col_index):
        col_as_row = []
        for row_index in range(self.size):
            col_as_row.append(self.grid[row_index][col_index])
        
        return col_as_row
    
    def row_to_col(self, row, col_index):
        for row_index in range(self.size):
            self.grid[row_index][col_index] = row[row_index]

    # check if any moves available
    def can_move(self):

        # if there are empty cells there is a legit move
        for row_index in range(self.size):
            for col_index in range(self.size):
                if self.grid[row_index][col_index]==0:
                    return True
        
        # here we know board is full, check if any move will change it(if there is a possible merge)
        for row_index in range(self.size):
            for col_index in range(self.size):
                if col_index!=(self.size-1) and self.grid[row_index][col_index]==self.grid[row_index][col_index+1]:
                    return True
                if row_index!=(self.size-1) and self.grid[row_index][col_index]==self.grid[row_index+1][col_index]:
                    return True
                
        return False


    


        


