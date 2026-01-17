import json

from candidate import Candidate
from configuration.configuration import Configuration
from configuration.task import Task


class ConfigurationLoader:
    def __init__(self, config_path, candidates_path):
        self.config_path = config_path
        self.candidates_path = candidates_path

    def load_configuration(self):
        config_file = open(self.config_path, 'r')
        config_data = json.load(config_file)
        pipelines = config_data["pipelines"]
        tasks_data = config_data["tasks"]
        tasks = []
        for task in tasks_data:
            budget = task["budget"]
            job = task["job"]
            tasks.append(Task(budget, job))
        config_file.close()
        return Configuration(pipelines, tasks)

    def get_candidates(self):
        candidates_file = open(self.candidates_path, 'r')
        candidates_data = json.load(candidates_file)
        candidates = []

        identifier = 1
        for candidate in candidates_data:
            age = candidate["Age"]
            gender = candidate["Gender"]
            education_level = candidate["Education Level"]
            job = candidate["Job Title"]
            experience = candidate["Years of Experience"]
            salary = candidate["Salary"]
            candidates.append(Candidate(identifier, age, gender, education_level, job, experience, salary))
            identifier += 1
        candidates_file.close()
        return candidates
