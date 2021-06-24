# http-chunk-tests
a python http chunk server, a java http client, and a python http client to test http-chunks.

# Run server
  ```bash
  cd python-server
  python main.py
  ```

  Output:
   ```bash
   Starting server, use <Ctrl-C> to stop
   ```

# Run java client

  ```bash
  cd java-apache-hc-client
  mvn package
  java -jar target/chunkedclient-1.0-SNAPSHOT-jar-with-dependencies.jar
  ```

  Output:
   ```bash
    10
    100
    1000
    8186
    1814
    -1
   ```
 
# Run python client

  ```bash
  cd python-client
  python main.py
  ```

  Output:
   ```bash
    10
    100
    1000
    10000
   ```