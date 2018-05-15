package com.example.android.caferecharge;

/**
 * Created by rachit on 09/03/18.
 */

public class FoodItem {
    private String name;
    private String price;
    private String quantity;
    private String imageUrl;
    private Boolean added_to_cart;
    private String section_name;

  public FoodItem(String name, String price, String quantity, String url, Boolean added_to_cart, String section_name){
      this.name=name;
      this.price=price;
      this.quantity=quantity;
      this.imageUrl=url;
      this.added_to_cart=added_to_cart;
      this.section_name=section_name;
  }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setAdded_to_cart(Boolean added_to_cart) {
        this.added_to_cart = added_to_cart;
    }

    public Boolean getAdded_to_cart() {
        return added_to_cart;
    }

    public FoodItem(){}

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }



    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }


}
