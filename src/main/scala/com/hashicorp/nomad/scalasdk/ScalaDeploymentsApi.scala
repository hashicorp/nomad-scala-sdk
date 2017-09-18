package com.hashicorp.nomad.scalasdk

import scala.collection.JavaConverters._

import com.hashicorp.nomad.apimodel.{ AllocationListStub, Deployment, DeploymentUpdateResponse }
import com.hashicorp.nomad.javasdk._

/** API for querying for information about deployments,
  * exposing the [[https://www.nomadproject.io/api/deployments.html deployments]] functionality of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param deploymentsApi the underlying Java SDK DeploymentsApi
  */
class ScalaDeploymentsApi(deploymentsApi: DeploymentsApi) {

  /** Lists deployments in the active region.
    *
    * @param deploymentIdPrefix an even-length prefix that, if given,
    *                           restricts the results to only deployments having an ID with this prefix
    * @param options            options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/deployments.html#list-deployments `GET /v1/deployments`]]
    */
  def list(deploymentIdPrefix: Option[String] = None, options: Option[ScalaQueryOptions[Seq[Deployment]]] = None): ServerQueryResponse[Seq[Deployment]] =
    deploymentsApi.list(deploymentIdPrefix.orNull, options.asJava(_.asScala))
      .map(_.asScala)


  /** Queries a deployment in the active region.
    *
    * @param deploymentId ID of the deployment to lookup
    * @param options      options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/deployments.html#read-deployment `GET /v1/deployment/{ID}`]]
    */
  @throws[NomadException]
  def info(deploymentId: String, options: Option[ScalaQueryOptions[Deployment]]): ServerQueryResponse[Deployment] =
  deploymentsApi.info(deploymentId, options.asJava)

  /** Lists the allocations belonging to a deployment in the active region.
    *
    * @param deploymentId the ID of the deployment to list allocations for
    * @param options      options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/deployments.html#list-allocations-for-deployment `GET /v1/deployment/<ID>/allocations`]]
    */
  def allocations(deploymentId: String, options: Option[ScalaQueryOptions[Seq[AllocationListStub]]]): ServerQueryResponse[Seq[AllocationListStub]] =
    deploymentsApi.allocations(deploymentId, options.asJava(_.asScala))
      .map(_.asScala)

  /** Fails a deployment in the active region.
    *
    * @param deploymentId the ID of the deployment to list allocations for
    * @param options      options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/deployments.html#fail-deployment `PUT /v1/deployment/fail/<ID>`]]
    */
  def fail(deploymentId: String, options: Option[WriteOptions]): ServerResponse[DeploymentUpdateResponse] =
    deploymentsApi.fail(deploymentId, options.orNull)

  /** Pauses or un-pauses a deployment in the active region.
    *
    * @param deploymentId the ID of the deployment to list allocations for
    * @param pause        true if the deployment should be paused, false if it should be un-paused
    * @param options      options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/deployments.html#pause-deployment `PUT /v1/deployment/pause/<ID>`]]
    */
  def pause(deploymentId: String, pause: Boolean, options: Option[WriteOptions]): ServerResponse[DeploymentUpdateResponse] =
    deploymentsApi.pause(deploymentId: String, pause, options.orNull)

  /** Promotes the canaries in the provided groups of a deployment in the active region.
    *
    * @param deploymentId the ID of the deployment to list allocations for
    * @param groups       when specified, only canaries in these groups will be promoted
    * @param options      options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/deployments.html#promote-deployment `PUT /v1/deployment/promote/<ID>`]]
    */
  def promote(deploymentId: String, groups: Option[Seq[String]] = None, options: Option[WriteOptions] = None): ServerResponse[DeploymentUpdateResponse] =
    groups match {
      case None => deploymentsApi.promoteAll(deploymentId, options.orNull)
      case Some(groups) => deploymentsApi.promoteGroups(deploymentId, groups.asJava, options.orNull)
    }

  /**
    * Sets the health of allocations that are part of a deployment.
    *
    * @param deploymentId the ID of the deployment to list allocations for
    * @param healthy      ids of allocations to be set healthy
    * @param unhealthy    ids of allocations to be set unhealthy
    * @param options      options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/deployments.html#set-allocation-health-in-deployment `PUT /v1/deployment/allocation-health/<ID>`]]
    */
  def setAllocHealth(
      deploymentId: String,
      healthy: Seq[String],
      unhealthy: Seq[String],
      options: Option[WriteOptions] = None
  ): ServerResponse[DeploymentUpdateResponse] =
    deploymentsApi.setAllocHealth(deploymentId, healthy.asJava, unhealthy.asJava, options.orNull)

}
