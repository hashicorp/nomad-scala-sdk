package com.hashicorp.nomad.scalasdk

import scala.collection.JavaConverters._

import com.hashicorp.nomad.apimodel.Namespace
import com.hashicorp.nomad.javasdk._

/**
  * API for querying for information about namespaces,
  * exposing the [[https://www.nomadproject.io/api/namespaces.html namespaces]] functionality of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param namespacesApi the underlying Java SDK NamespacesApi
  */
class ScalaNamespacesApi(namespacesApi: NamespacesApi) {

  /**
    * Deletes a namespace.
    *
    * @param namespaceId the ID of the namespace to delete
    * @param options     options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/namespaces.html#delete-namespace `DELETE /v1/namespace/:id`]]
    */
  def delete(namespaceId: String, options: Option[WriteOptions] = None): ServerResponse[Unit] =
    namespacesApi.delete(namespaceId, options.orNull)
      .map((_: Void) => ())

  /**
    * Queries a namespace.
    *
    * @param name    name of the namespace.
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/namespaces.html#read-namespace `GET /v1/namespace/:id`]]
    */
  def info(name: String, options: Option[ScalaQueryOptions[Namespace]] = None): ServerQueryResponse[Namespace] =
    namespacesApi.info(name, options.asJava)

  /**
    * Lists namespaces.
    *
    * @param namePrefix a name prefix that, if given,
    *                   restricts the results to only namespaces having a name with this prefix
    * @param options    options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/namespaces.html#list-namespaces `GET /v1/namespaces`]]
    */
  def list(namePrefix: Option[String] = None, options: Option[ScalaQueryOptions[Seq[Namespace]]] = None): ServerQueryResponse[Seq[Namespace]] =
    namespacesApi.list(namePrefix.orNull, options.asJava(_.asScala.toSeq))
      .map(_.asScala.toSeq)

  /**
    * Registers or updates a namespace.
    *
    * @param namespace the namespace to register
    * @param options   options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/namespaces.html#create-or-update-a-namespace `PUT /v1/namespace`]]
    */
  def register(namespace: Namespace, options: Option[WriteOptions] = None): ServerResponse[Unit] =
    namespacesApi.register(namespace, options.orNull)
      .map((_: Void) => ())

}
