import json
from math import log, atan

import matplotlib.pyplot as mat_plotter
import numpy as np

from agents.dynamic_programming_agent import DynamicProgrammingAgent
from agents.genetic_agent import GeneticAgent
from agents.search_agent import SearchAgent
from agents.simulated_annealing_agent import SimulatedAnnealingAgent
from multi_agent_system import MultiAgentSystem


class DataAnalyser:
    def __init__(self, config_path, candidates):
        config_file = open(config_path, 'r')
        config_data = json.load(config_file)
        budget = config_data["budget"]
        job = config_data["job"]
        self.sa_agent = SimulatedAnnealingAgent(budget, job, candidates)
        self.g_agent = GeneticAgent(budget, job, candidates)
        self.s_agent = SearchAgent(budget, job, candidates)
        self.dp_agent = DynamicProgrammingAgent(budget, job, candidates)

    def visualise_data_simulated_annealing_agent(self):
        self.sa_agent.solve_problem(lambda x: log(x, 10))
        log_schedule_rewards = self.sa_agent.get_total_rewards(self.sa_agent.solution)
        self.sa_agent.solve_problem(lambda x: atan(x / 10))
        inverse_tan_schedule_rewards = self.sa_agent.get_total_rewards(self.sa_agent.solution)
        self.sa_agent.solve_problem(lambda x: x - 1)
        decrement_schedule_rewards = self.sa_agent.get_total_rewards(self.sa_agent.solution)
        mat_plotter.bar(1, log_schedule_rewards, 0.2, color="red")
        mat_plotter.bar(2, inverse_tan_schedule_rewards, 0.2, color="green")
        mat_plotter.bar(3, decrement_schedule_rewards, 0.2, color="blue")
        mat_plotter.ylabel("Rewards")
        mat_plotter.xlabel("Schedules")
        mat_plotter.xticks([1, 2, 3], ["log base 10", "inverse tan", "decrement"])
        mat_plotter.text(1, log_schedule_rewards, log_schedule_rewards, ha='center')
        mat_plotter.text(2, inverse_tan_schedule_rewards, inverse_tan_schedule_rewards, ha='center')
        mat_plotter.text(3, decrement_schedule_rewards, decrement_schedule_rewards, ha='center')
        mat_plotter.title("Simulated Annealing Agent")
        mat_plotter.show()

    def visualise_data_search_agent(self):
        solution = self.s_agent.best_first_search()
        best_first_search_rewards = self.s_agent.get_total_rewards(solution)
        solution = self.s_agent.cheap_first_search()
        cheap_first_search_rewards = self.sa_agent.get_total_rewards(solution)
        solution = self.s_agent.expected_employer_vs_employee_pay_search()
        expected_employer_vs_employee_pay_search_rewards = self.sa_agent.get_total_rewards(solution)
        mat_plotter.bar(1, best_first_search_rewards, 0.2, color="red")
        mat_plotter.bar(2, cheap_first_search_rewards, 0.2, color="green")
        mat_plotter.bar(3, expected_employer_vs_employee_pay_search_rewards, 0.2, color="blue")
        mat_plotter.ylabel("Rewards")
        mat_plotter.xlabel("Searches")
        mat_plotter.xticks([1, 2, 3], ["best first", "cheap first", "expected employer vs employee pay"])
        mat_plotter.text(1, best_first_search_rewards, best_first_search_rewards, ha='center')
        mat_plotter.text(2, cheap_first_search_rewards, cheap_first_search_rewards, ha='center')
        mat_plotter.text(3, expected_employer_vs_employee_pay_search_rewards, expected_employer_vs_employee_pay_search_rewards, ha='center')
        mat_plotter.title("Search Agent")
        mat_plotter.show()

    def visualise_data_genetic_agent(self):
        solution = self.g_agent.choose_candidates(True, 0.02, 5)
        one_point_size_5_rewards = self.g_agent.get_total_rewards(solution)
        solution = self.g_agent.choose_candidates(False, 0.02, 5)
        uniform_size_5_rewards = self.g_agent.get_total_rewards(solution)

        solution = self.g_agent.choose_candidates(True, 0.02, 10)
        one_point_size_10_rewards = self.g_agent.get_total_rewards(solution)
        solution = self.g_agent.choose_candidates(False, 0.02, 10)
        uniform_size_10_rewards = self.g_agent.get_total_rewards(solution)

        solution = self.g_agent.choose_candidates(True, 0.02, 15)
        one_point_size_15_rewards = self.g_agent.get_total_rewards(solution)
        solution = self.g_agent.choose_candidates(False, 0.02, 15)
        uniform_size_15_rewards = self.g_agent.get_total_rewards(solution)

        solution = self.g_agent.choose_candidates(True, 0.02, 20)
        one_point_size_20_rewards = self.g_agent.get_total_rewards(solution)
        solution = self.g_agent.choose_candidates(False, 0.02, 20)
        uniform_size_20_rewards = self.g_agent.get_total_rewards(solution)

        solution = self.g_agent.choose_candidates(True, 0.02, 25)
        one_point_size_25_rewards = self.g_agent.get_total_rewards(solution)
        solution = self.g_agent.choose_candidates(False, 0.02, 25)
        uniform_size_25_rewards = self.g_agent.get_total_rewards(solution)

        solution = self.g_agent.choose_candidates(True, 0.02, 30)
        one_point_size_30_rewards = self.g_agent.get_total_rewards(solution)
        solution = self.g_agent.choose_candidates(False, 0.02, 30)
        uniform_size_30_rewards = self.g_agent.get_total_rewards(solution)

        crossover_labels = ["one-point crossover", "uniform crossover"]
        x_labels = ["5", "10", "15", "20", "25", "30"]
        x = np.arange(len(x_labels))
        mat_plotter.bar(x - 0.2, [one_point_size_5_rewards, one_point_size_10_rewards, one_point_size_15_rewards, one_point_size_20_rewards, one_point_size_25_rewards, one_point_size_30_rewards], 0.2, color="red")
        mat_plotter.bar(x, [uniform_size_5_rewards, uniform_size_10_rewards, uniform_size_15_rewards, uniform_size_20_rewards, uniform_size_25_rewards, uniform_size_30_rewards], 0.2, color="green")
        mat_plotter.legend(crossover_labels)
        mat_plotter.xticks(x, x_labels)

        mat_plotter.text(-0.2, one_point_size_5_rewards, one_point_size_5_rewards, ha='center')
        mat_plotter.text(0.8, one_point_size_10_rewards, one_point_size_10_rewards, ha='center')
        mat_plotter.text(1.8, one_point_size_15_rewards, one_point_size_15_rewards, ha='center')
        mat_plotter.text(2.8, one_point_size_20_rewards, one_point_size_20_rewards, ha='center')
        mat_plotter.text(3.8, one_point_size_25_rewards, one_point_size_25_rewards, ha='center')
        mat_plotter.text(4.8, one_point_size_30_rewards, one_point_size_30_rewards, ha='center')

        mat_plotter.text(0, uniform_size_5_rewards, uniform_size_5_rewards, ha='center')
        mat_plotter.text(1, uniform_size_10_rewards, uniform_size_10_rewards, ha='center')
        mat_plotter.text(2, uniform_size_15_rewards, uniform_size_15_rewards, ha='center')
        mat_plotter.text(3, uniform_size_20_rewards, uniform_size_20_rewards, ha='center')
        mat_plotter.text(4, uniform_size_25_rewards, uniform_size_25_rewards, ha='center')
        mat_plotter.text(5, uniform_size_30_rewards, uniform_size_30_rewards, ha='center')

        mat_plotter.title("Genetic Agent")
        mat_plotter.ylabel("Rewards")
        mat_plotter.xlabel("Population")
        mat_plotter.show()

        solution = self.g_agent.choose_candidates(True, 0.02, 20)
        one_point_mutation_2_rewards = self.g_agent.get_total_rewards(solution)
        solution = self.g_agent.choose_candidates(False, 0.02, 20)
        uniform_mutation_2_rewards = self.g_agent.get_total_rewards(solution)

        solution = self.g_agent.choose_candidates(True, 0.04, 20)
        one_point_mutation_4_rewards = self.g_agent.get_total_rewards(solution)
        solution = self.g_agent.choose_candidates(False, 0.04, 20)
        uniform_mutation_4_rewards = self.g_agent.get_total_rewards(solution)

        solution = self.g_agent.choose_candidates(True, 0.06, 20)
        one_point_mutation_6_rewards = self.g_agent.get_total_rewards(solution)
        solution = self.g_agent.choose_candidates(False, 0.06, 20)
        uniform_mutation_6_rewards = self.g_agent.get_total_rewards(solution)

        solution = self.g_agent.choose_candidates(True, 0.08, 20)
        one_point_mutation_8_rewards = self.g_agent.get_total_rewards(solution)
        solution = self.g_agent.choose_candidates(False, 0.08, 20)
        uniform_mutation_8_rewards = self.g_agent.get_total_rewards(solution)

        solution = self.g_agent.choose_candidates(True, 0.1, 20)
        one_point_mutation_10_rewards = self.g_agent.get_total_rewards(solution)
        solution = self.g_agent.choose_candidates(False, 0.1, 20)
        uniform_mutation_10_rewards = self.g_agent.get_total_rewards(solution)

        x_labels = ["2%", "4%", "6%", "8%", "10%"]
        x = np.arange(len(x_labels))
        mat_plotter.bar(x - 0.2, [one_point_mutation_2_rewards, one_point_mutation_4_rewards, one_point_mutation_6_rewards, one_point_mutation_8_rewards, one_point_mutation_10_rewards], 0.2, color="red")
        mat_plotter.bar(x, [uniform_mutation_2_rewards, uniform_mutation_4_rewards, uniform_mutation_6_rewards, uniform_mutation_8_rewards, uniform_mutation_10_rewards], 0.2, color="green")
        mat_plotter.legend(crossover_labels)
        mat_plotter.xticks(x, x_labels)

        mat_plotter.text(-0.2, one_point_mutation_2_rewards, one_point_mutation_2_rewards, ha='center')
        mat_plotter.text(0.8, one_point_mutation_4_rewards, one_point_mutation_4_rewards, ha='center')
        mat_plotter.text(1.8, one_point_mutation_6_rewards, one_point_mutation_6_rewards, ha='center')
        mat_plotter.text(2.8, one_point_mutation_8_rewards, one_point_mutation_8_rewards, ha='center')
        mat_plotter.text(3.8, one_point_mutation_10_rewards, one_point_mutation_10_rewards, ha='center')

        mat_plotter.text(0, uniform_mutation_2_rewards, uniform_mutation_2_rewards, ha='center')
        mat_plotter.text(1, uniform_mutation_4_rewards, uniform_mutation_4_rewards, ha='center')
        mat_plotter.text(2, uniform_mutation_6_rewards, uniform_mutation_6_rewards, ha='center')
        mat_plotter.text(3, uniform_mutation_8_rewards, uniform_mutation_8_rewards, ha='center')
        mat_plotter.text(4, uniform_mutation_10_rewards, uniform_mutation_10_rewards, ha='center')

        mat_plotter.title("Genetic Agent")
        mat_plotter.ylabel("Rewards")
        mat_plotter.xlabel("Mutation")
        mat_plotter.show()

    def visualise_dynamic_programming_agent(self):
        solution = self.dp_agent.solve_problem()
        dp_agent_rewards = self.dp_agent.get_total_rewards(solution)
        solution = self.s_agent.best_first_search()
        search_agent_rewards = self.s_agent.get_total_rewards(solution)
        self.sa_agent.solve_problem(lambda x: atan(x / 10))
        sa_agent_rewards = self.sa_agent.get_total_rewards(self.sa_agent.solution)
        solution = self.g_agent.choose_candidates(True, 0.02, 20)
        genetic_agent_rewards = self.g_agent.get_total_rewards(solution)
        mat_plotter.bar(1, dp_agent_rewards, 0.2, color='green')
        mat_plotter.bar(2, search_agent_rewards, 0.2, color='red')
        mat_plotter.bar(3, sa_agent_rewards, 0.2, color='blue')
        mat_plotter.bar(4, genetic_agent_rewards, 0.2, color='purple')
        x_labels = ["Dynamic Programming", "Search", "Simulated Annealing", "Genetic"]
        mat_plotter.title("Algorithms Comparison")
        mat_plotter.ylabel("Rewards")
        mat_plotter.xlabel("Algorithms")
        mat_plotter.xticks([1, 2, 3, 4], x_labels)
        mat_plotter.text(1, dp_agent_rewards, dp_agent_rewards, ha='center')
        mat_plotter.text(2, search_agent_rewards, search_agent_rewards, ha='center')
        mat_plotter.text(3, sa_agent_rewards, sa_agent_rewards, ha='center')
        mat_plotter.text(4, genetic_agent_rewards, genetic_agent_rewards, ha='center')
        mat_plotter.show()

        dp_agent_historical_rewards = self.dp_agent.historical_rewards[:350]
        dp_agent_iterations = np.arange(350)
        mat_plotter.plot(dp_agent_iterations, dp_agent_historical_rewards, marker='o', color='green')
        s_agent_historical_rewards = self.s_agent.historical_rewards
        s_agent_num_iterations = len(s_agent_historical_rewards)
        s_agent_iterations = np.arange(s_agent_num_iterations)
        mat_plotter.plot(s_agent_iterations, s_agent_historical_rewards, marker='o', color='red')
        sa_agent_historical_rewards = self.sa_agent.historical_rewards
        sa_agent_num_iterations = len(sa_agent_historical_rewards)
        sa_agent_iterations = np.arange(sa_agent_num_iterations)
        mat_plotter.plot(sa_agent_iterations, sa_agent_historical_rewards, marker='o', color='blue')
        g_agent_historical_rewards = self.g_agent.historical_rewards
        g_agent_num_iterations = len(g_agent_historical_rewards)
        g_agent_iterations = np.arange(g_agent_num_iterations)
        mat_plotter.plot(g_agent_iterations, g_agent_historical_rewards, marker='o', color='purple')
        mat_plotter.title("Agent Comparison")
        mat_plotter.ylabel("Rewards")
        mat_plotter.xlabel("Iterations")
        mat_plotter.legend(['Dynamic Programming Agent', 'Search Agent', 'Simulated Annealing Agent', 'Genetic Agent'])
        mat_plotter.show()

    def visualise_multi_agent_system(self, configuration, candidates):
        x = 1
        xs = []
        x_labels = []
        for pipeline in configuration.pipelines:
            multi_agent_system = MultiAgentSystem(pipeline, configuration, candidates)
            multi_agent_system.assign_candidate_to_roles()
            mat_plotter.bar(x, multi_agent_system.total_rewards)
            mat_plotter.text(x, multi_agent_system.total_rewards, multi_agent_system.total_rewards, ha='center')
            x_labels.append(f'Pipeline {pipeline}')
            xs.append(x)
            x += 1
        mat_plotter.xticks(xs, x_labels)
        mat_plotter.ylabel("Rewards")
        mat_plotter.xlabel("Pipelines")
        mat_plotter.title("Multi-agent System Pipelines")
        mat_plotter.show()