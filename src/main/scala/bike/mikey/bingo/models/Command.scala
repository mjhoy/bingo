package bike.mikey.bingo.models

sealed trait Command
case class Toggle(tileId: String) extends Command
