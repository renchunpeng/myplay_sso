package com.cpren.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author cdxu@iyunwen.com on 2019/8/27
 */
@Data
public class User implements Serializable {
    private String userName;

    private String age;

    private String sex;

    private String address;

    private Date birth;

    private String password;

    private List<String> auths;

    private String oldUrl;
}
