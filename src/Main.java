import org.apache.commons.codec.digest.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;


import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    static Map<String, String> checkSumDB = new HashMap<String, String>();
    public static void main(String []args) throws IOException, InterruptedException {
        while (true) {
            String url = "https://wiadomosci.onet.pl/";
            // query last checksum from map
            String lastChecksum = checkSumDB.get(url);
            // get current checksum using static utility method
            String currentChecksum = getChecksumForURL(url);
            if (currentChecksum.equals(lastChecksum)) {
                System.out.println("it haven't been updated");
            } else {
                // persist this checksum to map
                checkSumDB.put(url, currentChecksum);
                System.out.println("something in the content have changed...");
                // send email you can check: http://www.javacommerce.com/displaypage.jsp?name=javamail.sql&id=18274
            }
            Thread.sleep(10000);
        }
    }
    private static String getChecksumForURL(String spec) throws IOException {
        URL u = new URL(spec);
        HttpURLConnection huc = (HttpURLConnection) u.openConnection();
        huc.setRequestMethod("GET");
        huc.setDoOutput(true);
        huc.connect();
        return DigestUtils.sha256Hex(huc.getInputStream());
    }

    private static String firstElementFind() {
        WebDriver driver = new ChromeDriver();
        driver.get("https://wiadomosci.onet.pl/");

    }

}