


To start the explorer
```
docker-compose up
```

To stop the explorer
```
docker-compose down -v
```

To check the network 
```
docker network ls
```

To inspect the network added
```
docker network inspect fabric_test
```

To add to gitignore
```
git rm -r --cached <filename or folder>
```

Errors may get:

Error :  ['Default client peer is down and no channel details available database']

The below things need to check
1. config.json file configured with correct network?
2. connection-profile certificates are correctly mapped?
3. Is certificates using old network? if you restart the network it would change the network certificates as-well so need to copy fresh certificates from organizarions folder.
4. check channel name in connection-profile


The latest script added, it will run quickly
```
./explorerRun.sh
```