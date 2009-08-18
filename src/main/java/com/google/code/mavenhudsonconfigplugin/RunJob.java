package com.google.code.mavenhudsonconfigplugin;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.google.code.mavenhudsonconfigplugin.intern.HudsonControl;

/**
 * 
 * mvn com.google.code:maven-hudsonconfig-plugin:1.0-SNAPSHOT:run
 * 
 * @author mac
 *
 * @goal run
 * @requiresProject true
 */
public class RunJob extends BaseJob{

    public void execute() throws MojoExecutionException, MojoFailureException {
        defaultValues();
        HudsonControl hudson = new HudsonControl(hudsonUrl);
        try {
            hudson.runJob(name);
            getLog().info("stared job '" + name + "' on " + hudsonUrl);
        } catch (IOException e) {
            throw new MojoExecutionException("Error while connection to Hudson");
        }
    }

}
