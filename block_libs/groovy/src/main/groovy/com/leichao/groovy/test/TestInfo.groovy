package com.leichao.groovy.test

/**
 * TestInfo
 */
class TestInfo {

    String name
    int age
    boolean boy

    @Override
    String toString() {
        return "本地，I am $name, $age years old, " + (boy ? "I am a boy" : "I am a gril")
    }
}