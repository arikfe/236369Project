drop table if exists  `project`.`report`;
drop table if exists `project`.`user_roles`;
drop table if exists `project`.`users`;
drop table if exists `project`.`evacuation`;
drop table if exists `project`.`evacuation_user`;
drop table if exists  `project`.`document`;
create table document(
	id int(11) NOT NULL AUTO_INCREMENT,
	contentType varchar(20) not null,
	file mediumblob not null,
	filename varchar(100) not null,
	primary key(id)	
);
CREATE  TABLE users (
  username VARCHAR(45) NOT NULL ,
  password VARCHAR(60) NOT NULL ,
  fname varchar(40),
  lname varchar(40),
  imageId int(11),
  enabled TINYINT NOT NULL DEFAULT 1 ,
  PRIMARY KEY (username),
  foreign key (imageId) references document(id) on delete cascade
  );



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
  imageId int(11),
  foreign key(imageId) references document(id) on delete cascade,
  PRIMARY KEY (id),
  FOREIGN KEY (username) REFERENCES users (username));

CREATE TABLE evacuation (
 id int(11) NOT NULL AUTO_INCREMENT,
 estimated datetime NOT NULL,
 capacity INT default 0,
 means varchar(45) NOT NULL,
 geolat decimal(10,6) default NULL,
 geolng decimal(10,6) default NULL,
 primary key (id));
 
 create table evacuation_user (
 id bigint(11) references evacuation(id),
username varchar(45) references users(username),primary key(id,username));