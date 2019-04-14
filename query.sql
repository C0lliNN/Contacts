

create database Contatos;

use Contatos;

create table Contatos(
	idContato int primary key auto_increment,
	nome varchar(50) not null unique,
	sexo enum('Masculino','Feminino'),
	numero varchar(20) not null unique,
	email varchar(50) not null unique,
	descricao varchar(250)
);

/* Regras de Negocio

1 - Nao pode existir dois contatos com o mesmo nome
2 - Nao pode existir dois contatos com o mesmo numero
3 - Nao pode existir dois contatos com o mesmo email
4 - O campo descricao e opcional */





