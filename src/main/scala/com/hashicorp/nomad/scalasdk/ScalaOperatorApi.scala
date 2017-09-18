package com.hashicorp.nomad.scalasdk

import com.hashicorp.nomad.apimodel.RaftConfiguration
import com.hashicorp.nomad.javasdk._

/** API for operating a Nomad cluster,
  * exposing the [[https://www.nomadproject.io/api/operator.html operator]] functionality of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param operatorApi the underlying Java SDK OperatorApi
  */
class ScalaOperatorApi(operatorApi: OperatorApi) {

  /** Gets the cluster's Raft configuration.
    *
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/operator.html#read-raft-configuration `GET /v1/operator/raft/configuration`]]
    */
  def raftGetConfiguration(options: Option[ScalaQueryOptions[RaftConfiguration]] = None): NomadResponse[RaftConfiguration] =
    operatorApi.raftGetConfiguration(options.asJava)

  /**
    * Removes a raft peer from the cluster.
    *
    * @param address ip:port address of the peer to remove
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/operator.html#remove-raft-peer `DELETE /v1/operator/raft/peer`]]
    */
  def raftRemovePeerByAddress(address: String, options: Option[WriteOptions] = None): NomadResponse[Void] =
    operatorApi.raftRemovePeerByAddress(address, options.orNull)

}
