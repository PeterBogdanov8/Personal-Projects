from math import log
from math import atan
import threading
import queue
from agents.agent import Agent
from agents.dynamic_programming_agent import DynamicProgrammingAgent
from agents.genetic_agent import GeneticAgent
from agents.search_agent import SearchAgent
from agents.simulated_annealing_agent import SimulatedAnnealingAgent


class MultiAgentSystem:
    def __init__(self, pipeline, configuration, candidates):
        self.pipeline = pipeline
        self.configuration = configuration
        self.candidates = candidates
        self.solutions = []
        self.solution_tasks = []
        self.total_rewards = 0

    def get_simulated_annealing_process(self, fifo_queue, task, num_simulated_annealing_solutions):
        if num_simulated_annealing_solutions % 2 == 0:
            sa_agent = SimulatedAnnealingAgent(task.budget, task.job, self.candidates)
            return threading.Thread(target=sa_agent.solve_task, args=(fifo_queue, lambda x: log(x, 10),))
        elif num_simulated_annealing_solutions % 3 == 0:
            sa_agent = SimulatedAnnealingAgent(task.budget, task.job, self.candidates)
            return threading.Thread(target=sa_agent.solve_task, args=(fifo_queue, lambda x: atan(x / 10),))
        else:
            sa_agent = SimulatedAnnealingAgent(task.budget, task.job, self.candidates)
            return threading.Thread(target=sa_agent.solve_task, args=(fifo_queue, lambda x: x - 1),)

    def get_genetic_algorithm_process(self, fifo_queue, task, num_genetic_solutions):
        if num_genetic_solutions % 2 == 0:
            g_agent = GeneticAgent(task.budget, task.job, self.candidates)
            return threading.Thread(target=g_agent.choose_candidates_task, args=(fifo_queue, False, 0.02, 20))
        else:
            g_agent = GeneticAgent(task.budget, task.job, self.candidates)
            return threading.Thread(target=g_agent.choose_candidates_task, args=(fifo_queue, True, 0.02, 20))

    def get_search_algorithm_process(self, fifo_queue, task, num_search_solutions):
        if num_search_solutions % 3 == 0:
            s_agent = SearchAgent(task.budget, task.job, self.candidates)
            return threading.Thread(target=s_agent.cheap_first_search, args=(fifo_queue,))
        elif num_search_solutions % 2 == 0:
            s_agent = SearchAgent(task.budget, task.job, self.candidates)
            return threading.Thread(target=s_agent.expected_employer_vs_employee_pay_search, args=(fifo_queue,))
        else:
            s_agent = SearchAgent(task.budget, task.job, self.candidates)
            return threading.Thread(target=s_agent.best_first_search_task, args=(fifo_queue,))

    def get_dynamic_programming_algorithm_process(self, fifo_queue, task):
        dp_agent = DynamicProgrammingAgent(task.budget, task.job, self.candidates)
        return threading.Thread(target=dp_agent.solve_task, args=(fifo_queue,))

    def assign_candidate_to_roles(self):
        num_task = 1
        num_simulated_annealing_solutions = 1
        num_genetic_solutions = 1
        num_search_solutions = 1
        threads = []
        fifo_queue = queue.Queue()
        print(f'------------pipeline {self.pipeline} ------------')
        for task in self.configuration.tasks:
            if self.pipeline == 1:
                threads.append(self.get_simulated_annealing_process(fifo_queue, task, num_simulated_annealing_solutions))
                num_simulated_annealing_solutions += 1
            elif self.pipeline == 2:
                threads.append(self.get_genetic_algorithm_process(fifo_queue, task, num_genetic_solutions))
                num_genetic_solutions += 1
            elif self.pipeline == 3:
                if num_task % 4 == 0:
                    threads.append(self.get_dynamic_programming_algorithm_process(fifo_queue, task))
                elif num_task % 2 == 0:
                    threads.append(self.get_genetic_algorithm_process(fifo_queue, task, num_genetic_solutions))
                    num_genetic_solutions += 1
                elif num_task % 3 == 0:
                    threads.append(self.get_search_algorithm_process(fifo_queue, task, num_search_solutions))
                    num_search_solutions += 1
                else:
                    threads.append(self.get_simulated_annealing_process(fifo_queue, task, num_simulated_annealing_solutions))
                    num_simulated_annealing_solutions += 1
            num_task = num_task + 1

        for thread in threads:
            thread.start()
        for thread in threads:
            thread.join()

        assigned_employees = []
        while not fifo_queue.empty():
            solution_task = fifo_queue.get()
            self.solution_tasks.append(solution_task)
            solution = solution_task.solution
            assigned_employees += solution
            self.solutions.append(solution)

        assigned_employees = set(assigned_employees)
        assigned_employees = list(assigned_employees)
        unassigned_candidates = self.get_unassigned_candidates(assigned_employees)
        multiple_role_employees_and_indices = self.get_multiple_role_employees(assigned_employees)

        if len(multiple_role_employees_and_indices) > 0:
            self.use_backtracking(multiple_role_employees_and_indices, unassigned_candidates)

        for solution_task in self.solution_tasks:
            task = solution_task.task
            solution = solution_task.solution
            assigned_employees += solution
            print('--------------------------------------')
            print(f'Budget: {task.budget}')
            print(f'Role: {task.job}')
            print('--------------------------------------')
            agent = Agent(task.budget, task.job, self.candidates)
            agent.print_candidates(solution)
            rewards = agent.get_total_rewards(solution)
            self.total_rewards += rewards
            print('--------------------------------------')
            print(f'Rewards: {rewards}')
        print('--------------------------------------')
        print(f'Total Rewards {self.total_rewards}')

    def get_unassigned_candidates(self, assigned_employees):
        unassigned_candidates = []
        for candidate in self.candidates:
            if not assigned_employees.__contains__(candidate):
                unassigned_candidates.append(candidate)
        return unassigned_candidates

    def get_multiple_role_employees(self, employees):
        multiple_role_employees = []
        for employee in employees:
            is_assigned, solution_indices = self.is_assigned_multiple_roles(employee)
            if is_assigned:
                task_index = solution_indices[0]
                job = self.solution_tasks[task_index].task.job
                multiple_role_employees.append([employee, solution_indices, job])
        return multiple_role_employees

    def is_assigned_multiple_roles(self, employee):
        num_roles = 0
        index = 0
        for solution in self.solutions:
            if solution.__contains__(employee):
                num_roles += 1
            if num_roles > 1:
                return True, [index, solution.index(employee)]
            index += 1
        return False, None

    def use_backtracking(self, multiple_role_employees, unassigned_candidates):
        multiple_role_employee = multiple_role_employees.pop()
        return self.back_track_employees(multiple_role_employee, multiple_role_employees, unassigned_candidates)

    def back_track_employees(self, employee_and_indices, multiple_role_employees, unassigned_candidates):
        employee, solution_indices, job = employee_and_indices
        agent = Agent(0, job, unassigned_candidates)
        prev_unassigned_candidates = unassigned_candidates
        unassigned_candidates = agent.candidates
        if employee is not None:
            if len(unassigned_candidates) > 0:
                candidate = unassigned_candidates.pop()
                prev_unassigned_candidates.remove(candidate)
                task_index = solution_indices[0]
                solution_index = solution_indices[1]
                self.solutions[task_index][solution_index] = candidate
                self.solution_tasks[task_index].solution[solution_index] = candidate
            else:
                task_index = solution_indices[0]
                self.solutions[task_index].remove(employee)
                if len(self.solutions[task_index]) == 0:
                    raise Exception(f'Unable to assign employee to {job} job')
            assigned_to_multiple_roles, solution_indices = self.is_assigned_multiple_roles(employee)
            if assigned_to_multiple_roles:
                self.back_track_employees([employee, solution_indices, job], multiple_role_employees, unassigned_candidates)
        if len(multiple_role_employees) > 0:
            employee_and_indices = multiple_role_employees.pop()
            self.back_track_employees(employee_and_indices, multiple_role_employees, prev_unassigned_candidates)
