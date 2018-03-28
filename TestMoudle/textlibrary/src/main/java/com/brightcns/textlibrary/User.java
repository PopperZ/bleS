package com.brightcns.textlibrary;

/**
 * @author zhangfeng
 * @data： 26/3/18
 * @description：建造者设计模式初识
 */

public class User {
    private  final String name;//必选 名字
    private  final String age;//必选 年龄
    private  final String phone;//可选 电话
    private  final String id;//可选 身份证
    private  final String address;//可选 地址

    private User(UserBuilder userBuilder) {
        this.name = userBuilder.name;
        this.age = userBuilder.age;
        this.phone = userBuilder.phone;
        this.id = userBuilder.id;
        this.address = userBuilder.address;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getPhone() {
        return phone;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public static class UserBuilder{
        private  final String name;//必选 名字
        private  final String age;//必选 年龄
        private   String phone;//可选 电话
        private   String id;//可选 身份证
        private   String address;//可选 地址

        public UserBuilder(String name, String age) {
            this.name = name;
            this.age = age;
        }


        public UserBuilder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder age(String phone) {
            this.phone = phone;
            return this;
        }

        public UserBuilder address(String phone) {
            this.phone = phone;
            return this;
        }

        public User build() {
            return new User(this);
        }

    }
}
