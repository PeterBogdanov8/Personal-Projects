from enum import Enum


class Schedule(Enum):
    Divide = 'divide by iteration'
    DecrementByLoopNum = 'decrement by iteration'
    Decrement = 'decrement'

