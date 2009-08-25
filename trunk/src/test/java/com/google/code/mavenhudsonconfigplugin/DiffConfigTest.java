package com.google.code.mavenhudsonconfigplugin;


public class DiffConfigTest extends AbstractBaseJobTest {

    public void testExecute() throws Exception {
        DiffConfig mojo = (DiffConfig) loadMojo(OTHER,"diff");
        mojo.execute();
        assertTrue(true);
    }

}
