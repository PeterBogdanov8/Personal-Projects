import copy
import random


class ProblemSolver:
    def __init__(self, world, num_houses):
        self.world = world
        self.num_houses = num_houses
        self.average_cum_manhattan_dist_of_fittest = []
        self.cum_manhattan_dist = 0
        self.generation = 1
        self.population = []

    def initialise_the_population(self):
        self.population = []
        for i in range(self.world.size()):
            world_copy = copy.deepcopy(self.world)
            world_copy.add_houses(self.num_houses)
            world_copy.cum_manhattan_dist = self.calc_cum_average_manhattan_dist(world_copy.houses)
            self.population = self.add_world(self.population, world_copy)

    def add_world(self, population, world):
        if len(world.houses) != self.num_houses:
            raise Exception(f'houses: {world.houses} \n generation: {self.generation}')
        population.append(world)
        return population

    def calc_cum_average_manhattan_dist(self, houses):
        cum_manhattan_dist = 0
        for house in houses:
            cum_manhattan_dist += (min(
                abs(school[0] - house[0]) + abs(school[1] - house[1])
                for school in self.world.schools
            ) + min(
                abs(library[0] - house[0]) + abs(library[1] - house[1])
                for library in self.world.libraries
            ) + min(
                abs(clinic[0] - house[0]) + abs(clinic[1] - house[1])
                for clinic in self.world.clinics
            ) + min(
                abs(recreation_centre[0] - house[0]) + abs(recreation_centre[1] - house[1])
                for recreation_centre in self.world.recreation_centres
            ) + min(
                abs(mall[0] - house[0]) + abs(mall[1] - house[1])
                for mall in self.world.malls
            )) / 5
        return cum_manhattan_dist

    def get_fittest_individual(self, solutions):
        best_cum_average_manhattan_dist = 0
        fittest_individual = None
        for individual in solutions:
            world_copy = copy.deepcopy(self.world)
            world_copy.houses = individual
            world_copy.cum_manhattan_dist = self.calc_cum_average_manhattan_dist(individual)
            if best_cum_average_manhattan_dist == 0:
                best_cum_average_manhattan_dist = world_copy.cum_manhattan_dist
                fittest_individual = individual
            if world_copy.cum_manhattan_dist < best_cum_average_manhattan_dist:
                fittest_individual = individual
                best_cum_average_manhattan_dist = world_copy.cum_manhattan_dist
        return fittest_individual

    def solve_problem(self, rank_based=True):
        self.average_cum_manhattan_dist_of_fittest = []
        self.generation = 1
        sample_size = 1
        self.initialise_the_population()
        sorted_population = sorted(self.population, key=lambda x: x.cum_manhattan_dist);
        fittest_individual = sorted_population[0]
        least_fit_individual = sorted_population[len(sorted_population) - 1]
        self.average_cum_manhattan_dist_of_fittest.append(fittest_individual.cum_manhattan_dist / self.num_houses)
        while (fittest_individual.cum_manhattan_dist - least_fit_individual.cum_manhattan_dist) != 0:
            self.tournament_selection(sample_size, rank_based)
            sorted_population = sorted(self.population, key=lambda x: x.cum_manhattan_dist);
            fittest_individual = sorted_population[0]
            least_fit_individual = sorted_population[len(sorted_population) - 1]
            self.average_cum_manhattan_dist_of_fittest.append(fittest_individual.cum_manhattan_dist / self.num_houses)
            self.generation += 1
            if self.generation % 2:
                sample_size += 1

        return fittest_individual

    def mutate(self, houses):
        point = random.randrange(0, len(houses))
        copy_world = copy.deepcopy(self.world)
        copy_world.houses = houses
        houses_list = list(houses)
        houses_list[point] = copy_world.get_available_position()
        houses = set(houses_list)
        copy_world.houses = houses
        return houses

    def fix_defects(self, world, child):
        num_defects = self.num_houses - len(child)
        while num_defects > 0:
            child.add(world.get_available_position())
            world.houses = child
            num_defects -= 1
        return child

    def random_sample_from_population(self, size):
        sample = []
        for i in range(0, size):
            index = random.randrange(0, self.num_houses)
            sample.append(self.population[index].houses)
        return sample

    def rank_based_sample_from_population(self, size):
        sample = []
        sorted_population = sorted(self.population, key=lambda x: x.cum_manhattan_dist)
        for i in range(0, size):
            index = random.randrange(0, self.num_houses)
            sample.append(sorted_population[index].houses)
        return sample

    def tournament_selection(self, size, rank_based):
        next_generation = []
        for i in range(0, len(self.population)):
            sample1 = None
            sample2 = None
            if rank_based:
                sample1 = self.rank_based_sample_from_population(size)
                sample2 = self.rank_based_sample_from_population(size)
            else:
                sample1 = self.random_sample_from_population(size)
                sample2 = self.random_sample_from_population(size)
            individual1 = self.get_fittest_individual(sample1)
            individual2 = self.get_fittest_individual(sample2)
            point = random.randint(1, self.num_houses - 1)
            child1 = set(list(individual1)[0:point] + list(individual2)[point:])
            child2 = set(list(individual2)[0:point] + list(individual1)[point:])

            can_mutate = random.randrange(0, 1) <= 0.01
            if can_mutate:
                child1 = self.mutate(child1)
                child2 = self.mutate(child2)

            if len(child1) != self.num_houses:
                world_copy = copy.deepcopy(self.world)
                world_copy.houses = child1
                child1 = self.fix_defects(world_copy, child1)

            if len(child2) != self.num_houses:
                world_copy = copy.deepcopy(self.world)
                world_copy.houses = child2
                child2 = self.fix_defects(world_copy, child2)

            candidates = [individual1, individual2, child1, child2]
            world_copy = copy.deepcopy(self.world)
            world_copy.houses = self.get_fittest_individual(candidates)
            world_copy.cum_manhattan_dist = self.calc_cum_average_manhattan_dist(world_copy.houses)
            if world_copy.cum_manhattan_dist == 0:
                world_copy.cum_manhattan_dist = self.calc_cum_average_manhattan_dist(world_copy.houses)
            next_generation = self.add_world(next_generation, world_copy)
        if next_generation:
            self.population = next_generation
