package bike.mikey.bingo.models

case class Game(won: Boolean, tiles: List[Tile])

object Game {
  private def connected(tileIdx: Int, step: Int => Int, limit: Int, tiles: List[Tile]): Boolean = {
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
