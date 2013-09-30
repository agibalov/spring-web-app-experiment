create table Something(x int);
insert into Something(x) values(123);

create table Users(
	Id int identity,
	Name varchar(256) not null,
	Password varchar(256) not null,
	Type int not null
);

create unique index UserNameIndex on Users(Name);

create table Sessions(
	Id int identity,
	Token char(36) not null,
	UserId int not null	
);

alter table Sessions add foreign key (UserId) references Users (Id);

create table Categories(
	Id int identity,
	Name varchar(256) not null 
);

create table Articles(
	Id int identity,
	Title varchar(256) not null,
	Text varchar(65536) not null,
	CreatedAt timestamp not null,
	UpdatedAt timestamp,
	ReadCount int not null,
	UserId int not null,
	CategoryId int not null
);

alter table Articles add foreign key (UserId) references Users (Id);
alter table Articles add foreign key (CategoryId) references Categories (Id);

create table Comments(
	Id int identity,
	Text varchar(65536) not null,
	CreatedAt timestamp not null,
	UpdatedAt timestamp,
	ArticleId int not null,
	UserId int not null
);

alter table Comments add foreign key (ArticleId) references Articles (Id);
alter table Comments add foreign key (UserId) references Users (Id);

create table ArticleVotes(
	Id int identity,
	CreatedAt timestamp not null,
	UpdatedAt timestamp,
	ArticleId int not null,
	UserId int not null,
	Vote int not null
);

alter table ArticleVotes add foreign key (ArticleId) references Articles (Id);
alter table ArticleVotes add foreign key (UserId) references Users (Id);
