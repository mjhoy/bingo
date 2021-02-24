package bike.mikey.bingo.models

sealed trait Tile {
  def id: String
  def active: Boolean
}
case class Square(text: String, picked: Boolean) extends Tile {
  def id = text
  def active = picked
}
case class Free() extends Tile {
  def id = "free"
  def active = true
}
