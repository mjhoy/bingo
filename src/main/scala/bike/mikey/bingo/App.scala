package bike.mikey.bingo

import org.scalajs.dom
import com.raquo.laminar.api.L._

object App {
  def main(args: Array[String]): Unit = {
    println("Hi from the console!")
    dom.document.addEventListener(
      "DOMContentLoaded",
      { (e: dom.Event) =>
        val appContainer = dom.document.querySelector("#appContainer")
        val appElement = div(h1("Hello, world!"))
        render(appContainer, appElement)
      }
    )
  }
}
