package com.hashicorp.nomad.scalasdk

import scala.collection.JavaConverters._

import com.hashicorp.nomad.apimodel.{ AclToken, AclTokenListStub }
import com.hashicorp.nomad.javasdk._

/**
  * API for managing ACL tokens,
  * exposing the [[https://www.nomadproject.io/api/acl-tokens.html ACL tokens]] functionality of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param aclTokensApi the underlying Java SDK AclTokensApi
  */
class ScalaAclTokensApi(aclTokensApi: AclTokensApi) {

  /**
    * Bootstraps the ACL system and returns the initial management token.
    *
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/acl-tokens.html#bootstrap-token `PUT /v1/acl/bootstrap`]]
    */
  def bootstrap(options: Option[WriteOptions] = None): ServerResponse[AclToken] =
    aclTokensApi.bootstrap(options.orNull)

  /**
    * Creates an ACL token, returning a token with a server-assigned accessor ID and secret ID.
    *
    * @param token   a token with no accessor ID
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/acl-tokens.html#create-token `PUT /v1/acl/token`]]
    */
  def create(token: AclToken, options: Option[WriteOptions] = None): ServerResponse[AclToken] =
    aclTokensApi.create(token, options.orNull)

  /**
    * Deletes an ACL token.
    *
    * @param accessorId accessorId of the token to delete
    * @param options    options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/acl-token.html#delete-token `DELETE /v1/acl/token/:accessor_id`]]
    */
  def delete(accessorId: String, options: Option[WriteOptions] = None): ServerResponse[Void] =
    aclTokensApi.delete(accessorId, options.orNull)

  /**
    * Retrieves an ACL token.
    *
    * @param accessorId accessor ID of the token.
    * @param options    options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/acl-tokens.html#read-token `GET /v1/acl/token/:accessor_id`]]
    */
  def info(accessorId: String, options: Option[ScalaQueryOptions[AclToken]] = None): ServerQueryResponse[AclToken] =
    aclTokensApi.info(accessorId, options.asJava)

  /**
    * Lists ACL tokens.
    *
    * @param accessorIdPrefix an even-length accessor ID prefix that, if given,
    *                         restricts the results to only tokens having an accessor ID with this prefix
    * @param options          options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/acl-tokens.html#list-tokens `GET /v1/acl/tokens`]]
    */
  def list(accessorIdPrefix: Option[String], options: Option[ScalaQueryOptions[Seq[AclTokenListStub]]] = None): ServerQueryResponse[Seq[AclTokenListStub]] =
    aclTokensApi.list(accessorIdPrefix.orNull, options.asJava(_.asScala))
      .map(_.asScala)

  /**
    * Retrieves the ACL token currently being used.
    *
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/acl-tokens.html#read-self-token `GET /v1/acl/token/self`]]
    */
  def self(options: Option[ScalaQueryOptions[AclToken]] = None): ServerQueryResponse[AclToken] =
    aclTokensApi.self(options.asJava)

  /**
    * Updates an ACL token.
    *
    * @param token   a token with with an accessor ID
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/acl-tokens.html#update-token `PUT /v1/acl/token/:accessor_id`]]
    */
  def update(token: AclToken, options: Option[WriteOptions] = None): ServerResponse[AclToken] =
    aclTokensApi.update(token, options.orNull)

}
