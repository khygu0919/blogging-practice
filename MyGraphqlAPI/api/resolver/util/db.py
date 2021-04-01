import inspect
import psycopg2
import os
import re

def execute(query):
    with psycopg2.connect(host=os.environ["DB_HOST"], user=os.environ["DB_USER"],
    dbname=os.environ["DB_NAME"], password=os.environ["DB_PASSWORD"]) as conn:
        with conn.cursor() as cur:
            try:
                cur.execute(query)
                cols = [des.name.rstrip('_') for des in cur.description]
                rows = cur.fetchall()
                res = [dict(zip(cols, row)) for row in rows]
                return res
            except Exception as e:
                print(str(e))
                cur.close()
    
def sanitize(func):
    """
    db 인자를 다루는 함수에서 사용해야 하는 decorator
    Args:
        func: db 인자를 다루는 함수

    Returns: SQL escaping된 인자들로 치환된 func
    """
    def wrapper(*args, **kwargs):
        # 기본 인자값 처리 
        default_params = (
            (k, v.default)
            for k, v in inspect.signature(func).parameters.items()
            if v.default is not inspect.Parameter.empty
        )
        for k, v in default_params:
            if k not in kwargs:
                kwargs[k] = v
        for k, v in kwargs.items():
            if v is None:
                kwargs[k] = 'NULL'
            elif isinstance(v, str):
                v = re.sub('[\s\t\'\/\\\;=&+!#$%*"]', '', v)
                v = v.replace("'", "''")
                kwargs[k] = "'" + v + "'"
        return func(*args, **kwargs)
    return wrapper