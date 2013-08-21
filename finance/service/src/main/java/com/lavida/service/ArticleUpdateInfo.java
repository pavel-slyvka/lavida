package com.lavida.service;

/**
 * Created: 8:42 21.08.13
 * The ArticleUpdateInfo is a holder of needed information about updating process.
 *
 * @author Ruslan
 */
public class ArticleUpdateInfo {
    private int addedCount;
    private int updatedCount;
    private int deletedCount;

    public int getAddedCount() {
        return addedCount;
    }

    public void setAddedCount(int addedCount) {
        this.addedCount = addedCount;
    }

    public int getUpdatedCount() {
        return updatedCount;
    }

    public void setUpdatedCount(int updatedCount) {
        this.updatedCount = updatedCount;
    }

    public int getDeletedCount() {
        return deletedCount;
    }

    public void setDeletedCount(int deletedCount) {
        this.deletedCount = deletedCount;
    }

    @Override
    public String toString() {
        return "ArticleUpdateInfo{" +
                "addedCount=" + addedCount +
                ", updatedCount=" + updatedCount +
                ", deletedCount=" + deletedCount +
                '}';
    }
}
