package com.lavida.service;

import com.lavida.service.dao.TagDao;
import com.lavida.service.entity.TagJdo;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created: 18:59 19.08.13
 * The TagService is a service for the TagJdo.
 * @author Ruslan
 */
@Service
public class TagService {

    @Resource
    private TagDao tagDao;

    @Transactional
    public void save(TagJdo tagJdo) {
        tagDao.put(tagJdo);
    }

    @Transactional
    public void update(TagJdo tagJdo) {
        tagDao.update(tagJdo);
    }

    public TagJdo getById(int id) {
        return tagDao.getById(TagJdo.class, id);
    }

    public List<TagJdo> getAll () {
        return tagDao.getAll(TagJdo.class);
    }

    @Transactional
    public void delete (int id) {
        tagDao.delete(TagJdo.class, id);
    }

    public TagJdo getTagByName (String name) {
         return tagDao.getTagByName(name);
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-security.xml");
        TagService service = context.getBean(TagService.class);
        service.save(new TagJdo("cash", "Наличные", new Date(), true));
        service.save(new TagJdo("terminal", "Терминал", new Date(), true));
        System.out.println(service.getAll());
    }

}
