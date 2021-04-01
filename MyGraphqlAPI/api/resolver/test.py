from schema.test import *
from .util import db

class Query(ObjectType):
    get_score = Field(Score, input=GetScoreInput(required=True))
    list_score = List(Score, input=GetScoreInput(required=True))

    def resolve_get_score(root, info, input):
        return get_score(**input)

    def resolve_list_score(root, info, input):
        return list_score(**input)


class Mutation(ObjectType):
    create_score = Field(Score, input=PostScoreInput(required=True))
    update_score = Field(Score, input=PostScoreInput(required=True))
    delete_score = Field(Score, input=PostScoreInput(required=True))

    def resolve_create_score(root, info, input):
        return create_score(**input)

    def resolve_update_score(root, info, input):
        return update_score(**input)
        
    def resolve_delete_score(root, info, input):
        return delete_score(**input)


@db.sanitize
def create_score(grade, class_code, subject_type, name=None, score=None):
    print(f'subject : {subject_type}')
    return db.execute(f"""
        INSERT INTO practice.grade_test (grade, class_code, subject_type, name_, score) VALUES
        ({grade}, {class_code}, {subject_type}, {name}, {score}) RETURNING *""")[0]


@db.sanitize
def get_score(grade, class_code, subject_type, name):
    return db.execute(f"""SELECT * FROM practice.grade_test
        WHERE grade={grade} AND class_code={class_code} AND subject_type={subject_type} AND name_={name}""")[0]


@db.sanitize
def list_score(grade, class_code, name=None):
    return db.execute(f"SELECT * FROM practice.grade_test WHERE grade={grade} AND class_code={class_code} AND name_={name}")


@db.sanitize
def update_score(grade, class_code, subject_type, name=None, score=None):
    return db.execute(f"""
        UPDATE practice.grade_test SET
        score = COALESCE({score}, -1)
    WHERE grade={grade} AND class_code={class_code} AND subject_type={subject_type} AND name_={name}
    RETURNING *;
""")[0]


@db.sanitize
def delete_score(grade, class_code, subject_type, name=None):
    return db.execute(f"""
    DELETE FROM practice.grade_test
    WHERE grade={grade} AND class_code={class_code} AND subject_type={subject_type} AND name_={name}
    RETURNING *;
""")[0]
