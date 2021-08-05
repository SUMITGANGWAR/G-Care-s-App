package com.sumit.myidea;

public class tools {
    String tool_name;
    String uri;


    public tools(String tools_name, String uri) {
        this.tool_name=tools_name;
        this.uri=uri;
    }

    public String getTools_name() {
        return tool_name;
    }

    public void setTools_name(String tools) {
        this.tool_name = tool_name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
