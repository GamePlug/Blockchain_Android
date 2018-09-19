package com.mula.gradle.test

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * TestPlugin
 */
class TestPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.extensions.add("MulaTest", TestInfo)
        println '=========================='
        println '========TestPlugin========' + project.MulaTest.toString()
        println '=========================='
        project.task('mulaTest') {
            group 'mula'
            doLast {
                println '=========================='
                println '=========mulaTest=========' + project.MulaTest.toString()
                println '=========================='
            }
        }
    }

}
