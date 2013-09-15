create table Something(x int);
insert into Something(x) values(123);

create table Users(
	Id int identity,
	RowUuid char(36) not null,
	Name varchar(256) not null
);

create table Categories(
	Id int identity,
	RowUuid char(36) not null,
	Name varchar(256) not null 
);

create table Article(
	Id int identity,
	RowUuid char(36) not null,
	Title varchar(256) not null,
	Text varchar(1024) not null,
	UserId int not null,
	CategoryId int not null
);

alter table Article add foreign key (UserId) references Users (Id);
alter table Article add foreign key (CategoryId) references Categories (Id);