package com.google.code.mavenhudsonconfigplugin.intern;

import java.util.List;
import java.util.NoSuchElementException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

/**
 * The Class HudsonConfig.
 * 
 * @author Jens Ritter
 */
public final class HudsonConfig {

    /** The doc. */
    private Document doc = null;
    
    /** The root. */
    private Element root = null;
    
    /** The jobname. */
    private String jobname = null;

    /**
     * Instantiates a new hudson config.
     */
    private HudsonConfig() {

    }

    /**
     * Parses the document.
     * 
     * @param jobName the job name
     * @param doc the doc
     * 
     * @return the hudson config
     */
    public static HudsonConfig parseDocument(String jobName, Document doc) {
        HudsonConfig cfg = new HudsonConfig();
        cfg.doc = doc;
        cfg.root = doc.getRootElement();
        cfg.jobname = jobName;
        return cfg;
    }

    /**
     * Gets the xml.
     * 
     * @return the xml
     */
    public String getXml() {
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        return out.outputString(doc);
    }
    
    /**
     * Gets the job name.
     * 
     * @return the job name
     */
    public String getJobName() {
        return jobname;
    }
    
    /**
     * Sets the job name.
     * 
     * @param value the new job name
     */
    public void setJobName(String value) {
        jobname = value;
    }
    // ---------

    /**
     * Int val.
     * 
     * @param value the value
     * 
     * @return the int
     */
    private int intVal(Element value) {
        String val = value.getTextTrim();
        return Integer.valueOf(val);
    }

    /**
     * Bol val.
     * 
     * @param it the it
     * 
     * @return true, if successful
     */
    private boolean bolVal(Element it) {
        String txt = it.getTextTrim();
        if (txt.equals("true")) {
            return true;
        }
        if (txt.endsWith("false")) {
            return false;
        }
        throw new RuntimeException(it.getName() + ":Unknwon Boolean Value : " + txt);
    }

    /**
     * To bool.
     * 
     * @param value the value
     * 
     * @return the string
     */
    private String toBool(boolean value) {
        if (value) {
            return "true";
        }
        return "false";
    }

    /**
     * Gets the description.
     * 
     * @return the description
     */
    public String getDescription() {
        return root.getChild("description").getTextNormalize();

    }

    /**
     * Sets the description.
     * 
     * @param description the new description
     */
    public void setDescription(String description) {
        root.getChild("description").setText(description);
    }

    /**
     * Gets the log rotator num to keep.
     * 
     * @return the log rotator num to keep
     */
    public int getLogRotatorNumToKeep() {
        if (root.getChild("logRotator") == null) {
            throw new NoSuchElementException("logRotator");
        }
        return intVal(root.getChild("logRotator").getChild("numToKeep"));

    }

    /**
     * Sets the log rotator num to keep.
     * 
     * @param logRotatorNumToKeep the new log rotator num to keep
     */
    public void setLogRotatorNumToKeep(int logRotatorNumToKeep) {
        if (root.getChild("logRotator") == null) {
            root.addContent(new Element("logRotator"));
        }
        if (root.getChild("logRotator").getChild("numToKeep") == null) {
            root.getChild("logRotator").addContent(new Element("numToKeep"));
        }
        root.getChild("logRotator").getChild("numToKeep").setText("" + logRotatorNumToKeep);
    }

    /**
     * Gets the svn locations.
     * 
     * @return the svn locations
     */
    public int getSvnLocations() {
        Element svnRoot = root.getChild("scm");
        Element svnLocations = svnRoot.getChild("locations");
        return svnLocations.getChildren("hudson.scm.SubversionSCM_-ModuleLocation").size();
    }

    /**
     * Gets the svn remote.
     * 
     * @param index the index
     * 
     * @return the svn remote
     */
    public String getSvnRemote(int index) {
        Element svnRoot = root.getChild("scm");
        Element svnLocations = svnRoot.getChild("locations");
        Element parent = (Element) svnLocations.getChildren("hudson.scm.SubversionSCM_-ModuleLocation").get(index);
        return parent.getChild("remote").getTextNormalize();
    }

