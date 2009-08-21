package com.google.code.mavenhudsonconfigplugin;

import java.io.File;

/**
 * The Class TestGetConfigJobTest.
 * 
 * @author Jens Ritter
 */
public class TestGetConfigJobTest extends AbstractBaseJobTest {

    /**
     * Test execute.
     * 
     * @throws Exception the exception
     */
    public void testExecute() throws Exception {
        File out = new File("target/config.xml");
        if (out.exists()) {
            assertTrue(out.delete());
        }
        assertFalse(out.exists());
        GetConfigJob mojo = (GetConfigJob) loadMojo(SIMPLE,"config");
        assertNotNull(mojo);
        mojo.execute();
        assertTrue(out.exists());
    }

}
