class Agent:
    def __init__(self, budget, job, candidates):
        self.budget = budget
        self.job = job
        self.candidates = list(filter(self.get_candidate_by_job, candidates))
        self.historical_rewards = []

    def get_candidate_by_job(self, candidate):
        return str.__contains__(candidate.job, self.job)

    def get_total_rewards(self, solution):
        expense = 0
        rewards = 0
        for candidate in solution:
            rewards += candidate.experience
            expense += candidate.salary

        if expense > self.budget:
            return 0
        else:
            return rewards

    def print_candidates(self, candidates):
        for candidate in candidates:
            print('-----------------------')
            candidate.print_candidate()
