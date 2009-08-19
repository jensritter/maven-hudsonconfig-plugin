package com.google.code.mavenhudsonconfigplugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.google.code.mavenhudsonconfigplugin.intern.HudsonConfig;

/**
 * 
 * mvn com.google.code:maven-hudsonconfig-plugin:1.0-SNAPSHOT:generate
 * 
 * @author Jens Ritter<jens.ritter@gmail.com>
 * 
 * @goal generate
 * @phase generate-sources
 * @requiresProject true
 */
public class GenerateConfig extends BaseJob {

    /**
     * Max day to keep all.
     * 
     * If there is a plugin setting, it will always override existend hudson-values.
     * 
     * @parameter
     */
    private Integer keepBuildsNum;
    protected Integer keepBuildsNum_used;
    

    /**
     * Localdir for the SVN checkout.
     * 
     * only used for creating new configs.
     * 
     * @parameter
     */
    private String localpart;
    protected String localpart_used;

    /**
     * Location of the file.
     * 
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;
    protected File outputDirectory_used;

    /**
     * Location of template ( if not set - MY default will be used )
     * 
     * @parameter
     */
    private File templateFile;
    protected File templateFile_used;
    
    /**
     * Setting default "goals" for Hudsonbuild
     * 
     * @parameter
     */
    private String goals;
    protected String goals_used;
    
    protected String svnPath_used;
    protected String description_used;


    @Override
    protected void defaultValues(HudsonConfig cfg) throws MojoExecutionException {
        getLog().debug("BaseGenerateConfig.defaultValues:");
        
        getLog().debug("keepBuildDays : ");
        if (keepBuildsNum != null) {
            getLog().debug(keepBuildsNum.toString());
        } else {
            getLog().debug("NULL");
        }
        
        if (keepBuildsNum == null) {
            getLog().debug("No config in pom. Looking in Hudson...");
            if (cfg != null) {
                keepBuildsNum_used = cfg.getLogRotatorNumToKeep();
                
            }
            if (keepBuildsNum_used == null) {
                getLog().debug("No config in Hudson - using default : 20");
                keepBuildsNum_used=20;
            }
        } else {
            keepBuildsNum_used = keepBuildsNum;
        }
        

        getLog().debug("SVN");
        if (project.getScm() != null && project.getScm().getDeveloperConnection() != null) {
            svnPath_used = project.getScm().getDeveloperConnection();
            getLog().debug(svnPath_used);
            // format
            // scm:<scm_provider><delimiter><provider_specific_part>
            if (!svnPath_used.startsWith("scm:svn:")) {
                throw new MojoExecutionException(MYNAME + ": Currently only Subversion is supported"); // lazy - but it
                                                                                                       // works for me
                                                                                                       // ;)
            }
            svnPath_used = svnPath_used.substring(8, svnPath_used.length());
            getLog().debug("Transformed to : " + svnPath_used);
        } else {
            throw new MojoExecutionException(MYNAME + ": There is no <scm><developerConnection> in your project-pom");
        }

        getLog().debug("SVN-Localpart");
        getLog().debug(localpart);
        if (cfg != null) {
            if (cfg.getSvnLocations() != 1) {
                getLog().debug("Remote HudsonConfig does not have a svn-local-part");
            } else {
                localpart_used = cfg.getSvnLocal(0);
            }
        } else {
            if (localpart == null || localpart.equals("")) {
                // guessing the localpart.
                localpart_used = parseSvnName(svnPath_used);
                getLog().debug("Guessing localpart from scm-path: " + localpart);
            }
        }
        

        getLog().debug("DESCRIPTION");
        getLog().debug(project.getDescription());
        if (project.getDescription() == null) {
            description_used = "";
        } else {
            description_used = project.getDescription();
            // never use the hudson-description !!
        }
        
        getLog().debug("goals"); 
        getLog().debug(goals);
        // order : currentHudsonConfig, pluginConfig, Default 
        if (cfg != null) {
            getLog().debug("getting GOALS from Hudson : ");
            getLog().debug(cfg.getGoals());
            
            goals_used = cfg.getGoals();
        }
        if (goals_used == null) {
            goals_used = goals;
            if (goals_used == null) {
                getLog().debug("Setting default to 'clean install deploy site site:deploy'");
                goals_used = "clean install deploy site site:deploy";
            }
        }
    }

    protected String parseSvnName(String value) {
        String testValue = value;
        String[] splitted = testValue.split("/");
        String find = splitted[splitted.length - 1];
        if (find.toLowerCase().equals("trunk")) {
            find = splitted[splitted.length - 2];
        }
        return find;
    }




    
    public void execute() throws MojoExecutionException, MojoFailureException {
        defaultValues(null);
        
        buildConfig(null);


        getLog().info(MYNAME + ": New config succesfull written to target/config.xml");
        getLog().info(MYNAME + ": ");
        getLog().info(MYNAME + ": Copy it to hudson");
    }
    
    
    
    public String buildConfig(HudsonConfig cfg) throws MojoExecutionException {
       
        if (cfg != null) {
            SAXBuilder builder = new SAXBuilder();
            Document doc = null;
            try {
                if (templateFile == null) {
                    doc = builder.build(getClass().getResourceAsStream("/config.xml"));
                } else {
                    doc = builder.build(new FileInputStream(templateFile));
                }
            } catch (IOException e) {
                throw new MojoExecutionException(MYNAME + ": can't read template : " + e.getMessage());
            } catch (JDOMException e) {
                throw new MojoExecutionException(MYNAME + ": can't read template: " + e.getMessage());
            }
            cfg = HudsonConfig.parseDocument(jobName_used,doc);
        }
        
        // MAPPING:
        
        cfg.setDescription(description_used);
        cfg.setLogRotatorNumToKeep(keepBuildsNum_used);
        if (cfg.getSvnLocations() != 1) {
            throw new MojoExecutionException("No or more than one scm in the HUDSON config present. don't known what to do");
        }
        cfg.setSvnRemote(0, svnPath_used);
        cfg.setSvnLocal(0, localpart_used);
        cfg.setGoals(goals_used);
        
        

        File f = outputDirectory;
        if (!f.exists()) {
            f.mkdirs();
        }
        File conf = new File(f, "config.xml");
        FileWriter w = null;
        try {
            w = new FileWriter(conf);

            w.write(cfg.getXml());
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + conf, e);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        

        return cfg.getXml();

    }
}
