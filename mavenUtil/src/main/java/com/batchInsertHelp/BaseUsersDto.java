package com.batchInsertHelp;


public class BaseUsersDto {

    /* id主键自增 */
    private String id;
    /* 用户名 */
    private String c_userName;
    /* 密码 */
    private String c_passWord;
    /* 手机号码 */
    private String c_phone;
    /* 年龄 */
    private String i_age;
    /* 昵称 */
    private String c_nick;
    /* 头像（图片的路径） */
    private String c_headUrl;
    /* 邮箱 */
    private String c_email;
    /* 身份证 */
    private String c_identityCard;
    /* 创建日期 */
    private String t_createTime;
    /* 盐 */
    private String c_salt;
    /* 账号状态 0：未激活 1：激活 */
    private String i_status;
    /* 角色表的外键 */
    private String role_id;

    /** Ĭ�ϵĹ��캯�� */
    public BaseUsersDto() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    } 
    public String getC_userName() {
        return c_userName;
    }

    public void setC_userName(String c_userName) {
        this.c_userName = c_userName;
    } 
    public String getC_passWord() {
        return c_passWord;
    }

    public void setC_passWord(String c_passWord) {
        this.c_passWord = c_passWord;
    } 
    public String getC_phone() {
        return c_phone;
    }

    public void setC_phone(String c_phone) {
        this.c_phone = c_phone;
    } 
    public String getI_age() {
        return i_age;
    }

    public void setI_age(String i_age) {
        this.i_age = i_age;
    } 
    public String getC_nick() {
        return c_nick;
    }

    public void setC_nick(String c_nick) {
        this.c_nick = c_nick;
    } 
    public String getC_headUrl() {
        return c_headUrl;
    }

    public void setC_headUrl(String c_headUrl) {
        this.c_headUrl = c_headUrl;
    } 
    public String getC_email() {
        return c_email;
    }

    public void setC_email(String c_email) {
        this.c_email = c_email;
    } 
    public String getC_identityCard() {
        return c_identityCard;
    }

    public void setC_identityCard(String c_identityCard) {
        this.c_identityCard = c_identityCard;
    } 
    public String getT_createTime() {
        return t_createTime;
    }

    public void setT_createTime(String t_createTime) {
        this.t_createTime = t_createTime;
    } 
    public String getC_salt() {
        return c_salt;
    }

    public void setC_salt(String c_salt) {
        this.c_salt = c_salt;
    } 
    public String getI_status() {
        return i_status;
    }

    public void setI_status(String i_status) {
        this.i_status = i_status;
    } 
    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    } 

}
