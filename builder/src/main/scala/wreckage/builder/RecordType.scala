package wreckage.builder

case class RecordType(alias: String, parent: Option[RecordType], fields: Seq[(String, String)])
