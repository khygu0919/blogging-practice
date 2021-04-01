from graphene import *

class SubjectType(Enum):
    MATH = 1
    ENGLISH = 2
    SCIENCE = 3
    PHYSICAL_EDUCATION = 4

class Score(ObjectType):
    grade = Int(required=True)
    class_code = Int(required=True)
    subject_type = SubjectType(required=True)
    name = String(required=True)
    score = Int()

class PostScoreInput(InputObjectType):
    grade = Int(required=True)
    class_code = Int(required=True)
    subject_type = SubjectType(required=True)
    name = String(required=True)
    score = Int()
    
class GetScoreInput(InputObjectType):
    grade = Int()
    class_code = Int()
    subject_type = SubjectType()
    name = String()
    score = Int()