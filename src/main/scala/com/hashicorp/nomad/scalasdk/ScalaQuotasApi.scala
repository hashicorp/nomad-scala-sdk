package com.hashicorp.nomad.scalasdk

import scala.collection.JavaConverters._

import com.hashicorp.nomad.apimodel.{ QuotaSpec, QuotaUsage }
import com.hashicorp.nomad.javasdk._

/**
  * API for managing quotas,
  * exposing the [[https://www.nomadproject.io/api/quotas.html ACL policies]] functionality of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param quotasApi the underlying Java SDK QuotasApi
  */
class ScalaQuotasApi(quotasApi: QuotasApi) {

  /**
    * Deletes a quota specification.
    *
    * @param quotaName the name of the quota to delete
    * @param options   options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/quotas.html#delete-quota-specification `DELETE /v1/quota/:name`]]
    */
  def delete(quotaName: String, options: Option[WriteOptions] = None): ServerResponse[Unit] =
    quotasApi.delete(quotaName, options.orNull)
      .map((_: Void) => ())

  /**
    * Queries a quota specification.
    *
    * @param name    name of the quota.
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/quotas.html#read-quota-specification `GET /v1/quota/:name`]]
    */
  def info(name: String, options: Option[ScalaQueryOptions[QuotaSpec]] = None): ServerQueryResponse[QuotaSpec] =
    quotasApi.info(name, options.asJava)

  /**
    * Lists quota specifications.
    *
    * @param namePrefix a name prefix that, if given,
    *                   restricts the results to only quotas having a name with this prefix
    * @param options    options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/quotas.html#list-quota-specifications `GET /v1/quotas`]]
    */
  def list(namePrefix: Option[String], options: Option[ScalaQueryOptions[Seq[QuotaSpec]]] = None): ServerQueryResponse[Seq[QuotaSpec]] =
    quotasApi.list(namePrefix.orNull, options.asJava(_.asScala))
      .map(_.asScala)

  /**
    * Lists quota usages.
    *
    * @param namePrefix a name prefix that, if given,
    *                   restricts the results to only quotas having a name with this prefix
    * @param options    options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/quotas.html#list-quota-usages `GET /v1/quota-usages`]]
    */
  def listUsage(namePrefix: Option[String], options: Option[ScalaQueryOptions[Seq[QuotaUsage]]] = None): ServerQueryResponse[Seq[QuotaUsage]] =
    quotasApi.listUsage(namePrefix.orNull, options.asJava(_.asScala))
      .map(_.asScala)

  /**
    * Registers or updates a quota.
    *
    * @param quota   the quota to register
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/quotas.html#create-or-update-quota-specification `PUT /v1/quota`]]
    */
  def register(quota: QuotaSpec, options: Option[WriteOptions] = None): ServerResponse[Unit] =
    quotasApi.register(quota, options.orNull)
      .map((_: Void) => ())

  /**
    * Queries a quota usage.
    *
    * @param name    name of the quota.
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/quotas.html#read-quota-usage `GET /v1/quota/:name`]]
    */
  def usage(name: String, options: Option[ScalaQueryOptions[QuotaUsage]] = None): ServerQueryResponse[QuotaUsage] =
    quotasApi.usage(name, options.asJava)

}
