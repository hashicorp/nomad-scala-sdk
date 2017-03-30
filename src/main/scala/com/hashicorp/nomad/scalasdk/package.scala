package com.hashicorp.nomad

import java.util.concurrent.ExecutionException

import scala.concurrent._

import com.hashicorp.nomad.javasdk.{ NomadResponse, QueryOptions, ServerQueryResponse, ServerResponse }

/** An SDK for interacting with Nomad from Scala.
  *
  * The central class in the SDK is [[com.hashicorp.nomad.scalasdk.NomadScalaApi]].
  */
package object scalasdk {

//  /** Syntactic sugar for [[ListenableFuture]]s, the futures returned by the Java API.
//    *
//    * @param future the future to operate on
//    * @tparam A the type held by the future
//    */
//  implicit final class ListenableFutureSyntax[A](private val future: ListenableFuture[A]) extends AnyVal {
//
//    /** Returns a Scala [[scala.concurrent.Future]] equivalent to the [[ListenableFuture]]. */
//    def asScala: Future[A] =
//      asScala(identity)
//
//    /** Returns a Scala [[scala.concurrent.Future]] equivalent to the [[ListenableFuture]]
//      *
//      * @param f a function that maps the Java value eventually held by the ListenableFuture to its Scala equivalent
//      * @tparam B the type that will be held in the Scala Future
//      */
//    def asScala[B](f: A => B): Future[B] = {
//      val promise = Promise[B]()
//      future.addListener(new Runnable {
//        override def run(): Unit =
//          try promise.trySuccess(f(future.get()))
//          catch {
//            case e: ExecutionException => promise.tryFailure(e.getCause)
//            case e: Throwable          => promise.tryFailure(e)
//          }
//      }, MoreExecutors.directExecutor())
//      promise.future
//    }
//  }

  /** Syntactic sugar for [[NomadResponse]]s.
    *
    * @param response the response to operate on
    * @tparam A the Java type representing the body of the response
    */
  private[scalasdk] implicit final class FutureNomadResponseSyntax[A](
      private val response: NomadResponse[A]) extends AnyVal {

    /** Returns a Scala representation of the response future.
      *
      * @param f a function that maps the response body from its Java representation to its Scala representation
      * @tparam B the Scala type representing the body of the response
      */
    def map[B](f: A => B): NomadResponse[B] =
      new NomadResponse(response.getRawEntity, f(response.getValue))
  }

  /** Syntactic sugar for [[ServerResponse]]s.
    *
    * @param response the response to operate on
    * @tparam A the Java type representing the body of the response
    */
  private[scalasdk] implicit final class FutureServerResponseSyntax[A](
      private val response: ServerResponse[A]) extends AnyVal {

    /** Returns a Scala representation of the response future.
      *
      * @param f a function that maps the response body from its Java representation to its Scala representation
      * @tparam B the Scala type representing the body of the response
      */
    def map[B](f: A => B): ServerResponse[B] =
      new ServerResponse(response.getHttpResponse, response.getRawEntity, f(response.getValue))
  }

  /** Syntactic sugar for [[ServerQueryResponse]]s.
    *
    * @param response the response to operate on
    * @tparam A the Java type representing the body of the response
    */
  private[scalasdk] implicit final class FutureServerQueryResponseSyntax[A](
      private val response: ServerQueryResponse[A]) extends AnyVal {

    /** Returns a Scala representation of the response future.
      *
      * @param f a function that maps the response body from its Java representation to its Scala representation
      * @tparam B the Scala type representing the body of the response
      */
    def map[B](f: A => B): ServerQueryResponse[B] =
      new ServerQueryResponse(response.getHttpResponse, response.getRawEntity, f(response.getValue))
  }

  /** Syntactic sugar for optional ScalaQueryOptions.
    *
    * @param options the options to operate on
    * @tparam A the Scala type representing the response body for the method the options will be used with
    */
  private[scalasdk] implicit final class QueryOptionsSyntax[A](private val options: Option[ScalaQueryOptions[A]]) extends AnyVal {

    /** Returns equivalent QueryOptions for the Java API, assuming the Scala and Java types representing the response body are the same. */
    def asJava: QueryOptions[A] =
      asJava(identity)

    /** Returns equivalent QueryOptions for the Java API.
      *
      * @param responseValueToScala a function that maps the response body from its Java representation to its Scala representation
      * @tparam J the Java type representing the response body for the method the options will be used with
      */
    def asJava[J](responseValueToScala: J => A): QueryOptions[J] =
      options.map(_.asJava(responseValueToScala)).orNull

  }

}