    /**
     * Sets the svn remote.
     * 
     * @param index the index
     * @param svnRemote the svn remote
     */
    public void setSvnRemote(int index, String svnRemote) {
        Element svnRoot = root.getChild("scm");
        Element svnLocations = svnRoot.getChild("locations");
        Element parent = (Element) svnLocations.getChildren("hudson.scm.SubversionSCM_-ModuleLocation").get(index);
        parent.getChild("remote").setText(svnRemote);
    }

    /**
     * Gets the svn local.
     * 
     * @param index the index
     * 
     * @return the svn local
     */
    public String getSvnLocal(int index) {
        Element svnRoot = root.getChild("scm");
        Element svnLocations = svnRoot.getChild("locations");
        Element parent = (Element) svnLocations.getChildren("hudson.scm.SubversionSCM_-ModuleLocation").get(index);
        return parent.getChild("local").getTextNormalize();

    }

    /**
     * Sets the svn local.
     * 
     * @param index the index
     * @param svnLocal the svn local
     */
    public void setSvnLocal(int index, String svnLocal) {
        Element svnRoot = root.getChild("scm");
        Element svnLocations = svnRoot.getChild("locations");
        Element parent = (Element) svnLocations.getChildren("hudson.scm.SubversionSCM_-ModuleLocation").get(index);
        parent.getChild("local").setText(svnLocal);
    }

    /**
     * Checks if is disabled.
     * 
     * @return true, if is disabled
     */
    public boolean isDisabled() {
        return bolVal(root.getChild("disabled"));

    }

    /**
     * Sets the disabled.
     * 
     * @param disabled the new disabled
     */
    public void setDisabled(boolean disabled) {
        root.getChild("disabled").setText(toBool(disabled));
    }

    /**
     * Checks if is block build when upstream building.
     * 
     * @return true, if is block build when upstream building
     */
    public boolean isBlockBuildWhenUpstreamBuilding() {
        if (root.getChild("blockBuildWhenUpstreamBuilding") == null) {
            throw new NoSuchElementException("blockBuildWhenUpstreamBuilding");
        }
        return bolVal(root.getChild("blockBuildWhenUpstreamBuilding"));

    }

    /**
     * Sets the block build when upstream building.
     * 
     * @param blockBuildWhenUpstreamBuilding the new block build when upstream building
     */
    public void setBlockBuildWhenUpstreamBuilding(boolean blockBuildWhenUpstreamBuilding) {
        root.getChild("blockBuildWhenUpstreamBuilding").setText(toBool(blockBuildWhenUpstreamBuilding));
    }

    /**
     * Gets the goals.
     * 
     * @return the goals
     */
    public String getGoals() {
        if (root.getChild("goals") == null) {
            throw new NoSuchElementException("goals");
        }
        return root.getChild("goals").getTextNormalize();
    }

    /**
     * Sets the goals.
     * 
     * @param goals the new goals
     */
    public void setGoals(String goals) {
        root.getChild("goals").setText(goals);
    }

    /**
     * Gets the maven opts.
     * 
     * @return the maven opts
     */
    public String getMavenOpts() {
        if (root.getChild("mavenOpts") == null) {
            throw new NoSuchElementException("mavenOpts");
        }
        return root.getChild("mavenOpts").getTextNormalize();

    }

    /**
     * Sets the maven opts.
     * 
     * @param mavenOpts the new maven opts
     */
    public void setMavenOpts(String mavenOpts) {
        root.getChild("mavenOpts").setText(mavenOpts);
    }

    /**
     * Gets the find bugs threshold limit.
     * 
     * @return the find bugs threshold limit
     */
    public String getFindBugsThresholdLimit() {
        Element reporters = root.getChild("reporters");
        Element parent = reporters.getChild("hudson.plugins.findbugs.FindBugsReporter");
        return parent.getChild("thresholdLimit").getTextNormalize();
    }

    /**
     * Sets the find bugs threshold limit.
     * 
     * @param findBugsThresholdLimit the new find bugs threshold limit
     */
    public void setFindBugsThresholdLimit(String findBugsThresholdLimit) {
        Element reporters = root.getChild("reporters");
        Element parent = reporters.getChild("hudson.plugins.findbugs.FindBugsReporter");
        parent.getChild("thresholdLimit").setText(findBugsThresholdLimit);
    }

