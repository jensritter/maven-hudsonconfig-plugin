package com.google.code.mavenhudsonconfigplugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Pattern;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

/**
 * 
 * mvn org.jens:maven-hudsonconfig-plugin:1.0-SNAPSHOT:generate
 * 
 * @author mac
 *
 * @goal generate
 * @phase process-sources
 * @requiresProject true
 */
public class DisplayInfo extends AbstractMojo{
    
    private static final String MYNAME="Hudsonconfig";
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
     * Location of template
     * ( if not set - MY default will be used ) 
     * @parameter
     */
    private File templateFile; 

    protected static final Pattern SVNPATTERN = Pattern.compile("/(\\w+)/?(trunk)?/?$/");
    
    private void defaultValues() throws MojoExecutionException {
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
                throw new MojoExecutionException(MYNAME+": Currently only Subversion is supported"); // lazy - but it works for me ;) 
            }
            correctedSVN = correctedSVN.substring(8,correctedSVN.length());
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
            correctedDescription="";
        } else {
            correctedDescription = project.getDescription();
        }
    }
    
    protected String parseSvnName(String value) {
        String testValue = value;
        String[] splitted = testValue.split("/");
        System.out.println(splitted[splitted.length-1]);
        String find = splitted[splitted.length-1];
        if (find.toLowerCase().equals("trunk")) {
            find = splitted[splitted.length-2];
        }
        return find;
    }
    
    private String correctedSVN;
    private String correctedDescription;
    /**
     * 
     * 
     */
    public void execute() throws MojoExecutionException, MojoFailureException {
        defaultValues();
        String template;
        try {
            if (templateFile == null) {
                template = getFile(getClass().getResourceAsStream("/config.xml"));
            } else {
                template = getFile(new FileInputStream(templateFile));
            }
        } catch (IOException e) {
            throw new MojoExecutionException(MYNAME +": can't read template");
        }
            template = 
                template.replaceAll("\\$DESCRIPTION", correctedDescription)
                .replaceAll("\\$NUMTOKEEP", keepBuildsNum.toString())
                .replaceAll("\\$SVNPATH", correctedSVN)
                .replaceAll("\\$SVNLOCAL", localpart);
            
            File f = outputDirectory;
            if (!f.exists()) {
                f.mkdirs();
            }
            File conf = new File(f, "config.xml");
            FileWriter w = null;
            try {
                w = new FileWriter(conf);

                w.write(template);
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
            
        
    }

    
    private String getFile(InputStream in) throws IOException {
        byte[] cache = new byte[1024];
        StringBuffer buffer = new StringBuffer(1024);
        while (in.available() !=0)
        {
            in.read(cache);
            buffer.append(new String(cache));
        }

        in.close();
        return buffer.toString();
    }

}
