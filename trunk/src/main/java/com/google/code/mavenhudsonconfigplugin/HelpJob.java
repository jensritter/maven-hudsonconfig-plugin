package com.google.code.mavenhudsonconfigplugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * mvn com.google.code:maven-hudsonconfig-plugin:0.1-SNAPSHOT:help
 * 
 * @author Jens Ritter -jens.ritter.gmail.com-
 *
 * @goal help
 * @requiresProject true
 */
public class HelpJob extends AbstractBaseJob{

    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("Available Goals:");
        System.out.println("");
        System.out.println("diff");
        System.out.println("    shows the current Hudsonconfig and the current config in this pom-file");
        System.out.println("");
        System.out.println("update");
        System.out.println("");
        System.out.println("    update hudson to the config based on this pom-file");
        System.out.println("");
        System.out.println("run");
        System.out.println("    run the hudson job");
        System.out.println("");
        System.out.println("list");
        System.out.println("    list all hudsonjobs");
        System.out.println("");
        System.out.println("config");
        System.out.println("    get current config from Hudson and save it to target/config.xml");
        System.out.println("");
        System.out.println("generate");
        System.out.println("    create target/config.xml based on this pom-file");
        System.out.println("");
        
    }

}
