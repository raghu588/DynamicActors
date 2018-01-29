import akka.AkkaException
import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.concurrent.Await

//Greetings Actor
case class Hello(times: Int = 1)
case class HelloResponse(response: String)

class GreetingsActor extends Actor {

  var count: Int = 0

  override def receive: Receive = {
    case Hello(times) => { count += 1; sender ! HelloResponse( "Hello " * times ) }
    case Count() => sender ! CountResponse(count)
    case _ => unhandled()
  }
}
//Greeting Actor object
object GreetingsActor {
  def props = Props(classOf[GreetingsActor])
}

//SuperVisorActor
case class Count()
case class CountResponse(count: Int)

class SupervisorActor extends Actor{

  val greeter: ActorRef = context.actorOf(GreetingsActor.props)

  override def receive: Receive = {
    case x: Hello => { greeter ! x }

    case response: HelloResponse =>  println( response.response )

    case c: Count => {
      implicit val timeout = new Timeout(1 seconds)
      // ask the greeter for count
      val future = greeter ? c
      // synchronously wait for a response
      val result = Await.result(future,timeout.duration).asInstanceOf[CountResponse] // synchronously wait for a response
      println(s"response is ${result}")
    }

    case _ => unhandled()
  }
}

//SupervisorActor Object
object SupervisorActor {
  def props = Props(classOf[SupervisorActor])
}


//MainClass
object ActorDynamic extends App {
  val _system: ActorSystem = ActorSystem.create("hello-system")
  val supervisor: ActorRef = _system.actorOf(SupervisorActor.props)

  supervisor ! Hello(5)
  supervisor ! Hello(2)
  supervisor ! Hello(3)
  supervisor ! Count()

  Thread.sleep(5000)

  supervisor ! PoisonPill
  _system.terminate()


}

