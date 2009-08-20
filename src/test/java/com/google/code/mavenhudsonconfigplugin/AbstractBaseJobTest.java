package com.google.code.mavenhudsonconfigplugin;

import java.io.File;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;


public class AbstractBaseJobTest extends AbstractMojoTestCase {

    public static final String SIMPLE="src/test/resources/simple.xml";
    public static final String VALUES="src/test/resources/values.xml";

    public AbstractBaseJob loadMojo(String value, String goal) throws Exception {
        File pom = new File( getBasedir(), value );
        AbstractBaseJob result = (AbstractBaseJob) lookupMojo(goal, pom);
        // FAKE !!
        if (result instanceof GenerateConfig) {
            ((GenerateConfig)result).outputDirectory = new File("target");
        }

        return result;
    }

    public void testBase() throws Exception {
        File pom = new File( getBasedir(), SIMPLE );
        AbstractBaseJob mojo = (AbstractBaseJob) lookupMojo("list", pom);
        assertNotNull(mojo);


        mojo.defaultValues(null);

        assertEquals("http://www.jens.org:8080/hudson",mojo.hudsonUrl_used);
        assertEquals("maven-hudsonconfig-plugin",mojo.jobName_used);
    }
}
