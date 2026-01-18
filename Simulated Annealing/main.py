import json

from agent import Agent
from schedule import Schedule
from world import World
from colorama import Fore
import matplotlib.pyplot as plotter
import numpy

file = open("configuration/config.json")
config_data = json.load(file)
config_num = 1

schedules = [Schedule.Divide.value, Schedule.DecrementByLoopNum.value, Schedule.Decrement.value]
config_labels = []
avg_manhattan_dists_divide = []
avg_manhattan_dists_dec_by_num_loop = []
avg_manhattan_dists_dec = []

num_steps_dists_divide = []
num_steps_dists_dec_by_num_loop = []
num_steps_dists_dec = []

for config in config_data["configurations"]:
    config_labels.append(f'Configuration {config_num}')
    colour = ["blue", "green", "red"]
    num_rows = config["num_rows"]
    num_cols = config["num_cols"]
    num_residences = config["num_residences"]
    num_campuses = config["num_campuses"]
    num_clinics = config["num_clinics"]
    num_shops = config["num_shops"]
    num_bus_stops = config["num_bus_stops"]
    print(Fore.WHITE + f"Configuration {config_num}")
    print(f'Number of rows: {num_rows}')
    print(f'Number of columns: {num_cols}')
    print(f'Number of residences: {num_residences}')
    print(f'Number of campuses: {num_campuses}')
    print(f'Number of clinics: {num_clinics}')
    print(f'Number of shops: {num_shops}')
    print(f'Number of bus stops: {num_bus_stops}')
    world = World(num_rows, num_cols, num_residences, num_campuses, num_clinics, num_shops, num_bus_stops)
    print(Fore.WHITE + 'Initial State:\n')
    world.print_word()
    agent = Agent(world)
    agent.simulated_annealing(Schedule.Divide)
    avg_manhattan_dists_divide.append(agent.cum_manhattan_dist / num_residences)
    num_steps_dists_divide.append(agent.num_steps)
    print(Fore.WHITE + f'\nSolution for {Schedule.Divide.value}:\n')
    agent.world.print_word()
    print(
        f"{Fore.WHITE}Cumulative Average Manhattan distance: {str(agent.cum_manhattan_dist)} \nA residence's "
        f"Cumulative Average "
        f"Manhattan distance on average: {str(agent.cum_manhattan_dist / num_residences)} \nSteps: {str(agent.num_steps)}")
    print()

    agent.simulated_annealing(Schedule.DecrementByLoopNum)
    avg_manhattan_dists_dec_by_num_loop.append(agent.cum_manhattan_dist / num_residences)
    num_steps_dists_dec_by_num_loop.append(agent.num_steps)
    print(Fore.WHITE + f'\nSolution for {Schedule.DecrementByLoopNum.value}:\n')
    agent.world.print_word()
    print(
        f"{Fore.WHITE}Cumulative Average Manhattan distance: {str(agent.cum_manhattan_dist)} \nA residence's "
        f"Cumulative Average "
        f"Manhattan distance on average: {str(agent.cum_manhattan_dist / num_residences)} \nSteps: {str(agent.num_steps)}")

    agent.simulated_annealing(Schedule.Decrement)
    avg_manhattan_dists_dec.append(agent.cum_manhattan_dist / num_residences)
    num_steps_dists_dec.append(agent.num_steps)
    print(Fore.WHITE + f'\nSolution for {Schedule.Decrement.value}:\n')
    agent.world.print_word()
    print(
        f"{Fore.WHITE}Cumulative Average Manhattan distance: {str(agent.cum_manhattan_dist)} \nA residence's "
        f"Cumulative Average "
        f"Manhattan distance on average: {str(agent.cum_manhattan_dist / num_residences)} \nSteps: {str(agent.num_steps)}")
    print()
    config_num += 1

x = numpy.arange(len(config_labels))
plotter.bar(x - 0.2, avg_manhattan_dists_divide, 0.2, color='blue')
plotter.bar(x, avg_manhattan_dists_dec_by_num_loop, 0.2, color='green')
plotter.bar(x + 0.2, avg_manhattan_dists_dec, 0.2, color='red')
plotter.xticks(x, config_labels)
plotter.xlabel("Configurations")
plotter.ylabel("A residence's Cumulative Average Manhattan distance on average")
plotter.legend(schedules)
plotter.title("Manhattan Distance")
plotter.show()

plotter.bar(x - 0.2, num_steps_dists_divide, 0.2, color='blue')
plotter.bar(x, num_steps_dists_dec_by_num_loop, 0.2, color='green')
plotter.bar(x + 0.2, num_steps_dists_dec, 0.2, color='red')
plotter.xticks(x, config_labels)
plotter.xlabel("Configurations")
plotter.ylabel("Steps")
plotter.legend(schedules)
plotter.title("Steps")
plotter.show()

file.close()
