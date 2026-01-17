import copy

from configuration.solution_task import SolutionTask
from configuration.task import Task
from graph.graph import Graph
from agents.agent import Agent


class DynamicProgrammingAgent(Agent):
    def __init__(self, budget, job, candidates):
        super().__init__(budget, job, candidates)
        self.graph = Graph(self.candidates)

    def solve_task(self, queue):
        task = Task(self.budget, self.job)
        solution = self.solve_problem()
        queue.put(SolutionTask(task, solution))

    def solve_problem(self):
        solutions = []
        best_solution = None
        self.historical_rewards = []
        reward = 0
        for candidate in self.candidates:
            solutions += self.solve_subproblem(candidate)
        for solution in solutions:
            new_reward = self.get_total_rewards(solution)
            if new_reward > reward:
                best_solution = solution
                reward = new_reward
            self.historical_rewards.append(reward)
        return best_solution

    def get_new_solutions(self, child, solutions):
        for solution in solutions:
            solution.append(child)
        return solutions

    def solve_subproblem(self, candidate):
        solutions = [[candidate]]
        node = self.graph.get_node(candidate.id)
        for child in node.potential_children:
            copy_solutions = copy.deepcopy(solutions)
            new_solution = self.get_new_solutions(child, copy_solutions)
            solutions += new_solution
        return solutions
