package com.lavida.swing.handler;

import com.lavida.service.*;
import com.lavida.service.entity.*;
import com.lavida.swing.dialog.settings.SelectingCategoriesEditingDialog;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * The SelectingCategoriesEditingDialogHandler
 * <p/>
 * Created: 14.10.13 11:40.
 *
 * @author Ruslan.
 */
@Component
public class SelectingCategoriesEditingDialogHandler {

    @Resource
    private SelectingCategoriesEditingDialog dialog;

    @Resource
    private BrandService brandService;

    @Resource
    private SizeService sizeService;

    @Resource
    private ShopService shopService;

    @Resource
    private SellerService sellerService;

    @Resource
    private TagService tagService;


    public void changeButtonClicked() {
        String categoryName = (String) dialog.getCategoriesBox().getSelectedItem();

        if (SelectingCategoriesEditingDialog.CategoryType.BRAND.getName().equals(categoryName)) {
            String selectedValue = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.BRAND.getName()).getSelectedValue();
            String changedValue = (String) dialog.showInputDialog("dialog.selectingCategories.change.title",
                    "dialog.selectingCategories.change.message", null, null, selectedValue);
            if (changedValue != null) {
                BrandJdo brandJdo = brandService.getByName(selectedValue);
                brandJdo.setName(changedValue);
                brandService.update(brandJdo);
                JList<String> viewList = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.BRAND.getName());
                ((DefaultListModel<String>) viewList.getModel()).removeElement(selectedValue);
                ((DefaultListModel<String>) viewList.getModel()).addElement(changedValue);
                sortListModel((DefaultListModel<String>) viewList.getModel());
            }
        } else if (SelectingCategoriesEditingDialog.CategoryType.SIZE.getName().equals(categoryName)) {
            String selectedValue = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.SIZE.getName()).getSelectedValue();
            String changedValue = (String) dialog.showInputDialog("dialog.selectingCategories.change.title",
                    "dialog.selectingCategories.change.message", null, null, selectedValue);
            if (changedValue != null) {
                SizeJdo sizeJdo = sizeService.getByName(selectedValue);
                sizeJdo.setName(changedValue);
                sizeService.update(sizeJdo);
                JList<String> viewList = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.SIZE.getName());
                ((DefaultListModel<String>) viewList.getModel()).removeElement(selectedValue);
                ((DefaultListModel<String>) viewList.getModel()).addElement(changedValue);
                sortListModel((DefaultListModel<String>) viewList.getModel());
            }

        } else if (SelectingCategoriesEditingDialog.CategoryType.SHOP.getName().equals(categoryName)) {
            String selectedValue = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.SHOP.getName()).getSelectedValue();
            String changedValue = (String) dialog.showInputDialog("dialog.selectingCategories.change.title",
                    "dialog.selectingCategories.change.message", null, null, selectedValue);
            if (changedValue != null) {
                ShopJdo shopJdo = shopService.getByName(selectedValue);
                shopJdo.setName(changedValue);
                shopService.update(shopJdo);
                JList<String> viewList = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.SHOP.getName());
                ((DefaultListModel<String>) viewList.getModel()).removeElement(selectedValue);
                ((DefaultListModel<String>) viewList.getModel()).addElement(changedValue);
                sortListModel((DefaultListModel<String>) viewList.getModel());
            }

        } else if (SelectingCategoriesEditingDialog.CategoryType.SELLER.getName().equals(categoryName)) {
            String selectedValue = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.SELLER.getName()).getSelectedValue();
            String changedValue = (String) dialog.showInputDialog("dialog.selectingCategories.change.title",
                    "dialog.selectingCategories.change.message", null, null, selectedValue);
            if (changedValue != null) {
                SellerJdo sellerJdo = sellerService.getByName(selectedValue);
                sellerJdo.setName(changedValue);
                sellerService.update(sellerJdo);
                JList<String> viewList = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.SELLER.getName());
                ((DefaultListModel<String>) viewList.getModel()).removeElement(selectedValue);
                ((DefaultListModel<String>) viewList.getModel()).addElement(changedValue);
                sortListModel((DefaultListModel<String>) viewList.getModel());
            }

        } else if (SelectingCategoriesEditingDialog.CategoryType.TAG.getName().equals(categoryName)) {
            String selectedValue = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.TAG.getName()).getSelectedValue();
            TagJdo selectedTag = null;
            for (TagJdo tag : tagService.getAll()) {
                if (tag.getTitle().equals(selectedValue)) {
                    selectedTag = tag;
                }
            }
            if (selectedTag != null) {
                String changedTitle = (String) dialog.showInputDialog("dialog.selectingCategories.change.title",
                        "dialog.selectingCategories.change.message", null, null, selectedValue);
                if (changedTitle != null) {
                    String changedName = (String) dialog.showInputDialog("dialog.selectingCategories.change.tagName",
                            "dialog.selectingCategories.change.tagName.message", null, null, selectedTag.getName());
                    if (changedName != null) {
                        selectedTag.setTitle(changedTitle);
                        selectedTag.setName(changedName);
                        tagService.update(selectedTag);
                        JList<String> viewList = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.TAG.getName());
                        ((DefaultListModel<String>) viewList.getModel()).removeElement(selectedValue);
                        ((DefaultListModel<String>) viewList.getModel()).addElement(changedTitle);
                        sortListModel((DefaultListModel<String>) viewList.getModel());
                    }
                }
            }

        }
    }

    public void addButtonClicked() {
        String categoryName = (String) dialog.getCategoriesBox().getSelectedItem();
        String newValue = (String) dialog.showInputDialog("dialog.selectingCategories.add.title",
                "dialog.selectingCategories.add.message", null, null, null);
        if (newValue.isEmpty()) {
            dialog.showWarningMessage("mainForm.exception.message.dialog.title", "dialog.selecting.categories.editing.add.notEntered.message");
            return;
        }
        if (SelectingCategoriesEditingDialog.CategoryType.BRAND.getName().equals(categoryName)) {
            if (newValue != null) {
                BrandJdo brandJdo = new BrandJdo(newValue);
                brandService.update(brandJdo);
                JList<String> viewList = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.BRAND.getName());
                ((DefaultListModel<String>) viewList.getModel()).addElement(newValue);
                sortListModel((DefaultListModel<String>) viewList.getModel());
            }
        } else if (SelectingCategoriesEditingDialog.CategoryType.SIZE.getName().equals(categoryName)) {
            if (newValue != null) {
                SizeJdo sizeJdo = new SizeJdo(newValue);
                sizeService.update(sizeJdo);
                JList<String> viewList = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.SIZE.getName());
                ((DefaultListModel<String>) viewList.getModel()).addElement(newValue);
                sortListModel((DefaultListModel<String>) viewList.getModel());
            }

        } else if (SelectingCategoriesEditingDialog.CategoryType.SHOP.getName().equals(categoryName)) {
            if (newValue != null) {
                ShopJdo shopJdo = new ShopJdo(newValue);
                shopService.update(shopJdo);
                JList<String> viewList = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.SHOP.getName());
                ((DefaultListModel<String>) viewList.getModel()).addElement(newValue);
                sortListModel((DefaultListModel<String>) viewList.getModel());
            }

        } else if (SelectingCategoriesEditingDialog.CategoryType.SELLER.getName().equals(categoryName)) {
            if (newValue != null) {
                SellerJdo sellerJdo = new SellerJdo(newValue);
                sellerService.update(sellerJdo);
                JList<String> viewList = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.SELLER.getName());
                ((DefaultListModel<String>) viewList.getModel()).addElement(newValue);
                sortListModel((DefaultListModel<String>) viewList.getModel());
            }

        } else if (SelectingCategoriesEditingDialog.CategoryType.TAG.getName().equals(categoryName)) {
            String newTitle = newValue;
            if (newTitle != null) {
                String newName = (String) dialog.showInputDialog("dialog.selectingCategories.add.tagName.title",
                        "dialog.selectingCategories.add.tagName.message", null, null, null);
                if (newName != null) {
                    TagJdo selectedTag = new TagJdo(newName, newTitle, new Date(), true);
                    selectedTag.setTitle(newTitle);
                    selectedTag.setName(newName);
                    tagService.update(selectedTag);
                    JList<String> viewList = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.TAG.getName());
                    ((DefaultListModel<String>) viewList.getModel()).addElement(newTitle);
                    sortListModel((DefaultListModel<String>) viewList.getModel());
                }
            }
        }
    }

    public void deleteButtonClicked() {
        String categoryName = (String) dialog.getCategoriesBox().getSelectedItem();

        if (SelectingCategoriesEditingDialog.CategoryType.BRAND.getName().equals(categoryName)) {
            String selectedValue = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.BRAND.getName()).getSelectedValue();
            int result = dialog.showConfirmDialog("dialog.selectingCategories.delete.title", "dialog.selectingCategories.delete.message");
            switch (result) {
                case JOptionPane.YES_OPTION:
                    BrandJdo brandJdo = brandService.getByName(selectedValue);
                    brandService.delete(brandJdo.getId());
                    JList<String> viewList = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.BRAND.getName());
                    ((DefaultListModel<String>) viewList.getModel()).removeElement(selectedValue);
                    sortListModel((DefaultListModel<String>) viewList.getModel());
                    break;
                case JOptionPane.NO_OPTION:
            }
        } else if (SelectingCategoriesEditingDialog.CategoryType.SIZE.getName().equals(categoryName)) {
            String selectedValue = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.SIZE.getName()).getSelectedValue();
            int result = dialog.showConfirmDialog("dialog.selectingCategories.delete.title", "dialog.selectingCategories.delete.message");
            switch (result) {
                case JOptionPane.YES_OPTION:
                    SizeJdo sizeJdo = sizeService.getByName(selectedValue);
                    sizeService.delete(sizeJdo.getId());
                    JList<String> viewList = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.SIZE.getName());
                    ((DefaultListModel<String>) viewList.getModel()).removeElement(selectedValue);
                    sortListModel((DefaultListModel<String>) viewList.getModel());
                    break;
                case JOptionPane.NO_OPTION:
            }

        } else if (SelectingCategoriesEditingDialog.CategoryType.SHOP.getName().equals(categoryName)) {
            String selectedValue = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.SHOP.getName()).getSelectedValue();
            int result = dialog.showConfirmDialog("dialog.selectingCategories.delete.title", "dialog.selectingCategories.delete.message");
            switch (result) {
                case JOptionPane.YES_OPTION:
                    ShopJdo shopJdo = shopService.getByName(selectedValue);
                    shopService.delete(shopJdo.getId());
                    JList<String> viewList = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.SHOP.getName());
                    ((DefaultListModel<String>) viewList.getModel()).removeElement(selectedValue);
                    sortListModel((DefaultListModel<String>) viewList.getModel());
                    break;
                case JOptionPane.NO_OPTION:
            }

        } else if (SelectingCategoriesEditingDialog.CategoryType.SELLER.getName().equals(categoryName)) {
            String selectedValue = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.SELLER.getName()).getSelectedValue();
            int result = dialog.showConfirmDialog("dialog.selectingCategories.delete.title", "dialog.selectingCategories.delete.message");
            switch (result) {
                case JOptionPane.YES_OPTION:
                    SellerJdo sellerJdo = sellerService.getByName(selectedValue);
                    sellerService.delete(sellerJdo.getId());
                    JList<String> viewList = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.SELLER.getName());
                    ((DefaultListModel<String>) viewList.getModel()).removeElement(selectedValue);
                    sortListModel((DefaultListModel<String>) viewList.getModel());
                    break;
                case JOptionPane.NO_OPTION:
            }

        } else if (SelectingCategoriesEditingDialog.CategoryType.TAG.getName().equals(categoryName)) {
            String selectedValue = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.TAG.getName()).getSelectedValue();
            TagJdo selectedTag = null;
            for (TagJdo tag : tagService.getAll()) {
                if (tag.getTitle().equals(selectedValue)) {
                    selectedTag = tag;
                }
            }
            if (selectedTag != null) {
                int result = dialog.showConfirmDialog("dialog.selectingCategories.delete.title", "dialog.selectingCategories.delete.message");
                switch (result) {
                    case JOptionPane.YES_OPTION:
                        tagService.delete(selectedTag.getId());
                        JList<String> viewList = dialog.getCategoriesJListMap().get(SelectingCategoriesEditingDialog.CategoryType.TAG.getName());
                        ((DefaultListModel<String>) viewList.getModel()).removeElement(selectedValue);
                        sortListModel((DefaultListModel<String>) viewList.getModel());
                        break;
                    case JOptionPane.NO_OPTION:
                }
            }

        }
    }

    public void cancelButtonClicked() {
        dialog.getChangeButton().setEnabled(false);
        dialog.getDeleteButton().setEnabled(false);
        dialog.getCategoriesBox().setSelectedIndex(0);
        dialog.hide();

    }

    public void initializeListModelsMap() {
        List<BrandJdo> brandJdoList = brandService.getAll();
        DefaultListModel<String> brandModel = new DefaultListModel<>();
        for (BrandJdo brandJdo : brandJdoList) {
            brandModel.addElement(brandJdo.getName());
        }
        dialog.getCategoriesJListMap().put(SelectingCategoriesEditingDialog.CategoryType.BRAND.getName(), createViewList(brandModel));

        List<SizeJdo> sizeJdoList = sizeService.getAll();
        DefaultListModel<String> sizeModel = new DefaultListModel<>();
        for (SizeJdo sizeJdo : sizeJdoList) {
            sizeModel.addElement(sizeJdo.getName());
        }
        dialog.getCategoriesJListMap().put(SelectingCategoriesEditingDialog.CategoryType.SIZE.getName(), createViewList(sizeModel));

        List<ShopJdo> shopJdoList = shopService.getAll();
        DefaultListModel<String> shopModel = new DefaultListModel<>();
        for (ShopJdo shopJdo : shopJdoList) {
            shopModel.addElement(shopJdo.getName());
        }
        dialog.getCategoriesJListMap().put(SelectingCategoriesEditingDialog.CategoryType.SHOP.getName(), createViewList(shopModel));

        List<SellerJdo> sellerJdoList = sellerService.getAll();
        DefaultListModel<String> sellerModel = new DefaultListModel<>();
        for (SellerJdo seller : sellerJdoList) {
            sellerModel.addElement(seller.getName());
        }
        dialog.getCategoriesJListMap().put(SelectingCategoriesEditingDialog.CategoryType.SELLER.getName(), createViewList(sellerModel));

        List<TagJdo> tagJdoList = tagService.getAll();
        DefaultListModel<String> tagModel = new DefaultListModel<>();
        for (TagJdo tag : tagJdoList) {
            tagModel.addElement(tag.getTitle());
        }
        dialog.getCategoriesJListMap().put(SelectingCategoriesEditingDialog.CategoryType.TAG.getName(), createViewList(tagModel));
    }

    private JList<String> createViewList(DefaultListModel<String> listModel) {
        sortListModel(listModel);
        final JList<String> viewList = new JList<>(listModel);
        viewList.setLayoutOrientation(JList.VERTICAL);
        viewList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    if (viewList.getSelectedIndex() == -1) {
                        dialog.getChangeButton().setEnabled(false);
                        dialog.getDeleteButton().setEnabled(false);
                    } else {
                        dialog.getChangeButton().setEnabled(true);
                        dialog.getDeleteButton().setEnabled(true);
                    }
                }
            }
        });
        viewList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return viewList;
    }

    private void sortListModel(DefaultListModel<String> listModel) {
        String[] itemsArray = new String[listModel.size()];
        for (int i = 0; i < listModel.size(); ++i) {
            itemsArray[i] = listModel.get(i);
        }
        Arrays.sort(itemsArray);
        listModel.removeAllElements();
        for (String item : itemsArray) {
            listModel.addElement(item);
        }
    }

}
