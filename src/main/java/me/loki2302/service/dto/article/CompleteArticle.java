package me.loki2302.service.dto.article;

import java.util.List;

public class CompleteArticle extends BriefArticle {
    //public int ArticleId;
    //public String Title;
    public String Text;
    //public Date CreatedAt;
    //public Date UpdatedAt;
    //public int ReadCount;
    //public int CommentCount;
    //public int VoteCount;
    //public int AverageVote;
    //public BriefUser User;
    //public BriefCategory Category;
    public List<Comment> Comments;
    public boolean CanVote;
    public Integer CurrentVote;
}
