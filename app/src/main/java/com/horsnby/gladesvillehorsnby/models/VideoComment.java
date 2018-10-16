package com.horsnby.gladesvillehorsnby.models;

import com.horsnby.gladesvillehorsnby.DM;

import java.util.Date;

public class VideoComment {

    public int mediaId;
    public int mediaCommentId;
    public String comment;
    public int memberId;
    public String member;
    public String memberAvatar;
    public Date commentDate;

    public String getTimeAgo()
    {
        return DM.getTimeAgo(commentDate);
    }
}
