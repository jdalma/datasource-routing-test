drop table if exists ADDRESS;

create table ADDRESS (
    id int auto_increment primary key,
    street_address varchar(30) not null,
    city varchar(30) not null
);

insert into ADDRESS(street_address, city) values('금천구', '서울');
insert into ADDRESS(street_address, city) values('중구', '서울');
insert into ADDRESS(street_address, city) values('동대문구', '서울');
insert into ADDRESS(street_address, city) values('서초구', '서울');
insert into ADDRESS(street_address, city) values('사하구', '부산');
insert into ADDRESS(street_address, city) values('중구', '부산');
insert into ADDRESS(street_address, city) values('북구', '부산');
insert into ADDRESS(street_address, city) values('남구', '부산');
