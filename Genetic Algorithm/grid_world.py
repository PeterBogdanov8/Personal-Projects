import random
from colorama import Fore


class GridWorld:
    def __init__(self, num_row, num_col, num_schools, num_libraries, num_clinics, num_recreation_centres, num_malls):
        self.num_row = num_row
        self.num_col = num_col
        self.num_schools = num_schools
        self.num_libraries = num_libraries
        self.num_clinics = num_clinics
        self.num_recreation_centres = num_recreation_centres
        self.num_malls = num_malls
        self.cum_manhattan_dist = 0

        if self.size() < (num_schools + num_libraries + num_clinics + num_recreation_centres + num_malls):
            raise Exception("The is not enough space in the world to position the houses, schools, clinics, "
                            "recreation centres and mall")

        self.houses = set()
        self.schools = set()
        self.libraries = set()
        self.clinics = set()
        self.recreation_centres = set()
        self.malls = set()
        self.add_schools()
        self.add_libraries()
        self.add_clinics()
        self.add_recreation_centres()
        self.add_malls()

    def size(self):
        return self.num_row * self.num_col

    def get_available_position(self):
        row = random.randrange(self.num_row)
        col = random.randrange(self.num_col)

        while self.is_pos_taken(row, col):
            row = random.randrange(self.num_row)
            col = random.randrange(self.num_col)
        return row, col

    def is_pos_taken(self, row, col):
        return self.houses.__contains__((row, col)) or \
               self.schools.__contains__((row, col)) or \
               self.libraries.__contains__((row, col)) or \
               self.clinics.__contains__((row, col)) or \
               self.recreation_centres.__contains__((row, col)) or \
               self.malls.__contains__((row, col))

    def add_schools(self):
        for i in range(self.num_schools):
            self.schools.add(self.get_available_position())

    def add_libraries(self):
        for i in range(self.num_libraries):
            self.libraries.add(self.get_available_position())

    def add_clinics(self):
        for i in range(self.num_clinics):
            self.clinics.add(self.get_available_position())

    def add_recreation_centres(self):
        for i in range(self.num_recreation_centres):
            self.recreation_centres.add(self.get_available_position())

    def add_malls(self):
        for i in range(self.num_malls):
            self.malls.add(self.get_available_position())

    def add_houses(self, num_houses):
        for i in range(num_houses):
            self.houses.add(self.get_available_position())

    def print_grid_world(self):
        for r in range(self.num_row):
            row = ''
            for c in range(self.num_col):

                if self.houses.__contains__((r, c)):
                    row += (Fore.LIGHTCYAN_EX + 'H ')
                elif self.schools.__contains__((r, c)):
                    row += (Fore.MAGENTA + 'S ')
                elif self.libraries.__contains__((r, c)):
                    row += (Fore.BLUE + 'L ')
                elif self.clinics.__contains__((r, c)):
                    row += (Fore.RED + 'C ')
                elif self.recreation_centres.__contains__((r, c)):
                    row += (Fore.GREEN + 'R ')
                elif self.malls.__contains__((r, c)):
                    row += (Fore.LIGHTYELLOW_EX + 'M ')
                else:
                    row += (Fore.WHITE + '. ')
            print(row)
