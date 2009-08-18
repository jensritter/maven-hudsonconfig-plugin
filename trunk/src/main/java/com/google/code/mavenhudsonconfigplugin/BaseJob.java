package com.google.code.mavenhudsonconfigplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

public abstract class BaseJob extends AbstractMojo {
    
private static final String MYNAME="Hudsonconfig";
    
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;
    
    /**
     * URL to Hudson.
     *
     * @parameter
     */
    protected String hudsonUrl;
    
    protected String name;

    
    protected void defaultValues() throws MojoExecutionException {
        if (project == null) {
            throw new MojoExecutionException("No ProjectSettings from Plexus");
        }
        getLog().debug("updating values");
        
        getLog().debug("hudsonUrl");
        getLog().debug(hudsonUrl);
        String origUrl = hudsonUrl;
        if (hudsonUrl == null) {
            getLog().debug("No paramenter hudsonUrl - searching in ciManagement");
            if (project.getCiManagement() == null) {
                throw new MojoExecutionException(MYNAME+": No <hudsonUrl> in PluginConfig and No ciManagement in your pom. One of this is needed");
            }
            String systm = project.getCiManagement().getSystem();
            if (!systm.toLowerCase().trim().equals("hudson")) {
                throw new MojoExecutionException(MYNAME+": only Hudson is currently supported. '" + project.getCiManagement().getSystem()+"' is not.");
            }
            hudsonUrl = project.getCiManagement().getUrl();
            if (hudsonUrl == null || hudsonUrl.equals("")) {
                throw new MojoExecutionException("url for ciManagement is empty");
            }
            getLog().debug("guessed URL : " + hudsonUrl);
            
            if (hudsonUrl.contains("/job/")) {
                name = hudsonUrl.substring(hudsonUrl.indexOf("/job/")+5,hudsonUrl.length());
                hudsonUrl = hudsonUrl.substring(0,hudsonUrl.indexOf("/job/"));
                getLog().debug("removed JOB part from url");
            }
            
            if (name == null) {
                getLog().debug("guessing name from artifactId");
                getLog().info("Guessing JobName from artifactId. To disable this enter the full URL in ciManagement to this job");
                name = project.getArtifactId();
            }
        }
    }

}
