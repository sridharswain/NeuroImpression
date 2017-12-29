import angus.client
from pprint import pprint
import sys

x=sys.argv[1]
conn = angus.client.connect()
service = conn.services.get_service('age_and_gender_estimation', version=1)
job = service.process({'image': open(x, 'rb')})
pprint(job.result)
