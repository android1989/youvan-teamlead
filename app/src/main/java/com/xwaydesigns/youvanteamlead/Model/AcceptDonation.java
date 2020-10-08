package com.xwaydesigns.youvanteamlead.Model;

public class AcceptDonation
{
    public String student_id;
    public String student_name;
    public String student_image;
    public String student_class;
    public String mobile;
    public String item_quantity;

    public AcceptDonation(String student_id,String student_name, String student_image, String student_class, String mobile, String item_quantity) {
        this.student_id = student_id;
        this.student_name = student_name;
        this.student_image = student_image;
        this.student_class = student_class;
        this.mobile = mobile;
        this.item_quantity = item_quantity;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_image() {
        return student_image;
    }

    public void setStudent_image(String student_image) {
        this.student_image = student_image;
    }

    public String getStudent_class() {
        return student_class;
    }

    public void setStudent_class(String student_class) {
        this.student_class = student_class;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getItem_quantity() {
        return item_quantity;
    }

    public void setItem_quantity(String item_quantity) {
        this.item_quantity = item_quantity;
    }
}