    /**
     * Gets the pmd threshold limit.
     * 
     * @return the pmd threshold limit
     */
    public String getPmdThresholdLimit() {
        Element reporters = root.getChild("reporters");
        Element parent = reporters.getChild("hudson.plugins.pmd.PmdReporter");
        return parent.getChild("thresholdLimit").getTextNormalize();
    }

    /**
     * Sets the pmd threshold limit.
     * 
     * @param pmdThresholdLimit the new pmd threshold limit
     */
    public void setPmdThresholdLimit(String pmdThresholdLimit) {
        Element reporters = root.getChild("reporters");
        Element parent = reporters.getChild("hudson.plugins.pmd.PmdReporter");
        parent.getChild("thresholdLimit").setText(pmdThresholdLimit);
    }

    /**
     * Gets the checkstyle threshold limit.
     * 
     * @return the checkstyle threshold limit
     */
    public String getCheckstyleThresholdLimit() {
        Element reporters = root.getChild("reporters");
        Element parent = reporters.getChild("hudson.plugins.checkstyle.CheckStyleReporter");
        return parent.getChild("thresholdLimit").getTextNormalize();
    }

    /**
     * Sets the checkstyle threshold limit.
     * 
     * @param checkstyleThresholdLimit the new checkstyle threshold limit
     */
    public void setCheckstyleThresholdLimit(String checkstyleThresholdLimit) {
        Element reporters = root.getChild("reporters");
        Element parent = reporters.getChild("hudson.plugins.checkstyle.CheckStyleReporter");
        parent.getChild("thresholdLimit").setText(checkstyleThresholdLimit);
    }

    /**
     * Gets the cobertura report file.
     * 
     * @return the cobertura report file
     */
    public String getCoberturaReportFile() {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        return coberturaRoot.getChild("coberturaReportFile").getTextNormalize();

    }

    /**
     * Sets the cobertura report file.
     * 
     * @param coberturaReportFile the new cobertura report file
     */
    public void setCoberturaReportFile(String coberturaReportFile) {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        coberturaRoot.getChild("coberturaReportFile").setText(coberturaReportFile);
    }

    /**
     * Checks if is cobertura only stable.
     * 
     * @return true, if is cobertura only stable
     */
    public boolean isCoberturaOnlyStable() {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        return bolVal(coberturaRoot.getChild("onlyStable"));

    }

    /**
     * Sets the cobertura only stable.
     * 
     * @param coberturaOnlyStable the new cobertura only stable
     */
    public void setCoberturaOnlyStable(boolean coberturaOnlyStable) {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        coberturaRoot.getChild("onlyStable").setText(toBool(coberturaOnlyStable));
    }


    /**
     * Gets the cobertura x metrics.
     * 
     * @param type the type
     * 
     * @return the cobertura x metrics
     */
    private int getCoberturaXMetrics(String type) {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        Element sum = coberturaRoot.getChild(type).getChild("targets");
        return sum.getChildren("entry").size();
    }

    /**
     * Sets the cobertura x metric.
     * 
     * @param type the type
     * @param i the i
     * @param value the value
     * @param j the j
     */
    private void setCoberturaXMetric(String type, int i, String value, int j) {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        Element sum = coberturaRoot.getChild(type).getChild("targets");
        if (i + 1 > sum.getChildren("entry").size()) {
            appendCoberturaXMetric(type, value, j);
            return;
        }
        Element parent = (Element) sum.getChildren("entry").get(i);
        parent.getChild("hudson.plugins.cobertura.targets.CoverageMetric").setText(value);
        parent.getChild("int").setText(Integer.toString(j));
    }

    /**
     * Append cobertura x metric.
     * 
     * @param type the type
     * @param value the value
     * @param wert the wert
     */
    private void appendCoberturaXMetric(String type, String value, int wert) {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        Element sum = coberturaRoot.getChild(type).getChild("targets");
        Element neu = new Element("entry");
        Element kind = new Element("hudson.plugins.cobertura.targets.CoverageMetric");
        kind.setText(value);
        Element integer = new Element("int");
        integer.setText(Integer.toString(wert));

        neu.addContent(kind);
        neu.addContent(integer);
        sum.addContent(neu);
    }

