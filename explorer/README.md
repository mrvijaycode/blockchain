


To start the explorer
docker-compose up

To stop the eplorer
docker-compose down -v

to check the network 
docker network ls

docker network inspect fabric_test



Errors may get:

Error :  ['Default client peer is down and no channel details available database']

The below things need to check
1. config.json file configured with correct network?
2. connection-profile certificates are correctly mapped?
3. Is certificates using old network? if you restart the network it would change the network certificates aswell so need to copy fresh organizarions folder.