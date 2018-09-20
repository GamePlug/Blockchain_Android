package com.leichao.groovy.test

import org.gradle.api.Plugin
import org.gradle.api.Project
/**
 * TestPlugin
 */
class TestPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.add("BiuTest", TestInfo)
        println '=========================='
        println '==========BiuTest=========' + project.BiuTest.toString()
        println '=========================='
        project.task('biuTest') {
            group 'leichao'
            doLast {
                println '=========================='
                println '==========BiuTest=========' + project.BiuTest.toString()
                println '=========================='
            }
        }
    }

}
