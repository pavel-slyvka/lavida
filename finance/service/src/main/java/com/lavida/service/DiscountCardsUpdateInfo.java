package com.lavida.service;

/**
 * Created: 11:27 16.09.13
 *
 * @author Ruslan
 */
public class DiscountCardsUpdateInfo {
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
        return "DiscountCardsUpdateInfo{" +
                "addedCount=" + addedCount +
                ", updatedCount=" + updatedCount +
                ", deletedCount=" + deletedCount +
                '}';
    }
}
