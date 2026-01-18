import copy
import random
from math import exp
from math import floor
from math import sqrt

from schedule import Schedule


class Agent:
    def __init__(self, world):
        self.initial_world = copy.deepcopy(world)
        self.world = world
        self.cum_manhattan_dist = 0
        self.num_steps = 0

    def calc_cum_manhattan_dist(self, residences):
        cum_manhattan_dist = 0
        for residence in residences:
            cum_manhattan_dist += (min(
                abs(campus[0] - residence[0]) + abs(campus[1] - residence[1])
                for campus in self.world.campuses
            ) + min(
                abs(clinics[0] - residence[0]) + abs(clinics[1] - residence[1])
                for clinics in self.world.clinics
            ) + min(
                abs(shops[0] - residence[0]) + abs(shops[1] - residence[1])
                for shops in self.world.shops
            ) + min(
                abs(bus_stops[0] - residence[0]) + abs(bus_stops[1] - residence[1])
                for bus_stops in self.world.bus_stops
            )) / 4
        return cum_manhattan_dist

    def simulated_annealing(self, schedule):
        temp = self.world.num_row * self.world.num_col * 2
        loop_num = 0
        self.num_steps = 0
        self.cum_manhattan_dist = 0
        self.world = copy.deepcopy(self.initial_world)
        while temp > 10:
            move = None
            move_to_remove = None
            next_manhattan_dist = None

            current_manhattan_dist = self.calc_cum_manhattan_dist(self.world.residences)
            rand_pos = self.world.get_available_position()
            for residence in self.world.residences:
                test_residence = set(self.world.residences.copy())
                test_residence.remove(residence)
                test_residence.add(rand_pos)
                dist = self.calc_cum_manhattan_dist(test_residence)
                if next_manhattan_dist is None or dist < next_manhattan_dist:
                    next_manhattan_dist = dist
                    move = rand_pos
                    move_to_remove = residence

            manhattan_dist_delta = current_manhattan_dist - next_manhattan_dist
            if manhattan_dist_delta > 0:
                self.world.residences.remove(move_to_remove)
                self.world.residences.add(move)
                self.cum_manhattan_dist = self.calc_cum_manhattan_dist(self.world.residences)
                self.num_steps += 1
            elif exp(-manhattan_dist_delta/temp) < random.randrange(0, 1):
                self.world.residences.remove(move_to_remove)
                self.world.residences.add(move)
                self.cum_manhattan_dist = self.calc_cum_manhattan_dist(self.world.residences)
                self.num_steps += 1
            loop_num += 1
            if schedule == Schedule.Divide:
                temp /= loop_num
            elif schedule == Schedule.DecrementByLoopNum:
                temp -= loop_num
            elif schedule == Schedule.Decrement:
                temp -= 1

