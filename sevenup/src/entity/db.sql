create table job (id integer primary key AUTOINCREMENT not null,
name text ,groups text,content text,cron text);

create table job_log (id integer primary key AUTOINCREMENT not null,
job_id integer ,time text);


create table memo (id integer primary key AUTOINCREMENT not null,
parent_id integer,name text ,content text);
