package com.google.code.mavenhudsonconfigplugin;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;

/**
 * The Class AbstractBaseJobTest.
 * 
 * @author Jens Ritter
 */
public class AbstractBaseJobTest extends AbstractMojoTestCase {

    /** The Constant SIMPLE. */
    public static final String SIMPLE="src/test/resources/simple.xml";
    
    /** The Constant VALUES. */
    public static final String VALUES="src/test/resources/values.xml";
    
    /** The Constant OTHER. */
    public static final String OTHER="src/test/resources/other.xml";

    /**
     * Load mojo.
     * 
     * @param value the value
     * @param goal the goal
     * 
     * @return the abstract base job
     * 
     * @throws Exception the exception
     */
    public AbstractBaseJob loadMojo(String value, String goal) throws Exception {
        File pom = new File( getBasedir(), value );
        AbstractBaseJob result = (AbstractBaseJob) lookupMojo(goal, pom);
        // FAKE !!
        if (result instanceof GenerateConfig) {
            ((GenerateConfig)result).outputDirectory = new File("target");
        }

        return result;
    }

    /**
     * Test base.
     * 
     * @throws Exception the exception
     */
    public void testBase() throws Exception {
        File pom = new File( getBasedir(), SIMPLE );
        AbstractBaseJob mojo = (AbstractBaseJob) lookupMojo("list", pom);
        assertNotNull(mojo);


        mojo.defaultValues(null);

        assertEquals("http://www.jens.org:8080/hudson",mojo.hudsonUrl_used);
        assertEquals("maven-hudsonconfig-plugin",mojo.jobName_used);
    }
}
