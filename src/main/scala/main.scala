import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import scala.concurrent.ExecutionContextExecutor

object Main extends App {
	given system: ActorSystem = ActorSystem("scala-web-server")
	given executionContext: ExecutionContextExecutor = system.dispatcher

	val route = concat(
		path("hello") {
			get {
				complete("Hello, World!")
			}
		},
		path("greet" / Segment) { username =>
			get {
				complete(s"Hello, $username!")
			}
		}
	)

	val bindingFuture = Http().newServerAt("localhost", 5000).bind(route)

	println(s"Server online at http://localhost:5000/")
	println(s"hello path: http://localhost:5000/hello")
	println(s"greet path http://localhost:5000/greet/John")
	println(s"Press RETURN to stop...")
	scala.io.StdIn.readLine()

	bindingFuture
		.flatMap(_.unbind())
		.onComplete(_ => system.terminate())
}

