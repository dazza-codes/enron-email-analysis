
-- Useage:
-- mysql -u enron -p --default-character-set=utf8 enron_email_records < db/mysql_structure_init.sql

-- Recent versions of MySQL use a "strict" mode that will not allow setting a zero date as a default.
-- This SQL_MODE setting is a work-around to allow setting a zero date (the implicit default).
SET SQL_MODE='ALLOW_INVALID_DATES';


-- drop tables

SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS messages CASCADE;
SET FOREIGN_KEY_CHECKS=1;


-- recreate tables and constraints

CREATE TABLE users (
  user_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  email VARCHAR(256) NOT NULL,
  PRIMARY KEY (user_id)
);

CREATE TABLE messages (
  message_id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_id INT UNSIGNED NOT NULL,
  thread_id VARCHAR(256) NOT NULL,
  email_content VARCHAR(256),
  sent_at TIMESTAMP,
  PRIMARY KEY (message_id),
  FOREIGN KEY (user_id) REFERENCES users(user_id)
);

