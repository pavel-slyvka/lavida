package com.lavida.swing.handler;

import com.lavida.service.entity.ChangedFieldJdo;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.entity.DiscountCardJdo;
import com.lavida.service.remote.google.LavidaGoogleException;
import com.lavida.service.utils.DateConverter;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.RefreshChangesDialog;
import com.lavida.swing.exception.LavidaSwingRuntimeException;
import com.lavida.swing.exception.RemoteUpdateException;
import com.lavida.swing.service.ChangedFieldServiceSwingWrapper;
import com.lavida.swing.service.ArticleServiceSwingWrapper;
import com.lavida.swing.service.ConcurrentOperationsService;
import com.lavida.swing.service.DiscountCardServiceSwingWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.awt.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The RefreshChangesDialogHandler
 * <p/>
 * Created: 02.10.13 13:21.
 *
 * @author Ruslan.
 */
@Component
public class RefreshChangesDialogHandler {
    private static final Logger logger = LoggerFactory.getLogger(RefreshChangesDialogHandler.class);

    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;

    @Resource
    private RefreshChangesDialog dialog;

    @Resource
    private ChangedFieldServiceSwingWrapper changedFieldServiceSwingWrapper;

    @Resource
    private ArticleServiceSwingWrapper articleServiceSwingWrapper;

    @Resource
    private DiscountCardServiceSwingWrapper discountCardServiceSwingWrapper;

    @Resource
    private ConcurrentOperationsService concurrentOperationsService;


    public void cancelButtonClicked() {
        dialog.getTableModel().setSelectedChangedField(null);
        dialog.hide();

    }

    public void deselectAllItemClicked() {
        for (ChangedFieldJdo changedFieldJdo : dialog.getTableModel().getTableData()) {
            if (changedFieldJdo.isSelected()) {
                changedFieldJdo.setSelected(false);
            }
        }
    }

    public void deleteSelectedItemClicked() {
        for (ChangedFieldJdo changedFieldJdo : dialog.getTableModel().getTableData()) {
            if (changedFieldJdo.isSelected()) {
                changedFieldServiceSwingWrapper.delete(changedFieldJdo);
            }
        }
    }

