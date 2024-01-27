CREATE TABLE user (
   id INTEGER AUTO_INCREMENT,
   name varchar(25) NOT NULL,
   PRIMARY KEY(id)
);

insert into user (id, name)
values (1, 'primary1'), (2, 'primary2'), (3, 'primary3'), (4, 'primary4');
