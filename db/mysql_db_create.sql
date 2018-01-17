
DROP DATABASE IF EXISTS enron_email_records;
CREATE DATABASE enron_email_records
    DEFAULT CHARACTER SET utf8
    DEFAULT COLLATE utf8_general_ci;

GRANT ALL PRIVILEGES ON enron_email_records.*
  TO 'enron'@'localhost' IDENTIFIED BY 'enronPass';

