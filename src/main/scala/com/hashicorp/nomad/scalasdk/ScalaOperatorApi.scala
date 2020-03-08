package com.hashicorp.nomad.scalasdk

import java.lang
import java.math.BigInteger

import com.hashicorp.nomad.apimodel._
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
  def raftRemovePeerByAddress(address: String, options: Option[WriteOptions] = None): NomadResponse[Unit] =
    operatorApi.raftRemovePeerByAddress(address, options.orNull)
      .map((_: Void) => ())

  /** Gets the health of the autopilot status.
    *
    * @param options options controlling how the request is performed
    * @see <a href="https://www.nomadproject.io/api/operator.html#read-health">{@code GET /v1/operator/autopilot/health}</a>
    */
  def getHealth(options: Option[ScalaQueryOptions[OperatorHealthReply]] = None): NomadResponse[OperatorHealthReply] =
    operatorApi.getHealth(options.asJava)

  /** Gets the autopilot configuration.
    *
    * @param options options controlling how the request is performed
    * @see <a href="https://www.nomadproject.io/api/operator.html#read-autopilot-configuration">{@code GET /v1/operator/autopilot/configuration}</a>
    */
  def getAutopilotConfiguration(options: Option[ScalaQueryOptions[AutopilotConfiguration]] = None): NomadResponse[AutopilotConfiguration] =
    operatorApi.getAutopilotConfiguration(options.asJava)

  /** Updates the autopilot configuration.
    *
    * @param autopilotConfiguration the desired autopilot configuration
    * @param options options controlling how the request is performed
    * @see <a href="https://www.nomadproject.io/api/operator.html#update-autopilot-configuration">{@code PUT /v1/operator/autopilot/configuration}</a>
    */
  def updateAutopilotConfiguration(autopilotConfiguration: AutopilotConfiguration,
                                   options: Option[WriteOptions] = None): NomadResponse[lang.Boolean] =
    operatorApi.updateAutopilotConfiguration(autopilotConfiguration, options.orNull)

  /** Gets the scheduler configuration.
    *
    * @param options options controlling how the request is performed
    * @see <a href="https://www.nomadproject.io/api/operator.html#read-scheduler-configuration">{@code GET /v1/operator/scheduler/configuration}</a>
    */
  def getSchedulerConfiguration(options: Option[ScalaQueryOptions[SchedulerConfigurationResponse]] = None): NomadResponse[SchedulerConfigurationResponse] =
    operatorApi.getSchedulerConfiguration(options.asJava)

  /** Updates the scheduler configuration.
    *
    * @param schedulerConfiguration the desired scheduler configuration
    * @param options options controlling how the request is performed
    * @param cas if not null, use check-and-set semantics on the update
    * @see <a href="https://www.nomadproject.io/api/operator.html#update-scheduler-configuration">{@code PUT /v1/operator/scheduler/configuration}</a>
    */
  def updateSchedulerConfiguration(schedulerConfiguration: SchedulerConfiguration,
                                   options: Option[WriteOptions] = None,
                                   cas: Option[BigInteger] = None ): NomadResponse[SchedulerSetConfigurationResponse] =
    operatorApi.updateSchedulerConfiguration(schedulerConfiguration, options.orNull, cas.orNull)

}
