curl -s -X POST "http://localhost:4567/create?first_name=Leia&last_name=Organa&balance=20000"
curl -s -X POST "http://localhost:4567/create?first_name=Han&last_name=Solo&balance=2000"
curl -s -X POST "http://localhost:4567/create?first_name=Chu&last_name=Baka&balance=2000"
curl -s -X GET http://localhost:4567/sum
curl -s -X PUT "http://localhost:4567/transfer?from=1&to=2&amount=500"
curl -s -X PUT "http://localhost:4567/transfer?from=2&to=3&amount=500"
curl -s -X PUT "http://localhost:4567/transfer?from=3&to=1&amount=500"
curl -s -X GET http://localhost:4567/users
curl -s -X GET http://localhost:4567/sum