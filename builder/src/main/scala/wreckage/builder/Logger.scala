package wreckage.builder

object Logger {
  def error(msg: String){
    println("[ERROR] "+msg)
  }
  def info(msg: String){
    println("[INFO]  "+msg)
  }
  def debug(msg: String){
    println("[DEBUG] "+msg)
  }
}

