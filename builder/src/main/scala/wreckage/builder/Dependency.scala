package wreckage.builder

case class Dependency(groupId: Seq[String], artifactId: String, version: String) {
  def toXML = s"""
        <dependency>
            <groupId>${groupId.mkString(".")}</groupId>
            <artifactId>$artifactId</artifactId>
            <version>$version</version>
        </dependency>
    """
}
