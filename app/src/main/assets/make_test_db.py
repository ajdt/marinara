#!/usr/bin/env python
# @author: Armando Diaz Tolentino (cer0.istari@gmail.com)
# desc: code to generate a test database for Marinara app
#
from datetime import datetime, timedelta
from random import randrange

mkDelete = lambda table: 'DELETE FROM ' + table + ' ; '
mkInsert = lambda table, values_str: 'INSERT INTO ' + table + ' ' + values_str + ' ;'

def stringify(elem):
    if isinstance(elem, str):
        return "'" + elem + "'"
    elif isinstance(elem, float): # supress scientific notation
        return '{:f}'.format(elem)
    else:
        return str(elem)

def mkValueStr(values_arr):
    string_values = map(stringify, values_arr)
    return 'VALUES(' + ','.join(string_values) + ')'

TASK_TABLE = 'TASK'
SESSIONS_TABLE = 'POMODORO_SESSION'

############################## date functions ##############################

def randomDateTime(min_date, max_date):
    delta_seconds = (max_date - min_date).total_seconds()
    return min_date + timedelta(seconds=randrange(delta_seconds))

def datetimeToEpocMillisec(datetime_obj):
    delta = (datetime_obj - datetime(1970,1,1))
    return delta.total_seconds() * 1000 # convert to seconds str and parse to int

def deleteTableEntries():
    return mkDelete(TASK_TABLE) + '\n'  + mkDelete(SESSIONS_TABLE) + '\n'

# TASK ( ID INTEGER PRIMARY KEY AUTOINCREMENT , NAME TEXT NOT NULL UNIQUE, STATUS  NOT NULL );
def createNewTasks():
    first_task = [1, 'code', 'ACTIVE']
    second_task = [2, 'chores', 'ACTIVE']
    return mkInsert(TASK_TABLE, mkValueStr(first_task)) + '\n' + mkInsert(TASK_TABLE, mkValueStr(second_task)) + '\n'

# POMODORO_SESSION ( ID INTEGER PRIMARY KEY AUTOINCREMENT , COMPLETIONDATE INTEGER NULL, TASK INTEGER, DURATION INTEGER );
def createRandomSessions():
    min_date = datetime(2016, 6, 6)
    max_date = datetime(2016, 8, 6)
    duration = 25 * 60  * 1000 # 25 minutes in millisec format

    all_session_values = []
    for id in range(1,31):
        timestamp = datetimeToEpocMillisec(randomDateTime(min_date, max_date))
        task_id = 1 if id & 1 == 1 else 2
        all_session_values.append([id, timestamp, task_id, duration])
    value_strings =  map(mkValueStr, all_session_values)
    return '\n'.join(map(lambda x: mkInsert(SESSIONS_TABLE, x), value_strings))



def main():
    file_obj = open('test_db.sql', 'w')
    file_obj.write(deleteTableEntries())
    file_obj.write(createNewTasks())
    file_obj.write(createRandomSessions())
    file_obj.close()

main()
    
