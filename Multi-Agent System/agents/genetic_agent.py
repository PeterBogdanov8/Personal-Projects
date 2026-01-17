import random

from agents.agent import Agent
from configuration.solution_task import SolutionTask
from configuration.task import Task


class GeneticAgent(Agent):
    def __init__(self, budget, job, candidates):
        super().__init__(budget, job, candidates)
        self.chromosomes = []
        self.num_chromosomes = 0
        self.mutation_rate = 0

    def create_chromosome(self, use_one_point_crossover):
        chromosome = []
        if use_one_point_crossover:
            for i in range(len(self.candidates)):
                chromosome.append(0)
            index = random.randint(0, len(self.candidates) - 1)
            chromosome[index] = 1
        else:
            for i in range(len(self.candidates)):
                chromosome.append(random.randint(0, 1))
        return chromosome

    def create_mask(self):
        mask = []
        for i in range(len(self.candidates)):
            mask.append(random.randint(0, 1))
        return mask

    def create_chromosomes(self, use_one_point_crossover):
        chromosomes = []
        for i in range(self.num_chromosomes):
            chromosomes.append(self.create_chromosome(use_one_point_crossover))
        return chromosomes

    def get_sample(self, sample_size):
        sample = []
        for i in range(sample_size):
            index = random.randrange(self.num_chromosomes)
            sample.append(self.chromosomes[index])
        return sample

    def get_least_fit_chromosome(self, chromosomes):
        lowest_reward = self.get_total_rewards(self.get_solution(chromosomes[0]))
        least_fit_chromosome = chromosomes[0]
        for chromosome in chromosomes:
            reward = self.get_total_rewards(self.get_solution(chromosome))
            if lowest_reward > reward:
                least_fit_chromosome = chromosome
                lowest_reward = reward
        return least_fit_chromosome

    def get_fittest_chromosome(self, chromosomes):
        highest_reward = 0
        fittest_chromosome = chromosomes[0]
        for chromosome in chromosomes:
            reward = self.get_total_rewards(self.get_solution(chromosome))
            if highest_reward < reward:
                fittest_chromosome = chromosome
                highest_reward = reward
        return fittest_chromosome

    def mutate_chromosome(self, chromosome):
        mutation_point = random.randint(0, len(chromosome) - 1)
        if chromosome[mutation_point] == 1:
            chromosome[mutation_point] = 0
        else:
            chromosome[mutation_point] = 1
        return chromosome

    def uniform_crossover(self, chromosome1, chromosome2):
        mask = self.create_mask()
        offspring1 = mask
        offspring2 = mask
        index = 0
        for element in mask:
            if element == 1:
                offspring1[index] == chromosome1[index]
                offspring2[index] == chromosome2[index]
            else:
                offspring1[index] == chromosome2[index]
                offspring2[index] == chromosome1[index]
            index += 1
        return offspring1, offspring2

    def one_point_crossover(self, chromosome1, chromosome2):
        cut_of_point = random.randint(1, len(self.candidates) - 1)
        offspring1 = chromosome1[:cut_of_point] + chromosome2[cut_of_point:]
        offspring2 = chromosome2[:cut_of_point] + chromosome1[cut_of_point:]
        return offspring1, offspring2

    def tournament_selection(self, sample_size, use_one_point_crossover):
        new_chromosomes = []
        for i in range(len(self.chromosomes)):
            first_sample = self.get_sample(sample_size)
            second_sample = self.get_sample(sample_size)
            chromosome1 = self.get_fittest_chromosome(first_sample)
            chromosome2 = self.get_fittest_chromosome(second_sample)
            offsprings = None
            if use_one_point_crossover:
                offsprings = self.one_point_crossover(chromosome1, chromosome2)
            else:
                offsprings = self.uniform_crossover(chromosome1, chromosome2)
            offspring1 = offsprings[0]
            offspring2 = offsprings[1]

            can_mutate = random.randrange(0, 1) <= self.mutation_rate
            if can_mutate:
                offspring1 = self.mutate_chromosome(offspring1)
                offspring2 = self.mutate_chromosome(offspring2)
            new_chromosomes.append(self.get_fittest_chromosome([chromosome1, chromosome2, offspring1, offspring2]))
        self.chromosomes = new_chromosomes


    def get_solution(self, chromosome):
        solution = []
        index = 0
        for element in chromosome:
            if element == 1:
                candidate = self.candidates[index]
                solution.append(candidate)
            index = index + 1
        return solution

    def choose_candidates_task(self, queue, use_one_point_crossover, mutation_rate, population_size):
        task = Task(self.budget, self.job)
        solution = self.choose_candidates(use_one_point_crossover, mutation_rate, population_size)
        queue.put(SolutionTask(task, solution))

    def choose_candidates(self, use_one_point_crossover, mutation_rate, population_size):
        self.historical_rewards = []
        self.mutation_rate = mutation_rate
        self.num_chromosomes = population_size
        generation = 1
        self.chromosomes = self.create_chromosomes(use_one_point_crossover)

        fittest_chromosome_rewards = self.get_total_rewards(self.get_solution(self.get_fittest_chromosome(self.chromosomes)))
        least_fit_chromosome_rewards = self.get_total_rewards(self.get_solution(self.get_least_fit_chromosome(self.chromosomes)))
        previous_rewards = fittest_chromosome_rewards
        while (fittest_chromosome_rewards - least_fit_chromosome_rewards) != 0:
            self.tournament_selection(generation, use_one_point_crossover)
            fittest_chromosome_rewards = self.get_total_rewards(self.get_solution(self.get_fittest_chromosome(self.chromosomes)))
            least_fit_chromosome_rewards = self.get_total_rewards(self.get_solution(self.get_least_fit_chromosome(self.chromosomes)))
            generation += 1
            if previous_rewards == fittest_chromosome_rewards:
                self.mutation_rate = self.mutation_rate + 0.005
            previous_rewards = fittest_chromosome_rewards
            self.historical_rewards.append(fittest_chromosome_rewards)
        return self.get_solution(self.get_fittest_chromosome(self.chromosomes))


