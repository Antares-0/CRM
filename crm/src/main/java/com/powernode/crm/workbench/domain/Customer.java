package com.powernode.crm.workbench.domain;

public class Customer {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer.id
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer.owner
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    private String owner;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer.name
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer.website
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    private String website;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer.phone
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    private String phone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer.create_by
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    private String createBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer.create_time
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    private String createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer.edit_by
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    private String editBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer.edit_time
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    private String editTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer.contact_summary
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    private String contactSummary;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer.next_contact_time
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    private String nextContactTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer.description
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    private String description;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_customer.address
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    private String address;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_customer.id
     *
     * @return the value of tbl_customer.id
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_customer.id
     *
     * @param id the value for tbl_customer.id
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_customer.owner
     *
     * @return the value of tbl_customer.owner
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public String getOwner() {
        return owner;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_customer.owner
     *
     * @param owner the value for tbl_customer.owner
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_customer.name
     *
     * @return the value of tbl_customer.name
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_customer.name
     *
     * @param name the value for tbl_customer.name
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_customer.website
     *
     * @return the value of tbl_customer.website
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public String getWebsite() {
        return website;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_customer.website
     *
     * @param website the value for tbl_customer.website
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public void setWebsite(String website) {
        this.website = website == null ? null : website.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_customer.phone
     *
     * @return the value of tbl_customer.phone
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_customer.phone
     *
     * @param phone the value for tbl_customer.phone
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_customer.create_by
     *
     * @return the value of tbl_customer.create_by
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_customer.create_by
     *
     * @param createBy the value for tbl_customer.create_by
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_customer.create_time
     *
     * @return the value of tbl_customer.create_time
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_customer.create_time
     *
     * @param createTime the value for tbl_customer.create_time
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime == null ? null : createTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_customer.edit_by
     *
     * @return the value of tbl_customer.edit_by
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public String getEditBy() {
        return editBy;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_customer.edit_by
     *
     * @param editBy the value for tbl_customer.edit_by
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public void setEditBy(String editBy) {
        this.editBy = editBy == null ? null : editBy.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_customer.edit_time
     *
     * @return the value of tbl_customer.edit_time
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public String getEditTime() {
        return editTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_customer.edit_time
     *
     * @param editTime the value for tbl_customer.edit_time
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public void setEditTime(String editTime) {
        this.editTime = editTime == null ? null : editTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_customer.contact_summary
     *
     * @return the value of tbl_customer.contact_summary
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public String getContactSummary() {
        return contactSummary;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_customer.contact_summary
     *
     * @param contactSummary the value for tbl_customer.contact_summary
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public void setContactSummary(String contactSummary) {
        this.contactSummary = contactSummary == null ? null : contactSummary.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_customer.next_contact_time
     *
     * @return the value of tbl_customer.next_contact_time
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public String getNextContactTime() {
        return nextContactTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_customer.next_contact_time
     *
     * @param nextContactTime the value for tbl_customer.next_contact_time
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public void setNextContactTime(String nextContactTime) {
        this.nextContactTime = nextContactTime == null ? null : nextContactTime.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_customer.description
     *
     * @return the value of tbl_customer.description
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_customer.description
     *
     * @param description the value for tbl_customer.description
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tbl_customer.address
     *
     * @return the value of tbl_customer.address
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public String getAddress() {
        return address;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tbl_customer.address
     *
     * @param address the value for tbl_customer.address
     *
     * @mbggenerated Mon May 16 21:02:47 CST 2022
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }
}