package com.google.code.mavenhudsonconfigplugin;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import com.google.code.mavenhudsonconfigplugin.intern.HudsonControl;

/**
 * 
 * mvn com.google.code:maven-hudsonconfig-plugin:1.0-SNAPSHOT:run
 * 
 * @author Jens Ritter -jens.ritter.gmail.com-
 *
 * @goal run
 * @requiresProject true
 */
public class RunJob extends AbstractBaseJob{

    public void execute() throws MojoExecutionException, MojoFailureException {
        defaultValues(null);
        HudsonControl hudson = new HudsonControl(hudsonUrl_used);
        try {
            hudson.runJob(jobName_used);
            getLog().info("stared job '" + jobName_used + "' on " + hudsonUrl_used);
        } catch (IOException e) {
            throw new MojoExecutionException("Error while connection to Hudson");
        }
    }

}
