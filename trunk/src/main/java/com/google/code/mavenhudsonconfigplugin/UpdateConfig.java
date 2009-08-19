package com.google.code.mavenhudsonconfigplugin;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdom.JDOMException;

import com.google.code.mavenhudsonconfigplugin.intern.HudsonConfig;
import com.google.code.mavenhudsonconfigplugin.intern.HudsonControl;

/**
 * 
 * mvn com.google.code:maven-hudsonconfig-plugin:1.0-SNAPSHOT:update
 * 
 * @author Jens Ritter -jens.ritter.gmail.com-
 * 
 * @goal update
 * @requiresProject true
 */
public class UpdateConfig extends GenerateConfig{

    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        defaultValues(null);
        
        HudsonControl ctl = new HudsonControl(hudsonUrl_used);
       
        try {
            HudsonConfig cfg = ctl.getConfig(jobName_used);
            defaultValues(cfg);
            
            
            String content = buildConfig(cfg);
            ctl.saveConfig(jobName_used, content);
            getLog().info("Hudson config Updated");
        } catch (HttpException e) {
            e.printStackTrace();
            throw new MojoExecutionException(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            throw new MojoExecutionException(e.getMessage());
        } catch (JDOMException e) {
            e.printStackTrace();
            throw new MojoExecutionException(e.getMessage());
        }
    }

}
