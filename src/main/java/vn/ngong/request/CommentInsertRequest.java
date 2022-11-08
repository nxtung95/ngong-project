package vn.ngong.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class CommentInsertRequest {
    @Column(name = "parrent_id")
    private String parrentId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "rate")
    private int rate;

    @Column(name = "title")
    private String title;

    @Column(name = "comment")
    private String comment;

    @Column(name = "code")
    private String video;

    @Column(name = "code")
    private String images;
}
