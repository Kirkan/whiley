package autoTests;

public enum ProductType {

    EBOOK("E-Book", "Add to cart", "//form/button[contains(@class, 'add-to-cart-button')]"),
    OBOOK("O-Book", "View on Wiley Online Library", "/a[contains(@class, 'learn-more-button')]");

    private String type;
    private String label;
    private String locator;

    public String getType() {
        return type;
    }

    public String getLocator() {
        return locator;
    }

    public String getLabel() {
        return label;
    }

    ProductType(String type, String label, String locator) {
        this.type = type;
        this.label = label;
        this.locator = locator;
    }
}
