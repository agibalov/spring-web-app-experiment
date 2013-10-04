create table Something(x int);
insert into Something(x) values(123);

create table Users(
	Id int identity primary key,
	Name varchar(256) not null,
	Password varchar(256) not null,
	Type int not null
);

create unique index UserNameIndex on Users(Name);

create table Sessions(
	Id int identity primary key,
	Token char(36) not null,
	UserId int not null constraint SessionUserRef references Users(Id)	
);

create unique index SessionTokenIndex on Sessions(Token);

create table Categories(
	Id int identity primary key,
	Name varchar(256) not null 
);

create table Articles(
	Id int identity primary key,
	Title varchar(256) not null,
	Text varchar(65536) not null,
	CreatedAt timestamp not null,
	UpdatedAt timestamp,
	ReadCount int not null,
	UserId int not null constraint ArticleUserRef references Users(Id),
	CategoryId int not null constraint ArticleCategoryRef references Categories(Id)
);

create table Comments(
	Id int identity primary key,
	Text varchar(65536) not null,
	CreatedAt timestamp not null,
	UpdatedAt timestamp,
	ArticleId int not null constraint CommentArticleRef references Articles(Id),
	UserId int not null constraint CommentUserRef references Users(Id)
);

create table ArticleVotes(
	Id int identity primary key,
	CreatedAt timestamp not null,
	UpdatedAt timestamp,
	Vote int not null,
	ArticleId int not null constraint ArticleVoteArticleRef references Articles(Id),
	UserId int not null constraint ArticleVoteUserRef references Users(Id)	
);

create view DetailedArticle as
select 
  A.Id as Id, A.Title as Title, A.Text as Text, A.CreatedAt as CreatedAt, A.UpdatedAt as UpdatedAt, A.ReadCount as ReadCount,
  count(Comment.Id) as CommentCount, 
  count(V.Id) as VoteCount, avg(V.Vote) as AverageVote,
  U.Id as UserId, U.Name as UserName, 
  Category.Id as CategoryId, Category.Name as CategoryName 
from Articles as A
join Users as U on U.Id = A.UserId
join Categories as Category on Category.Id = A.CategoryId
left join Comments as Comment on Comment.ArticleId = A.Id
left join ArticleVotes as V on V.ArticleId = A.Id
group by A.Id, A.Title, A.Text, A.CreatedAt, A.UpdatedAt, A.ReadCount, U.Id, U.Name, Category.Id, Category.Name;
