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
 * mvn com.google.code:maven-hudsonconfig-plugin:0.1-SNAPSHOT:diff
 * 
 * @author Jens Ritter -jens.ritter.gmail.com-
 * 
 * @goal diff
 * @requiresProject true
 */
public class DiffConfig extends GenerateConfig{

    
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        defaultValues(null);
        
        HudsonControl ctl = new HudsonControl(hudsonUrl_used);
       
        try {
            HudsonConfig cfg = ctl.getConfig(jobName_used);
            defaultValues(cfg);
            final HudsonConfig newCfg = buildConfig(cfg);
            System.out.println("Description:");
            System.out.println("    OLD:" + cfg.getDescription());
            System.out.println("    NEW:" +newCfg.getDescription());
            
            System.out.println("LogRotator-NumToKeep:");
            System.out.println("    OLD:" + cfg.getLogRotatorNumToKeep());
            System.out.println("    NEW:" + newCfg.getLogRotatorNumToKeep());
            
            System.out.println("Goals:");
            System.out.println("    OLD:" + cfg.getGoals());
            System.out.println("    NEW:" + newCfg.getGoals());
            
            System.out.println("SVN:");
            System.out.println("    OLD:" + cfg.getSvnRemote(0));
            System.out.println("    NEW:" + newCfg.getSvnRemote(0));
            
            System.out.println("SVN-LocalPart:");
            System.out.println("    OLD:" + cfg.getSvnLocal(0));
            System.out.println("    NEW:" + newCfg.getSvnLocal(0));
            
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
