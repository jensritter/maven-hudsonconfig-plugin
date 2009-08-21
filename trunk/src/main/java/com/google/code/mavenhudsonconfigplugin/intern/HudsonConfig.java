package com.google.code.mavenhudsonconfigplugin.intern;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public final class HudsonConfig {

    private Document doc = null;
    private Element root = null;
    private String jobname = null;

    public HudsonConfig() {
        root = new Element("EMPTY");
        doc = new Document(root);
        jobname = "";
    }

    public static HudsonConfig parseDocument(String jobName, Document doc) {
        HudsonConfig cfg = new HudsonConfig();
        cfg.doc = doc;
        cfg.root = doc.getRootElement();
        cfg.jobname = jobName;
        return cfg;
    }

    public String getXml() {
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        return out.outputString(doc);
    }
    
    public String getJobName() {
        return jobname;
    }
    
    public void setJobName(String value) {
        jobname = value;
    }
    // ---------

    private Integer intVal(Element value) {
        String val = value.getTextTrim();
        if (val == null || val.equals("")) {
            return null;
        }
        return Integer.valueOf(val);
    }

    private Boolean bolVal(Element it) {
        String txt = it.getTextTrim();
        if (txt.equals("")) {
            return null;
        }
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
        if (root.getChild("description") == null) {
            return "";
        }
        return root.getChild("description").getTextNormalize();

    }

    public void setDescription(String description) {
        Element it = getChild(root,"description");
        it.setText(description);
    }

    private Element getChild(Element element, String name) {
        Element node = element.getChild(name);
        if (node == null) {
            node = new Element(name);
            element.addContent(node);
        }
        return node; 
    }

    public Integer getLogRotatorNumToKeep() {
        Element logRotator = getChild(root,"logRotator");
        Element child = getChild(logRotator,"numToKeep");
        return intVal(child);

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
        Element svnRoot = getChild(root,"scm");
        Element svnLocations = getChild(svnRoot,"locations");
        return svnLocations.getChildren("hudson.scm.SubversionSCM_-ModuleLocation").size();
    }

    public String getSvnRemote(int index) {
        Element svnRoot = getChild(root,"scm");
        Element svnLocations = getChild(svnRoot,"locations");
        Element parent = getChildren(svnLocations,"hudson.scm.SubversionSCM_-ModuleLocation",index);
        return getChild(parent,"remote").getTextNormalize();
    }

    public void setSvnRemote(int index, String svnRemote) {
        Element svnRoot = getChild(root,"scm");
        Element svnLocations = getChild(svnRoot,"locations");
        Element parent = getChildren(svnLocations,"hudson.scm.SubversionSCM_-ModuleLocation",index);
        getChild(parent,"remote").setText(svnRemote);
    }
    
    @SuppressWarnings("unchecked")
    private Element getChildren(Element root, String name, int index) {
        List<Element> list = (List<Element>) root.getChildren(name);
        Element parent = null;
        if (list == null) {
            parent = getChild(root,name);
        } else {
            if (index == list.size()) {
                parent = new Element(name);
                root.addContent(parent);
            } else {
                parent = list.get(index);
            }
        }
        return parent;
    }

    public String getSvnLocal(int index) {
        Element svnRoot = getChild(root,"scm");
        Element svnLocations = getChild(svnRoot,"locations");
        Element parent = getChildren(svnLocations,"hudson.scm.SubversionSCM_-ModuleLocation",index);
        return getChild(parent,"local").getTextNormalize();

    }

    public void setSvnLocal(int index, String svnLocal) {
        Element svnRoot = getChild(root,"scm");
        Element svnLocations = getChild(svnRoot,"locations");
        Element parent = getChildren(svnLocations,"hudson.scm.SubversionSCM_-ModuleLocation",index);
        getChild(parent,"local").setText(svnLocal);
    }

    public Boolean isDisabled() {
        return bolVal(getChild(root,"disabled"));

    }

    public void setDisabled(boolean disabled) {
        getChild(root,"disabled").setText(toBool(disabled));
    }

    public Boolean isBlockBuildWhenUpstreamBuilding() {
        return bolVal(getChild(root,"blockBuildWhenUpstreamBuilding"));

    }

    public void setBlockBuildWhenUpstreamBuilding(boolean blockBuildWhenUpstreamBuilding) {
        getChild(root,"blockBuildWhenUpstreamBuilding").setText(toBool(blockBuildWhenUpstreamBuilding));
    }

    public String getGoals() {
        return getChild(root,"goals").getTextNormalize();
    }

    public void setGoals(String goals) {
        getChild(root,"goals").setText(goals);
    }

    public String getMavenOpts() {
        return getChild(root,"mavenOpts").getTextNormalize();

    }

    public void setMavenOpts(String mavenOpts) {
        getChild(root,"mavenOpts").setText(mavenOpts);
    }

    public String getFindBugsThresholdLimit() {
        Element reporters = getChild(root,"reporters");
        Element parent = getChild(reporters,"hudson.plugins.findbugs.FindBugsReporter");
        return getChild(parent,"thresholdLimit").getTextNormalize();
    }

    public void setFindBugsThresholdLimit(String findBugsThresholdLimit) {
        Element reporters = getChild(root,"reporters");
        Element parent = getChild(reporters,"hudson.plugins.findbugs.FindBugsReporter");
        getChild(parent,"thresholdLimit").setText(findBugsThresholdLimit);
    }

    public String getPmdThresholdLimit() {
        Element reporters = getChild(root,"reporters");
        Element parent = getChild(reporters,"hudson.plugins.pmd.PmdReporter");
        return getChild(parent,"thresholdLimit").getTextNormalize();
    }

    public void setPmdThresholdLimit(String pmdThresholdLimit) {
        Element reporters = getChild(root,"reporters");
        Element parent = getChild(reporters,"hudson.plugins.pmd.PmdReporter");
        getChild(parent,"thresholdLimit").setText(pmdThresholdLimit);
    }

    public String getCheckstyleThresholdLimit() {
        Element reporters = getChild(root,"reporters");
        Element parent = getChild(reporters,"hudson.plugins.checkstyle.CheckStyleReporter");
        return getChild(parent,"thresholdLimit").getTextNormalize();
    }

    public void setCheckstyleThresholdLimit(String checkstyleThresholdLimit) {
        Element reporters = getChild(root,"reporters");
        Element parent = getChild(reporters,"hudson.plugins.checkstyle.CheckStyleReporter");
        getChild(parent,"thresholdLimit").setText(checkstyleThresholdLimit);
    }

    public String getCoberturaReportFile() {
        Element publishers = getChild(root,"publishers");
        Element coberturaRoot = getChild(publishers,"hudson.plugins.cobertura.CoberturaPublisher");
        return getChild(coberturaRoot,"coberturaReportFile").getTextNormalize();

    }

    public void setCoberturaReportFile(String coberturaReportFile) {
        Element publishers = getChild(root,"publishers");
        Element coberturaRoot = getChild(publishers,"hudson.plugins.cobertura.CoberturaPublisher");
        getChild(coberturaRoot,"coberturaReportFile").setText(coberturaReportFile);
    }

    public Boolean isCoberturaOnlyStable() {
        Element publishers = getChild(root,"publishers");
        Element coberturaRoot = getChild(publishers,"hudson.plugins.cobertura.CoberturaPublisher");
        return bolVal(getChild(coberturaRoot,"onlyStable"));

    }

    public void setCoberturaOnlyStable(boolean coberturaOnlyStable) {
        Element publishers = getChild(root,"publishers");
        Element coberturaRoot = getChild(publishers,"hudson.plugins.cobertura.CoberturaPublisher");
        getChild(coberturaRoot,"onlyStable").setText(toBool(coberturaOnlyStable));
    }


    @SuppressWarnings("unchecked")
    private int getCoberturaXMetrics(String type) {
        Element publishers = getChild(root,"publishers");
        Element coberturaRoot = getChild(publishers,"hudson.plugins.cobertura.CoberturaPublisher");
        Element parent = getChild(coberturaRoot,type);
        Element targets = getChild(parent,"targets");
        List<Element> result = targets.getChildren("entry");
        if (result == null) {
            return 0;
        }
        return result.size();
    }

    private void setCoberturaXMetric(String type, int i, String value, int j) {
        Element publishers = getChild(root,"publishers");
        Element coberturaRoot = getChild(publishers,"hudson.plugins.cobertura.CoberturaPublisher");
        Element elementType = getChild(coberturaRoot,type);
        Element sum = getChild(elementType,"targets");
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
        Element publisher = getChild(root,"publishers");
        Element warings = getChild(publisher,"hudson.plugins.warnings.WarningsPublisher");
        return getChild(warings,"thresholdLimit").getTextNormalize();
    }

    public void setWarningsThreasholdLimit(String warningsThreasholdLimit) {
        Element publisher = getChild(root,"publishers");
        Element warings = getChild(publisher,"hudson.plugins.warnings.WarningsPublisher");
        getChild(warings,"thresholdLimit").setText(warningsThreasholdLimit);
    }

    @SuppressWarnings("unchecked")
    public int getWarningsParsers() {
        Element publisher = getChild(root,"publishers");
        Element warings = getChild(publisher,"hudson.plugins.warnings.WarningsPublisher");
        Element parser = getChild(warings,"parserNames");
        List<Element> result = parser.getChildren("string");
        if (result == null) {
            return 0;
        }
        return result.size();
    }

    @SuppressWarnings("unchecked")
    public String getWarningsParser(int i) {
        Element publisher = getChild(root,"publishers");
        Element warings = getChild(publisher,"hudson.plugins.warnings.WarningsPublisher");
        Element parser = getChild(warings,"parserNames");
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

    public Boolean isIgnoreUpstreamChanges() {
        return bolVal(getChild(root,"ignoreUpstremChanges"));
    }

    public void setIgnoredUpstreamChanges(boolean b) {
        getChild(root,"ignoreUpstremChanges").setText(toBool(b));        
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
