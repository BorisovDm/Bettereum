# -*- coding: utf-8 -*-
import json
from django.views.decorators.csrf import csrf_exempt
from django.http import HttpResponse, JsonResponse


TEAMS_FOOTBALL = ['REAL', 'BARSELONA']
TEAMS_BASKETBALL = ['LAL', 'Miami heats']
TEAMS_CHESS = ['KASPAROV', 'KARLSEN']
TEAMS_BOX = ['KLICHKO', 'VALUEV']

CHALLENGES = {\
  'football': TEAMS_FOOTBALL,\
  'basketball': TEAMS_BASKETBALL,\
  'chess': TEAMS_CHESS,\
  'box': TEAMS_BOX\
}

@csrf_exempt
def auth(request):
  result = 10023
  return HttpResponse(str(result))


def get_chalenges(request):
  result=[]
  for item in CHALLENGES:
  	result.append(item)
  return JsonResponse(result, safe=False)

@csrf_exempt
def get_teams(request):
  s = request.body.decode('utf-8')[1:-1]
  print(s)
  return JsonResponse(CHALLENGES[s], safe=False)


