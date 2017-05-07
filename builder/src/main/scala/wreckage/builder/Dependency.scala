package wreckage.builder

import java.nio.file.Path

sealed trait Dependency extends Product with Serializable {
  def groupId: Seq[String]
  def artifactId: String
  def version: String

  def toXML = s"""
        <dependency>
            <groupId>${groupId.mkString(".")}</groupId>
            <artifactId>$artifactId</artifactId>
            <version>$version</version>
        </dependency>
    """
}

final case class ManagedDependency(groupId: Seq[String], artifactId: String, version: String)
  extends Dependency

final case class UnmanagedDependency(groupId: Seq[String], artifactId: String, version: String, jarlocation: Path)
  extends Dependency
