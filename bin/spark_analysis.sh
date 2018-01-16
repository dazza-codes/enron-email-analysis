#!/bin/bash

PACKAGE_JAR="target/scala-2.11/enron-emails_2.11-1.0.jar"
SPARK_HOME=/opt/spark-2.2.1-bin-hadoop2.7
${SPARK_HOME}/bin/spark-submit \
  --class "SparkAnalysisApp" \
  --master local[2] \
  ${PACKAGE_JAR} \
  enron_email_records.parquet  2> logs/spark_analysis.log

