package com.ciit.freelanceplus.Model;

public class OrderModel {

    public String title, projectType, description, image, deadline, budget, files, seller_files, remark, buyer_id, seller_id, status;
    public int id;
    public String seller_name, seller_email, buyer_name, buyer_emmail;

    @Override
    public String toString() {
        return "OrderModel{" +
                "title='" + title + '\'' +
                ", projectType='" + projectType + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", deadline='" + deadline + '\'' +
                ", budget='" + budget + '\'' +
                ", files='" + files + '\'' +
                ", seller_files='" + seller_files + '\'' +
                ", remark='" + remark + '\'' +
                ", buyer_id='" + buyer_id + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", status='" + status + '\'' +
                ", id=" + id +
                ", seller_name='" + seller_name + '\'' +
                ", seller_email='" + seller_email + '\'' +
                ", buyer_name='" + buyer_name + '\'' +
                ", buyer_emmail='" + buyer_emmail + '\'' +
                '}';
    }
}
