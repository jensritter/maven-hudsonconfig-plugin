package com.google.code.mavenhudsonconfigplugin;

import java.io.File;


public class TestGetConfigJobTest extends AbstractBaseJobTest {

	public void testExecute() throws Exception {
		File out = new File("target/config.xml");
		if (out.exists()) {
			out.delete();
		}
		assertFalse(out.exists());
		GetConfigJob mojo = (GetConfigJob) loadMojo(SIMPLE,"config");
		assertNotNull(mojo);
		mojo.execute();
		assertTrue(out.exists());
	}

}
