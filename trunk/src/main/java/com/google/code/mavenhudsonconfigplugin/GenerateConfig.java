package com.google.code.mavenhudsonconfigplugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
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

    private static final String MYNAME = "Hudsonconfig";
    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    MavenProject project;

    /**
     * Max day to keep all.
     * 
     * @parameter
     */
    private Integer keepBuildsNum;

    /**
     * Localdir for the SVN checkout
     * 
     * @parameter
     */
    private String localpart;

    /**
     * Location of the file.
     * 
     * @parameter expression="${project.build.directory}"
     * @required
     */
    private File outputDirectory;

    /**
     * Location of template ( if not set - MY default will be used )
     * 
     * @parameter
     */
    private File templateFile;

    @Override
    protected void defaultValues() throws MojoExecutionException {
        getLog().debug("keepBuildDays : ");
        if (keepBuildsNum == null) {
            getLog().debug("NULL");
            keepBuildsNum = new Integer(20);
            getLog().debug("Setting to : " + keepBuildsNum.toString());
        }

        getLog().debug("SVN");
        if (project.getScm() != null && project.getScm().getDeveloperConnection() != null) {
            correctedSVN = project.getScm().getDeveloperConnection();
            getLog().debug(correctedSVN);
            // format
            // scm:<scm_provider><delimiter><provider_specific_part>
            if (!correctedSVN.startsWith("scm:svn:")) {
                throw new MojoExecutionException(MYNAME + ": Currently only Subversion is supported"); // lazy - but it
                                                                                                       // works for me
                                                                                                       // ;)
            }
            correctedSVN = correctedSVN.substring(8, correctedSVN.length());
            getLog().debug("Transformed to : " + correctedSVN);
        } else {
            throw new MojoExecutionException(MYNAME + ": There is no <scm><developerConnection> in your project-pom");
        }

        getLog().debug("SVN-Localpart");
        getLog().debug(localpart);
        if (localpart == null || localpart.equals("")) {
            // guessing the localpart.
            localpart = parseSvnName(correctedSVN);
            getLog().debug("Guessing localpart : " + localpart);
        }

        getLog().debug("DESCRIPTION");
        getLog().debug(project.getDescription());
        if (project.getDescription() == null) {
            correctedDescription = "";
        } else {
            correctedDescription = project.getDescription();
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

    private String correctedSVN;
    private String correctedDescription;



    
    public void execute() throws MojoExecutionException, MojoFailureException {
        defaultValues();
        
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
            cfg = HudsonConfig.parseDocument(doc);
        }
        
        
        cfg.setDescription(correctedDescription);
        cfg.setLogRotatorNumToKeep(keepBuildsNum);
        if (cfg.getSvnLocations() != 1) {
            throw new MojoExecutionException("No or more than one scm in the HUDSON config present. don't known what to do");
        }
        cfg.setSvnRemote(0, correctedSVN);
        cfg.setSvnLocal(0, localpart);
        
        
        

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
