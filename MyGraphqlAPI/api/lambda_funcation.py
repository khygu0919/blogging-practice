import json
import graphene
import resolver as r

def post_query_build(grade, classCode, subjectType, name, score):
    query = """
        mutation CreateScore ($input: PostScoreInput!) {
            createScore (input: $input) {
                grade
                classCode
                subjectType
                name
                score
            }
        }
        """
    variables = {'input': {'grade': grade, 'classCode': classCode, 'subjectType': subjectType, 'name': name, 'score': score}}
    return query, variables
    
def update_query_build(grade, classCode, subjectType, name, score):
    query = """
        mutation UpdateScore ($input: PostScoreInput!) {
            updateScore (input: $input) {
                grade
                classCode
                subjectType
                name
                score
            }
        }
        """
    variables = {'input': {'grade': grade, 'classCode': classCode, 'subjectCode': subjectType, 'name': name, 'score': score}}
    return query, variables
    
def delete_query_build(grade, classCode, subjectType, name):
    query = """
        mutation DeleteScore ($input: PostScoreInput!) {
            deleteScore (input: $input) {
                grade
                classCode
                subjectType
                name
                score
            }
        }
        """
    variables = {'input': {'grade': grade, 'classCode': classCode, 'subjectType': subjectType, 'name': name}}
    return query, variables
    
def get_query_build(grade, classCode, subjectType, name):
    query = """
        query GetScore ($input: GetScoreInput!) {
            getScore (input: $input) {
                grade
                classCode
                subjectType
                name
                score
            }
        }
        """
    variables = {'input': {'grade': grade, 'classCode': classCode, 'subjectType': subjectType, 'name': name}}
    return query, variables

def list_query_build(grade, classCode, name):
    query = """
        query ListScore ($input: GetScoreInput!) {
            listScore (input: $input) {
                subjectType
                name
                score
            }
        }
        """
    variables = {'input': {'grade': grade, 'classCode': classCode, 'name': name}}
    return query, variables

server = graphene.Schema(query=r.test.Query, mutation=r.test.Mutation)

def lambda_handler(event, context):
    
    try:
        """
        input = {'grade': 1, 'classCode': 1, 'subjectType': 'SCIENCE', 'name': 'ㅋㅋㄹㅃㅃ', 'score': 12345}
        query, params = post_query_build(**input)
        """
        """
        input = {'grade': 1, 'classCode': 1, 'subjectType': 'MATH, 'name': 'ㅋㅋㄹㅃㅃ'}
        query, params = get_query_build(**input)
        """
        """
        input = {'grade': 1, 'classCode': 1, 'name': 'ㅋㅋㄹㅃㅃ'}
        query, params = list_query_build(**input)
        """
        """
        input = {'grade': 1, 'classCode': 1, 'subjectType': ENGLISH, 'name': 'ㅋㅋㄹㅃㅃ', 'score': 54321}
        query, params = update_query_build(**input)
        """
        """
        input = {'grade': 1, 'classCode': 1, 'subjectType': 'MATH, 'name': 'ㅋㅋㄹㅃㅃ'}
        query, params = delete_query_build(**input)
        """
        query = event['query']
        params = {'input': event['input']}
        result = server.execute(query, variables=params)
        return result.data
    except Exception as e:
        print(str(e))
