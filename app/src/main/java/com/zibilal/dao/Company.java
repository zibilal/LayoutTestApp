package com.zibilal.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "COMPANY".
 */
public class Company {

    private Long id;
    /** Not-null value. */
    private String name;
    private int ranking;
    /** Not-null value. */
    private java.util.Date create_date;
    /** Not-null value. */
    private java.util.Date update_date;

    public Company() {
    }

    public Company(Long id) {
        this.id = id;
    }

    public Company(Long id, String name, int ranking, java.util.Date create_date, java.util.Date update_date) {
        this.id = id;
        this.name = name;
        this.ranking = ranking;
        this.create_date = create_date;
        this.update_date = update_date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    /** Not-null value. */
    public java.util.Date getCreate_date() {
        return create_date;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCreate_date(java.util.Date create_date) {
        this.create_date = create_date;
    }

    /** Not-null value. */
    public java.util.Date getUpdate_date() {
        return update_date;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUpdate_date(java.util.Date update_date) {
        this.update_date = update_date;
    }

}