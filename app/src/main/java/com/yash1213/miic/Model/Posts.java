package com.yash1213.miic.Model;

public class Posts {

    public String postId, imageUri, description;

    public Posts() {
    }

    public Posts(String postId, String imageUri, String description) {
        this.postId = postId;
        this.imageUri = imageUri;
        this.description = description;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
