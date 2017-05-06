package wreckage.builder

case class Dependency(groupId: String, artifactId: String, version: String) {
  def toXML = s"""
        <dependency>
            <groupId>$groupId</groupId>
            <artifactId>$artifactId</artifactId>
            <version>$version</version>
        </dependency>
    """
}


