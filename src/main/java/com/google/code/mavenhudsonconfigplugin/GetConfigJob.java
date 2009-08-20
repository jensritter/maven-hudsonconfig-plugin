package com.google.code.mavenhudsonconfigplugin;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.google.code.mavenhudsonconfigplugin.intern.HudsonControl;

/**
 * 
 * mvn com.google.code:maven-hudsonconfig-plugin:config
 * 
 * @author Jens Ritter -jens.ritter.gmail.com-
 * 
 * @goal config
 * @requiresProject true
 */
public class GetConfigJob extends GenerateConfig {

    public void execute() throws MojoExecutionException, MojoFailureException {
        defaultValues(null);
        HudsonControl hudson = new HudsonControl(hudsonUrl_used);
        try {
            writeToFile(hudson.getConfigAsXml(jobName_used));
            getLog().info("");
            getLog().info("");
            getLog().info("File written to " + outputDirectory.toString() + File.separator + "config.xml");
            getLog().info("");
            getLog().info("");
            getLog().info("");
            getLog().info("");
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        }
    }

}
