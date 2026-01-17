class Candidate:
    def __init__(self, identifier, age, gender, education_level, job, experience, salary):
        self.id = identifier
        self.age = age
        self.gender = gender
        self.education_level = education_level
        self.job = job
        self.experience = experience
        self.salary = salary

    def print_candidate(self):
        print(f'Id: {self.id}')
        print(f'Age: {self.age}')
        print(f'Gender: {self.gender}')
        print(f'Education Level: {self.education_level}')
        print(f'Job Title: {self.job}')
        print(f'Years of experience: {self.experience}')
        print(f'Salary: {self.salary}')
