package com.google.code.mavenhudsonconfigplugin.intern;

/**
 * The Class HudsonJob.
 * 
 * @author Jens Ritter
 */
public class HudsonJob {

    /** The name. */
    String name;
    
    /** The url. */
    String url;
    
    /** The color. */
    String color;
    
    /** The enabled. */
    boolean enabled;
    
    /**
     * Gets the name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name.
     * 
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the url.
     * 
     * @return the url
     */
    public String getUrl() {
        return url;
    }
    
    /**
     * Sets the url.
     * 
     * @param url the new url
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * Gets the color.
     * 
     * @return the color
     */
    public String getColor() {
        return color;
    }
    
    /**
     * Sets the color.
     * 
     * @param color the new color
     */
    public void setColor(String color) {
        this.color = color;
    }
    
    /**
     * Checks if is enabled.
     * 
     * @return true, if is enabled
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    /**
     * Sets the enabled.
     * 
     * @param enabled the new enabled
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((color == null) ? 0 : color.hashCode());
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        HudsonJob other = (HudsonJob) obj;
        if (color == null) {
            if (other.color != null)
                return false;
        } else if (!color.equals(other.color))
            return false;
        if (enabled != other.enabled)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }
    
}
