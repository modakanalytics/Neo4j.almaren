package com.github.music.of.the.ainur.almaren.neo4j

import org.apache.spark.sql.{AnalysisException, Column, DataFrame, SaveMode, SparkSession}
import org.scalatest._
import org.apache.spark.sql.functions._
import com.github.music.of.the.ainur.almaren.Almaren
import com.github.music.of.the.ainur.almaren.builder.Core.Implicit
import com.github.music.of.the.ainur.almaren.neo4j.Neo4j.Neo4jImplicit

class Test extends FunSuite with BeforeAndAfter {

  val almaren = Almaren("neo4j-almaren")

  val spark = almaren.spark
    .master("local[*]")
    .config("spark.ui.enabled", "false")
    .config("spark.sql.shuffle.partitions", "1")
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR")

  import spark.implicits._

  val df = Seq(
    ("John", "Smith", "London"),
    ("David", "Jones", "India"),
    ("Michael", "Johnson", "Indonesia"),
    ("Chris", "Lee", "Brazil"),
    ("Mike", "Brown", "Russia")
  ).toDF("first_name", "last_name", "country")

  df.createOrReplaceTempView("person_info")


  //write data fron noe4j
  val df1 = almaren.builder
    .sourceSql("select * from person_info")
    .targetNeo4j(
      "bolt://localhost:7687",
      Some("neo4j"),
      Some("neo4j1234"),
      Map("labels" -> "Person",
        "node.keys" -> "first_name, last_name, country"),
      SaveMode.Overwrite
    ).batch

  // Read Data From Neo4j
  val neo4jDf = almaren.builder
    .sourceNeo4j(
      "bolt://localhost:7687",
      Some("neo4j"),
      Some("neo4j1234"),
      Map("labels" -> "Person")
    ).batch


  test(df, neo4jDf.drop("<id>", "<lables>"), "Neo4j Read and Write")

  def test(df1: DataFrame, df2: DataFrame, name: String): Unit = {
    testCount(df1, df2, name)
    testCompare(df1, df2, name)
  }

  def testCount(df1: DataFrame, df2: DataFrame, name: String): Unit = {
    val count1 = df1.count()
    val count2 = df2.count()
    val count3 = spark.emptyDataFrame.count()
    test(s"Count Test:$name should match") {
      assert(count1 == count2)
    }
    test(s"Count Test:$name should not match") {
      assert(count1 != count3)
    }
  }

  // Doesn't support nested type and we don't need it :)
  def testCompare(df1: DataFrame, df2: DataFrame, name: String): Unit = {
    val diff = compare(df1, df2)
    test(s"Compare Test:$name should be zero") {
      assert(diff == 0)
    }
    test(s"Compare Test:$name, should not be able to join") {
      assertThrows[AnalysisException] {
        compare(df2, spark.emptyDataFrame)
      }
    }
  }

  private def compare(df1: DataFrame, df2: DataFrame): Long = {
    val diff = df1.as("df1").join(df2.as("df2"), joinExpression(df1), "leftanti")
    diff.show(false)
    diff.count
  }

  private def joinExpression(df1: DataFrame): Column =
    df1.schema.fields
      .map(field => col(s"df1.${field.name}") <=> col(s"df2.${field.name}"))
      .reduce((col1, col2) => col1.and(col2))

  after {
    spark.stop
  }
}


