package com.google.code.mavenhudsonconfigplugin;


/**
 * The Class GenerateConfigTest.
 * 
 * @author Jens Ritter
 */
public class GenerateConfigTest extends AbstractBaseJobTest {
	
	public void testParameter() throws Exception {
		GenerateConfig cfg = (GenerateConfig) loadMojo(VALUES,"generate");
		cfg.defaultValues(null);
		
		
		assertEquals("DESCRIPTION",cfg.description_used);
		assertEquals("clean install deploy site site:deploy",cfg.goals_used);
		assertEquals("http://www.google.de/hudson/jobs/google",cfg.hudsonUrl_used);
		assertEquals("google",cfg.jobName_used);
		assertEquals("svn",cfg.localpart_used);
		assertEquals("https://maven-hudsonconfig-plugin.googlecode.com/svn/trunk/",cfg.svnPath_used);		
	}

	
	// Parser : 
    public void testPattern() {
        okThis("http://localhost/test/trunk/");
        okThis("http://localhost/test/trunk");
        okThis("http://localhost/test/tag/test-1.0");
        okThis("http://localhost/test/tag/test-1.0/");
        okThis("http://localhost/test");
        failThis("http://localhost");
    }

    GenerateConfig info = new GenerateConfig();

    private void okThis(String path) {
        String find = info.parseSvnName(path);
        assertTrue(find.equals("test") || find.equals("test-1.0"));
    }
    
    private void failThis(String path) {
        String find = info.parseSvnName(path);
        assertFalse(find.equals("test") || find.equals("test-1.0"));
    }
    
    

}
