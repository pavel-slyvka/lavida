package com.lavida.service;

import com.lavida.service.entity.ArticleJdo;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * ArticleServiceTest.
 * Created: 14:10 21.08.13
 *
 * @author Pavel
 */
public class ArticleServiceTest {

    @Test
    public void testUpdateDatabase() {
        List<ArticleJdo> dbOldArticles = Arrays.asList(
                createArticleJdo(2, 2, "222", "name2", "brand2", "S", 100.5, null),
                createArticleJdo(3, 4, "444", "name4", "brand4", "S", 100.5, null),
                createArticleJdo(1, 1, "111", "name1", "brand1", "S", 100.5, null)
        );
        List<ArticleJdo> remoteArticles = Arrays.asList(
                createArticleJdo(0, 1, "111", "name1", "brand1", "S", 100.5, null),
                createArticleJdo(0, 2, "333", "name3", "brand3", "S", 100.5, null),
                createArticleJdo(0, 3, "222", "name2", "brand2", "S", 100.5, null)
        );
        List<ArticleJdo> toAdd = Arrays.asList(
                createArticleJdo(0, 3, "222", "name2", "brand2", "S", 100.5, null)
        );
        List<ArticleJdo> toUpdate = Arrays.asList(
                createArticleJdo(2, 2, "333", "name3", "brand3", "S", 100.5, null)
        );
        List<ArticleJdo> toRemove = Arrays.asList(
                createArticleJdo(3, 4, "444", "name4", "brand4", "S", 100.5, null)
        );

        ArticleService articleService = spy(new ArticleService());
        doReturn(dbOldArticles).when(articleService).getAll();
        doNothing().when(articleService).save(any(List.class));
        doNothing().when(articleService).update(any(List.class));
        doNothing().when(articleService).remove(any(List.class));

        ArticleUpdateInfo articleUpdateInfo = articleService.updateDatabase(new ArrayList<ArticleJdo>(remoteArticles));

        assertEquals(toAdd.size(), articleUpdateInfo.getAddedCount());
        assertEquals(toUpdate.size(), articleUpdateInfo.getUpdatedCount());
        assertEquals(toRemove.size(), articleUpdateInfo.getDeletedCount());
        verify(articleService, times(1)).save(toAdd);
        verify(articleService, times(1)).update(toUpdate);
        verify(articleService, times(1)).remove(toRemove);
    }

    private ArticleJdo createArticleJdo(int dbId, int spreadsheetId, String code, String name, String brand, String size, double priceUAH, String sold) {
        ArticleJdo articleJdo = new ArticleJdo();
        articleJdo.setId(dbId);
        articleJdo.setSpreadsheetRow(spreadsheetId);
        articleJdo.setCode(code);
        articleJdo.setName(name);
        articleJdo.setBrand(brand);
        articleJdo.setSize(size);
        articleJdo.setPriceUAH(priceUAH);
        articleJdo.setSold(sold);
        return articleJdo;
    }
}
