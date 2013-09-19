package com.lavida.service.preset.settings;

/**
 * The class for a custom preset of {@link com.lavida.service.preset.settings.UserSettings}.
 * Created: 15:21 18.09.13
 *
 * @author Ruslan
 */
public class Preset {

    private String name;

    private ArticlesTableSettings articlesTableSettings;

    private DiscountCardsTableSettings discountCardsTableSettings;

    public Preset() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArticlesTableSettings getArticlesTableSettings() {
        return articlesTableSettings;
    }

    public void setArticlesTableSettings(ArticlesTableSettings articlesTableSettings) {
        this.articlesTableSettings = articlesTableSettings;
    }

    public DiscountCardsTableSettings getDiscountCardsTableSettings() {
        return discountCardsTableSettings;
    }

    public void setDiscountCardsTableSettings(DiscountCardsTableSettings discountCardsTableSettings) {
        this.discountCardsTableSettings = discountCardsTableSettings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Preset preset = (Preset) o;

        if (articlesTableSettings != null ? !articlesTableSettings.equals(preset.articlesTableSettings) : preset.articlesTableSettings != null)
            return false;
        if (discountCardsTableSettings != null ? !discountCardsTableSettings.equals(preset.discountCardsTableSettings) : preset.discountCardsTableSettings != null)
            return false;
        if (!name.equals(preset.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (articlesTableSettings != null ? articlesTableSettings.hashCode() : 0);
        result = 31 * result + (discountCardsTableSettings != null ? discountCardsTableSettings.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Preset{" +
                "name='" + name + '\'' +
                ", articlesTableSettings=" + articlesTableSettings +
                ", discountCardsTableSettings=" + discountCardsTableSettings +
                '}';
    }
}
