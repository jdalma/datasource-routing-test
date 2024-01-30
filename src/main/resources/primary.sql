CREATE TABLE MEMBER (
    id INTEGER NOT NULL,
    name varchar(25) NOT NULL,
    PRIMARY KEY(id)
);

insert into MEMBER (id, name)
values (1, 'primary1'), (2, 'primary2'), (3, 'primary3'), (4, 'primary4');
