package com.github.music.of.the.ainur.almaren.neo4j

import org.apache.spark.sql.{DataFrame,SaveMode}
import com.github.music.of.the.ainur.almaren.Tree
import com.github.music.of.the.ainur.almaren.builder.Core
import com.github.music.of.the.ainur.almaren.state.core.{Target,Source}

private[almaren] case class SourceNeo4j(url: String, options:Map[String,String] = Map()) extends Source {
  def source(df: DataFrame): DataFrame = {
    logger.info(s"url:{$url}")
    df.sparkSession.read.format("org.neo4j.spark.DataSource")
      .option("url", url)
      .options(options)
      .load()
  }
}

private[almaren] case class TargetNeo4j(url: String, options:Map[String,String] = Map(), saveMode:SaveMode) extends Target {
  def target(df: DataFrame): DataFrame = {
    logger.info(s"url:{$url}, saveMode:{$saveMode}")
    df.write.format("org.neo4j.spark.DataSource")
      .option("url", url)
      .options(options)
      .mode(saveMode)
      .save
    df
  }
}

private[almaren] trait Neo4jConnector extends Core {
  def targetNeo4j(url: String, options:Map[String,String] = Map(), saveMode:SaveMode = SaveMode.ErrorIfExists): Option[Tree] =
     TargetNeo4j(url,options,saveMode)

  def sourceNeo4j(url: String , options:Map[String,String] = Map()): Option[Tree] =
    SourceNeo4j(url,options)
}

object Neo4j {
  implicit class Neo4jImplicit(val container: Option[Tree]) extends Neo4jConnector
}
