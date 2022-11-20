package vn.ngong.request;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class CommentInsertRequest {
    private int parrentId;

    private int productId;

    private String userName;

    private String email;

    private String phoneNumber;

    private int rate;

    private String title;

    private String comment;

    private String video;

    private String images;
}
