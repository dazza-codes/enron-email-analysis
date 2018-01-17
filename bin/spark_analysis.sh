#!/bin/bash

#ASSEMBLY_JAR="target/scala-2.11/EnronEmailAnalysis-assembly-1.0.jar"

PACKAGE_JAR="target/scala-2.11/enron_email_analysis_2.11-1.0.jar"

SPARK_HOME=/opt/spark-2.2.1-bin-hadoop2.7
${SPARK_HOME}/bin/spark-submit \
  --class "SparkAnalysisApp" \
  --master local[2] \
  --jars ./lib/mysql-connector-java-5.1.45.jar \
  ${PACKAGE_JAR} \
  enron_email_records.parquet  2> logs/spark_analysis.log

echo "Check logs in logs/spark_analysis.log"

