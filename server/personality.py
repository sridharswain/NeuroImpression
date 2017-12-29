import sys
import ast
# MYERS-BRIGGS PREDICTOR, WHEN BIG FIVE PERSONALITY TRAITS GIVEN AS INPUT
Financial_personality = []
#Give this traits matrix as input
dict_personality = {}


def personality_analyzer(traits_matrix):
    Personality_word_list = []
    traits_matrix = list(traits_matrix)
    e = traits_matrix[0]
    o = traits_matrix[1]
    a = traits_matrix[2]
    c = traits_matrix[3]
    n = traits_matrix[4]


    # Still tuning these numbers...
    if e > .45:
        Personality_word_list.append('E')
        Financial_personality.append('HE')
    elif e <= .45:
        Personality_word_list.append('I')
        Financial_personality.append('LE')
    if o > .56:
        Personality_word_list.append('N')
        Financial_personality.append('HO')
    elif o <= .56:
        Personality_word_list.append('S')
        Financial_personality.append('LO')
    if a > .42:
        Personality_word_list.append('F')
        Financial_personality.append('HA')
    elif a <= .42:
        Personality_word_list.append('T')
        Financial_personality.append('LA')
    if c > .42:
        Personality_word_list.append('J')
        Financial_personality.append('HC')
    elif c <= .42:
        Personality_word_list.append('P')
        Financial_personality.append('LC')
    if n > .33:
        Personality_word_list.append('T')
        Financial_personality.append('HN')
    elif n <= .33:
        Personality_word_list.append('A')
        Financial_personality.append('LN')
    return Personality_word_list


wasim = personality_analyzer([0.34433828,0.685737772,0.522553165,0.538556575,0.475304089])
ajit = personality_analyzer([0.931221184,0.334744764,0.900048517,0.285480923,0.51951473])
upasna  =  personality_analyzer([0.1016266,0.421602763,0.697764984,0.419510717,0.423365467])
satyendra = personality_analyzer([0.402589681,0.645149953,0.867484824,0.725706679,0.964437597])
seema = personality_analyzer([0.644196007,0.313369846,0.561761976,0.489636422,0.555638728])
sanjay = personality_analyzer([0.271994242,0.074831671,0.606832355,0.696789019,0.119037044])
bipin = personality_analyzer([0.71489501,0.795371271,0.005279465,0.978876803,0.324406025])


list_agents = [wasim,ajit,upasna,satyendra,seema,sanjay,bipin]
names_agents = ['Wasim','Ajit','Abhishek','Satyendra','Sanjay','Seema','Bipin']


if __name__ == '__main__':
	if(len(sys.argv)==2):
		personality_analyzer(ast.literal_eval(sys.argv[1]))

	else:
		print("Wrong Input")



#MAPPING FINANCIAL PERSONALITY TRAITS
import numpy as np
def map_to_financial(Financial_personality):
    list_person = [['LE','HO','LA','HC','HN'], ['LE','LO','HA','LC','LN'], ['LE','HO','LA','LC','HN'], ['HE','HO','HA','LC','LN'], ['HE','LO','HA','LC','LN']]
    person = ['The Fitbit Financier', 'Ostrich', 'The Hoarder', 'The Cash Splasher', 'The Anxious Investor']
    index_list = []
    for i in list_person:
        x = len((set(i) & set(Financial_personality)))
        index_list.append(x)
    ind = np.argmax(index_list)
    return person[ind]


def map_to_agent(client_traits):
    client_trait = personality_analyzer(client_traits)
    index_list = []
    for i in list_agents:
        x = len((set(i) & set(client_trait)))
        index_list.append(x)
    ind = np.argmax(index_list)
    return names_agents[ind]

fin_per = map_to_financial(Financial_personality)

dict_personality["The Fitbit Financier"] = ["Level Term Insurance Plans", "Endowment Insurance Plans"]
dict_personality["Ostrich"] = ["Decreasing Level Term Insurance Plans", "ULIPS Insurance Plans"]
dict_personality["The Hoarder"] = ["Level Term Insurance Plans", "Whole Life Insurance Plans", "Endowment Policies"]
dict_personality["The Cash Splasher"] = ["Universal Life Insurance Plans", "Decreasing Level Term Insurance Plans"]
dict_personality["The Anxious Investor"] = ["Variable Universal Insurance Plans", "ULIPS Insurance Plans"]


print(map_to_financial(Financial_personality),':', map_to_agent(ast.literal_eval(sys.argv[1])),':', ';'.join(dict_personality[fin_per]))
