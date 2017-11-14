package com.hashicorp.nomad.scalasdk

import com.hashicorp.nomad.apimodel.SearchResponse
import com.hashicorp.nomad.javasdk.{ SearchApi, ServerQueryResponse }

/** API for performing searches in the Nomad cluster,
  * exposing the [[https://www.nomadproject.io/api/search.html search]] functionality of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param searchApi the underlying Java SDK SearchApi
  */
class ScalaSearchApi(searchApi: SearchApi) {

  /** Returns a list of matches for a particular context and prefix.
    *
    * @param prefix  items with this prefix are returned
    * @param context one of the following: allocs, deployment, evals, jobs, nodes, namespaces, quotas, all
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/api/search.html `PUT /v1/search`]]
    */
  def prefixSearch(prefix: String, context: String, options: Option[ScalaQueryOptions[SearchResponse]] = None): ServerQueryResponse[SearchResponse] =
    searchApi.prefixSearch(prefix, context, options.asJava)

}
