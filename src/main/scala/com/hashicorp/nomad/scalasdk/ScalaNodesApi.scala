package com.hashicorp.nomad.scalasdk

import scala.collection.JavaConverters._

import com.hashicorp.nomad.apimodel.{ AllocationListStub, Node, NodeListStub }
import com.hashicorp.nomad.javasdk._

/** API for querying for information about client nodes,
  * exposing the functionality of the `/v1/nodes` and `/v1/node` endpoints of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param nodesApi the underlying API from the Java SDK
  */
class ScalaNodesApi(nodesApi: NodesApi) {

  /** List the allocations belonging to a nodes in the active region.
    *
    * @param nodeId  ID of the node to list allocations for
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/node.html `GET /v1/node/{ID}/allocations`]]
    */
  def allocations(nodeId: String, options: Option[ScalaQueryOptions[Seq[AllocationListStub]]] = None): ServerQueryResponse[Seq[AllocationListStub]] =
    nodesApi.allocations(nodeId, options.asJava(_.asScala))
      .map(_.asScala)

  /** Creates a new evaluation for a node.
    *
    * @param nodeId  ID of the node to evaluate
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/node.html `PUT /v1/node/<ID>/evaluate`]]
    */
  def forceEvaluate(nodeId: String, options: Option[WriteOptions] = None): ServerResponse[Unit] =
    nodesApi.forceEvaluate(nodeId, options.orNull)
      .map((_: Void) => ())

  /** Queries a node in the active region.
    *
    * @param nodeId  ID of the node to query
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/node.html `GET /v1/node/{ID}`]]
    */
  def info(nodeId: String, options: Option[ScalaQueryOptions[Node]] = None): ServerQueryResponse[Node] =
    nodesApi.info(nodeId, options.asJava)

  /** Lists client nodes in the active region.
    *
    * @param nodeIdPrefix an even-length prefix that, if given,
    *                     restricts the results to only nodes having an ID with this prefix
    * @param options      options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/nodes.html `GET /v1/nodes`]]
    */
  def list(
      nodeIdPrefix: Option[String] = None,
      options: Option[ScalaQueryOptions[Seq[NodeListStub]]] = None): ServerQueryResponse[Seq[NodeListStub]] =
    nodesApi.list(nodeIdPrefix.orNull, options.asJava(_.asScala))
      .map(_.asScala)

  /** Toggles drain mode on or off on a node in the active region.
    * <p>
    * When drain mode is enabled, no further allocations will be assigned and existing allocations will be migrated.
    *
    * @param nodeId  ID of the node to control
    * @param enabled drain mode is turned on when this is true, and off when false.
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/node.html#put-post `PUT /v1/node/{ID}/drain`]]
    */
  def toggleDrain(nodeId: String, enabled: Boolean, options: Option[WriteOptions] = None): EvaluationResponse =
    nodesApi.toggleDrain(nodeId, enabled, options.orNull)

}
