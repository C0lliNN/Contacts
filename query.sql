

create database Contacts;

use Contacts;

create table Contacts(
	idContact int primary key auto_increment,
	name varchar(50) not null unique,
	gender enum('Male','Female'),
	phoneNumber varchar(20) not null unique,
	email varchar(50),
    description varchar(250)
);

/* Test Examples */

insert into Contacts values (null, "James", "Male", "7543010", null, null);
insert into Contacts values (null, "John", "Male", "6543210", null, null);
insert into Contacts values (null, "Robert", "Male", "8443094", null, null);
insert into Contacts values (null, "David", "Male", "8531354", null, null);
insert into Contacts values (null, "Richard", "Male", "6253216", null, null);
insert into Contacts values (null, "Charles", "Male", "4312536", null, null);
insert into Contacts values (null, "Joseph", "Male", "2632367", null, null);

insert into Contacts values (null, "Mary", "Female", "3263010", null, null);
insert into Contacts values (null, "Patricia", "Female", "95434210", null, null);
insert into Contacts values (null, "Carol", "Female", "8463094", null, null);
insert into Contacts values (null, "Ruth", "Female", "6254510", null, null);
insert into Contacts values (null, "Deborah", "Female", "8541354", null, null);
insert into Contacts values (null, "Laura", "Female", "6253616", null, null);
insert into Contacts values (null, "Donna", "Female", "4312636", null, null);
insert into Contacts values (null, "Margaret", "Female", "2232367", null, null);






