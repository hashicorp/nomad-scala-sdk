package com.hashicorp.nomad.scalasdk

import com.hashicorp.nomad.apimodel.Node
import com.hashicorp.nomad.javasdk.{ NomadApiClient, NomadApiConfiguration }
import org.apache.http.HttpHost

/** Companion object for NomadScalaApi with helpful apply methods. */
object NomadScalaApi {

  /** Creates a Scala API client.
    *
    * @param address the scheme (http or https), host and port of the agent to connect to,
    *                e.g. "http://localhost:4646"
    **/
  def apply(address: String): NomadScalaApi =
    new NomadScalaApi(new NomadApiClient(address))

  /** Creates a Scala API client.
    *
    * @param address the scheme (http or https), host and port of the agent to connect to
    **/
  def apply(address: HttpHost): NomadScalaApi =
    new NomadScalaApi(new NomadApiClient(address))

  /** Returns a new NomadScalaApi.
    *
    * @param config the configuration for the new API instance
    */
  def apply(config: NomadApiConfiguration): NomadScalaApi =
    new NomadScalaApi(new NomadApiClient(config))

}

/** An API for interacting with Nomad in Scala.
  *
  * The Scala API is a wrapper for the NomadApiClient from the Nomad Java SDK that provides more Scala friendly types
  * for convenient use from Scala.
  *
  * @param apiClient the underlying API client from the Nomad Java SDK
  */
class NomadScalaApi(val apiClient: NomadApiClient) {

  /** Returns the API's configuration */
  def config: NomadApiConfiguration =
    apiClient.getConfig

  /** Closes the underlying NomadApiClient's HTTP client. */
  def close(): Unit =
    apiClient.close()

  /** Returns an API for agent a cluster management. */
  def agent: ScalaAgentApi =
    new ScalaAgentApi(apiClient.getAgentApi)

  /** Returns an API for querying information about allocations. */
  def allocations: ScalaAllocationsApi =
    new ScalaAllocationsApi(apiClient.getAllocationsApi)

  /** Returns an API for interacting directly with a client node.
    *
    * @param node the client node to connect to
    */
  def client(node: Node): ScalaClientApi =
    new ScalaClientApi(apiClient.getClientApi(node))

  /** Returns an API for interacting directly with a client node.
    *
    * @param clientAddress the HTTP or HTTPS address of the client node
    */
  def client(clientAddress: HttpHost): ScalaClientApi =
    new ScalaClientApi(apiClient.getClientApi(clientAddress))

  /** Returns an API for interacting directly with a client node after looking up its address.
    *
    * @param nodeId the nodeId of the client node to connect to
    */
  def lookupClientApiByNodeId(nodeId: String): ScalaClientApi =
    new ScalaClientApi(apiClient.lookupClientApiByNodeId(nodeId))

  /** Returns an API for managing deployments. */
  def getDeploymentsApi: ScalaDeploymentsApi =
    new ScalaDeploymentsApi(apiClient.getDeploymentsApi)

  /** Returns an API for querying information about evaluations. */
  def evaluations: ScalaEvaluationsApi =
    new ScalaEvaluationsApi(apiClient.getEvaluationsApi)

  /** Returns an API for submitting and managing jobs. */
  def jobs: ScalaJobsApi =
    new ScalaJobsApi(apiClient.getJobsApi)

  /** Returns an API for querying information about the client nodes in the Nomad cluster. */
  def nodes: ScalaNodesApi =
    new ScalaNodesApi(apiClient.getNodesApi)

  /** Returns an API for operating the Nomad cluster. */
  def operatorApi: ScalaOperatorApi =
    new ScalaOperatorApi(apiClient.getOperatorApi)

  /** Returns an API for listing the regions in the Nomad cluster. */
  def regions: ScalaRegionsApi =
    new ScalaRegionsApi(apiClient.getRegionsApi)

  /** Returns an API for searching for items in Nomad cluster. */
  def search: ScalaSearchApi =
    new ScalaSearchApi(apiClient.getSearchApi)

  /** Returns an API for querying the status of the Nomad cluster. */
  def status: ScalaStatusApi =
    new ScalaStatusApi(apiClient.getStatusApi)

  /** Returns an API for performing system maintenance operations on the Nomad cluster. */
  def system: ScalaSystemApi =
    new ScalaSystemApi(apiClient.getSystemApi)

}
