# Neo4j Connector

[![Build Status](https://travis-ci.com/modakanalytics/neo4j.almaren.svg?branch=master)](https://travis-ci.com/modakanalytics/neo4j.almaren)

```
libraryDependencies += "com.github.music-of-the-ainur" %% "neo4j-almaren" % "0.1.0-2.4"
```

Neo4j Connector was implemented using [https://github.com/neo4j-contrib/neo4j-spark-connector](https://github.com/neo4j-contrib/neo4j-spark-connector).
For more details check the following [link](https://github.com/neo4j-contrib/neo4j-spark-connector).

```
spark-shell --master "local[*]" --packages "com.github.music-of-the-ainur:almaren-framework_2.11:0.5.0-2.4,com.github.music-of-the-ainur:neo4j-almaren_2.11:0.1.0-2.4"
```

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
| Label |  Label is a name or identifier to a Node or a Relationship in Neo4j Database. |
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

