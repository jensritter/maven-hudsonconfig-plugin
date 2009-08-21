package com.google.code.mavenhudsonconfigplugin;


/**
 * The Class TestHelpJobTest.
 * 
 * @author Jens Ritter
 */
public class TestHelpJobTest extends AbstractBaseJobTest {

    /**
     * Test execute.
     * 
     * @throws Exception the exception
     */
    public void testExecute() throws Exception {
        HelpJob mojo = (HelpJob) loadMojo(SIMPLE,"help");
        mojo.execute();
        assertTrue(true);
    }

}
