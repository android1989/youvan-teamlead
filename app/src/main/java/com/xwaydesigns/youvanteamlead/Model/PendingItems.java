package com.xwaydesigns.youvanteamlead.Model;

public class PendingItems
{
    public String donation_id;
    public String item_id;
    public String student_id;
    public String item_quantity;
    public String item_image;
    public String item_name;
    public String student_name;
    public String mobile;


    public PendingItems(String donation_id, String item_id, String student_id, String item_quantity, String item_image, String item_name, String student_name, String mobile)
    {
        this.donation_id = donation_id;
        this.item_id = item_id;
        this.student_id = student_id;
        this.item_quantity = item_quantity;
        this.item_image = item_image;
        this.item_name = item_name;
        this.student_name = student_name;
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDonation_id() {
        return donation_id;
    }

    public void setDonation_id(String donation_id) {
        this.donation_id = donation_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }

    public String getItem_image() {
        return item_image;
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }
}
