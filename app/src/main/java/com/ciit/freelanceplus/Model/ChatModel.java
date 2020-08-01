package com.ciit.freelanceplus.Model;

public class ChatModel {

    public String buyer_id,
            buyer_name,
            seller_id,
            seller_name,
            sender;
    public String message;
    public String order_id;


    public ChatModel(){}


    @Override
    public String toString() {
        return "ChatModel{" +
                "buyer_id='" + buyer_id + '\'' +
                ", buyer_name='" + buyer_name + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", seller_name='" + seller_name + '\'' +
                ", sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", order_id='" + order_id + '\'' +
                '}';
    }
}
