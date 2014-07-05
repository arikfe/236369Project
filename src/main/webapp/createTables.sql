CREATE TABLE report (
  id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  title varchar(100) NOT NULL,
  content varchar(1000) NOT NULL,
  expiration datetime NOT NULL,
  geolat decimal(10,6) default NULL,
  geolng decimal(10,6) default NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (username) REFERENCES users (username));