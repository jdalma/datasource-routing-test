CREATE TABLE MEMBER (
    id INTEGER NOT NULL,
    name varchar(25) NOT NULL,
    PRIMARY KEY(id)
);

insert into MEMBER (id, name)
values (1, 'secondary1'), (2, 'secondary2'), (3, 'secondary3'), (4, 'secondary4');
