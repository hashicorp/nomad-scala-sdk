package com.hashicorp.nomad.scalasdk

import java.math.BigInteger

import scala.collection.JavaConverters._

import com.hashicorp.nomad.apimodel._
import com.hashicorp.nomad.javasdk._

/** API for managing and querying jobs,
  * exposing the functionality of the `/v1/jobs` and `/v1/job` endpoints of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param jobsApi the underlying API from the Java SDK
  * @see [[https://www.nomadproject.io/docs/http/json-jobs.html Job Specification]]
  *      for documentation about the [[Job]] structure.
  */
class ScalaJobsApi(jobsApi: JobsApi) {

  /** Lists the allocations belonging to a job in the active region.
    *
    * @param jobId   the ID of the job to list allocations for
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/job.html `GET /v1/job/<ID>/allocations`]]
    */
  def allocations(jobId: String, options: Option[ScalaQueryOptions[Seq[AllocationListStub]]] = None): ServerQueryResponse[Seq[AllocationListStub]] =
    jobsApi.allocations(jobId, options.asJava(_.asScala))
      .map(_.asScala)

  /** Deregisters a job in the active region,
    * and stops all allocations that are part of it.
    *
    * @param jobId   the ID of the job to deregister
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/job.html#delete `DELETE /v1/job/<ID>`]]
    */
  def deregister(jobId: String, options: Option[WriteOptions] = None): EvaluationResponse =
    jobsApi.deregister(jobId, options.orNull)

  // TODO no prefix for these evaluations?
  /** Lists the evaluations belonging to a job in the active region.
    *
    * @param jobId   the ID of the job to list evaluations for
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/job.html `GET /v1/job/<ID>/evaluations`]]
    */
  def evaluations(jobId: String, options: Option[ScalaQueryOptions[Seq[Evaluation]]] = None): ServerQueryResponse[Seq[Evaluation]] =
    jobsApi.evaluations(jobId, options.asJava(_.asScala))
      .map(_.asScala)

  /** Creates a new evaluation for a job in the active region.
    *
    * This can be used to force run the scheduling logic if necessary.
    *
    * @param jobId   the ID of the job to evaluate
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/job.html `PUT /v1/job/<ID>/evaluate`]]
    */
  def forceEvaluate(jobId: String, options: Option[WriteOptions] = None): EvaluationResponse =
    jobsApi.forceEvaluate(jobId, options.orNull)

  /** Queries a job in the active region.
    *
    * @param jobId   the ID of the job to query
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/job.html `GET /v1/job/{ID}`]]
    */
  def info(jobId: String, options: Option[ScalaQueryOptions[Job]] = None): ServerQueryResponse[Job] =
    jobsApi.info(jobId, options.asJava)

  /** Lists jobs in the active region.
    *
    * @param jobIdPrefix an even-length prefix that, if given,
    *                    restricts the results to only jobs having an ID with this prefix
    * @param options     options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/jobs.html `GET /v1/jobs`]]
    */
  def list(jobIdPrefix: Option[String] = None, options: Option[ScalaQueryOptions[Seq[JobListStub]]] = None): ServerQueryResponse[Seq[JobListStub]] =
    jobsApi.list(jobIdPrefix.orNull, options.asJava(_.asScala))
      .map(_.asScala)

  /** Forces a new instance of a periodic job in the active region.
    * <p>
    * A new instance will be created even if it violates the job's prohibit_overlap settings.
    * As such, this should be only used to immediately run a periodic job.
    *
    * @param jobId   the ID of the job to force a run of
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/job.html `PUT /v1/job/{ID}/periodic/force`]]
    */
  def periodicForce(jobId: String, options: Option[WriteOptions] = None): EvaluationResponse =
    jobsApi.periodicForce(jobId, options.orNull)

  /** Invokes a dry-run of the scheduler for a job in the active region.
    * <p>
    * Can be used together with the modifyIndex parameter of [[register]]
    * to inspect what will happen before registering a job.
    *
    * @param job     detailed specification of the job to plan for
    * @param diff    indicates whether a diff between the current and submitted versions of the job
    *                should be included in the response.
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/intro/getting-started/jobs.html#modifying-a-job Modifying a Job]]
    * @see [[https://www.nomadproject.io/docs/http/job.html `PUT /v1/job/{ID}/periodic/force`]]
    */
  def plan(job: Job, diff: Boolean, options: Option[WriteOptions] = None): ServerResponse[JobPlanResponse] =
    jobsApi.plan(job, diff, options.orNull)

  /** Registers or updates a job in the active region.
    *
    * @param job     detailed specification of the job to register
    * @param modifyIndex when specified, the registration is only performed if the job's modify index matches.
    *                    This can be used to make sure the job hasn't changed since getting a [[plan]].
    * @param options     options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/jobs.html#put-post `PUT /v1/jobs`]]
    */
  def register(job: Job, modifyIndex: Option[BigInteger] = None, options: Option[WriteOptions] = None): EvaluationResponse =
    jobsApi.register(job, modifyIndex.orNull, options.orNull)

  /** Queries the summary of a job in the active region.
    *
    * @param jobId ID of the job to get a summary for
    * @param options options controlling how the request is performed
    * @see [[https://www.nomadproject.io/docs/http/job.html `GET /v1/job/{ID}/summary`]]
    */
  def summary(jobId: String, options: Option[ScalaQueryOptions[JobSummary]] = None): ServerQueryResponse[JobSummary] =
    jobsApi.summary(jobId, options.asJava)

}
