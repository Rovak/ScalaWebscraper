package scraper.demo

/**
 * Main application which will be used in the packaged version
 */
object MainApp {

  var serverType: String = "client"
  var port: Int = 2555

  /**
   * Run the app and switch modes based on the given console arguments
   *
   * @param args
   * 	server|client 	if server or client mode
   * 	port			if client then add port number
   */
  def main(args: Array[String]) {

    if (args.length >= 1) {
      serverType = args(0)
    }

    if (args.length >= 2 && serverType == "client") {
      port = args(1).toInt
    }

    serverType.toLowerCase() match {
      case "client" => runClient
      case "server" => runServer
    }
  }

  def runServer = {
    println("Running as Server")
    new ServerApp
  }

  def runClient = {
    println("Running as Client on port: " + port.toString)
    new ClientApp(port)
  }

}