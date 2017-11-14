package com.hashicorp.nomad.scalasdk

import scala.collection.JavaConverters._

import com.hashicorp.nomad.apimodel.{ AgentHealthResponse, AgentSelf, ServerMembers }
import com.hashicorp.nomad.javasdk.{ AgentApi, NomadResponse }

/** Scala API for Nomad agent and cluster management,
  * exposing the functionality of the `/v1/agent/…` endpoints of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param agentApi the underlying Java SDK AgentApi
  */
class ScalaAgentApi private[scalasdk](agentApi: AgentApi) {

  /** Performs a basic healthcheck.
    *
    * @see [[https://www.nomadproject.io/api/agent.html#health `GET /v1/agent/health`]]
    */
  def health(): NomadResponse[AgentHealthResponse] =
    agentApi.health()

  /** Queries for information about the agent we are connected to.
    *
    * @see [[https://www.nomadproject.io/docs/http/agent-self.html `GET /v1/agent/self`]]
    */
  def self(): NomadResponse[AgentSelf] =
    agentApi.self()

  /** Queries for the known peers in the gossip pool.
    *
    * @see [[https://www.nomadproject.io/docs/http/agent-members.html `GET /v1/agent/members`]]
    */
  def members(): NomadResponse[ServerMembers] =
    agentApi.members()

  /** Forces a member of the gossip pool from the "failed" state into the "left" state.
    *
    * @param nodeName the name of the node to force out of the pool
    * @see [[https://www.nomadproject.io/docs/http/agent-force-leave.html `PUT /v1/agent/force-leave`]]
    */
  def forceLeave(nodeName: String): NomadResponse[Unit] =
    agentApi.forceLeave(nodeName)
      .map((_: Void) => ())

  /** Queries an agent in client mode for its list of known servers.
    *
    * @see [[https://www.nomadproject.io/docs/http/agent-servers.html#get `GET /v1/agent/servers`]]
    */
  def servers(): NomadResponse[Seq[String]] =
    agentApi.servers()
      .map(_.asScala: Seq[String])

  /** Updates the list of known servers to the given addresses, replacing all previous addresses.
    *
    * @param addresses the server addresses
    * @see [[https://www.nomadproject.io/docs/http/agent-servers.html#put-post `PUT /v1/agent/servers`]]
    */
  def setServers(addresses: Iterable[String]): NomadResponse[Unit] =
    agentApi.setServers(addresses.asJava)
      .map((_: Void) => ())

  /** Causes the agent to join a cluster by joining the gossip pool at one of the given addresses.
    *
    * @param addresses the addresses to try joining
    * @see [[https://www.nomadproject.io/docs/http/agent-join.html `PUT /v1/agent/join`]]
    */
  def join(addresses: Iterable[String]): NomadResponse[Unit] =
    agentApi.setServers(addresses.asJava)
      .map((_: Void) => ())
}
