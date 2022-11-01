# Neo4j Connector

[![Build Status](https://github.com/modakanalytics/neo4j.almaren/actions/workflows/neo4j-almaren-githubactions.yml/badge.svg)](https://github.com/modakanalytics/neo4j.almaren/actions/workflows/neo4j-almaren-githubactions.yml)

Neo4j Connector was implemented using [https://github.com/neo4j-contrib/neo4j-spark-connector](https://github.com/neo4j-contrib/neo4j-spark-connector).
For more details check the following [link](https://github.com/neo4j-contrib/neo4j-spark-connector).

To add Neo4j connector dependency to your sbt build:

```
libraryDependencies += "com.github.music-of-the-ainur" %% "neo4j-almaren" % "0.1.3-2.4"
```

To run in spark-shell:

```
spark-shell --master "local[*]" --packages "com.github.music-of-the-ainur:almaren-framework_2.12:0.9.8-2.4,com.github.music-of-the-ainur:neo4j-almaren_2.12:0.1.3-2.4"
```

Neo4j Connector is available in [Maven Central](https://mvnrepository.com/artifact/com.github.music-of-the-ainur)
repository.

| version                    | Connector Artifact                                           |
|----------------------------|--------------------------------------------------------------|
| Spark 3.3.x and scala 2.13 | `com.github.music-of-the-ainur:neo4j-almaren_2.13:0.1.3-3.3` |
| Spark 3.3.x and scala 2.12 | `com.github.music-of-the-ainur:neo4j-almaren_2.12:0.1.3-3.3` |
| Spark 3.2.x and scala 2.12 | `com.github.music-of-the-ainur:neo4j-almaren_2.12:0.1.3-3.2` |
| Spark 3.1.x and scala 2.12 | `com.github.music-of-the-ainur:neo4j-almaren_2.12:0.1.3-3.1` |
| Spark 2.4.x and scala 2.12 | `com.github.music-of-the-ainur:neo4j-almaren_2.12:0.1.3-2.4` |
| Spark 2.4.x and scala 2.11 | `com.github.music-of-the-ainur:neo4j-almaren_2.11:0.1.3-2.4` |


## Source and Target

### Source 
#### Parameteres


| Parameters | Description|
|-----------------|--------------------|
|  url  | The url of the Neo4j instance to connect to  |
|----|----|
| Options | Description             |
|------------|-------------------------|
| authentication.basic.username     | Username to use for basic authentication type    |
|  authentication.basic.password  |Username to use for basic authentication type|
|authentication.custom.credentials|These are the credentials authenticating the principal|
| labels |  labels is a name or identifier to a Node or a Relationship in Neo4j Database. |
|Nodes |  Nodes are often used to represent entities. The simplest possible graph is a single node.|
|Relationship|  A relationship connects two nodes. Relationships organize nodes into structures, allowing a graph to resemble a list, a tree, a map, or a compound entity — any of which may be combined into yet more complex, richly inter-connected structures.|

For More Driver options check the following [link](https://neo4j.com/developer/spark/configuration/)

#### Example


```scala
import org.apache.spark.sql.{AnalysisException, Column, DataFrame, SaveMode, SparkSession}
import org.scalatest._
import org.apache.spark.sql.functions._
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import com.github.music.of.the.ainur.almaren.neo4j.Neo4j.Neo4jImplicit

  val almaren = Almaren("neo4j-almaren")


  val df = almaren.builder
    .sourceNeo4j(
      "bolt://localhost:7687",
      Some("neo4j"),
      Some("neo4j1234"),
      Map("labels" -> "Person")
    ).batch
```



### Target:
#### Parameters

| Parameters | Description|
|-----------------|--------------------|
|  url  | The url of the Neo4j instance to connect to  |
|--------|------|
| Options | Description      |
| authentication.basic.username      | Username to use for basic authentication type    |
|  authentication.basic.password |Username to use for basic authentication type|
|authentication.custom.credentials|These are the credentials authenticating the principal|
|SaveMode|SaveMode is used to specify the expected behavior of saving a DataFrame to a data source.|
|node.keys|Comma separated list of properties considered as node keys in case of you’re using SaveMode.Overwrite|


For More Driver options check the following [link](https://neo4j.com/developer/spark/configuration/)

#### Example

```scala
import org.apache.spark.sql.{AnalysisException, Column, DataFrame, SaveMode, SparkSession}
import org.scalatest._
import org.apache.spark.sql.functions._
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import com.github.music.of.the.ainur.almaren.neo4j.Neo4j.Neo4jImplicit

  val almaren = Almaren("neo4j-almaren")


  val df = almaren.builder
    .sourceSql("select * from person_info")
    .targetNeo4j(
      "bolt://localhost:7687",
      Some("neo4j"),
      Some("neo4j1234"),
      Map("labels" -> "Person"),
      SaveMode.ErrorIfExists
    ).batch

