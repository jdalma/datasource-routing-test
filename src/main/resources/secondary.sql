CREATE TABLE user (
  id INTEGER AUTO_INCREMENT,
  name varchar(25) NOT NULL,
  PRIMARY KEY(id)
);

insert into user (id, name)
values (1, 'secondary1'), (2, 'secondary2'), (3, 'secondary3'), (4, 'secondary4');
