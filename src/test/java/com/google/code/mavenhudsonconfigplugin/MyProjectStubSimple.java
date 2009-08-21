package com.google.code.mavenhudsonconfigplugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Build;
import org.apache.maven.model.CiManagement;
import org.apache.maven.model.Model;
import org.apache.maven.model.Scm;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.codehaus.plexus.util.ReaderFactory;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

/**
 * The Class MyProjectStubSimple.
 * 
 * @author Jens Ritter
 */
public class MyProjectStubSimple extends MavenProjectStub {
    
    /** The ci. */
    private CiManagement ci;
    
    /** The scm. */
    private Scm scm; 
    
    /** The build. */
    private Build build;

    /**
     * Instantiates a new my project stub simple.
     */
    public MyProjectStubSimple() {
        loadData("src/test/resources/simple.xml");
    }

    /**
     * Load data.
     * 
     * @param path the path
     */
    @SuppressWarnings("unchecked")
    protected void loadData(String path) {
        MavenXpp3Reader pomReader = new MavenXpp3Reader();
        Model model;
        try {
            model = pomReader.read(ReaderFactory.newXmlReader(new File(getBasedir(),path)));
            setModel(model);
        } catch (IOException e) {
            throw new RuntimeException(e );
        } catch (XmlPullParserException e) {
            throw new RuntimeException(e );
        }

        setGroupId(model.getGroupId() );
        setArtifactId(model.getArtifactId() );
        setVersion(model.getVersion() );
        setName(model.getName() );
        setUrl(model.getUrl() );
        setPackaging(model.getPackaging() );
        setCiManagement(model.getCiManagement());
        setScm(model.getScm());
        setDescription(model.getDescription());

        Build buildTemp = new Build();
        buildTemp.setFinalName(model.getArtifactId() );
        buildTemp.setDirectory(getBasedir() + "/target" );
        buildTemp.setSourceDirectory(getBasedir() + "/src/main/java" );
        buildTemp.setOutputDirectory(getBasedir() + "/target/classes" );
        buildTemp.setTestSourceDirectory(getBasedir() + "/src/test/java" );
        buildTemp.setTestOutputDirectory(getBasedir() + "/target/test-classes" );

        setBuild(buildTemp );

        List compileSourceRoots = new ArrayList();
        compileSourceRoots.add(getBasedir() + "/src/main/java" );
        setCompileSourceRoots(compileSourceRoots );

        List testCompileSourceRoots = new ArrayList();
        testCompileSourceRoots.add(getBasedir() + "/src/test/java" );
        setTestCompileSourceRoots(testCompileSourceRoots );

    }

    /* (non-Javadoc)
     * @see org.apache.maven.plugin.testing.stubs.MavenProjectStub#setCiManagement(org.apache.maven.model.CiManagement)
     */
    @Override
    public void setCiManagement(CiManagement value) {
        this.ci = value;
    }

    /* (non-Javadoc)
     * @see org.apache.maven.plugin.testing.stubs.MavenProjectStub#getCiManagement()
     */
    @Override
    public CiManagement getCiManagement() {
        return ci;
    }

    /* (non-Javadoc)
     * @see org.apache.maven.plugin.testing.stubs.MavenProjectStub#setScm(org.apache.maven.model.Scm)
     */
    @Override
    public void setScm(Scm value) {
        this.scm = value;
    }

    /* (non-Javadoc)
     * @see org.apache.maven.plugin.testing.stubs.MavenProjectStub#getScm()
     */
    @Override
    public Scm getScm() {
        return scm;
    }

    /* (non-Javadoc)
     * @see org.apache.maven.plugin.testing.stubs.MavenProjectStub#getBuild()
     */
    @Override
    public Build getBuild() {
        return build;
    }

    /* (non-Javadoc)
     * @see org.apache.maven.plugin.testing.stubs.MavenProjectStub#setBuild(org.apache.maven.model.Build)
     */
    @Override
    public void setBuild(Build build) {
        this.build = build;
    }


}
