package com.google.code.mavenhudsonconfigplugin;

import com.google.code.mavenhudsonconfigplugin.GenerateConfig;

import junit.framework.TestCase;

public class GenerateConfigTest extends TestCase {
    
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
