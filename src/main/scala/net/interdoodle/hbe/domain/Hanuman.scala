package net.interdoodle.hbe.domain

import akka.stm.Ref
import akka.actor.Actor
import akka.event.EventHandler
import collection.mutable.{HashMap, LinkedList}
import net.interdoodle.hbe.message.{MonkeyResult, PageGenerated, TypingRequest, SimulationStatus}
import scala.collection.JavaConversions._

/** Monkey god (supervises Monkey supervisors)
 * @author Mike Slinn */

class Hanuman(val simulationID:String,
              val maxTicks:Int,
              val monkeysPerVisor:Int,
              val document:String,
              val simulationStatusRef:Ref[SimulationStatus]) extends Actor {
  var simulationStatus = simulationStatusRef.get
  val monkeyResultRefList = new HashMap[String, Ref[MonkeyResult]]()
  var running = true // is this boolean required or is there a better way?


  override def postStop() = {
    running = false
    for (val monkeyVisorRef <- self.linkedActors.values()) {
      monkeyVisorRef.stop() // monkeyVisor's postStop() also stops linked Monkeys
      self.unlink(monkeyVisorRef)
    }
  }

  override def preStart() = {
    var ticks = 0
    createMonkeyVisor()
    while (running && ticks<=maxTicks) {
      tick() // until this Actor is stopped
      ticks += 1
    }
  }

  def createMonkeyVisor() {
    val monkeyResult = new MonkeyResult()
    val monkeyVisorRef = Actor.actorOf(new MonkeyVisor(simulationID, document, monkeysPerVisor, monkeyResultRefList))
    simulationStatus.putSimulation(simulationID, Some(monkeyVisorRef))
    self.link(monkeyVisorRef)
    monkeyVisorRef.start()
  }

  def receive = {
    case "MonkeyVisor is done" =>
      // TODO close MonkeyVisor, summarize and stop this Hanuman Actor if no more MonkeyVisors
      EventHandler.info(this, "MonkeyVisor is done")

    case _ =>
      EventHandler.info(this, "Hanuman received an unknown message")
  }

  /** Simulate time slice, wherein a page is generated by each monkey */
  def tick() {
    for (val monkeyVisor <- self.linkedActors.values()) {
      monkeyVisor ! "generatePages"
    }
  }
}