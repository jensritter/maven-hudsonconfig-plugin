package com.google.code.mavenhudsonconfigplugin;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdom.JDOMException;

import com.google.code.mavenhudsonconfigplugin.intern.HudsonControl;

/**
 * 
 * mvn com.google.code:maven-hudsonconfig-plugin:0.1-SNAPSHOT:list
 * 
 * @author Jens Ritter -jens.ritter.gmail.com-
 *
 * @goal list
 * @requiresProject true
 */
public class ListJob extends AbstractBaseJob{
    
    public void execute() throws MojoExecutionException, MojoFailureException {
        defaultValues(null);
        HudsonControl hudson = new HudsonControl(hudsonUrl_used);
        getLog().info("Jobs on " + hudsonUrl_used);
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
