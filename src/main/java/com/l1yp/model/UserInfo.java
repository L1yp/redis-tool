package com.l1yp.model;

import com.l1yp.enums.MessageType;
import com.l1yp.enums.SexType;

/**
 * @Author Lyp
 * @Date 2020-08-27
 * @Email l1yp@qq.com
 */
public class UserInfo {

    public Long id;
    public String name;
    public SexType sex;
    public MessageType type;

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", type=" + type +
                '}';
    }


    // public Long getId() {
    //     return id;
    // }
    //
    // public void setId(Long id) {
    //     this.id = id;
    // }
    //
    // public String getName() {
    //     return name;
    // }
    //
    // public void setName(String name) {
    //     this.name = name;
    // }
    //
    // public SexType getSex() {
    //     return sex;
    // }
    //
    // public void setSex(SexType sex) {
    //     this.sex = sex;
    // }
}