    /**
     * Gets the cobertura x metric.
     * 
     * @param type the type
     * @param i the i
     * 
     * @return the cobertura x metric
     */
    private String getCoberturaXMetric(String type, int i) {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        Element sum = coberturaRoot.getChild(type).getChild("targets");
        return ((Element) sum.getChildren("entry").get(i)).getChild("hudson.plugins.cobertura.targets.CoverageMetric")
                .getTextNormalize();
    }

    /**
     * Gets the cobertura x metric value.
     * 
     * @param type the type
     * @param i the i
     * 
     * @return the cobertura x metric value
     */
    private int getCoberturaXMetricValue(String type, int i) {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        Element sum = coberturaRoot.getChild(type).getChild("targets");
        return intVal(((Element) sum.getChildren("entry").get(i)).getChild("int"));
    }

    // Healthy

    /**
     * Gets the cobertura healthy metrics.
     * 
     * @return the cobertura healthy metrics
     */
    public int getCoberturaHealthyMetrics() {
        return getCoberturaXMetrics("healthyTarget");
    }

    /**
     * Sets the cobertura healthy metric.
     * 
     * @param i the i
     * @param string the string
     * @param j the j
     */
    public void setCoberturaHealthyMetric(int i, String string, int j) {
        setCoberturaXMetric("healthyTarget", i, string, j);
    }

    /**
     * Append cobertura healthy metric.
     * 
     * @param value the value
     * @param wert the wert
     */
    public void appendCoberturaHealthyMetric(String value, int wert) {
        appendCoberturaXMetric("healthyTarget", value, wert);
    }

    /**
     * Gets the cobertura healthy metric.
     * 
     * @param i the i
     * 
     * @return the cobertura healthy metric
     */
    public String getCoberturaHealthyMetric(int i) {
        return getCoberturaXMetric("healthyTarget", i);
    }

    /**
     * Gets the cobertura healthy metric value.
     * 
     * @param i the i
     * 
     * @return the cobertura healthy metric value
     */
    public int getCoberturaHealthyMetricValue(int i) {
        return getCoberturaXMetricValue("healthyTarget", i);
    }

    // UnHealthy

    /**
     * Sets the cobertura un healthy metric.
     * 
     * @param i the i
     * @param string the string
     * @param j the j
     */
    public void setCoberturaUnHealthyMetric(int i, String string, int j) {
        setCoberturaXMetric("unhealthyTarget", i, string, j);
    }

    /**
     * Gets the cobertura un healthy metrics.
     * 
     * @return the cobertura un healthy metrics
     */
    public int getCoberturaUnHealthyMetrics() {
        return getCoberturaXMetrics("unhealthyTarget");
    }

    /**
     * Gets the cobertura un healthy metric.
     * 
     * @param i the i
     * 
     * @return the cobertura un healthy metric
     */
    public String getCoberturaUnHealthyMetric(int i) {
        return getCoberturaXMetric("unhealthyTarget", i);

    }

    /**
     * Gets the cobertura un healthy metric value.
     * 
     * @param i the i
     * 
     * @return the cobertura un healthy metric value
     */
    public int getCoberturaUnHealthyMetricValue(int i) {
        return getCoberturaXMetricValue("unhealthyTarget", i);
    }

    // FAIL

    /**
     * Gets the cobertura fail metrics.
     * 
     * @return the cobertura fail metrics
     */
    public int getCoberturaFailMetrics() {
        return getCoberturaXMetrics("failingTarget");

    }

    /**
     * Gets the cobertura fail metric.
     * 
     * @param i the i
     * 
     * @return the cobertura fail metric
     */
    public String getCoberturaFailMetric(int i) {
        return getCoberturaXMetric("failingTarget", i);
    }

    /**
     * Gets the cobertura fail metric value.
     * 
     * @param i the i
     * 
     * @return the cobertura fail metric value
     */
    public int getCoberturaFailMetricValue(int i) {
        return getCoberturaXMetricValue("failingTarget", i);
    }

    /**
     * Sets the cobertura fail metric.
     * 
     * @param i the i
     * @param string the string
     * @param j the j
     */
    public void setCoberturaFailMetric(int i, String string, int j) {
        setCoberturaXMetric("failingTarget", i, string, j);
    }

