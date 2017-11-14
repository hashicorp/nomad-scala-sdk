package com.hashicorp.nomad.scalasdk

import scala.collection.JavaConverters._

import com.hashicorp.nomad.apimodel.{AllocationListStub, Evaluation}
import com.hashicorp.nomad.javasdk._

/** API for querying for information about evaluations,
  * exposing the functionality of the `/v1/evaluations` and `/v1/evaluation` endpoints of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param evaluationsApi the underlying API from the Java SDK
  */
class ScalaEvaluationsApi(evaluationsApi: EvaluationsApi) {

  /** Queries an evaluation in the active region.
    *
    * @param evaluationId ID of the evaluation to lookup
    * @param options      options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/eval.html `GET /v1/evaluation/{ID}`]]
    */
  def info(evaluationId: String, options: Option[ScalaQueryOptions[Evaluation]] = None): ServerQueryResponse[Evaluation] =
    evaluationsApi.info(evaluationId, options.asJava)

  /** Lists evaluations in the active region.
    *
    * @param evaluationIdPrefix an even-length prefix that, if given,
    *                           restricts the results to only evaluations having an ID with this prefix
    * @param options            options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/evals.html `GET /v1/evaluations`]]
    */
  def list(
      evaluationIdPrefix: Option[String] = None,
      options: Option[ScalaQueryOptions[Seq[Evaluation]]] = None
  ): ServerQueryResponse[Seq[Evaluation]] =
    evaluationsApi.list(evaluationIdPrefix.orNull, options.asJava(_.asScala))
      .map(_.asScala)

  /** Lists allocations created or modified an evaluation in the active region.
    *
    * @param evaluationId ID of the evaluation that created or modified the allocations
    * @param options      options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/evals.html `GET /v1/evaluation/<ID>/allocations`]]
    */
  def allocations(
      evaluationId: String,
      options: Option[ScalaQueryOptions[Seq[AllocationListStub]]] = None
  ): ServerQueryResponse[Seq[AllocationListStub]] =
    evaluationsApi.allocations(evaluationId, options.asJava(_.asScala))
      .map(_.asScala)

  /** Poll the server until an evaluation has completed.
    *
    * @param evaluationId ID of the evaluation to poll for
    * @param waitStrategy the wait strategy to use during polling
    */
  def pollForCompletion(evaluationId: String, waitStrategy: WaitStrategy): ServerQueryResponse[Evaluation] =
    evaluationsApi.pollForCompletion(evaluationId, waitStrategy)

  /** Poll the server until an evaluation has completed.
    *
    * @param evaluation an EvaluationResponse containing the ID of the evaluation to poll for
    * @param waitStrategy the wait strategy to use during polling
    */
  def pollForCompletion(evaluation: EvaluationResponse, waitStrategy: WaitStrategy): ServerQueryResponse[Evaluation] =
    pollForCompletion(evaluation.getValue, waitStrategy)
}
