package com.horsnby.gladesvillehorsnby.models;

import java.util.ArrayList;
import java.util.List;

public class NewResponse {

    /**
     * error : false
     * message : success
     * data : [{"eventId":30,"memberId":31,"eventName":"Baseball practise","eventStart":"2018-04-28T09:37:00","eventEnd":"2018-04-28T09:37:00","location":"","groupId":158,"groupName":"Testing","addNotificationToGroupHome":false,"sendEmailNotification":false,"notes":"Bring your own bats","longitude":null,"latitude":null,"dateAdded":"2018-04-16T09:37:44.857","comments":[{"eventCommentId":12,"eventId":30,"comment":"Hello","commentDate":"2018-04-16T13:28:02.93","memberId":24,"memberName":"Vamsi Sairam","memberAvatar":"https://baseballnsw.blob.core.windows.net/profileimages/24_avatar.jpg"},{"eventCommentId":11,"eventId":30,"comment":"hi","commentDate":"2018-04-16T09:59:25.587","memberId":24,"memberName":"Vamsi Sairam","memberAvatar":"https://baseballnsw.blob.core.windows.net/profileimages/24_avatar.jpg"}],"memberAvatar":"https://sportsclubhqprod.blob.core.windows.net/profileimages/3244_avatar.jpg"},{"eventId":31,"memberId":31,"eventName":"Fund raised bake sale","eventStart":"2018-05-05T09:37:00","eventEnd":"2018-05-05T09:37:00","location":"","groupId":158,"groupName":"Testing","addNotificationToGroupHome":false,"sendEmailNotification":false,"notes":"Get your mum to bring food.","longitude":null,"latitude":null,"dateAdded":"2018-04-16T09:38:09.98","comments":[],"memberAvatar":"https://sportsclubhqprod.blob.core.windows.net/profileimages/3244_avatar.jpg"}]
     */

    private boolean error;
    private String message;
    private List<Event> data = new ArrayList<>();

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Event> getData() {
        return data;
    }

    public void setData(List<Event> data) {
        this.data = data;
    }

    public static class Event {
        /**
         * eventId : 30
         * memberId : 31
         * eventName : Baseball practise
         * eventStart : 2018-04-28T09:37:00
         * eventEnd : 2018-04-28T09:37:00
         * location :
         * groupId : 158
         * groupName : Testing
         * addNotificationToGroupHome : false
         * sendEmailNotification : false
         * notes : Bring your own bats
         * longitude : null
         * latitude : null
         * dateAdded : 2018-04-16T09:37:44.857
         * comments : [{"eventCommentId":12,"eventId":30,"comment":"Hello","commentDate":"2018-04-16T13:28:02.93","memberId":24,"memberName":"Vamsi Sairam","memberAvatar":"https://baseballnsw.blob.core.windows.net/profileimages/24_avatar.jpg"},{"eventCommentId":11,"eventId":30,"comment":"hi","commentDate":"2018-04-16T09:59:25.587","memberId":24,"memberName":"Vamsi Sairam","memberAvatar":"https://baseballnsw.blob.core.windows.net/profileimages/24_avatar.jpg"}]
         * memberAvatar : https://sportsclubhqprod.blob.core.windows.net/profileimages/3244_avatar.jpg
         */

        private int eventId;
        private int memberId;
        private String eventName;
        private String eventStart;
        private String eventEnd;
        private String location;
        private int groupId;
        private String groupName;
        private boolean addNotificationToGroupHome;
        private boolean sendEmailNotification;
        private String notes;
        private Object longitude;
        private Object latitude;
        private String dateAdded;
        private String memberAvatar;
        private List<Comments> comments;

        public int getEventId() {
            return eventId;
        }

        public void setEventId(int eventId) {
            this.eventId = eventId;
        }

        public int getMemberId() {
            return memberId;
        }

        public void setMemberId(int memberId) {
            this.memberId = memberId;
        }

        public String getEventName() {
            return eventName;
        }

        public void setEventName(String eventName) {
            this.eventName = eventName;
        }

        public String getEventStart() {
            return eventStart;
        }

        public void setEventStart(String eventStart) {
            this.eventStart = eventStart;
        }

        public String getEventEnd() {
            return eventEnd;
        }

        public void setEventEnd(String eventEnd) {
            this.eventEnd = eventEnd;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getGroupId() {
            return groupId;
        }

        public void setGroupId(int groupId) {
            this.groupId = groupId;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public boolean isAddNotificationToGroupHome() {
            return addNotificationToGroupHome;
        }

        public void setAddNotificationToGroupHome(boolean addNotificationToGroupHome) {
            this.addNotificationToGroupHome = addNotificationToGroupHome;
        }

        public boolean isSendEmailNotification() {
            return sendEmailNotification;
        }

        public void setSendEmailNotification(boolean sendEmailNotification) {
            this.sendEmailNotification = sendEmailNotification;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public Object getLongitude() {
            return longitude;
        }

        public void setLongitude(Object longitude) {
            this.longitude = longitude;
        }

        public Object getLatitude() {
            return latitude;
        }

        public void setLatitude(Object latitude) {
            this.latitude = latitude;
        }

        public String getDateAdded() {
            return dateAdded;
        }

        public void setDateAdded(String dateAdded) {
            this.dateAdded = dateAdded;
        }

        public String getMemberAvatar() {
            return memberAvatar;
        }

        public void setMemberAvatar(String memberAvatar) {
            this.memberAvatar = memberAvatar;
        }

        public List<Comments> getComments() {
            return comments;
        }

        public void setComments(List<Comments> comments) {
            this.comments = comments;
        }

        public static class Comments {
            /**
             * eventCommentId : 12
             * eventId : 30
             * comment : Hello
             * commentDate : 2018-04-16T13:28:02.93
             * memberId : 24
             * memberName : Vamsi Sairam
             * memberAvatar : https://baseballnsw.blob.core.windows.net/profileimages/24_avatar.jpg
             */

            private int eventCommentId;
            private int eventId;
            private String comment;
            private String commentDate;
            private int memberId;
            private String memberName;
            private String memberAvatar;

            public int getEventCommentId() {
                return eventCommentId;
            }

            public void setEventCommentId(int eventCommentId) {
                this.eventCommentId = eventCommentId;
            }

            public int getEventId() {
                return eventId;
            }

            public void setEventId(int eventId) {
                this.eventId = eventId;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getCommentDate() {
                return commentDate;
            }

            public void setCommentDate(String commentDate) {
                this.commentDate = commentDate;
            }

            public int getMemberId() {
                return memberId;
            }

            public void setMemberId(int memberId) {
                this.memberId = memberId;
            }

            public String getMemberName() {
                return memberName;
            }

            public void setMemberName(String memberName) {
                this.memberName = memberName;
            }

            public String getMemberAvatar() {
                return memberAvatar;
            }

            public void setMemberAvatar(String memberAvatar) {
                this.memberAvatar = memberAvatar;
            }
        }
    }
}
