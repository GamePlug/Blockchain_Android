package com.leichao.groovy.config;

/**
 * ConfigInfo
 */
public class ConfigInfo {

    private String name;
    private int age;
    private boolean boy;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isBoy() {
        return boy;
    }

    public void setBoy(boolean boy) {
        this.boy = boy;
    }

    @Override
    public String toString() {
        return "中文会乱码，I am " + name + ", " + age + " years old, " + (boy ? "I am a boy" : "I am a gril");
    }

}
