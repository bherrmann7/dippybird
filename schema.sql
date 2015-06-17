
create database bob_blog;

create user 'bobblog'@'localhost' identified by 'freshfish';

GRANT ALL ON bob_blog.* TO 'bob_blog'@'localhost';

create table entry (id INT NOT NULL AUTO_INCREMENT,date_created datetime, body varchar(1024), title char(100),
   PRIMARY KEY (id));

insert into entry values (null, now(), "<p>Freaking test blog <b>lunch</b>", "something I liked" );

insert into entry values (null, now(), "New ways of formatting code in include sententces and such. <ul><li>boo<li>rumble tumble</ul>The end", "Another thing I thought" );

