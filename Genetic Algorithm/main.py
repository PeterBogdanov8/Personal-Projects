import json
from problem_solver import ProblemSolver
from grid_world import GridWorld
from colorama import Fore
import matplotlib.pyplot as graph_plotter


json_file = open('config/configurations.json')
json_config_data = json.load(json_file)
num_config = 0

for json_config in json_config_data["config"]:
    num_rows = json_config["num_rows"]
    num_cols = json_config["num_cols"]
    num_houses = json_config["num_houses"]
    num_schools = json_config["num_schools"]
    num_libraries = json_config["num_libraries"]
    num_clinics = json_config["num_clinics"]
    num_recreation_centres = json_config["num_recreation_centres"]
    num_malls = json_config["num_malls"]
    world = GridWorld(num_rows, num_cols, num_schools, num_libraries, num_clinics, num_recreation_centres, num_malls)
    problem_solver = ProblemSolver(world, num_houses)
    num_config += 1
    print(Fore.WHITE + f"Config {num_config}")
    world = problem_solver.solve_problem()
    world.print_grid_world()
    print(Fore.WHITE + f"A house's Cumulative Average Manhattan distance on average: {world.cum_manhattan_dist/ num_houses}")
    print(Fore.WHITE + f"Generations: {problem_solver.generation}")
    generations = list(range(1, len(problem_solver.average_cum_manhattan_dist_of_fittest) + 1))
    graph_plotter.plot(generations, problem_solver.average_cum_manhattan_dist_of_fittest, linestyle="solid")
    graph_plotter.title(f"Configuration {num_config}")
    graph_plotter.ylabel("House's cumulative average Manhattan distance on average")
    graph_plotter.xlabel("Generations")
    graph_plotter.show()