    public void revertChangesItemClicked() {
        concurrentOperationsService.startOperation("Reverting changes", new Runnable() {
            @Override
            public void run() {
                boolean postponed = false;
                Exception exception = null;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(messageSource.getMessage("dialog.changed.field.refresh.revert.finish.message", null, localeHolder.getLocale()));
                stringBuilder.append("\n");
                for (ChangedFieldJdo changedFieldJdo : dialog.getTableModel().getTableData()) {
                    if (changedFieldJdo.isSelected()) {
                        if ("code".equals(changedFieldJdo.getFieldName()) || "size".equals(changedFieldJdo.getFieldName())) {
                            Toolkit.getDefaultToolkit().beep();
                            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.article.changedField.illegal.field.message");
                            return;
                        }
                        if (ChangedFieldJdo.RefreshOperationType.DELETED.equals(changedFieldJdo.getRefreshOperationType()) ||
                                ChangedFieldJdo.RefreshOperationType.SAVED.equals(changedFieldJdo.getRefreshOperationType())) {
                            Toolkit.getDefaultToolkit().beep();
                            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.article.changedField.illegal.operationType.message");
                            return;
                        }
                        switch (changedFieldJdo.getObjectType()) {
                            case ARTICLE:
                                ArticleJdo articleJdo = revertArticleChangedField(changedFieldJdo);
                                ArticleJdo oldArticleJdo;
                                try {
                                    oldArticleJdo = (ArticleJdo) articleJdo.clone();
                                } catch (CloneNotSupportedException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    articleServiceSwingWrapper.updateToSpreadsheet(oldArticleJdo, articleJdo, null);
                                } catch (RemoteUpdateException | LavidaGoogleException e) {
                                    postponed = true;
                                    exception = e;
                                }

                            case DISCOUNT_CARD:
                                DiscountCardJdo discountCardJdo = revertDiscountCardChangedField (changedFieldJdo);
                                DiscountCardJdo oldDiscountCardJdo;
                                try {
                                    oldDiscountCardJdo = (DiscountCardJdo) discountCardJdo.clone();
                                } catch (CloneNotSupportedException e) {
                                    throw new RuntimeException(e);
                                }
                                try {
                                    discountCardServiceSwingWrapper.updateToSpreadsheet(oldDiscountCardJdo, discountCardJdo);
                                } catch (RemoteUpdateException | LavidaGoogleException e) {
                                    postponed = true;
                                    exception = e;
                                }
                        }
                        changedFieldServiceSwingWrapper.delete(changedFieldJdo);

                    }
                }
                String message = convertToMultiline(new String(stringBuilder));
                dialog.getMainForm().showInfoToolTip(message);
                if (postponed) {
                    if (exception instanceof RemoteUpdateException) {
                        throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.GOOGLE_SERVICE_EXCEPTION, exception);
                    } else if (exception instanceof LavidaGoogleException) {
                        throw new LavidaSwingRuntimeException(((LavidaGoogleException) exception).getErrorCode(), exception);
                    }
                }
            }
        });

    }

    private String convertToMultiline(String orig) {
        return "<html>" + orig.replaceAll("\n", "<br>"); //+ "</html>"
    }

    private DiscountCardJdo revertDiscountCardChangedField(ChangedFieldJdo changedFieldJdo) {
        DiscountCardJdo revertedCard = null;
        for (DiscountCardJdo discountCardJdo : discountCardServiceSwingWrapper.getAll()) {
            if (changedFieldJdo.getCode() != null ? changedFieldJdo.getCode().equals(discountCardJdo.getNumber()) :
                    discountCardJdo.getNumber() == null) {
                SimpleDateFormat calendarFormatter = new SimpleDateFormat("dd.MM.yyyy");
                calendarFormatter.setLenient(false);
                String value = changedFieldJdo.getOldValue();
                try {
                    Field field = DiscountCardJdo.class.getDeclaredField(changedFieldJdo.getFieldName());
                    field.setAccessible(true);
                    if (int.class == field.getType()) {
                        int typeValue;
                        if (value.isEmpty()) {
                            typeValue = 0;
                        } else {
                            typeValue = Integer.parseInt(value);
                        }
                        field.setInt(discountCardJdo, typeValue);
                    } else if (boolean.class == field.getType()) {
                        boolean typeValue = Boolean.parseBoolean(value);
                        field.setBoolean(discountCardJdo, typeValue);
                    } else if (double.class == field.getType()) {
                        double typeValue;
                        if (value.isEmpty()) {
                            typeValue = 0.0;
                        } else {
                            typeValue = fixIfNeedAndParseDouble(value);
                            typeValue = BigDecimal.valueOf(typeValue).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                        }
                        field.setDouble(discountCardJdo, typeValue);
                    } else if (char.class == field.getType()) {
                        char typeValue = value.charAt(0);
                        field.setChar(discountCardJdo, typeValue);
                    } else if (long.class == field.getType()) {
                        long typeValue;
                        if (value.isEmpty()) {
                            typeValue = 0l;
                        } else {
                            typeValue = Long.parseLong(value);
                        }
                        field.setLong(discountCardJdo, typeValue);
                    } else if (Integer.class == field.getType()) {
                        Integer typeValue;
                        if (value.isEmpty()) {
                            typeValue = 0;
                        } else {
                            typeValue = Integer.parseInt(value);
                        }
                        field.set(discountCardJdo, typeValue);
                    } else if (Boolean.class == field.getType()) {
                        Boolean typeValue = Boolean.parseBoolean(value);
                        field.set(discountCardJdo, typeValue);
                    } else if (Double.class == field.getType()) {
                        Double typeValue;
                        if (value.isEmpty()) {
                            typeValue = 0.0;
                        } else {
                            typeValue = fixIfNeedAndParseDouble(value);
                            typeValue = BigDecimal.valueOf(typeValue).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                        }
                        field.set(discountCardJdo, typeValue);
                    } else if (Character.class == field.getType()) {
                        Character typeValue = value.charAt(0);
                        field.set(discountCardJdo, typeValue);
                    } else if (Long.class == field.getType()) {
                        Long typeValue;
                        if (value.isEmpty()) {
                            typeValue = 0L;
                        } else {
                            typeValue = Long.parseLong(value);
                        }
                        field.set(discountCardJdo, typeValue);
                    } else if (Calendar.class == field.getType()) {
                        Calendar typeValue = Calendar.getInstance();
                        if (!value.isEmpty()) {
                            String formattedValue = value.replace(",", ".").trim();
                            if (formattedValue.matches("\\d{2}.\\d{2}.\\d{4}")) {
                                Date time;
                                try {
                                    time = calendarFormatter.parse(formattedValue);
                                } catch (ParseException e) {
                                    throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.DATE_FORMAT_EXCEPTION, e);
                                }
                                typeValue.setTime(time);
                                field.set(discountCardJdo, typeValue);
                            }
                        } else {
                            field.set(discountCardJdo, null);
                        }
                    } else if (Date.class == field.getType()) {
                        if (!value.isEmpty()) {
                            String formattedValue = value.replace(",", ".").trim();
                            if (formattedValue.matches("\\d{2}.\\d{2}.\\d{4} \\d{2}:\\d{2}:\\d{2}")) {
                                Date typeValue;
                                try {
                                    typeValue = DateConverter.convertStringToDate(formattedValue);
                                } catch (ParseException e) {
                                    throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.DATE_FORMAT_EXCEPTION, e);
                                }
                                field.set(discountCardJdo, typeValue);
                            }
                        } else {
                            field.set(discountCardJdo, null);
                        }
                    } else {
                        field.set(discountCardJdo, value);
                    }

                } catch (NoSuchFieldException | IllegalAccessException e) {
                                    throw new RuntimeException(e);
                }
                revertedCard = discountCardJdo;
                break;
            }
        }

        return revertedCard;
    }

    private ArticleJdo revertArticleChangedField(ChangedFieldJdo changedFieldJdo) {
        ArticleJdo revertedArticle = null;
        for (ArticleJdo articleJdo : articleServiceSwingWrapper.getAll()) {
            if ((changedFieldJdo.getCode() != null ? changedFieldJdo.getCode().equals(articleJdo.getCode())
                    : articleJdo.getCode() == null) && (changedFieldJdo.getSize() != null ?
                    changedFieldJdo.getSize().equals(articleJdo.getSize()) : articleJdo.getSize() == null) &&
                    articleFieldHasValue(articleJdo, changedFieldJdo.getFieldName(), changedFieldJdo.getNewValue())
                    ) {
                SimpleDateFormat calendarFormatter = new SimpleDateFormat("dd.MM.yyyy");
                calendarFormatter.setLenient(false);
                String value = changedFieldJdo.getOldValue();
                try {
                    Field field = ArticleJdo.class.getDeclaredField(changedFieldJdo.getFieldName());
                    field.setAccessible(true);
                    if (int.class == field.getType()) {
                        int typeValue;
                        if (value.isEmpty()) {
                            typeValue = 0;
                        } else {
                            typeValue = Integer.parseInt(value);
                        }
                        field.setInt(articleJdo, typeValue);
                    } else if (boolean.class == field.getType()) {
                        boolean typeValue = Boolean.parseBoolean(value);
                        field.setBoolean(articleJdo, typeValue);
                    } else if (double.class == field.getType()) {
                        double typeValue;
                        if (value.isEmpty()) {
                            typeValue = 0.0;
                        } else {
                            typeValue = fixIfNeedAndParseDouble(value);
                            typeValue = BigDecimal.valueOf(typeValue).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                        }
                        field.setDouble(articleJdo, typeValue);
                    } else if (char.class == field.getType()) {
                        char typeValue = value.charAt(0);
                        field.setChar(articleJdo, typeValue);
                    } else if (long.class == field.getType()) {
                        long typeValue;
                        if (value.isEmpty()) {
                            typeValue = 0l;
                        } else {
                            typeValue = Long.parseLong(value);
                        }
                        field.setLong(articleJdo, typeValue);
                    } else if (Integer.class == field.getType()) {
                        Integer typeValue;
                        if (value.isEmpty()) {
                            typeValue = 0;
                        } else {
                            typeValue = Integer.parseInt(value);
                        }
                        field.set(articleJdo, typeValue);
                    } else if (Boolean.class == field.getType()) {
                        Boolean typeValue = Boolean.parseBoolean(value);
                        field.set(articleJdo, typeValue);
                    } else if (Double.class == field.getType()) {
                        Double typeValue;
                        if (value.isEmpty()) {
                            typeValue = 0.0;
                        } else {
                            typeValue = fixIfNeedAndParseDouble(value);
                            typeValue = BigDecimal.valueOf(typeValue).setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
                        }
                        field.set(articleJdo, typeValue);
                    } else if (Character.class == field.getType()) {
                        Character typeValue = value.charAt(0);
                        field.set(articleJdo, typeValue);
                    } else if (Long.class == field.getType()) {
                        Long typeValue;
                        if (value.isEmpty()) {
                            typeValue = 0L;
                        } else {
                            typeValue = Long.parseLong(value);
                        }
                        field.set(articleJdo, typeValue);
                    } else if (Calendar.class == field.getType()) {
                        Calendar typeValue = Calendar.getInstance();
                        if (!value.isEmpty()) {
                            String formattedValue = value.replace(",", ".").trim();
                            if (formattedValue.matches("\\d{2}.\\d{2}.\\d{4}")) {
                                Date time;
                                try {
                                    time = calendarFormatter.parse(formattedValue);
                                } catch (ParseException e) {
                                    throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.DATE_FORMAT_EXCEPTION, e);
                                }
                                typeValue.setTime(time);
                                field.set(articleJdo, typeValue);
                            }
                        } else {
                            field.set(articleJdo, null);
                        }
                    } else if (Date.class == field.getType()) {
                        if (!value.isEmpty()) {
                            String formattedValue = value.replace(",", ".").trim();
                            if (formattedValue.matches("\\d{2}.\\d{2}.\\d{4} \\d{2}:\\d{2}:\\d{2}")) {
                                Date typeValue;
                                try {
                                    typeValue = DateConverter.convertStringToDate(formattedValue);
                                } catch (ParseException e) {
                                    throw new LavidaSwingRuntimeException(LavidaSwingRuntimeException.DATE_FORMAT_EXCEPTION, e);
                                }
                                field.set(articleJdo, typeValue);
                            }
                        } else {
                            field.set(articleJdo, null);
                        }
                    } else {
                        field.set(articleJdo, value);
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new RuntimeException(e);
                }
                revertedArticle = articleJdo;
                break;
            }
        }

        return revertedArticle;
    }

    private boolean articleFieldHasValue(ArticleJdo articleJdo, String fieldName, String value) {
        String fieldValue;
        SimpleDateFormat calendarFormatter = new SimpleDateFormat("dd.MM.yyyy");
        calendarFormatter.setLenient(false);
        try {
            Field field = ArticleJdo.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            if (int.class == field.getType()) {
                fieldValue = String.valueOf(field.getInt(articleJdo));
            } else if (boolean.class == field.getType()) {
                fieldValue = String.valueOf(field.getBoolean(articleJdo));
            } else if (double.class == field.getType()) {
                fieldValue = String.valueOf(field.getDouble(articleJdo));
            } else if (char.class == field.getType()) {
                fieldValue = String.valueOf(field.getChar(articleJdo));
            } else if (long.class == field.getType()) {
                fieldValue = String.valueOf(field.getLong(articleJdo));
            } else if (Integer.class == field.getType()) {
                fieldValue = String.valueOf(field.getInt(articleJdo));
            } else if (Boolean.class == field.getType()) {
                fieldValue = String.valueOf(field.getBoolean(articleJdo));
            } else if (Double.class == field.getType()) {
                fieldValue = String.valueOf(field.getDouble(articleJdo));
            } else if (Character.class == field.getType()) {
                fieldValue = String.valueOf(field.getChar(articleJdo));
            } else if (Long.class == field.getType()) {
                fieldValue = String.valueOf(field.getLong(articleJdo));
            } else if (Calendar.class == field.getType()) {
                fieldValue = field.get(articleJdo) != null ? calendarFormatter.format(((Calendar) field.get(articleJdo)).getTime()) : "";
            } else if (Date.class == field.getType()) {
                fieldValue = field.get(articleJdo) != null ? DateConverter.convertDateToString((Date) field.get(articleJdo)) : "";
            } else {
                fieldValue = field.get(articleJdo) != null ? (String) field.get(articleJdo) : "";
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("There is no such field: " + fieldName + " in the ArticleJdo.class!", e);
        }

        return (value.equals(fieldValue));
    }

    private double fixIfNeedAndParseDouble(String doubleString) {
        if (doubleString == null || doubleString.trim().isEmpty()) {
            return 0;
        }
        return Double.parseDouble(doubleString.replace(" ", "").replace(",", "."));
    }

}
