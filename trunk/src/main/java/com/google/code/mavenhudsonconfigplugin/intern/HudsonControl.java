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

/**
 * The Class HudsonControl.
 * 
 * @author Jens Ritter
 */
public class HudsonControl {
    
    
    /** The logger. */
    private Log logger = LogFactory.getLog(HudsonControl.class);
    
    /** The url. */
    private String url = "";

    /** The sax. */
    private SAXBuilder sax;
    
    /** The client. */
    HttpClient client;
    
    /**
     * Instantiates a new hudson control.
     * 
     * @param value the value
     */
    public HudsonControl(String value) {
        url = value;
        sax = new SAXBuilder();
        client = new HttpClient();
    }

    /**
     * Gets the command.
     * 
     * @param value the value
     * 
     * @return the command
     * 
     * @throws HttpException the http exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws JDOMException the JDOM exception
     */
    private Element getCommand(String value) throws HttpException, IOException, JDOMException {
        return getCommand(value,true);
    }

    /**
     * Gets the command.
     * 
     * @param value the value
     * @param parsePath the parse path
     * 
     * @return the command
     * 
     * @throws HttpException the http exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws JDOMException the JDOM exception
     */
    private Element getCommand(String value,boolean parsePath) throws HttpException, IOException, JDOMException {
        return getHttpPostParsed(value,parsePath).getRootElement();
    }
    
    /**
     * Gets the http post parsed.
     * 
     * @param value the value
     * @param parsePath the parse path
     * 
     * @return the http post parsed
     * 
     * @throws HttpException the http exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws JDOMException the JDOM exception
     */
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

    /**
     * Post http.
     * 
     * @param value the value
     * @param param the param
     * @param content the content
     * 
     * @throws HttpException the http exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
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
    
    /**
     * Gets the http get plain.
     * 
     * @param value the value
     * 
     * @return the http get plain
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
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
    
    /**
     * Gets the http get.
     * 
     * @param value the value
     * 
     * @return the http get
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws JDOMException the JDOM exception
     */
    private Document getHttpGet(String value) throws IOException, JDOMException {
        GetMethod method = new GetMethod(url + "/"  + value);
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                logger.fatal("Method failed: " + method.getStatusLine());
                throw new HttpException(method.getStatusLine().toString());
              }
         // Read the response body.
            InputStream in = method.getResponseBodyAsStream();
            Document document = sax.build(in);
            
            in.close();
            return document;
        } finally {
            method.releaseConnection();
        }
    }
    
    /**
     * Post http plain.
     * 
     * @param value the value
     * 
     * @return the string
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private String postHttpPlain(String value) throws IOException {
        PostMethod method = new PostMethod(url + "/"  + value);
        try {
            // Execute the method.
            int statusCode = client.executeMethod(method);
            if (statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
                logger.debug(method.getStatusLine());
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
     * Xml2 hudson job.
     * 
     * @param jobXml the job xml
     * 
     * @return the hudson job
     */
    private HudsonJob xml2HudsonJob(Element jobXml) {
        HudsonJob job = new HudsonJob();
        job.setName(jobXml.getChildTextNormalize("name"));
        job.setUrl(jobXml.getChildTextNormalize("url"));
        job.setColor(jobXml.getChildTextNormalize("color"));
        return job;
    }



    /**
     * Ping.
     * 
     * @return true, if successful
     * 
     * @throws HttpException the http exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws JDOMException the JDOM exception
     */
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



    /**
     * Gets the jobs.
     * 
     * @return the jobs
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws JDOMException the JDOM exception
     */
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



    /**
     * Gets the job.
     * 
     * @param projectname the projectname
     * 
     * @return the job
     * 
     * @throws HttpException the http exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws JDOMException the JDOM exception
     */
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

    /**
     * Gets the config as xml.
     * 
     * @param projectname the projectname
     * 
     * @return the config as xml
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public String getConfigAsXml(String projectname) throws IOException {
        return getHttpGetPlain("job/" + projectname + "/config.xml");
    }
    
    /**
     * Creates the job.
     * 
     * @param name the name
     * @param config the config
     * 
     * @throws HttpException the http exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void createJob(String name, String config) throws HttpException, IOException {
        // http://www.jens.org:8080/hudson/createItem
        // queryPara = name=JOBNAME
        Map<String,String> para = new HashMap<String, String>();
        para.put("name", name);
        postHttp("createItem", para, config);
    }

    /**
     * Delete job.
     * 
     * @param projectname the projectname
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void deleteJob(String projectname) throws IOException {
        //http://www.jens.org:8080/hudson/job/JUNITTEST/doDelete
        postHttpPlain("job/" + projectname + "/doDelete");
    }



    /**
     * Gets the jobs as string.
     * 
     * @return the jobs as string
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws JDOMException the JDOM exception
     */
    public List<String> getjobsAsString() throws IOException, JDOMException {

        ArrayList<String> result = new ArrayList<String>();
        for(HudsonJob it : getJobs()) {
            result.add(it.getName());
        }
        return result;
    }



    /**
     * Run job.
     * 
     * @param projectname the projectname
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void runJob(String projectname) throws IOException {
        // http://www.jens.org:8080/hudson/job/MasterPom-1.0/build
        postHttpPlain("/job/" + projectname + "/build");
    }
    
    /**
     * Disable job.
     * 
     * @param projectname the projectname
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void disableJob(String projectname) throws IOException {
        // http://www.jens.org:8080/hudson/job/MasterPom-1.0/disable
        postHttpPlain("job/" + projectname + "/disable");
    }
    
    /**
     * Enable job.
     * 
     * @param projectname the projectname
     * 
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void enableJob(String projectname) throws IOException {
        // http://www.jens.org:8080/hudson/job/MasterPom-1.0/enable
        postHttpPlain("job/" + projectname + "/enable");
    }

    /**
     * Gets the config.
     * 
     * @param projectname the projectname
     * 
     * @return the config
     * 
     * @throws HttpException the http exception
     * @throws IOException Signals that an I/O exception has occurred.
     * @throws JDOMException the JDOM exception
     */
    public HudsonConfig getConfig(String projectname) throws HttpException, IOException, JDOMException {
        Document cfg = getHttpGet("job/" + projectname + "/config.xml");
        HudsonConfig config = HudsonConfig.parseDocument(projectname,cfg);
        return config;
    }

    /**
     * Save config.
     * 
     * @param name the name
     * @param xml the xml
     * 
     * @throws HttpException the http exception
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public void saveConfig(String name, String xml) throws HttpException, IOException {
        // http://www.jens.org:8080/hudson/job/Bugs/config.xml
        postHttp("job/" + name + "/config.xml", null, xml);
        
    }

}
