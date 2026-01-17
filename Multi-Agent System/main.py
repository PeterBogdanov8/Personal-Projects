from configuration.configuration_loader import ConfigurationLoader
from data.data_analyser import DataAnalyser

configLoader = ConfigurationLoader('configuration/config.json', 'data/candidates.json')
candidates = configLoader.get_candidates()
config = configLoader.load_configuration()

dataAnalyser = DataAnalyser('data/data_analyser_config.json', candidates)
dataAnalyser.visualise_data_simulated_annealing_agent()
dataAnalyser.visualise_data_search_agent()
dataAnalyser.visualise_data_genetic_agent()
dataAnalyser.visualise_dynamic_programming_agent()
dataAnalyser.visualise_multi_agent_system(config, candidates)


