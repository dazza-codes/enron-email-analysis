package sql_test_models

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._

case class Person(var name: String, var age: Int) extends ActiveRecord

object Person extends ActiveRecordCompanion[Person]
