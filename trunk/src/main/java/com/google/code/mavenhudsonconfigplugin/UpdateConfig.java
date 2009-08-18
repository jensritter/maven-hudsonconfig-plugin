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
        getLog().info(hudsonUrl);
        defaultValues();
        getLog().info(hudsonUrl);
        HudsonControl ctl = new HudsonControl(hudsonUrl);
        try {
            HudsonConfig cfg = ctl.getConfig(name);
            String content = buildConfig(cfg);
            ctl.saveConfig(name, content);
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
