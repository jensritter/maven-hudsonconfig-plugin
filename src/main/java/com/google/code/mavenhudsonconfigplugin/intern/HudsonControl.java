package com.google.code.mavenhudsonconfigplugin.intern;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class HudsonControl {
    
    
    private Log logger = LogFactory.getLog(HudsonControl.class);
    
    private String url = "";
    private String username = null;
    private String password = null;
    private boolean needLogin = false;

    private SAXBuilder sax;
    
    HttpClient client;
    public HudsonControl(String value) {
        url = value;
        needLogin=false;
        sax = new SAXBuilder();
        client = new HttpClient();
    }

    private Element getCommand(String value) throws HttpException, IOException, JDOMException {
        return getCommand(value,true);
    }

    private Element getCommand(String value,boolean parsePath) throws HttpException, IOException, JDOMException {
        return getHttpPostParsed(value,parsePath).getRootElement();
    }
    
    private Document getHttpPostParsed(String value,boolean parsePath) throws HttpException, IOException, JDOMException {
        String conurl = null;
        if (parsePath) {
            conurl = url + "/"  + value;
        } else {
            conurl = value;
        }
        PostMethod method = new PostMethod(conurl);
        Document document = null;
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                logger.fatal("Method failed: " + method.getStatusLine());
                throw new HttpException(method.getStatusLine().toString());
              }
            // Read the response body.
            InputStream in = method.getResponseBodyAsStream();
            document = sax.build(in);
            
            in.close();
            return document;
        } finally {
            method.releaseConnection();
        }
    }

    private void postHttp(String value, Map<String,String> param, String content) throws HttpException, IOException {
        String query = "";
        if (param != null) {
            query = "?";
            for(Map.Entry<String, String> ent : param.entrySet()) {
                query += ent.getKey() + "=" + ent.getValue() + "&";
            }
            query = query.substring(0,query.length()-1);
        }
        PostMethod method = new PostMethod(url + "/"  + value + query);
        if (content != null) {
            StringRequestEntity att = new StringRequestEntity(content,"text/xml",null);
            method.setRequestEntity(att);
        }
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                logger.fatal("Method failed: " + method.getStatusLine());
                throw new HttpException(method.getStatusLine().toString());
              }
           
        } finally {
            method.releaseConnection();
        }
    }
    private String getHttpGetPlain(String value) throws IOException {
        GetMethod method = new GetMethod(url + "/"  + value);
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                logger.fatal("Method failed: " + method.getStatusLine());
                throw new HttpException(method.getStatusLine().toString());
              }
            // Read the response body.
            String result = method.getResponseBodyAsString(Short.MAX_VALUE);
            return result;
        } finally {
            method.releaseConnection();
        }
    }
    
    private String postHttpPlain(String value) throws IOException {
        PostMethod method = new PostMethod(url + "/"  + value);
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);
            if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                // all
            } else {
                if (statusCode != HttpStatus.SC_OK) {
                    logger.fatal("Method failed: " + method.getStatusLine());
                    throw new HttpException(method.getStatusLine().toString());
                  }
            }
            // Read the response body.
            String result = method.getResponseBodyAsString(Short.MAX_VALUE);
            return result;
        } finally {
            method.releaseConnection();
        }
    }

    
    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }



    private HudsonJob xml2HudsonJob(Element jobXml) {
        HudsonJob job = new HudsonJob();
        job.setName(jobXml.getChildTextNormalize("name"));
        job.setUrl(jobXml.getChildTextNormalize("url"));
        job.setColor(jobXml.getChildTextNormalize("color"));
        return job;
    }



    public boolean ping() throws HttpException, IOException, JDOMException {
        Element result = getCommand("api/xml");
        /*Format.getPrettyFormat();
        XMLOutputter out = new XMLOutputter(Format.getPrettyFormat());
        out.output(result,System.out);*/
        String useSec = result.getChildTextNormalize("useSecurity");
        if (useSec.equals("false")) {
            return true;
        }
        return false; // because i must implement it;
    }



    @SuppressWarnings("unchecked")
    public List<HudsonJob> getJobs() throws IOException, JDOMException {
        Element response = getCommand("api/xml");
        ArrayList<HudsonJob> result = new ArrayList<HudsonJob>();
        for(Element jobXml : (List<Element>)response.getChildren("job")) {
            HudsonJob job = xml2HudsonJob(jobXml);
            Element resp = getCommand(job.getUrl()+"/api/xml",false);
            String is = resp.getChildText("buildable");
            if (is.equals("true")) {
                job.setEnabled(true);
            } else {
                job.setEnabled(false);
            }
            result.add(job);
        }
        return result;
    }



    @SuppressWarnings("unchecked")
    public HudsonJob getjob(String projectname) throws HttpException, IOException, JDOMException {
        Element response = getCommand("api/xml");
        for(Element jobXml : (List<Element>)response.getChildren("job")) {
            if (jobXml.getChildTextNormalize("name").equals(projectname)) {
                HudsonJob job = xml2HudsonJob(jobXml);
                Element resp = getCommand(job.getUrl()+"/api/xml",false);
                String is = resp.getChildText("buildable");
                if (is.equals("true")) {
                    job.setEnabled(true);
                } else {
                    job.setEnabled(false);
                }
                return job;
            }
        }
        return null;
    }

    public String getConfig(String projectname) throws IOException {
        return getHttpGetPlain("job/" + projectname + "/config.xml");
    }
    
    public void createJob(String name, String config) throws HttpException, IOException {
        // http://www.jens.org:8080/hudson/createItem
        // queryPara = name=JOBNAME
        Map<String,String> para = new HashMap<String, String>();
        para.put("name", name);
        postHttp("createItem", para, config);
    }

    public void deleteJob(String projectname) throws IOException {
        //http://www.jens.org:8080/hudson/job/JUNITTEST/doDelete
        postHttpPlain("job/" + projectname + "/doDelete");
    }



    public List<String> getjobsAsString() throws IOException, JDOMException {

        ArrayList<String> result = new ArrayList<String>();
        for(HudsonJob it : getJobs()) {
            result.add(it.getName());
        }
        return result;
    }



    public void runJob(String projectname) throws IOException {
        // http://www.jens.org:8080/hudson/job/MasterPom-1.0/build
        postHttpPlain("/job/" + projectname + "/build");
    }
    
    public void disableJob(String projectname) throws IOException {
        // http://www.jens.org:8080/hudson/job/MasterPom-1.0/disable
        postHttpPlain("/job/" + projectname + "/disable");
    }
    
    public void enableJob(String projectname) throws IOException {
        // http://www.jens.org:8080/hudson/job/MasterPom-1.0/enable
        postHttpPlain("/job/" + projectname + "/enable");
    }

}
