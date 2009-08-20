package com.google.code.mavenhudsonconfigplugin;



public class ListJobTest extends AbstractBaseJobTest {

	public void testExecute() throws Exception {
		ListJob mojo = (ListJob) loadMojo(SIMPLE,"list");
		mojo.execute();
		assertTrue(true);
	}

}
