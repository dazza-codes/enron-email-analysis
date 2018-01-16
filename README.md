
# Usage

This project was built with scala 2.12 using the Scala Build Tool (sbt).  To run examples, install and run `sbt`, then:

```scala
// Parse and print a single email file
runMain MailParserScript "/data/enron_with_categories/1/70706.txt"
// Parse and print all the email files (*.txt) from a directory
runMain MailParserScript "/data/enron_with_categories/1/"
runMain MailParserScript "/data/enron_with_categories/"
// Save all the parsed email records to an AVRO file
runMain MailRecordsAvroScript "/data/src/enron_emails/enron_with_categories"
```


# Some Resources

- http://www.enronemail.com/enron-story/

- https://www.cs.cmu.edu/~./enron/
- http://bailando.sims.berkeley.edu/enron_email.html
- http://www.ahschulz.de/enron-email-data/

### Spark

- https://www.linkedin.com/pulse/using-apache-spark-perform-sentiment-analysis-enrons-email-saunders
- https://lucidworks.com/2016/12/13/analyzing-enron-with-solr-spark-and-fusion/
- https://github.com/medale/spark-mail

### Graphs

- https://github.com/okram/graph-bootcamp/wiki/Enron (neo4j)
- https://linkurio.us/blog/investigating-the-enron-email-dataset/ (neo4j)
- https://gist.github.com/rjurney/969814 (ruby project)

### Akka

- streams
  - https://doc.akka.io/docs/akka/current/stream/index.html
- connectors
  - https://developer.lightbend.com/docs/alpakka/current/index.html
  - https://doc.akka.io/docs/akka-stream-kafka/current/home.html

### Avro

- https://github.com/sksamuel/avro4s
- https://github.com/hopped/akka-avro-serializer/
- https://dzone.com/articles/kafka-avro-scala-example
- https://hortonworks.com/blog/the-data-lifecycle-part-one-avroizing-the-enron-emails/
  - https://s3.amazonaws.com/rjurney.public/enron.avro
- https://github.com/rjurney/Agile_Data_Code/blob/master/ch03/gmail/email.avro.schema
