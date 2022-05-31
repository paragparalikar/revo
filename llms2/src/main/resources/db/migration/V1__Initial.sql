drop table if exists department CASCADE;
drop table if exists part CASCADE; 
drop table if exists product CASCADE; 
drop table if exists reason CASCADE; 
drop table if exists ticket CASCADE; 
drop table if exists user CASCADE; 
drop table if exists user_departments CASCADE; 
drop table if exists user_pages CASCADE; 
drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start with 1 increment by 1;
create table department (id bigint not null, code integer not null, name varchar(255) not null, primary key (id));
create table part (id bigint not null, name varchar(255) not null, product_id bigint not null, primary key (id));
create table product (id bigint not null, name varchar(255) not null, primary key (id));
create table reason (id bigint not null, text varchar(255) not null, primary key (id));
create table ticket (id bigint not null, closed_timestamp timestamp, open_timestamp timestamp not null, station_id integer not null, status varchar(255) not null, department_id bigint not null, part_id bigint, reason_id bigint, primary key (id));
create table user (username varchar(255) not null, password varchar(255) not null, primary key (username));
create table user_departments (user_username varchar(255) not null, departments_id bigint not null, primary key (user_username, departments_id));
create table user_pages (user_username varchar(255) not null, pages varchar(255));
alter table department add constraint UK_q8ymhgj6pt1msox0o3bg51uvo unique (code);
alter table department add constraint UK_1t68827l97cwyxo9r1u6t4p7d unique (name);
alter table part add constraint UKlxgp3trvhgx4bg9mf7jne54da unique (name, product_id);
alter table product add constraint UK_jmivyxk9rmgysrmsqw15lqr5b unique (name);
alter table reason add constraint UK_c2vdrtmxlfqeyoovapto4pwsh unique (text);
alter table part add constraint FKasaab29o8vgv4m3n7qoio5qre foreign key (product_id) references product;
alter table ticket add constraint FKh33hi5t6a64q5nq9n0kwoa8to foreign key (department_id) references department;
alter table ticket add constraint FKk3n928uh8yecymxfgy495ikom foreign key (part_id) references part;
alter table ticket add constraint FK4vet27fjf7j7bv0jie8dbjtxl foreign key (reason_id) references reason;
alter table user_departments add constraint FKjn4my4789kvynwu6ena93qj4a foreign key (departments_id) references department;
alter table user_departments add constraint FK683ffw8r078mn8wnpxghncsow foreign key (user_username) references user;
alter table user_pages add constraint FKci7e3wjrb47lely76k8ok4k4x foreign key (user_username) references user;
insert into user (password, username) values ('admin', 'admin');
insert into user_pages (user_username, pages) values ('admin', 'dashboards');
insert into user_pages (user_username, pages) values ('admin', 'tickets');
insert into user_pages (user_username, pages) values ('admin', 'departments');
insert into user_pages (user_username, pages) values ('admin', 'reasons');
insert into user_pages (user_username, pages) values ('admin', 'products');
insert into user_pages (user_username, pages) values ('admin', 'users');
insert into user_pages (user_username, pages) values ('admin', 'parts');
insert into user_pages (user_username, pages) values ('admin', 'reports');