    /**
     * Gets the warnings threashold limit.
     * 
     * @return the warnings threashold limit
     */
    public String getWarningsThreasholdLimit() {
        Element publisher = root.getChild("publishers");
        Element warings = publisher.getChild("hudson.plugins.warnings.WarningsPublisher");
        return warings.getChild("thresholdLimit").getTextNormalize();
    }

    /**
     * Sets the warnings threashold limit.
     * 
     * @param warningsThreasholdLimit the new warnings threashold limit
     */
    public void setWarningsThreasholdLimit(String warningsThreasholdLimit) {
        Element publisher = root.getChild("publishers");
        Element warings = publisher.getChild("hudson.plugins.warnings.WarningsPublisher");
        warings.getChild("thresholdLimit").setText(warningsThreasholdLimit);
    }

    /**
     * Gets the warnings parsers.
     * 
     * @return the warnings parsers
     */
    public int getWarningsParsers() {
        Element publisher = root.getChild("publishers");
        Element warings = publisher.getChild("hudson.plugins.warnings.WarningsPublisher");
        Element parser = warings.getChild("parserNames");
        return parser.getChildren("string").size();
    }

    /**
     * Gets the warnings parser.
     * 
     * @param i the i
     * 
     * @return the warnings parser
     */
    @SuppressWarnings("unchecked")
    public String getWarningsParser(int i) {
        Element publisher = root.getChild("publishers");
        Element warings = publisher.getChild("hudson.plugins.warnings.WarningsPublisher");
        Element parser = warings.getChild("parserNames");
        List<Element> names = parser.getChildren("string");
        return names.get(i).getTextNormalize();
    }

    /**
     * Sets the warnings parser.
     * 
     * @param i the i
     * @param string the string
     */
    @SuppressWarnings("unchecked")
    public void setWarningsParser(int i, String string) {
        Element publisher = root.getChild("publishers");
        Element warings = publisher.getChild("hudson.plugins.warnings.WarningsPublisher");
        Element parser = warings.getChild("parserNames");
        List<Element> names = parser.getChildren("string");
        if (i + 1 > names.size()) {
            appendWarningsParser(string);
            return;
        }
        names.get(i).setText(string);
    }

    /**
     * Append warnings parser.
     * 
     * @param value the value
     */
    public void appendWarningsParser(String value) {
        Element publisher = root.getChild("publishers");
        Element warings = publisher.getChild("hudson.plugins.warnings.WarningsPublisher");
        Element names = warings.getChild("parserNames");
        Element neu = new Element("string");
        neu.setText(value);
        names.addContent(neu);
    }

    /**
     * Checks if is ignore upstream changes.
     * 
     * @return true, if is ignore upstream changes
     */
    public boolean isIgnoreUpstreamChanges() {
        if (root.getChild("ignoreUpstremChanges") == null) {
            throw new NoSuchElementException("NoSuchElemenet: ignoreUpstremChanges");
        }
        return bolVal(root.getChild("ignoreUpstremChanges"));
    }

    /**
     * Sets the ignored upstream changes.
     * 
     * @param b the new ignored upstream changes
     */
    public void setIgnoredUpstreamChanges(boolean b) {
        root.getChild("ignoreUpstremChanges").setText(toBool(b));        
    }

    /**
     * Checks for find bugs.
     * 
     * @return true, if successful
     */
    public boolean hasFindBugs() {
        Element reporters = root.getChild("reporters");
        if (reporters == null) {
            return false;
        }
        return reporters.getChild("hudson.plugins.findbugs.FindBugsReporter") != null;
    }

    /**
     * Checks for pmd.
     * 
     * @return true, if successful
     */
    public boolean hasPMD() {
        Element reporters = root.getChild("reporters");
        if (reporters == null) {
            return false;
        }
        return reporters.getChild("hudson.plugins.pmd.PmdReporter") != null;
    }

    /**
     * Checks for checkstyle.
     * 
     * @return true, if successful
     */
    public boolean hasCheckstyle() {
        Element reporters = root.getChild("reporters");
        if (reporters == null) {
            return false;
        }
        return reporters.getChild("hudson.plugins.checkstyle.CheckStyleReporter") != null;
    }

}
