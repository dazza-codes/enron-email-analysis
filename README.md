
# Requirements

- Java SDK (8)
- Scala Build Tool (sbt)
  - http://www.scala-sbt.org/1.0/docs/Setup.html
- Spark (2.2.1)
  - https://spark.apache.org/downloads.html
  - local mode

Developed on an Ubuntu 16.04 linux system, using Oracle Java 8.

# Usage

This project uses the Scala Build Tool (sbt).  To run examples, install and run the `sbt` REPL in this project
directory (where the `build.sbt` file is located).  The `sbt` should download all the project dependencies (any
warnings about dependency conflicts can be ignored, they arise from 3rd-party dependency resolution).

### Email Spark Analysis

Package a jar containing the application

```bash
sbt package
...
[info] Packaging {..}/{..}/target/scala-2.11/enron-emails_2.11-1.0.jar
```

Use spark-submit to run analysis
```bash
PACKAGE_JAR="target/scala-2.11/enron-emails_2.11-1.0.jar"
SPARK_HOME=/opt/spark-2.2.1-bin-hadoop2.7
${SPARK_HOME}/bin/spark-submit \
  --class "SparkAnalysisApp" \
  --master local[2] \
  ${PACKAGE_JAR} \
  enron_email_records.parquet  2> spark_analysis.log
```


# Some Resources

- http://www.enronemail.com/enron-story/

### Enron Email Data

- https://www.cs.cmu.edu/~./enron/
- http://bailando.sims.berkeley.edu/enron_email.html
- http://www.ahschulz.de/enron-email-data/
- http://info.nuix.com/Enron.html
  - "Nuix and EDRM worked together to cleanse the Enron data set of private information."
  - "The complete EDRM Enron Data Set is 18GB in total."
  
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
  - Kafka producer and consumer examples using AVRO data
- https://hortonworks.com/blog/the-data-lifecycle-part-one-avroizing-the-enron-emails/
  - https://s3.amazonaws.com/rjurney.public/enron.avro
- https://github.com/rjurney/Agile_Data_Code/blob/master/ch03/gmail/email.avro.schema

### Eel

- https://github.com/51zero/eel-sdk

"Eel is a toolkit for manipulating data in the hadoop ecosystem. By hadoop ecosystem we mean
file formats common to the big-data world, such as parquet, orc, csv in locations such as HDFS
or Hive tables. In contrast to distributed batch or streaming engines such as Spark or Flink,
Eel is an SDK intended to be used directly in process."

### ElasticSearch

- https://github.com/sksamuel/elastic4s

### Gmail analysis

- https://github.com/rjurney/Agile_Data_Code/tree/master/ch03
  - Pig, MongoDB, ElasticSearch

### Scala SQL

- https://github.com/aselab/scala-activerecord
- https://github.com/aselab/scala-activerecord-sample/tree/master/sample/src/main/scala
