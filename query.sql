

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






