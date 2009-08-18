package com.google.code.mavenhudsonconfigplugin;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdom.JDOMException;

import com.google.code.mavenhudsonconfigplugin.intern.HudsonControl;


/**
 * 
 * mvn com.google.code:maven-hudsonconfig-plugin:1.0-SNAPSHOT:list
 * 
 * @author Jens Ritter<jens.ritter@gmail.com>
 *
 * @goal list
 * @requiresProject true
 */
public class ListJobs extends BaseJob{
    
    public void execute() throws MojoExecutionException, MojoFailureException {
        defaultValues();
        HudsonControl hudson = new HudsonControl(hudsonUrl);
        getLog().info("Jobs on " + hudsonUrl);
        try {
            for(String it : hudson.getjobsAsString()) {
                getLog().info(it);
            }
        } catch (IOException e) {
           throw new MojoFailureException("Failure while communication Hudson");
        } catch (JDOMException e) {
            throw new MojoFailureException("Failure while communication Hudson");
        }
    }

}
