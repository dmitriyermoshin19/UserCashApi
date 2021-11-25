--DROP TABLE databasechangeloglock;
--DROP TABLE databasechangelog;
drop table if exists users, phones, profiles, users_phones, roles CASCADE;

create table roles (
id serial primary key,
name varchar(50)
);
insert into roles (name) values ('user'), ('admin');

create table users (
id serial primary key,
name varchar(100),
age int,
email varchar(100),
password varchar(100),
role_id int not null references roles(id),
profile_id int,
UNIQUE(email)
);
insert into users (name, age, email, password, role_id, profile_id)
values ('Petr', '22', 'email', 'abc', '2', '1');

create table profiles (
id serial primary key,
cash real
);
insert into profiles (cash) values ('22');

create table phones (
id serial primary key,
value varchar(100),
UNIQUE(value)
);
insert into phones (value) values ('+7856622');
insert into phones (value) values ('+7856623');

create table users_phones (
user_id int,
phone_id int,
constraint pr_key primary key (user_id, phone_id),
constraint fr_user foreign key (user_id) references users(id)
);
insert into users_phones (user_id, phone_id) values ('1', '1');
insert into users_phones (user_id, phone_id) values ('1', '2');