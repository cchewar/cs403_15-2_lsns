package gui_4

import java.awt.Dimension
import scala.swing._
import java.awt.Color

object Main {

  def main(args: Array[String]): Unit = {
    val model = new Model
    val view = new TextView
    //val view = new PanelView
    val controller = new Controller(view, model)
    view.init(controller)
  
  }
}
//********
// Model classes
//********
class Model {
  val playerOrder = new PlayerOrder
  playerOrder.enqueue("A")
  playerOrder.enqueue("B")
  playerOrder.enqueue("C")
  playerOrder.enqueue("D")

  def showPlayerOrder: String = { playerOrder.show }  
  def advancePlayerOrder: String = { playerOrder.advance }
}

class PlayerOrder extends scala.collection.mutable.Queue[String] {

  def advance = {
    val current = dequeue
    enqueue(current)
    toString
  }
  def show = {
    toString
  }
}

//********
// Controller 
//********
class Controller(view: View, model: Model) {

  def showPlayerOrder { 
    val result = model.showPlayerOrder 
    view.displayPlayerOrder(result)
  }

  def advancePlayerOrder { 
    val result = model.advancePlayerOrder 
    view.displayPlayerOrder(result)
  }
}

//********
// View classes
//********
abstract class View {
  var controller: Option[Controller] = None
  val frame = new MainFrame
  val playerOrderDisplay: Component

  def init(ctrl: Controller) {
    controller = Some(ctrl)

    frame.title = "Player Order Demo"
    frame.menuBar = createMenu
    frame.contents = playerOrderDisplay
    frame.size = new Dimension(200, 200)
    frame.centerOnScreen
    frame.visible = true
  }

  protected def createMenu = {
    new MenuBar {
      contents += new Menu("Player Order") {
        contents += new MenuItem(Action("Show") {
          controller.get.showPlayerOrder
        })
        contents += new MenuItem(Action("Advance") {
          controller.get.advancePlayerOrder
        })
      } 
      contents += new MenuItem(Action("Exit") {
        sys.exit(0)
      }) 
    } 
  }

  def displayPlayerOrder(players: String)
}

class TextView extends View {
  val playerOrderDisplay = new TextArea(20, 1) { background = Color.yellow }

  def displayPlayerOrder(players: String) {
    playerOrderDisplay.text = players + "\n"    
  }
}


class PanelView extends View {
 
  val order = Array(
      labelFactory(Color.red, Color.yellow), 
      labelFactory(), 
      labelFactory(), 
      labelFactory())
  
  private def labelFactory (
      borderColor : Color = Color.gray, 
      backgroundColor : Color = Color.white) = {
    new Label("?") { 
      border = javax.swing.BorderFactory.createLineBorder(borderColor) 
      background = backgroundColor
      preferredSize = new Dimension(30, 30)
      opaque = true
    }
  }
  
  val playerOrderDisplay = new BoxPanel(Orientation.Horizontal) { 
    contents += new Label("Player Order:")
    for (label <- order)
      contents += label    
    preferredSize = new Dimension(200, 200) 
  }
  
  def displayPlayerOrder(players: String) {
    val playersOnly = players.substring(12, players.length-1)
    var counter = 0
    for (player <- playersOnly.split(",").map(_.trim)) {
      order(counter).text = player
      counter += 1
    }  
  }
}

