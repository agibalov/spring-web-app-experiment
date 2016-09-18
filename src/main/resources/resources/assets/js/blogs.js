var Blogs = (function($) {
    function voteForArticle(articleId, vote) {
        var voteForArticleUrl = makeVoteForArticleUrl(articleId, vote);
        redirectWithPost(voteForArticleUrl);
    };
    
    function makeVoteForArticleUrl(articleId, vote) {
        return "/article/" + articleId + "/vote/" + vote;
    };
    
    function redirectWithPost(url) {
        var form = $('<form action="' + url + '" method="post"></form>');
        $("body").append(form);
        $(form).submit();
    };
    
    return {
        voteForArticle: voteForArticle
    };
})($);