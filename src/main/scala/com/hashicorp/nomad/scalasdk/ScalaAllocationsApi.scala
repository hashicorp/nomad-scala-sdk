package com.hashicorp.nomad.scalasdk

import scala.collection.JavaConverters._
import com.hashicorp.nomad.apimodel.{AllocStopResponse, Allocation, AllocationListStub}
import com.hashicorp.nomad.javasdk._

/** API for querying for information about allocations,
  * exposing the functionality of the `/v1/allocations` and `/v1/allocation` endpoints of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param allocationsApi the underlying API from the Java SDK
  */
class ScalaAllocationsApi private[scalasdk](allocationsApi: AllocationsApi) {

  /** Queries an allocation in the active region.
    *
    * @param id the allocation ID to lookup
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/alloc.html `GET /v1/allocation/{ID}`]]
    */
  def info(id: String, options: Option[ScalaQueryOptions[Allocation]] = None): ServerQueryResponse[Allocation] =
    allocationsApi.info(id, options.asJava)

  /** Lists allocations in the active region.
    *
    * @param allocationIdPrefix an even-length prefix that, if given,
    *                           restricts the results to only allocations having an ID with this prefix
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/allocs.html `GET /v1/allocations`]]
    */
  def list(
      allocationIdPrefix: Option[String] = None,
      options: Option[ScalaQueryOptions[Seq[AllocationListStub]]] = None
  ): ServerQueryResponse[Seq[AllocationListStub]] =
    allocationsApi.list(allocationIdPrefix.orNull, options.asJava((_: java.util.List[AllocationListStub]).asScala))
      .map(_.asScala)

  /** Stops and reschedules an allocation.
    *
    * @param id the allocation ID to stop
    * @return allocation stop response, including the id of the follow-up evaluation for any rescheduled alloc.
    * @see [[https://nomadproject.io/api-docs/allocations/#stop-allocation `PUT /v1/allocation/{ID}/stop`]]
    */
  def stop(id: String): ServerResponse[AllocStopResponse] = allocationsApi.stop(id)

  /** Sends a signal to an allocation or task.
    *
    * @param id the allocation ID to stop
    * @param signal the signal to send
    * @param task the name of the task, required if the task group has more than one task
    * @see [[https://nomadproject.io/api-docs/allocations/#signal-allocation `PUT /v1/allocation/{ID}/signal`]]
    */
  def signal(id: String, signal: String, task: Option[String] = None): Unit =
    allocationsApi.signal(id, signal, task.orNull)

}
