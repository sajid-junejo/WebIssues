/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pojos;

/**
 *
 * @author sajid.ali
 */
public class Comments {
    private int commentId;
    private String commentText;
    private int commentFormat;

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public int getCommentFormat() {
        return commentFormat;
    }

    public void setCommentFormat(int commentFormat) {
        this.commentFormat = commentFormat;
    }
}
