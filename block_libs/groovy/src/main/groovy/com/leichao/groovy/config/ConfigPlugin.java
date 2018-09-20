package com.leichao.groovy.config;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

/**
 * ConfigPlugin
 */
public class ConfigPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getExtensions().add("BiuConfig", new ConfigInfo());
        System.out.println("==========================");
        System.out.println("=========BiuConfig========" + project.getExtensions().getByName("BiuConfig").toString());
        System.out.println("==========================");
        project.task("biuConfig").doLast(new Action<Task>() {
            @Override
            public void execute(Task task) {
                System.out.println("==========================");
                System.out.println("=========BiuConfig========" + project.getExtensions().getByName("BiuConfig").toString());
                System.out.println("==========================");
            }
        }).setGroup("leichao");
    }

}
