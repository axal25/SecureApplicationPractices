package backend.app.secureapppractices.model;

public class Comment {
    private String id;
    private String commentator;
    private String comment;

    public Comment(String id, String commentator, String comment) {
        this.id = id;
        this.commentator = commentator;
        this.comment = comment;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommentator() {
        return commentator;
    }

    public void setCommentator(String commentator) {
        this.commentator = commentator;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
