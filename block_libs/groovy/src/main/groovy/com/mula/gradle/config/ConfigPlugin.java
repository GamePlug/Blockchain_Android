package com.mula.gradle.config;

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
        project.getExtensions().add("MulaConfig", new ConfigInfo());
        System.out.println("==========================");
        System.out.println("=======ConfigPlugin=======" + project.getExtensions().getByName("MulaConfig").toString());
        System.out.println("==========================");
        project.task("mulaConfig").doLast(new Action<Task>() {
            @Override
            public void execute(Task task) {
                System.out.println("==========================");
                System.out.println("========mulaConfig========" + project.getExtensions().getByName("MulaConfig").toString());
                System.out.println("==========================");
            }
        }).setGroup("mula");
    }

}
