# Sample / Template for creating alternative API for procedures

The existing CoreAPI in the form of GraphDatabaseService and what is accessible from that
has a design that dates back to the original embedded use of Neo4j. However, for the last
several years Neo4j has moved its primary deployment model to a server model, with remote
access via REST or BOLT (3.x), which Cypher as the primary query language. To satisfy
use cases where Cypher is still unsufficient, or not ideal, the old CoreAPI is accessible
from unmanaged extensions, plugins or Procedures (from Cypher). It is, however, not the ideal
choice of API for these use cases for several reasons:

* Does not support the Neo4j security model (since 2.2 a logged in user is needed for access,
and since 3.1 the enterprise edition supports read-only and other access levels).
* Provides too much fine grained access to graph internals, making it harder to support
larger changes in the underlying database.
* Generates too many objects under some circumstances, which can have a negative effect on
memory management in production systems, especially with clustering.

It would be good to look for better replacements for it, both for embedded use,
unmanaged extensions, plugins and procedures. The use in procedures is particularly
interesting because that is the API getting the most focus in future versions of Neo4j.
It is possible that the other API's might even be deprecated in future.

This template can be used by people interested in writing their own CoreAPI and injecting
that into procedures that use that API instead of the original. This is to facilitate
prototyping ideas that could be influential in the design of some future new CoreAPI.

## How to make your own CoreAPI

Fork this project, rename AmanziProcedures to whatever you plan to use for test procedures,
and then edit ProcAPI and your procedures classes to contain the API content of your choice. 

Edit the pom.xml to contain the project name of your choice, then run `mvn clean package`
to make the plugin jar. Copy the jar from target/ to the neo4j/plugins folder and restart
neo4j. Your procedures should be there, try `CALL dbms.procedures` to see them, or try
run them.

## Neo4j Version Dependencies

Note that writing a custom API means that you might find yourself accessing lower level
unsupported Neo4j APIs. This also means that your project will need to be updated for
each release of Neo4j.
There is no guarantee of any backwards support of the internal APIs that you use.

Only write your own API based on unsupported APIs if you really have a string need for it
or are dealing with some specific limitation of the current supported APIs.
