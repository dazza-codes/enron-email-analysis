package models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._

case class User(email: String) extends ActiveRecord

object User extends ActiveRecordCompanion[User]
