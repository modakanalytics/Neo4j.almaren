package com.github.music.of.the.ainur.almaren.neo4j

import org.apache.spark.sql.{DataFrame,SaveMode}
import com.github.music.of.the.ainur.almaren.Tree
import com.github.music.of.the.ainur.almaren.builder.Core
import com.github.music.of.the.ainur.almaren.state.core.{Target,Source}

private[almaren] case class SourceNeo4j(url: String, user: Option[String], password: Option[String], options:Map[String,String] = Map()) extends Source {
  def source(df: DataFrame): DataFrame = {
    logger.info(s"url:{$url}, user:{$user}")

    val params = (user, password) match {
      case (Some(user), None) => options + ("authentication.basic.username" -> user)
      case (Some(user), Some(password)) => options + ("authentication.basic.username" -> user, "authentication.basic.password" -> password)
      case (_, _) => options
    }

    df.sparkSession.read.format("org.neo4j.spark.DataSource")
      .option("url", url)
      .options(params)
      .load()
  }
}

private[almaren] case class TargetNeo4j(url: String, user: Option[String], password: Option[String], options:Map[String,String] = Map(), saveMode:SaveMode) extends Target {
  def target(df: DataFrame): DataFrame = {
    logger.info(s"url:{$url}, user:{$user}, saveMode:{$saveMode}")

    val params = (user, password) match {
      case (Some(user), None) => options + ("authentication.basic.username" -> user)
      case (Some(user), Some(password)) => options + ("authentication.basic.username" -> user, "authentication.basic.password" -> password)
      case (_, _) => options
    }

    df.write.format("org.neo4j.spark.DataSource")
      .option("url", url)
      .options(params)
      .mode(saveMode)
      .save
    df
  }
}

private[almaren] trait Neo4jConnector extends Core {
  def targetNeo4j(url: String, user: Option[String] = None, password: Option[String] = None, options:Map[String,String] = Map(), saveMode:SaveMode = SaveMode.ErrorIfExists): Option[Tree] =
     TargetNeo4j(url,user,password,options,saveMode)

  def sourceNeo4j(url: String, user: Option[String] = None, password: Option[String] = None, options:Map[String,String] = Map()): Option[Tree] =
    SourceNeo4j(url,user,password,options)
}

object Neo4j {
  implicit class Neo4jImplicit(val container: Option[Tree]) extends Neo4jConnector
}
