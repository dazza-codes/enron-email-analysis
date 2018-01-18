#!/bin/bash

parquetFile=$1
if [ "$parquetFile" == "" ]; then
    parquetFile="enron_email_records.parquet"
fi

#ASSEMBLY_JAR="target/scala-2.11/EnronEmailAnalysis-assembly-1.0.jar"

PACKAGE_JAR="target/scala-2.11/enron_email_analysis_2.11-1.0.jar"

SPARK_HOME=/opt/spark-2.2.1-bin-hadoop2.7
${SPARK_HOME}/bin/spark-submit \
  --class "SparkAnalysisApp" \
  --master local[2] \
  ${PACKAGE_JAR} \
  $parquetFile  2> logs/spark_analysis.log

  #--jars ./lib/mysql-connector-java-5.1.45.jar \
echo "Check logs in logs/spark_analysis.log"

