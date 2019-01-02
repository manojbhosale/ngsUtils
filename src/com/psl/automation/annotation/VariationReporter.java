package com.psl.automation.annotation;

import java.io.File;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

public class VariationReporter {
	
	private String postUrl = "https://www.ncbi.nlm.nih.gov/projects/SNP/VariantAnalyzer/var_rep.cgi"; //post url

    public String analyze(int organism, String src_assembly, String annot1, File annot) throws Exception {
        String result = null;
        HttpClient httpClient = new DefaultHttpClient(); //user agent
        //httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000); //user agent timeout must be at least 30 sec.
        
        HttpPost httpPost = new HttpPost(postUrl);
        MultipartEntity multiPartEntity = new MultipartEntity();

        //Setup form parameters
        multiPartEntity.addPart("source-assembly", new StringBody(src_assembly)); //reference assembly 
        multiPartEntity.addPart("annot1", new StringBody(annot1)); //query input as a text 
        multiPartEntity.addPart("api_protocol_version", new StringBody("1.0")); //API version
        //query as an input file
        FileBody fileBody = new FileBody(annot, "application/text");
        multiPartEntity.addPart("annot", fileBody);
        httpPost.setEntity(multiPartEntity);
        ResponseHandler<String> responseHandler = new BasicResponseHandler(); 

        //httpPost.
//       / httpClient
        
        try {
            result = httpClient.execute(httpPost, responseHandler);
            String jobIdLine = result.substring(0, result.indexOf("\n") + 1); //first line contains job id 
            result = jobIdLine + result.substring(result.indexOf("##")); //actual query result starts with lines beginning with '##' 
        }
        finally {
            httpClient.getConnectionManager().shutdown();
        }
        return result;
    }
    
}
