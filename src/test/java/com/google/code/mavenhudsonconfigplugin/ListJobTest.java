package com.google.code.mavenhudsonconfigplugin;

/**
 * The Class ListJobTest.
 */
public class ListJobTest extends AbstractBaseJobTest {

    /**
     * Test execute.
     * 
     * @throws Exception the exception
     */
    public void testExecute() throws Exception {
        ListJob mojo = (ListJob) loadMojo(SIMPLE,"list");
        mojo.execute();
        assertTrue(true);
    }

}
