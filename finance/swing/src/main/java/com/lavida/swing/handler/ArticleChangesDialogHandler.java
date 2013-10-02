package com.lavida.swing.handler;

import com.google.gdata.util.ServiceException;
import com.lavida.service.entity.ArticleChangedFieldJdo;
import com.lavida.service.entity.ArticleJdo;
import com.lavida.service.utils.DateConverter;
import com.lavida.swing.LocaleHolder;
import com.lavida.swing.dialog.ArticleChangesDialog;
import com.lavida.swing.service.ArticleChangedFieldServiceSwingWrapper;
import com.lavida.swing.service.ArticleServiceSwingWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * The ArticleChangesDialogHandler
 * <p/>
 * Created: 02.10.13 13:21.
 *
 * @author Ruslan.
 */
@Component
public class ArticleChangesDialogHandler {
    private static final Logger logger = LoggerFactory.getLogger(ArticleChangesDialogHandler.class);

    @Resource
    private MessageSource messageSource;

    @Resource
    private LocaleHolder localeHolder;

    @Resource
    private ArticleChangesDialog dialog;

    @Resource
    private ArticleChangedFieldServiceSwingWrapper articleChangedFieldServiceSwingWrapper;

    @Resource
    private ArticleServiceSwingWrapper articleServiceSwingWrapper;


    public void cancelButtonClicked() {
        dialog.getTableModel().setSelectedArticleChangedField(null);
        dialog.hide();

    }

    public void deselectAllItemClicked() {
        for (ArticleChangedFieldJdo articleChangedFieldJdo : dialog.getTableModel().getTableData()) {
            if (articleChangedFieldJdo.isSelected()) {
                articleChangedFieldJdo.setSelected(false);
            }
        }
    }

    public void deleteSelectedItemClicked() {
        for (ArticleChangedFieldJdo articleChangedFieldJdo : dialog.getTableModel().getTableData()) {
            if (articleChangedFieldJdo.isSelected()) {
                articleChangedFieldServiceSwingWrapper.delete(articleChangedFieldJdo);
            }
        }
    }

    public void revertChangesItemClicked() {
        for (ArticleChangedFieldJdo articleChangedFieldJdo : dialog.getTableModel().getTableData()) {
            if (articleChangedFieldJdo.isSelected()) {
                if ("code".equals(articleChangedFieldJdo.getFieldName()) || "size".equals(articleChangedFieldJdo.getFieldName())) {
                    Toolkit.getDefaultToolkit().beep();
                    dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.article.changedField.illegal.field.message");
                    return;
                }
                if (ArticleChangedFieldJdo.OperationType.DELETED.equals(articleChangedFieldJdo.getOperationType()) ||
                        ArticleChangedFieldJdo.OperationType.SAVED.equals(articleChangedFieldJdo.getOperationType())) {
                    Toolkit.getDefaultToolkit().beep();
                    dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.article.changedField.illegal.operationType.message");
                    return;
                }
                ArticleJdo articleJdo = revertArticleChangedField(articleChangedFieldJdo);
                articleChangedFieldServiceSwingWrapper.delete(articleChangedFieldJdo);
                try {
                    articleServiceSwingWrapper.updateToSpreadsheet(articleJdo, null);
                } catch (IOException | ServiceException e) {
                    logger.warn(e.getMessage(), e);
                    articleJdo.setPostponedOperationDate(new Date());
                }
                articleServiceSwingWrapper.update(articleJdo);
            }
        }

    }

    private ArticleJdo revertArticleChangedField(ArticleChangedFieldJdo articleChangedFieldJdo) {
        ArticleJdo revertedArticle = null;
        for (ArticleJdo articleJdo : articleServiceSwingWrapper.getAll()) {
            if ((articleChangedFieldJdo.getCode() != null ? articleChangedFieldJdo.getCode().equals(articleJdo.getCode())
                    : articleJdo.getCode() == null) && (articleChangedFieldJdo.getSize() != null ?
                    articleChangedFieldJdo.getSize().equals(articleJdo.getSize()) : articleJdo.getSize() == null) &&
                    articleFieldHasValue(articleJdo, articleChangedFieldJdo.getFieldName(), articleChangedFieldJdo.getNewValue())
                    ) {
                SimpleDateFormat calendarFormatter = new SimpleDateFormat("dd.MM.yyyy");
                calendarFormatter.setLenient(false);
                String value = articleChangedFieldJdo.getOldValue();
                try {
                    Field field = ArticleJdo.class.getDeclaredField(articleChangedFieldJdo.getFieldName());
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
                                    logger.error(e.getMessage(), e);
                                    throw new RuntimeException("Wrong calendar format for " + value + "!", e);
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
                                    logger.error(e.getMessage(), e);
                                    throw new RuntimeException("Wrong date format for " + value + "!", e);
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

        if (value.equals(fieldValue)) return true;
        return false;
    }

    private double fixIfNeedAndParseDouble(String doubleString) {
        if (doubleString == null || doubleString.trim().isEmpty()) {
            return 0;
        }
        return Double.parseDouble(doubleString.replace(" ", "").replace(",", "."));
    }

}
