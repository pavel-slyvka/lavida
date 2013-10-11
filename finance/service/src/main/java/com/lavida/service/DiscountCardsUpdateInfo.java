package com.lavida.service;

import com.lavida.service.entity.ChangedFieldJdo;

import java.util.List;

/**
 * Created: 11:27 16.09.13
 *
 * @author Ruslan
 */
public class DiscountCardsUpdateInfo {
    private int addedCount;
    private int updatedCount;
    private int deletedCount;
    private List<ChangedFieldJdo> changedFieldJdoList;

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

    public List<ChangedFieldJdo> getChangedFieldJdoList() {
        return changedFieldJdoList;
    }

    public void setChangedFieldJdoList(List<ChangedFieldJdo> changedFieldJdoList) {
        this.changedFieldJdoList = changedFieldJdoList;
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
