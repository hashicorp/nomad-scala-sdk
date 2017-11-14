package com.hashicorp.nomad.scalasdk

import scala.collection.JavaConverters._

import com.hashicorp.nomad.apimodel.{ SentinelPolicy, SentinelPolicyListStub }
import com.hashicorp.nomad.javasdk._

/** API for managing sentinel policies,
  * exposing the [[https://www.nomadproject.io/api/sentinel-policies.html sentinel policies]] functionality of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param sentinelPoliciesApi the underlying Java SDK SentinelPoliciesApi
  */
class ScalaSentinelPoliciesApi(sentinelPoliciesApi: SentinelPoliciesApi) {

  /**
    * Deletes an sentinel policy.
    *
    * @param policyName name of the policy to delete
    * @param options    options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/sentinel-policies.html#delete-policy `DELETE /v1/sentinel/policy/:name`]]
    */
  def delete(policyName: String, options: Option[WriteOptions] = None): ServerResponse[Unit] =
    sentinelPoliciesApi.delete(policyName, options.orNull)
      .map((_: Void) => ())

  /**
    * Retrieves an sentinel policy.
    *
    * @param name    name of the policy.
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/sentinel-policies.html#read-policy `GET /v1/sentinel/policy/:name`]]
    */
  def info(name: String, options: Option[ScalaQueryOptions[SentinelPolicy]] = None): ServerQueryResponse[SentinelPolicy] =
    sentinelPoliciesApi.info(name, options.asJava)

  /**
    * Lists sentinel policies.
    *
    * @param namePrefix a name prefix that, if given,
    *                   restricts the results to only policies having a name with this prefix
    * @param options    options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/sentinel-policies.html#list-policies `GET /v1/sentinel/policies`]]
    */
  def list(
      namePrefix: Option[String] = None,
      options: Option[ScalaQueryOptions[Seq[SentinelPolicyListStub]]] = None
  ): ServerResponse[Seq[SentinelPolicyListStub]] =
    sentinelPoliciesApi.list(namePrefix.orNull, options.asJava(_.asScala))
      .map(_.asScala)

  /**
    * Creates or updates an sentinel policy.
    *
    * @param policy  the policy
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/sentinel-policies.html#create-or-update-policy `PUT /v1/sentinel/policy/:name`]]
    */
  def upsert(policy: SentinelPolicy, options: Option[WriteOptions] = None): ServerResponse[Unit] =
    sentinelPoliciesApi.upsert(policy, options.orNull)
      .map((_: Void) => ())

}
