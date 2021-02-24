package bike.mikey.bingo

import scala.annotation.unused
import scala.util.{Failure, Success, Try}

import com.raquo.laminar.api.L._
import org.scalajs.dom

import bike.mikey.bingo.models.{Free, Game, Square, Tile}

sealed trait Command
case class Toggle(tileId: String) extends Command

object App {
  private val gameVar = Var(Game(won = false, tiles = List[Tile]()))

  private val commandObserver = Observer[Command] {
    case Toggle(tileId) =>
      gameVar.update(game => {
        val withPicked = game.tiles.map(t =>
          t match {
            case Free()               => t
            case Square(text, picked) => if (t.id == tileId) Square(text, !picked) else t
          },
        )
        Game(won = isBingo(withPicked), tiles = withPicked)
      })

    // calculate if we won
    // update game
  }

  lazy val node: HtmlElement = {
    val $game = gameVar.signal
    div(
      className <-- $game.map(game => if (game.won) "container bingoWon" else "container"),
      h1("Corporate Bingo!"),
      h2(
        a(
          href("/"),
          "Reset",
        ),
      ),
      ul(
        cls("tiles-list"),
        children <-- $game.map(_.tiles).split(_.id)(renderTile),
      ),
      div(
        className("bingoBanner"),
        "BINGO!",
      ),
    )
  }

  def renderTile(
    tileId: String,
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
    "Take the time to recognize",
    "Shout-out",
    "Circle back",
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
    gameVar.update(game => { game.copy(tiles = board) })
    dom.document.addEventListener(
      "DOMContentLoaded",
      { (_: dom.Event) =>
        val appContainer = dom.document.querySelector("#appContainer")
        render(appContainer, node)
      },
    )
  }

  def connected(tileIdx: Int, step: Int => Int, limit: Int, tiles: List[Tile]): Boolean = {
    if (tileIdx > limit) {
      return true
    }
    val tile = tiles(tileIdx)
    if (!tile.active) {
      return false
    }

    connected(step(tileIdx), step, limit, tiles)
  }

  def isBingo(tiles: List[Tile]): Boolean = {
    List(
      // Horizontal
      List(0, 5, 10, 15, 20).exists(idx => connected(idx, (_ + 1), idx + 4, tiles)),
      // Vertical
      List(0, 1, 2, 3, 4).exists(idx => connected(idx, (_ + 5), 24, tiles)),
      // Diag left -> right
      connected(0, (_ + 6), 24, tiles),
      // Diag right -> left
      connected(4, (_ + 4), 20, tiles),
    ).exists(identity)
  }
}
