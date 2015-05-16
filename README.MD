## Run the project
```
sbt clean run
```

## Build jar file and run the project
please replace */path/to/aspectjweaver.jar* with the full path path to the *aspectjweaver* 

```
sbt clean assembly
```

```
java -javaagent:/path/to/aspectjweaver.jar -jar target/scala-2.11/albgorski-akka-kamon.jar
```