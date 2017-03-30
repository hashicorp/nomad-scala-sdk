package com.hashicorp.nomad.scalasdk

import java.io.{PrintWriter, StringWriter}

import com.hashicorp.nomad.apimodel._
import com.hashicorp.nomad.javasdk.WaitStrategy._
import com.hashicorp.nomad.testutils.NomadAgentConfiguration.Builder
import com.hashicorp.nomad.testutils.NomadAgentProcess
import org.scalatest.FunSuite

class NomadScalaApiTest extends FunSuite {

  def withClientServer[A](use: NomadAgentProcess => A): A = {
    val serverLog = new StringWriter
    val agent = new NomadAgentProcess(new PrintWriter(serverLog), new Builder().setClientEnabled(true).build())
    try {
      use(agent)
    } catch {
      case e: Throwable =>
        throw new RuntimeException(s"Error during server use: $e\nServer log follows:\n$serverLog", e)
    }
    finally agent.close()
  }

  def withApi[A](agent: NomadAgentProcess)(use: NomadScalaApi => A): A = {
    val nomadApi = NomadScalaApi(agent.getHttpAddress)
    try {
      use(nomadApi)
    } finally
      nomadApi.close()
  }

  test("Should be able to run a job") {

    val task = new Task()
      .setName("Do the Thing")
      .setDriver("mock_driver")
      .addConfig("run_for", "20s")
      .setLogConfig(new LogConfig()
        .setMaxFiles(1)
        .setMaxFileSizeMb(1)
      )

    val job = new Job()
      .setId("run-some-stuff")
      .setName("Run Some Stuff")
      .setType("batch")
      .setPriority(1)
      .addTaskGroups(
        new TaskGroup()
          .setName("Group")
          .setCount(1)
          .addTasks(task)
      )

    withClientServer { agent =>
      withApi(agent) { api =>
        agent.pollUntilReady(api.apiClient, waitForMilliseconds(20000, 200))

        val Seq(region) = api.regions.list().getValue
        val Seq(node) = api.nodes.list().getValue

        val evalId = api.jobs.register(job.setRegion(region).addDatacenters(node.getDatacenter)).getValue
        val e = api.evaluations.pollForCompletion(evalId, waitForSeconds(10)).getValue
        assert(e.getFailedTgAllocs == null || e.getFailedTgAllocs.isEmpty)
        assert(e.getBlockedEval == null || e.getBlockedEval.isEmpty)
        assert(e.getNextEval == null || e.getNextEval.isEmpty)

        val Seq(allocation) = api.evaluations.allocations(e.getId).getValue
        api.lookupClientApiByNodeId(allocation.getNodeId)
      }
    }
  }

}
