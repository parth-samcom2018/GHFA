package com.horsnby.gladesvillehorsnby.models;

import com.horsnby.gladesvillehorsnby.DM;

import java.util.Date;

public class EventComment {
    public int eventCommentId;
    public int eventId;
    public String comment;
    public Date commentDate;
    public int memberId;
    public String memberName;
    public String memberAvatar;

    public String getTimeAgo() {
        return DM.getTimeAgo(commentDate);
    }

}
