package bike.mikey.bingo

import scala.annotation.unused
import scala.util.{Failure, Success, Try}

import com.raquo.laminar.api.L._
import org.scalajs.dom

sealed trait Tile {
  def id: String
  def active: Boolean
}
case class Square(text: String, picked: Boolean) extends Tile {
  def id: String = text
  def active = picked
}
case class Free() extends Tile {
  def id: String = "free"
  def active = true
}

sealed trait Command
case class Toggle(tileId: String) extends Command

object App {
  private val tilesVar = Var(List[Tile]())

  private val commandObserver = Observer[Command] {
    case Toggle(tileId) =>
      tilesVar.update(
        _.map(t =>
          t match {
            case Free()               => t
            case Square(text, picked) => if (t.id == tileId) Square(text, !picked) else t
          },
        ),
      )
    //tilesVar.update()
  }

  lazy val node: HtmlElement = {
    val $tiles = tilesVar.signal
    div(
      className("container"),
      h1("Corporate Bingo!"),
      ul(
        cls("tiles-list"),
        children <-- $tiles.split(_.id)(renderTile),
      ),
    )
  }

  def renderTile(
    @unused tileId: String,
    @unused initial: Tile,
    $item: Signal[Tile],
  ): HtmlElement = {
    li(
      className <-- $item.map(item =>
        item match {
          case Free()       => "tile free"
          case Square(_, _) => if (item.active) "tile active" else "tile normal"
        },
      ),
      span(
        className("inner"),
        child.text <-- $item.map { item =>
          item match {
            case Free()          => "Free square"
            case Square(text, _) => text
          }
        },
      ),
      onClick.mapTo(Toggle(tileId)) --> commandObserver,
    )
  }

  val keywords: List[String] = List(
    "Deep Dive",
    "Workstream",
    "Alignment",
    "Huddle",
    "Horizontal opportunities",
    "Team ceremonies",
    "Swimlane",
    "Best in class",
    "Day in and day out",
    "True north",
    "Lived experience",
    "High-touch",
    "Call-out",
    "Warm transfer",
    "Ask",
    "Give time back",
    "Flywheel",
    "Amex",
    "Funnel",
    "Top of the market",
    "Revenge travel",
    "Learnings",
    "TV ready",
    "Fast follower",
    "Copy paste",
    "Journey",
    "Leverage",
    "Cross-channel",
    "Speed to market",
  )

  def setNewSeed(): Long = {
    val l = scala.util.Random.nextLong().abs.toLong
    dom.window.location.hash = s"$l"
    l
  }

  def initSeed(): Long = {
    if (dom.window.location.hash.isEmpty()) {
      setNewSeed()
    } else {
      println(dom.window.location.hash)
      Try(dom.window.location.hash.tail.toLong) match {
        case Failure(_)     => setNewSeed()
        case Success(value) => value
      }
    }
  }

  def initBoard(seed: Long): List[Tile] = {
    val r = new scala.util.Random
    r.setSeed(seed)
    val shuffled = r.shuffle(keywords)
    val firstHalf = shuffled.take(12).map(t => Square(text = t, picked = false))
    val secondHalf = shuffled.drop(12).take(12).map(t => Square(text = t, picked = false))
    firstHalf ++ List(Free()) ++ secondHalf
  }

  def main(args: Array[String]): Unit = {
    val seed = initSeed()
    val board = initBoard(seed)
    tilesVar.update(_ => { board })
    dom.document.addEventListener(
      "DOMContentLoaded",
      { (_: dom.Event) =>
        val appContainer = dom.document.querySelector("#appContainer")
        render(appContainer, node)
      },
    )
  }
}
