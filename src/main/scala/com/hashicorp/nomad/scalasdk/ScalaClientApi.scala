package com.hashicorp.nomad.scalasdk

import java.io.InputStream

import scala.collection.JavaConverters._

import com.hashicorp.nomad.apimodel._
import com.hashicorp.nomad.javasdk._

/** API for interacting with a particular Nomad client,
  * exposing the functionality of the `/v1/client/…` endpoints of the
  * [[https://www.nomadproject.io/docs/http/index.html Nomad HTTP API]].
  *
  * @param clientApi the underlying Java SDK AgentApi
  */
class ScalaClientApi private[scalasdk](clientApi: ClientApi) {

  /** Queries the actual resource usage of the client node.
    *
    * @see [[https://www.nomadproject.io/docs/http/client-stats.html `GET /v1/client/stats`]]
    */
  def stats(): NomadResponse[HostStats] =
    clientApi.stats()

  /** Queries the resource usage of an allocation running on the client node.
    *
    * @param allocationId ID of the allocation to lookup
    * @see [[https://www.nomadproject.io/docs/http/client-allocation-stats.html `GET /v1/client/allocation/{ID}/stats`]]
    */
  def stats(allocationId: String): NomadResponse[AllocResourceUsage] =
    clientApi.stats(allocationId)

  /** Reads the contents of a file in an allocation directory.
    *
    * @param allocationId ID of the allocation that produced the file
    * @param path         the path of the file relative to the root of the allocation directory
    * @see [[https://www.nomadproject.io/docs/http/client-fs.html `GET /v1/client/fs/cat/{Allocation-ID}`]]
    */
  def cat(allocationId: String, path: String): NomadResponse[String] =
    clientApi.cat(allocationId, path)

  /** Reads the contents of a file in an allocation directory at a particular offset.
    *
    * @param allocationId ID of the allocation that produced the file
    * @param path         the path of the file relative to the root of the allocation directory
    * @param offset       the byte offset from where content will be read
    * @param limit        the number of bytes to read from the offset
    * @see [[https://www.nomadproject.io/docs/http/client-fs.html `GET /v1/client/fs/readat/{Allocation-ID}`]]
    */
  def readAt(allocationId: String, path: String, offset: Long, limit: Long): NomadResponse[String] =
    clientApi.readAt(allocationId, path, offset, limit)

  /** Streams the contents of a file in an allocation directory.
    * <p>
    * The stream handler's [[StreamHandler#handleFrame]] method will be called repeatedly with stream events,
    * until the [[StreamHandler#isDone]] method returns false.
    * <p>
    * In the event of an error, the stream handler's [[StreamHandler#handleThrowable]] method will be invoked.
    * <p>
    * Note that unless there is an error, the streaming connection to the client node will remain open until the stream
    * handler's isDone method returns true, even if the allocation has completed.
    * <p>
    * To retrieve the contents of a file without the complexity of streaming, use the [[#cat]] method instead.
    *
    * @param allocationId  the ID of the allocation that produced the file
    * @param path          the path of the file relative to the root of the allocation directory
    * @param offset        the byte offset at which to start streaming
    * @param origin        null or "start" indicate the the offset is relative to the beginning of the file,
    *                      "end" indicates that the offset is relative to end of the file.
    * @return a FramedStream that can be used to read the frames in the stream.
    * @see [[https://www.nomadproject.io/docs/http/client-fs.html `GET /v1/client/fs/stream/{Allocation-ID}`]]
    */
  def stream(allocationId: String, path: String, offset: Option[Long] = None, origin: Option[String] = None): FramedStream =
    clientApi.stream(allocationId, path, offset.map(long2Long).orNull, origin.orNull)

  /** Streams a task's stdout or stderr log.
    * <p>
    * Note that if follow is true, then unless there is an error, the streaming connection to the client node will
    * remain open until the stream handler's isDone method returns true, even if the allocation has completed.
    *
    * @param allocationId  the ID of the allocation that produced the log
    * @param taskName      the name of the task that produced the log
    * @param follow        if true, the stream remains open even after the end of the log has been reached
    * @param logType       "stdout" or "stderr"
    * @return a FramedStream that can be used to read the frames in the stream.
    * @see [[https://www.nomadproject.io/docs/http/client-fs.html `GET /v1/client/fs/logs/{Allocation-ID}`]]
    */
  def logs(allocationId: String, taskName: String, follow: Boolean, logType: String): InputStream =
    clientApi.logs(allocationId, taskName, follow, logType)

  /** Streams a task's stdout or stderr log.
    * <p>
    * Note that if follow is true, then unless there is an error, the streaming connection to the client node will
    * remain open until the stream handler's isDone method returns true, even if the allocation has completed.
    *
    * @param allocationId  the ID of the allocation that produced the log
    * @param taskName      the name of the task that produced the log
    * @param follow        if true, the stream remains open even after the end of the log has been reached
    * @param logType       "stdout" or "stderr"
    * @return a FramedStream that can be used to read the frames in the stream.
    * @see [[https://www.nomadproject.io/docs/http/client-fs.html `GET /v1/client/fs/logs/{Allocation-ID}`]]
    */
  def logsAsFrames(allocationId: String, taskName: String, follow: Boolean, logType: String): FramedStream =
    clientApi.logsAsFrames(allocationId, taskName, follow, logType)

  /** Lists the files in an allocation directory.
    *
    * @param allocationId ID of the allocation that owns the directory
    * @param path         the path relative to the root of the allocation directory
    * @see [[https://www.nomadproject.io/docs/http/client-fs.html `GET /v1/client/fs/ls/{Allocation-ID}`]]
    */
  def ls(allocationId: String, path: String): NomadResponse[Seq[AllocFileInfo]] =
    clientApi.ls(allocationId, path)
      .map(_.asScala.toSeq)

  /** Stat a file in an allocation directory.
    *
    * @param allocationId ID of the allocation that owns the file
    * @param path         the path relative to the root of the allocation directory
    * @see [[https://www.nomadproject.io/docs/http/client-fs.html `GET /v1/client/fs/stat/{Allocation-ID}`]]
    */
  def stat(allocationId: String, path: String): NomadResponse[AllocFileInfo] =
    clientApi.stat(allocationId, path)
}
