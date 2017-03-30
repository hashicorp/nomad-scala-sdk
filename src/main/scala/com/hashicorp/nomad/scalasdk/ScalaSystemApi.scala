package com.hashicorp.nomad.scalasdk

import com.hashicorp.nomad.javasdk.{NomadResponse, SystemApi, WriteOptions}

import scala.concurrent.Future

/** API for performing system maintenance that shouldn't be necessary for most users,
  * exposing the functionality of the
  * [[https://www.nomadproject.io/docs/http/system.html `/v1/system/…` endpoints]]
  * of the [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param systemApi the underlying API from the Java SDK
  */
class ScalaSystemApi(systemApi: SystemApi) {

  /** Initiates garbage collection of jobs, evals, allocations and nodes
    * in the active region.
    *
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/system.html `PUT /v1/system/gc`]]
    */
  def garbageCollect(options: Option[WriteOptions] = None): NomadResponse[Unit] =
    systemApi.garbageCollect(options.orNull)
      .map((_: Void) => ())

  /** Reconciles the summaries of all jobs in the active region.
    *
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/system.html `PUT /v1/system/reconcile/summaries`]]
    */
  def reconcileSummaries(options: Option[WriteOptions] = None): NomadResponse[Unit] =
    systemApi.reconcileSummaries(options.orNull)
      .map((_: Void) => ())

}
