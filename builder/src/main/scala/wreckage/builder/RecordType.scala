package wreckage.builder

case class RecordType(alias: String, parent: Option[String], fields: Seq[(String, String)])
