package com.google.code.mavenhudsonconfigplugin;

import com.google.code.mavenhudsonconfigplugin.DisplayInfo;

import junit.framework.TestCase;

public class DisplayInfoTest extends TestCase {
    
    public void testPattern() {

        okThis("http://localhost/test/trunk/");
        okThis("http://localhost/test/trunk");
        okThis("http://localhost/test/tag/test-1.0");
        okThis("http://localhost/test/tag/test-1.0/");
        okThis("http://localhost/test");
        failThis("http://localhost");
    }


    DisplayInfo info = new DisplayInfo();

    
    private void okThis(String path) {
        String find = info.parseSvnName(path);
        assertTrue(find.equals("test") || find.equals("test-1.0"));
    }
    
    private void failThis(String path) {
        String find = info.parseSvnName(path);
        assertFalse(find.equals("test") || find.equals("test-1.0"));
    }
    
    

}
