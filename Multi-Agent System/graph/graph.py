from graph.node import Node


class Graph:
    def __init__(self, candidates):
        self.nodes = []
        cs = candidates.copy()
        for candidate in candidates:
            node = Node(candidate, cs)
            self.nodes.append(node)

    def get_node(self, candidate_id):
        node = Node
        for n in self.nodes:
            if n.candidate.id == candidate_id:
                node = n
        return node
