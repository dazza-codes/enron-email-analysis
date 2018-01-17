import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
import sql_test_models._

// Based on
// https://github.com/aselab/scala-activerecord-sample/tree/master/sample/src/main/scala
// https://github.com/aselab/scala-activerecord-sample/blob/master/sample/src/main/scala/SimpleSample.scala

object SqlTestApp extends App {
  Tables.initialize(Map("schema" -> "sql_test_models.Tables"))

  // create 30 people
  (1 to 30).foreach { i => Person("name" + i, i).save }
  // find by name
  val person3 = Person.findBy("name", "name3").get
  // update person's age
  person3.age = 65
  person3.save

  println("* find people where age >= 20, order by age desc")
  println(Person.where(_.age.~ >= 20).orderBy(_.age desc).toList)

  Tables.cleanup // auto drop
}
