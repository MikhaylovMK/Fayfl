package com.example.fayfl.model;

import java.util.Date;

public class Order {

    public String phoneNumber;
    public String the_cost_of_travel;
    public String order_where;
    public String order_to_where;
    private long orderTime;

    public Order(){}
    public Order(String phoneNumber,String the_cost_of_travel,String order_where,String order_to_where){
        this.phoneNumber = phoneNumber;
        this.the_cost_of_travel = the_cost_of_travel;
        this.order_where = order_where;
        this.order_to_where = order_to_where;
        this.orderTime = new Date().getTime();
    }

    public String getThe_cost_of_travel() {
        return the_cost_of_travel;
    }

    public void setThe_cost_of_travel(String the_cost_of_travel) {
        this.the_cost_of_travel = the_cost_of_travel;
    }

    public String getOrder_where() {
        return order_where;
    }

    public void setOrder_where(String order_where) {
        this.order_where = order_where;
    }

    public String getOrder_to_where() {
        return order_to_where;
    }

    public void setOrder_to_where(String order_to_where) {
        this.order_to_where = order_to_where;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }
}
