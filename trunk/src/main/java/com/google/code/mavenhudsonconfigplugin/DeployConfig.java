package com.google.code.mavenhudsonconfigplugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * 
 * mvn com.google.code:maven-hudsonconfig-plugin:1.0-SNAPSHOT:deploy
 * 
 * @author mac
 *
 * @goal deploy
 * @phase deploy
 * @requiresProject true
 */
public class DeployConfig extends BaseJob{
    
    public void execute() throws MojoExecutionException, MojoFailureException {
        defaultValues();
        
    }
    


}
