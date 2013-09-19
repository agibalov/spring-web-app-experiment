create table Something(x int);
insert into Something(x) values(123);

create table Users(
	Id int identity,
	RowUuid char(36) not null,
	Name varchar(256) not null,
	Password varchar(256) not null
);

create table Categories(
	Id int identity,
	RowUuid char(36) not null,
	Name varchar(256) not null 
);

create table Articles(
	Id int identity,
	RowUuid char(36) not null,
	Title varchar(256) not null,
	Text varchar(65536) not null,
	CreatedAt timestamp not null,
	UpdatedAt timestamp,
	UserId int not null,
	CategoryId int not null
);

alter table Articles add foreign key (UserId) references Users (Id);
alter table Articles add foreign key (CategoryId) references Categories (Id);