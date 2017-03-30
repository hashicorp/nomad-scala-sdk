package com.hashicorp.nomad.scalasdk

import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.concurrent.Future

import com.hashicorp.nomad.javasdk.{NomadResponse, RegionsApi}

/** API for querying for information about regions,
  * exposing the functionality of the `/v1/regions` endpoint of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param regionsApi the underlying API from the Java SDK
  */
class ScalaRegionsApi(regionsApi: RegionsApi) {

  /** List the names of the known regions in the cluster.
    *
    * @see [[https://www.nomadproject.io/docs/http/regions.html `GET /v1/regions`]]
    */
  def list(): NomadResponse[Seq[String]] =
    regionsApi.list()
      .map(_.asScala)

}
