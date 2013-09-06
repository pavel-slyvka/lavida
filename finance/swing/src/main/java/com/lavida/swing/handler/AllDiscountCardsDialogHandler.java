package com.lavida.swing.handler;

import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.AllDiscountCardsDialog;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * The handler for the {@link com.lavida.swing.dialog.AllDiscountCardsDialog}.
 * Created: 11:34 06.09.13
 *
 * @author Ruslan
 */
@Component
public class AllDiscountCardsDialogHandler {

    @Resource
    private AllDiscountCardsDialog dialog;

    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;


    public void activateButtonClicked() {

    }

    public void disableButtonClicked() {

    }

    public void cancelButtonClicked() {

    }
}
