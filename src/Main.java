import org.apache.commons.codec.digest.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.net.*;
import java.util.*;

public class Main {
    static Map<String, String> checkSumDB = new HashMap<String, String>();
    static Map<String, String> lastAuto = new HashMap<String, String>();
    public static void main(String []args) throws IOException, InterruptedException {
        while (true) {
            String url = "https://wiadomosci.onet.pl/";
            String url2 = "https://www.olx.pl/motoryzacja/samochody/?search%5Bfilter_enum_car_body%5D%5B0%5D=cabriolet&search%5Bfilter_enum_car_body%5D%5B1%5D=sedan&search%5Bfilter_enum_car_body%5D%5B2%5D=coupe&search%5Bfilter_enum_car_body%5D%5B3%5D=hatchback&search%5Bfilter_enum_condition%5D%5B0%5D=notdamaged&search%5Bfilter_float_enginepower%3Afrom%5D=100&search%5Bfilter_float_milage%3Ato%5D=150000&search%5Bfilter_float_price%3Ato%5D=40000&search%5Bfilter_float_year%3Afrom%5D=2010&search%5Border%5D=created_at%3Adesc";
            // query last checksum from map
            String lastChecksum = checkSumDB.get(url2);
            // get current checksum using static utility method
            String currentChecksum = getChecksumForURL(url2);
            if (currentChecksum.equals(lastChecksum)) {
                System.out.println("it haven't been updated");
            } else {
                // persist this checksum to map
                checkSumDB.put(url, currentChecksum);
                System.out.println("something in the content have changed...");
                //String text = firstElementFind(url);
                if()
                thirdFromOlx(url2);
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

    public static String firstElementFind(String spec) {
        WebDriver driver = new ChromeDriver();
        String text = "";
        try {
            driver.get(spec);
            //driver.manage().timeouts().implicitlyWait(Duration.ofMillis(10000));
            WebElement textBoxe = driver.findElement(By.className("mediumNewsBox")); // name of the action box on otomoto
            text = textBoxe.getText();
            System.out.println("To jest tekst poszukiwanego elementu : " + text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            driver.quit();
        }
        return text;
    }

    public static String thirdFromOlx(String spec) {
        String text = "";
        WebDriver driver = new ChromeDriver();
        try {
            driver.get(spec);
            List<WebElement> auto = driver.findElements(By.className("css-u2ayx9"));
            text = auto.get(3).getText();
            System.out.println(text);
        } finally {
            driver.quit();
        }
        return text;
    }
}