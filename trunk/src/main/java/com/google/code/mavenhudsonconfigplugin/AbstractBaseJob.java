package com.google.code.mavenhudsonconfigplugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

import com.google.code.mavenhudsonconfigplugin.intern.HudsonConfig;
/**
 * 
 * Baseclass for all MavenGoals.
 * 
 * @author Jens Ritter -jens.ritter.gmail.com-
 *
 */
public abstract class AbstractBaseJob extends AbstractMojo {
    
    /**
     * Constant for logging.
     */
    public static final String MYNAME="Hudsonconfig";
    
    /**
     * Plexus-Injector for mavenproject.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;
    
    
    /**
     * URL to Hudson.
     * 
     * if this is not set, the values in ciManagement are tried.
     *
     * @parameter
     */
    private String hudsonUrl;
    protected String hudsonUrl_used;
    
    /**
     * Manual set the jobname for Hudson.
     * 
     * @parameter
     */
    private String jobName;
    protected String jobName_used;
    
    protected void defaultValues(HudsonConfig cfg) throws MojoExecutionException {
        getLog().debug("BaseJob.defaultValues:");
        if (project == null) {
            throw new MojoExecutionException("No ProjectSettings from Plexus");
        }
        getLog().debug("updating values");
        
        getLog().debug("hudsonUrl");
        getLog().debug(hudsonUrl);

        String tmpName = "";
        if (hudsonUrl == null) {
            getLog().debug("No paramenter hudsonUrl - searching in ciManagement");
            if (project.getCiManagement() == null) {
                throw new MojoExecutionException(MYNAME+": No <hudsonUrl> in PluginConfig and No ciManagement in your pom. One of this is needed");
            }
            String systm = project.getCiManagement().getSystem();
            if (!systm.toLowerCase().trim().equals("hudson")) {
                throw new MojoExecutionException(MYNAME+": only Hudson is currently supported. '" + project.getCiManagement().getSystem()+"' is not.");
            }
            hudsonUrl_used = project.getCiManagement().getUrl();
            if (hudsonUrl_used == null || hudsonUrl_used.equals("")) {
                throw new MojoExecutionException("url for ciManagement is empty");
            }
            getLog().debug("guessed URL : " + hudsonUrl_used);
            if (hudsonUrl_used.contains("/job/")) {
                tmpName = hudsonUrl_used.substring(hudsonUrl_used.indexOf("/job/")+5,hudsonUrl_used.length());
                hudsonUrl_used = hudsonUrl_used.substring(0,hudsonUrl_used.indexOf("/job/"));
                getLog().debug("removed JOB part from url : " + hudsonUrl_used);
            }
        } else {
            hudsonUrl_used = hudsonUrl;
        }
        getLog().debug("jobname");
        getLog().debug(jobName);
        if (jobName == null) {
            // jobName can be comming from the origHudsonConfig.
            if (cfg != null && cfg.getJobName() != null) {
                jobName_used = cfg.getJobName();
                getLog().debug("using jobname from orig-HudsonConfig : " + cfg.getJobName());
            } else {
                if (!tmpName.equals("")) {
                    jobName_used = tmpName;
                    getLog().debug("taking jobname from ciManagement-url :" + jobName_used);
                } else {
                    getLog().debug("guessing name from artifactId");
                    getLog().info("Guessing JobName from artifactId. To disable this guessing, enter the full URL in ciManagement to this job - or use the plugin-parameter 'jobName'");
                    jobName_used = project.getArtifactId();
                }
            }
        } else {
            jobName_used = jobName;
        }
        
        getLog().debug("Using Jobname : ");
        getLog().debug(jobName_used);
         
        
    }

}
