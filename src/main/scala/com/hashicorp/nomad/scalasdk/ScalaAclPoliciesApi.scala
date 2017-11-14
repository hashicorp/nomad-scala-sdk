package com.hashicorp.nomad.scalasdk

import scala.collection.JavaConverters._

import com.hashicorp.nomad.apimodel.{ AclPolicy, AclPolicyListStub }
import com.hashicorp.nomad.javasdk._

/**
  * API for managing ACL policies,
  * exposing the [[https://www.nomadproject.io/api/acl-policies.html ACL policies]] functionality of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param aclPoliciesApi the underlying Java SDK AclPoliciesApi
  */
class ScalaAclPoliciesApi(aclPoliciesApi: AclPoliciesApi) {

  /**
    * Deletes an ACL policy.
    *
    * @param policyName name of the policy to delete
    * @param options    options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/acl-policy.html#delete-policy `DELETE /v1/acl/policy/:name`]]
    */
  def delete(policyName: String, options: Option[WriteOptions] = None): ServerResponse[Unit] =
    aclPoliciesApi.delete(policyName, options.orNull)
      .map((_: Void) => ())

  /**
    * Retrieves an ACL policy.
    *
    * @param name    name of the policy.
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/acl-policies.html#read-policy `GET /v1/acl/policy/:name`]]
    */
  def info(name: String, options: Option[ScalaQueryOptions[AclPolicy]] = None): ServerQueryResponse[AclPolicy] =
    aclPoliciesApi.info(name, options.asJava)

  /**
    * Lists ACL policies.
    *
    * @param namePrefix a name prefix that, if given,
    *                   restricts the results to only policies having a name with this prefix
    * @param options    options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/acl-policies.html#list-policies `GET /v1/acl/policies`]]
    */
  def list(namePrefix: Option[String] = None, options: Option[ScalaQueryOptions[Seq[AclPolicyListStub]]] = None): ServerQueryResponse[Seq[AclPolicyListStub]] =
    aclPoliciesApi.list(namePrefix.orNull, options.asJava(_.asScala))
      .map(_.asScala)

  /**
    * Creates or updates an ACL policy.
    *
    * @param policy  the ACL policy
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/acl-policies.html#create-or-update-policy `PUT /v1/acl/policy/:name`]]
    */
  def upsert(policy: AclPolicy, options: Option[WriteOptions] = None): ServerResponse[Unit] =
    aclPoliciesApi.upsert(policy, options.orNull)
      .map((_: Void) => ())

}
