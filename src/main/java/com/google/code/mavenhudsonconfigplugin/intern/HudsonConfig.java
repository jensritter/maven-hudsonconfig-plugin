package com.google.code.mavenhudsonconfigplugin.intern;

import java.util.List;
import java.util.NoSuchElementException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class HudsonConfig {

    private Document doc;
    private Element root;

    private HudsonConfig() {

    }

    public static HudsonConfig parseDocument(Document doc) {
        HudsonConfig cfg = new HudsonConfig();
        cfg.doc = doc;
        cfg.root = doc.getRootElement();
        return cfg;
    }

    public String getXml() {
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        return out.outputString(doc);
    }
    // ---------

    private int intVal(Element value) {
        String val = value.getTextTrim();
        return Integer.valueOf(val);
    }

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

    private String toBool(boolean value) {
        if (value) {
            return "true";
        }
        return "false";
    }

    public String getDescription() {
        return root.getChild("description").getTextNormalize();

    }

    public void setDescription(String description) {
        root.getChild("description").setText(description);
    }

    public int getLogRotatorNumToKeep() {
        if (root.getChild("logRotator") == null) {
            throw new NoSuchElementException("logRotator");
        }
        return intVal(root.getChild("logRotator").getChild("numToKeep"));

    }

    public void setLogRotatorNumToKeep(int logRotatorNumToKeep) {
        if (root.getChild("logRotator") == null) {
            root.addContent(new Element("logRotator"));
        }
        if (root.getChild("logRotator").getChild("numToKeep") == null) {
            root.getChild("logRotator").addContent(new Element("numToKeep"));
        }
        root.getChild("logRotator").getChild("numToKeep").setText("" + logRotatorNumToKeep);
    }

    public int getSvnLocations() {
        Element svnRoot = root.getChild("scm");
        Element svnLocations = svnRoot.getChild("locations");
        return svnLocations.getChildren("hudson.scm.SubversionSCM_-ModuleLocation").size();
    }

    public String getSvnRemote(int index) {
        Element svnRoot = root.getChild("scm");
        Element svnLocations = svnRoot.getChild("locations");
        Element parent = (Element) svnLocations.getChildren("hudson.scm.SubversionSCM_-ModuleLocation").get(index);
        return parent.getChild("remote").getTextNormalize();
    }

    public void setSvnRemote(int index, String svnRemote) {
        Element svnRoot = root.getChild("scm");
        Element svnLocations = svnRoot.getChild("locations");
        Element parent = (Element) svnLocations.getChildren("hudson.scm.SubversionSCM_-ModuleLocation").get(index);
        parent.getChild("remote").setText(svnRemote);
    }

    public String getSvnLocal(int index) {
        Element svnRoot = root.getChild("scm");
        Element svnLocations = svnRoot.getChild("locations");
        Element parent = (Element) svnLocations.getChildren("hudson.scm.SubversionSCM_-ModuleLocation").get(index);
        return parent.getChild("local").getTextNormalize();

    }

    public void setSvnLocal(int index, String svnLocal) {
        Element svnRoot = root.getChild("scm");
        Element svnLocations = svnRoot.getChild("locations");
        Element parent = (Element) svnLocations.getChildren("hudson.scm.SubversionSCM_-ModuleLocation").get(index);
        parent.getChild("local").setText(svnLocal);
    }

    public boolean isDisabled() {
        return bolVal(root.getChild("disabled"));

    }

    public void setDisabled(boolean disabled) {
        root.getChild("disabled").setText(toBool(disabled));
    }

    public boolean isBlockBuildWhenUpstreamBuilding() {
        if (root.getChild("blockBuildWhenUpstreamBuilding") == null) {
            throw new NoSuchElementException("blockBuildWhenUpstreamBuilding");
        }
        return bolVal(root.getChild("blockBuildWhenUpstreamBuilding"));

    }

    public void setBlockBuildWhenUpstreamBuilding(boolean blockBuildWhenUpstreamBuilding) {
        root.getChild("blockBuildWhenUpstreamBuilding").setText(toBool(blockBuildWhenUpstreamBuilding));
    }

    public String getGoals() {
        if (root.getChild("goals") == null) {
            throw new NoSuchElementException("goals");
        }
        return root.getChild("goals").getTextNormalize();
    }

    public void setGoals(String goals) {
        root.getChild("goals").setText(goals);
    }

    public String getMavenOpts() {
        if (root.getChild("mavenOpts") == null) {
            throw new NoSuchElementException("mavenOpts");
        }
        return root.getChild("mavenOpts").getTextNormalize();

    }

    public void setMavenOpts(String mavenOpts) {
        root.getChild("mavenOpts").setText(mavenOpts);
    }

    public String getFindBugsThresholdLimit() {
        Element reporters = root.getChild("reporters");
        Element parent = reporters.getChild("hudson.plugins.findbugs.FindBugsReporter");
        return parent.getChild("thresholdLimit").getTextNormalize();
    }

    public void setFindBugsThresholdLimit(String findBugsThresholdLimit) {
        Element reporters = root.getChild("reporters");
        Element parent = reporters.getChild("hudson.plugins.findbugs.FindBugsReporter");
        parent.getChild("thresholdLimit").setText(findBugsThresholdLimit);
    }

    public String getPmdThresholdLimit() {
        Element reporters = root.getChild("reporters");
        Element parent = reporters.getChild("hudson.plugins.pmd.PmdReporter");
        return parent.getChild("thresholdLimit").getTextNormalize();
    }

    public void setPmdThresholdLimit(String pmdThresholdLimit) {
        Element reporters = root.getChild("reporters");
        Element parent = reporters.getChild("hudson.plugins.pmd.PmdReporter");
        parent.getChild("thresholdLimit").setText(pmdThresholdLimit);
    }

    public String getCheckstyleThresholdLimit() {
        Element reporters = root.getChild("reporters");
        Element parent = reporters.getChild("hudson.plugins.checkstyle.CheckStyleReporter");
        return parent.getChild("thresholdLimit").getTextNormalize();
    }

    public void setCheckstyleThresholdLimit(String checkstyleThresholdLimit) {
        Element reporters = root.getChild("reporters");
        Element parent = reporters.getChild("hudson.plugins.checkstyle.CheckStyleReporter");
        parent.getChild("thresholdLimit").setText(checkstyleThresholdLimit);
    }

    public String getCoberturaReportFile() {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        return coberturaRoot.getChild("coberturaReportFile").getTextNormalize();

    }

    public void setCoberturaReportFile(String coberturaReportFile) {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        coberturaRoot.getChild("coberturaReportFile").setText(coberturaReportFile);
    }

    public boolean isCoberturaOnlyStable() {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        return bolVal(coberturaRoot.getChild("onlyStable"));

    }

    public void setCoberturaOnlyStable(boolean coberturaOnlyStable) {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        coberturaRoot.getChild("onlyStable").setText(toBool(coberturaOnlyStable));
    }


    private int getCoberturaXMetrics(String type) {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        Element sum = coberturaRoot.getChild(type).getChild("targets");
        return sum.getChildren("entry").size();
    }

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

    private String getCoberturaXMetric(String type, int i) {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        Element sum = coberturaRoot.getChild(type).getChild("targets");
        return ((Element) sum.getChildren("entry").get(i)).getChild("hudson.plugins.cobertura.targets.CoverageMetric")
                .getTextNormalize();
    }

    private int getCoberturaXMetricValue(String type, int i) {
        Element publishers = root.getChild("publishers");
        Element coberturaRoot = publishers.getChild("hudson.plugins.cobertura.CoberturaPublisher");
        Element sum = coberturaRoot.getChild(type).getChild("targets");
        return intVal(((Element) sum.getChildren("entry").get(i)).getChild("int"));
    }

    // Healthy

    public int getCoberturaHealthyMetrics() {
        return getCoberturaXMetrics("healthyTarget");
    }

    public void setCoberturaHealthyMetric(int i, String string, int j) {
        setCoberturaXMetric("healthyTarget", i, string, j);
    }

    public void appendCoberturaHealthyMetric(String value, int wert) {
        appendCoberturaXMetric("healthyTarget", value, wert);
    }

    public String getCoberturaHealthyMetric(int i) {
        return getCoberturaXMetric("healthyTarget", i);
    }

    public int getCoberturaHealthyMetricValue(int i) {
        return getCoberturaXMetricValue("healthyTarget", i);
    }

    // UnHealthy

    public void setCoberturaUnHealthyMetric(int i, String string, int j) {
        setCoberturaXMetric("unhealthyTarget", i, string, j);
    }

    public int getCoberturaUnHealthyMetrics() {
        return getCoberturaXMetrics("unhealthyTarget");
    }

    public String getCoberturaUnHealthyMetric(int i) {
        return getCoberturaXMetric("unhealthyTarget", i);

    }

    public int getCoberturaUnHealthyMetricValue(int i) {
        return getCoberturaXMetricValue("unhealthyTarget", i);
    }

    // FAIL

    public int getCoberturaFailMetrics() {
        return getCoberturaXMetrics("failingTarget");

    }

    public String getCoberturaFailMetric(int i) {
        return getCoberturaXMetric("failingTarget", i);
    }

    public int getCoberturaFailMetricValue(int i) {
        return getCoberturaXMetricValue("failingTarget", i);
    }

    public void setCoberturaFailMetric(int i, String string, int j) {
        setCoberturaXMetric("failingTarget", i, string, j);
    }

    public String getWarningsThreasholdLimit() {
        Element publisher = root.getChild("publishers");
        Element warings = publisher.getChild("hudson.plugins.warnings.WarningsPublisher");
        return warings.getChild("thresholdLimit").getTextNormalize();
    }

    public void setWarningsThreasholdLimit(String warningsThreasholdLimit) {
        Element publisher = root.getChild("publishers");
        Element warings = publisher.getChild("hudson.plugins.warnings.WarningsPublisher");
        warings.getChild("thresholdLimit").setText(warningsThreasholdLimit);
    }

    public int getWarningsParsers() {
        Element publisher = root.getChild("publishers");
        Element warings = publisher.getChild("hudson.plugins.warnings.WarningsPublisher");
        Element parser = warings.getChild("parserNames");
        return parser.getChildren("string").size();
    }

    @SuppressWarnings("unchecked")
    public String getWarningsParser(int i) {
        Element publisher = root.getChild("publishers");
        Element warings = publisher.getChild("hudson.plugins.warnings.WarningsPublisher");
        Element parser = warings.getChild("parserNames");
        List<Element> names = parser.getChildren("string");
        return names.get(i).getTextNormalize();
    }

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

    public void appendWarningsParser(String value) {
        Element publisher = root.getChild("publishers");
        Element warings = publisher.getChild("hudson.plugins.warnings.WarningsPublisher");
        Element names = warings.getChild("parserNames");
        Element neu = new Element("string");
        neu.setText(value);
        names.addContent(neu);
    }

    public boolean isIgnoreUpstreamChanges() {
        if (root.getChild("ignoreUpstremChanges") == null) {
            throw new NoSuchElementException("NoSuchElemenet: ignoreUpstremChanges");
        }
        return bolVal(root.getChild("ignoreUpstremChanges"));
    }

    public void setIgnoredUpstreamChanges(boolean b) {
        root.getChild("ignoreUpstremChanges").setText(toBool(b));        
    }

    public boolean hasFindBugs() {
        Element reporters = root.getChild("reporters");
        if (reporters == null) {
            return false;
        }
        return reporters.getChild("hudson.plugins.findbugs.FindBugsReporter") != null;
    }

    public boolean hasPMD() {
        Element reporters = root.getChild("reporters");
        if (reporters == null) {
            return false;
        }
        return reporters.getChild("hudson.plugins.pmd.PmdReporter") != null;
    }

    public boolean hasCheckstyle() {
        Element reporters = root.getChild("reporters");
        if (reporters == null) {
            return false;
        }
        return reporters.getChild("hudson.plugins.checkstyle.CheckStyleReporter") != null;
    }

}
