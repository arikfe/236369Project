CREATE  TABLE users (
  username VARCHAR(45) NOT NULL ,
  password VARCHAR(60) NOT NULL ,
  fname varchar(40),
  lname varchar(40),
  enabled TINYINT NOT NULL DEFAULT 1 ,
  PRIMARY KEY (username));



CREATE TABLE user_roles (
  user_role_id int(11) NOT NULL AUTO_INCREMENT,
  username varchar(45) NOT NULL,
  role varchar(45) NOT NULL,
  PRIMARY KEY (user_role_id),
  UNIQUE KEY uni_username_role (role,username),
  KEY fk_username_idx (username),
  CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username));

INSERT INTO users(username,password,fname,lname,enabled)
VALUES ('admin','$2a$10$04TVADrR6/SPLBjsK0N30.Jf5fNjBugSACeGv1S69dZALR7lSov0y','admin','236369', true);

INSERT INTO user_roles (username, role)
VALUES ('admin', 'ROLE_USER');
INSERT INTO user_roles (username, role)
VALUES ('admin', 'ROLE_ADMIN');


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