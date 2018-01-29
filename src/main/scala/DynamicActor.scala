import akka.actor
import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.event.Logging

class actorLogic extends Actor {

  def receive = {
    case msg: String => println("dynamically invoked"+self.path.address+self.path.name)
    //case _ => println("Not Invoked")
  }
}

object DynamicActor{

  def dynamicActorCreate(i: Int) = {

    var asy = ActorSystem("ActorSystem")
    for (x <- 1 to i) {
      var x = asy.actorOf(Props[actorLogic])
      x ! "hello"
    }
  }


  def dynamicActorCreateName(myactor: String) = {

    var asy = ActorSystem("ActorSystem")
    var myactor = asy.actorOf(Props[actorLogic])
    myactor ! "hello"
  }


  def main(args: Array[String])= {

    val s = ActorSystem("asy")
    val invokeActor = s.actorOf(Props[actorLogic])
    invokeActor ! dynamicActorCreate(10)

    invokeActor ! dynamicActorCreateName("myname")
  }
}



