import random

from configuration.solution_task import SolutionTask
from configuration.task import Task
from graph.graph import Graph
from agents.agent import Agent


class SearchAgent(Agent):
    def __init__(self, budget, job, candidates):
        super().__init__(budget, job, candidates)
        self.graph = Graph(self.candidates)
        self.task = Task(self.budget, self.job)

    def get_initial_candidate(self):
        reward = 0
        candidate = None
        for c in self.candidates:
            new_reward = self.get_total_rewards([c])
            if new_reward > reward:
                candidate = c
                reward = new_reward
        return candidate

    def best_first_search_task(self, queue):
        solution = self.best_first_search()
        queue.put(SolutionTask(self.task, solution))

    def cheap_first_search_task(self, queue):
        solution = self.cheap_first_search()
        queue.put(SolutionTask(self.task, solution))

    def expected_employer_vs_employee_pay_search_task(self, queue):
        solution = self.expected_employer_vs_employee_pay_search()
        queue.put(SolutionTask(self.task, solution))

    def best_first_search(self):
        self.historical_rewards = []
        traverse_graph = True
        candidate = self.get_initial_candidate()
        solution = [candidate]
        while traverse_graph:
            node = self.graph.get_node(candidate.id)
            rewards = self.get_total_rewards(solution)
            child_to_add = None
            for child in node.potential_children:
                solution_copy = solution.copy()
                solution_copy.append(child)
                new_rewards = self.get_total_rewards(solution_copy)
                if (new_rewards > rewards) & (child not in solution):
                    child_to_add = child
                    rewards = new_rewards
            if child_to_add is None:
                traverse_graph = False
            else:
                solution.append(child_to_add)
                candidate = child_to_add
            self.historical_rewards.append(self.get_total_rewards(solution))
        return solution

    def cheap_first_search(self):
        self.historical_rewards = []
        traverse_graph = True
        candidate = self.get_initial_candidate()
        solution = [candidate]
        while traverse_graph:
            node = self.graph.get_node(candidate.id)
            expected_pay = node.potential_children[0].salary
            child_to_add = None
            for child in node.potential_children:
                solution_copy = solution.copy()
                solution_copy.append(child)
                rewards = self.get_total_rewards(solution_copy)
                new_expected_pay = child.salary
                if (new_expected_pay <= expected_pay) & (rewards > 0) & (child not in solution):
                    child_to_add = child
                    expected_pay = new_expected_pay
            if child_to_add is None:
                traverse_graph = False
            else:
                solution.append(child_to_add)
                candidate = child_to_add
            self.historical_rewards.append(self.get_total_rewards(solution))
        return solution

    def expected_employer_vs_employee_pay_search(self):
        self.historical_rewards = []
        traverse_graph = True
        candidate = self.get_initial_candidate()
        solution = [candidate]
        # Average yearly pay of a US qualified worker according to https://www.glassdoor.com/Salaries/white-collar-salary-SRCH_KO0,12.htm
        employer_expected_yearly_pay = 69000
        while traverse_graph:
            node = self.graph.get_node(candidate.id)
            expected_employer_pay = employer_expected_yearly_pay * node.potential_children[0].experience
            expected_employee_pay = node.potential_children[0].salary
            pay_heuristic = expected_employer_pay - expected_employee_pay
            child_to_add = None
            for child in node.potential_children:
                solution_copy = solution.copy()
                solution_copy.append(child)
                rewards = self.get_total_rewards(solution_copy)
                expected_employer_pay = employer_expected_yearly_pay * child.experience
                expected_employee_pay = child.salary
                new_pay_heuristic = expected_employer_pay - expected_employee_pay
                if (new_pay_heuristic >= pay_heuristic) & (rewards > 0) & (child not in solution):
                    child_to_add = child
                    pay_heuristic = new_pay_heuristic
            if child_to_add is None:
                traverse_graph = False
            else:
                solution.append(child_to_add)
                candidate = child_to_add
            self.historical_rewards.append(self.get_total_rewards(solution))
        return solution
