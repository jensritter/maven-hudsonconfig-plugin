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
 * @author Jens Ritter<jens.ritter@gmail.com>
 * 
 * @goal update
 * @requiresProject true
 */
public class UpdateConfig extends GenerateConfig{

    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        defaultValues();
        
        HudsonControl ctl = new HudsonControl(hudsonUrl);
        try {
            HudsonConfig cfg = ctl.getConfig(jobName);
            this.correctedGoals = cfg.getGoals();
            this.correctedKeepBuildsNum= cfg.getLogRotatorNumToKeep();
            
            defaultValues();
            
            
            String content = buildConfig(cfg);
            ctl.saveConfig(jobName, content);
            getLog().info("Hudson config Updated");
        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JDOMException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
