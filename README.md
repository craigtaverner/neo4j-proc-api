# Sample / Template for creating alternative API for procedures

The existing CoreAPI in the form of GraphDatabaseService and what is accessible from that
has many design flaws as a result of its extreme age. We would like to find a better
replacement for it, both for embedded use, unmanaged extensions, plugins and procedures.
The use in procedures is particularly interesting because that is the API we would like to
focus on going forwards, with some or all other cases being deprecated.

The reasons why the Core API is not idea are discussed extensively in other places (ref.?).
Here we want to focus on describing how to create and inject an alternative API into
Neo4j 3.x. This technique should even work with Neo4j 3.0.

## Make your own CoreAPI

Fork this project, rename AmanziProcedures to whatever you plan to use for test procedures,
and then edit ProcAPI and your procedures classes to contain the API content of your choice. 

Edit the pom.xml to contain the project name of your choice, then run `mvn clean package`
to make the plugin jar. Copy the jar from target/ to the neo4j/plugins folder and restart
neo4j. Your procedures should be there, try `CALL dbms.procedures` to see them, or try
run them.

## Neo4j Version Dependencies

Note that writing a custom API means that you might find yourself accessing lower level unsupported
Neo4j APIs. This also means that your project will need to be updated for each release of
Neo4j. There is no guarantee of any backwards support of the internal APIs that you use.

Only write your own API based on unsupported APIs if you really have a string need for it
or are dealing with some specific limitation of the current supported APIs.
