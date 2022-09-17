create table category (id bigint not null, name varchar(255) not null, primary key (id));
alter table reason add column category_id bigint not null;
create table station (id integer not null, name varchar(255), primary key (id));
alter table category drop constraint if exists UK_46ccwnsi9409t36lurvtyljak;
alter table category add constraint UK_46ccwnsi9409t36lurvtyljak unique (name);
alter table part drop constraint if exists UKlxgp3trvhgx4bg9mf7jne54da;
alter table part add constraint UKlxgp3trvhgx4bg9mf7jne54da unique (name, product_id);
alter table reason add constraint FK2p4tbn2twb7ug300lhhitbvnh foreign key (category_id) references category;
alter table ticket add constraint FKoxse8ypy5yhkpfjycknroqquy foreign key (station_id) references station;