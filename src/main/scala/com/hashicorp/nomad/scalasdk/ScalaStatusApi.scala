package com.hashicorp.nomad.scalasdk

import com.hashicorp.nomad.javasdk.{NomadResponse, StatusApi}
import scala.collection.JavaConverters._
import scala.collection.mutable
import scala.concurrent.Future

/** API for querying for information about the status of the cluster,
  * exposing the functionality of the `/v1/status/…` endpoints of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param statusApi the underlying API from the Java SDK
  * @see [[https://www.nomadproject.io/docs/http/status.html `/v1/status documentation`]]
  */
class ScalaStatusApi(statusApi: StatusApi) {

  /** Queries the address of the Raft leader in the given or active region.
    *
    * @param region the region to forward the request to
    * @see [[https://www.nomadproject.io/docs/http/status.html `GET /v1/status/peers`]]
    */
  def leader(region: Option[String] = None): NomadResponse[String] =
    statusApi.leader(region.orNull)

  /** List the addresses of the Raft peers in the active region.
    *
    * @param region the region to forward the request to
    * @see [[https://www.nomadproject.io/docs/http/status.html `GET /v1/status/peers`]]
    */
  def peers(region: Option[String] = None): NomadResponse[Seq[String]] =
    statusApi.peers(region.orNull)
      .map(_.asScala.toSeq)

}
