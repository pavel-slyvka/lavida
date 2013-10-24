package com.lavida.service;

import com.lavida.service.dao.UrlDao;
import com.lavida.service.entity.Url;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * The UrlService
 * <p/>
 * Created: 24.10.13 11:31.
 *
 * @author Ruslan.
 */
@Service
public class UrlService {

    @Resource
    private UrlDao urlDao;

    @Transactional
    public void save (Url url) {
        urlDao.put(url);
    }

    @Transactional
    public void update (Url url) {
        urlDao.update(url);
    }

    @Transactional
    public void delete (int id) {
        urlDao.delete(Url.class, id);
    }

    public Url getById (int id) {
        return urlDao.getById(Url.class, id);
    }

    public List<Url> getAll () {
        return urlDao.getAll(Url.class);
    }

    public Url findByUrlString (String urlString) {
        return urlDao.findByUrlString(urlString);
    }

}
