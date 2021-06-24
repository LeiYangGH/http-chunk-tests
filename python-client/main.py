import requests

resp = requests.get('http://10.71.20.181:8080', stream=True)
if resp.status_code == 200:
    for part in resp.iter_content(chunk_size=100000):
        print(len(part))
# 10
# 100
# 1000
# 10000
