class Node:
    def __init__(self, candidate, candidates):
        self.candidate = candidate
        self.potential_children = []
        for c in candidates:
            if c != candidate:
                self.potential_children.append(c)
