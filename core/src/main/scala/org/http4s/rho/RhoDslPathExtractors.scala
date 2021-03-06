package org.http4s.rho

import org.http4s.rho.bits.PathAST._
import org.http4s.rho.bits._
import shapeless.{::, HNil}

import scala.reflect.runtime.universe.TypeTag

trait RhoDslPathExtractors[F[_]] {

  private val stringTag = implicitly[TypeTag[String]]

  implicit def pathMatch(s: String): TypedPath[F, HNil] = TypedPath(PathMatch(s))

  implicit def pathMatch(s: Symbol): TypedPath[F, String :: HNil] =
    TypedPath(PathCapture(s.name, None, StringParser.strParser, stringTag))

  /**
    * Defines a path variable of a URI that should be bound to a route definition
    */
  def pathVar[T](implicit parser: StringParser[F, T], m: TypeTag[T]): TypedPath[F, T :: HNil] =
    pathVar(m.tpe.toString.toLowerCase)(parser)

  /**
    * Defines a path variable of a URI that should be bound to a route definition
    */
  def pathVar[T](id: String)(implicit parser: StringParser[F, T]): TypedPath[F, T :: HNil] =
    TypedPath(PathCapture[F](id, None, parser, stringTag))

  /**
    * Defines a path variable of a URI with description that should be bound to a route definition
    */
  def pathVar[T](id: String, description: String)(implicit parser: StringParser[F, T]): TypedPath[F, T :: HNil] =
    TypedPath(PathCapture[F](id, Some(description), parser, stringTag))

}
