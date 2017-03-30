package com.hashicorp.nomad.scalasdk

import java.math.BigInteger

import com.hashicorp.nomad.javasdk.{ Predicate, QueryOptions, ServerQueryResponse, WaitStrategy }

/** Options for queries to a Nomad server API.
  *
  * @param region                the region to which the request should be forwarded (when None the API's region setting is used)
  * @param index                 the long-polling query index to use for blocking queries
  * @param waitStrategy          the wait strategy to use for long-polling blocking queries
  * @param allowStale            whether to allow stale responses
  *                              (see [[https://www.nomadproject.io/docs/http/index.html#consistency-modes Consistency Modes]])
  * @param repeatedPollPredicate when specified, the server is repeatedly polled until the response satisfied the
  *                              predicate or the waitStrategy throws a TimeoutException.
  * @see [[https://www.nomadproject.io/docs/http/index.html#blocking-queries Blocking Queries]]
  * @tparam A the expected response type.
  */
case class ScalaQueryOptions[A](
    region: Option[String] = None,
    index: Option[BigInteger] = None,
    waitStrategy: Option[WaitStrategy] = None,
    allowStale: Boolean = false,
    repeatedPollPredicate: Option[ServerQueryResponse[A] => Boolean] = None) {

  /** Returns Java [[QueryOptions]] equivalent to these options.
    *
    * @param f a function that maps a Java response value to its Scala equivalent
    * @tparam B the Java type representing the response value
    */
  private[scalasdk] def asJava[B](f: B => A): QueryOptions[B] = {
    val javaOptions = new QueryOptions[B]()
    if (allowStale) javaOptions.setAllowStale(true)
    index.foreach(javaOptions.setIndex)
    region.foreach(javaOptions.setRegion)
    waitStrategy.foreach(javaOptions.setWaitStrategy)
    repeatedPollPredicate.foreach(p => javaOptions.setRepeatedPollPredicate(new Predicate[ServerQueryResponse[B]] {
      override def apply(r: ServerQueryResponse[B]): Boolean =
        p(new ServerQueryResponse(r.getHttpResponse, r.getRawEntity, f(r.getValue)))
    }))
    javaOptions
  }

  /** Returns Java [[QueryOptions]] equivalent to these options,
    * assuming the Java and Scala representations of the response value are the same.
    */
  private[scalasdk] def asJava: QueryOptions[A] = asJava(identity)

}
