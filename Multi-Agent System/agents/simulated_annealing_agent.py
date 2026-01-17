import random
from math import exp

from agents.agent import Agent
from configuration.solution_task import SolutionTask
from configuration.task import Task


class SimulatedAnnealingAgent(Agent):
    def __init__(self, budget, job, candidates):
        super().__init__(budget, job, candidates)
        self.solution = []

    def solve_task(self, queue, schedule):
        task = Task(self.budget, self.job)
        solution = self.solve_problem(schedule)
        queue.put(SolutionTask(task, solution))

    def solve_problem(self, schedule):
        self.historical_rewards = []
        self.solution = []
        temp = pow(len(self.candidates), 2)
        while temp > 0:
            possible_solution = self.solution.copy()
            current_rewards = self.get_total_rewards(self.solution)
            candidate = None

            # Determine if the agent should try to add a candidate to the solution or remove a candidate from the solution
            add_candidate = (random.randint(0, 1) == 1 & (len(self.candidates) != 0)) | len(self.solution) <= 1
            if add_candidate:  # Try to add a random candidate to the solution
                candidate = self.candidates.pop(random.randint(0, (len(self.candidates) - 1)))
            else:  # Try to remove candidate from the solution
                candidate = self.solution.pop(random.randint(0, (len(self.solution) - 1)))

            possible_solution.append(candidate)
            potential_future_rewards = self.get_total_rewards(possible_solution)
            rewards_delta = current_rewards - potential_future_rewards
            if rewards_delta < 0:
                self.solution.append(candidate) # Add the candidate to the solution
            elif exp(-rewards_delta / temp) < random.randrange(0, 1) & (potential_future_rewards > 0):
                self.candidates.append(candidate)  # Accept the removal of the candidate
            elif not add_candidate:
                self.solution.append(candidate) # Don't remove the candidate from the solution
            else:
                self.candidates.append(candidate)   # Don't add the candidate to the solution
            self.historical_rewards.append(self.get_total_rewards(self.solution))
            temp = schedule(temp)
        return self.solution
