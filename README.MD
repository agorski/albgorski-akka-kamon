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
 java -Djava.library.path=/Users/albert.gorski/projects-scala/albgorski-akka-kamon/sigar -javaagent:/Users/albert.gorski/devel/aspectjweaver/aspectjweaver-1.8.6.jar -jar target/scala-2.11/albgorski-akka-kamon.jar
```