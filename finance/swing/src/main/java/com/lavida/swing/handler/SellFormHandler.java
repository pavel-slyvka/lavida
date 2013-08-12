package com.lavida.swing.handler;

import com.lavida.swing.form.SellForm;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created: 12:04 12.08.13
 *
 * @author Ruslan
 */
@Component
public class SellFormHandler {
    @Resource
    private SellForm form;
}
