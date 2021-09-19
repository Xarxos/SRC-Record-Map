import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.*;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SRCClient {

    private String apiURL;

    public SRCClient(String apiURL) {
        this.apiURL = apiURL;
    }

    public static void printStream(HttpsURLConnection con) throws IOException {
        BufferedReader in;
        try {
            con.getInputStream();
        } catch (IOException e) {
                in = new BufferedReader(
                    //new InputStreamReader(con.getInputStream()));
                    new InputStreamReader(con.getErrorStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println(inputLine);
                }
                in.close();
        }


    }

    public static void printStream2(HttpsURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }
        in.close();
    }

    public static boolean pagination(HttpsURLConnection con) throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine = in.readLine();
        in.close();
        return inputLine.contains("Europa Universalis IV");
    }

    public void readGames() {
        URL url = null;
        HttpsURLConnection con = null;
        int offset = -1000;
        String pagination = null;
        boolean nextPage = true;

        try {

           do {
               offset += 1000;
               pagination = "&offset=" + offset;
               System.out.println(pagination);
                url = new URL(apiURL.concat(pagination));
                con = (HttpsURLConnection) url.openConnection();
                con.setRequestMethod("GET");
               nextPage = !pagination(con);

                Map<String, String> params = new HashMap<>();
                //params.put("pagination", "false");
                //con.setDoOutput(true);
                //DataOutputStream out = new DataOutputStream(con.getOutputStream());
                //out.writeBytes(makeParamString(params));
                //out.flush();
                //out.close();


                //printStream2(con);
                con.disconnect();

            } while (nextPage);
            System.out.println("Yeah.");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void print_https_cert(HttpsURLConnection con){

        if(con!=null){

            try {

                System.out.println("Response Code : " + con.getResponseCode());
                System.out.println("Cipher Suite : " + con.getCipherSuite());
                System.out.println("\n");

                Certificate[] certs = con.getServerCertificates();
                for(Certificate cert : certs){
                    System.out.println("Cert Type : " + cert.getType());
                    System.out.println("Cert Hash Code : " + cert.hashCode());
                    System.out.println("Cert Public Key Algorithm : "
                            + cert.getPublicKey().getAlgorithm());
                    System.out.println("Cert Public Key Format : "
                            + cert.getPublicKey().getFormat());
                    System.out.println("\n");
                }

            } catch (SSLPeerUnverifiedException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }

        }

    }
/*
    private Channel makeChannel(Element currentChannel) {
        String channelName = currentChannel.getAttribute("name");
        String channelID = currentChannel.getAttribute("id");
        String scheduleURL = "http://api.sr.se/v2/scheduledepisodes?channelid=" + channelID;

        return new Channel(channelName, scheduleURL, controller);
    }

 */

    private String makeParamString(Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder requestData = new StringBuilder();

        for(Map.Entry<String, String> param : params.entrySet()) {
            if(requestData.length() != 0) {
                requestData.append('&');
            }

            requestData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            requestData.append('=');
            requestData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        return requestData.toString();
    }
}
