package com.coding.test.githubreposearch;

/**
 * Created by winpr on 2/6/2018.
 */

class Repository {
    private String avatar;
    private String repoName;
    private String description;
    private int stars;

    String getAvatar() {
        return avatar;
    }

    void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    String getRepoName() {
        return repoName;
    }

    void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    int getStars() {
        return stars;
    }

    void setStars(int stars) {
        this.stars = stars;
    }
}
