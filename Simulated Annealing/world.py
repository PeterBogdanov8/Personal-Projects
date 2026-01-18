import random
from colorama import Fore


class World:
    def __init__(self, num_row, num_col, num_residences, num_campuses, num_clinics, num_shops, num_bus_stops):
        self.num_row = num_row
        self.num_col = num_col
        self.num_residences = num_residences
        self.num_campuses = num_campuses
        self.num_clinics = num_clinics
        self.num_shops = num_shops
        self.num_bus_stops = num_bus_stops

        if (num_row * num_col) < (num_residences + num_campuses + num_clinics + num_shops + num_bus_stops):
            raise Exception("The is not enough space in the world to position the residences, campuses, clinics, "
                            "shops and bus stops")

        self.residences = set()
        self.campuses = set()
        self.clinics = set()
        self.shops = set()
        self.bus_stops = set()
        self.add_campuses()
        self.add_residences()
        self.add_clinics()
        self.add_shops()
        self.add_bus_stops()

    def get_available_position(self):
        row = random.randrange(self.num_row)
        col = random.randrange(self.num_col)

        while self.is_pos_taken(row, col):
            row = random.randrange(self.num_row)
            col = random.randrange(self.num_col)
        return row, col

    def is_pos_taken(self, row, col):
        return self.residences.__contains__((row, col)) or \
               self.campuses.__contains__((row, col)) or \
               self.clinics.__contains__((row, col)) or \
               self.shops.__contains__((row, col)) or \
               self.bus_stops.__contains__((row, col))

    def add_campuses(self):
        for i in range(self.num_campuses):
            self.campuses.add(self.get_available_position())

    def add_residences(self):
        for i in range(self.num_residences):
            self.residences.add(self.get_available_position())

    def add_clinics(self):
        for i in range(self.num_clinics):
            self.clinics.add(self.get_available_position())

    def add_shops(self):
        for i in range(self.num_shops):
            self.shops.add(self.get_available_position())

    def add_bus_stops(self):
        for i in range(self.num_bus_stops):
            self.bus_stops.add(self.get_available_position())

    def print_word(self):
        for r in range(self.num_row):
            row = ''
            for c in range(self.num_col):

                if self.residences.__contains__((r, c)):
                    row += (Fore.MAGENTA + 'R ')
                elif self.campuses.__contains__((r, c)):
                    row += (Fore.LIGHTYELLOW_EX + 'C ')
                elif self.clinics.__contains__((r, c)):
                    row += (Fore.RED + 'H ')
                elif self.shops.__contains__((r, c)):
                    row += (Fore.GREEN + 'S ')
                elif self.bus_stops.__contains__((r, c)):
                    row += (Fore.BLUE + 'B ')
                else:
                    row += (Fore.WHITE + '. ')
            print(row)
