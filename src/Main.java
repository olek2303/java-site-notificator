import org.apache.commons.codec.digest.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.*;
import java.net.*;
import java.time.Duration;
import java.util.*;

public class Main {
    static Map<String, String> checkSumDB = new HashMap<String, String>();

    public static void main(String []args) throws IOException, InterruptedException {
        while (true) {
            String url = "https://www.otomoto.pl/osobowe/seg-cabrio--seg-city-car--seg-compact--seg-coupe--seg-sedan/od-2012?search%5Bfilter_float_mileage%3Ato%5D=150000&search%5Bfilter_float_engine_power%3Afrom%5D=100&search%5Bfilter_float_price%3Ato%5D=40000&search%5Bfilter_enum_damaged%5D=0&search%5Border%5D=created_at_first%3Adesc&search%5Badvanced_search_expanded%5D=true";
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
                String text = firstElementFind(url);
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
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(10000));
            WebElement textBoxe = driver.findElement(By.className("ooa-7wotau")); // name of the action box on otomoto
            textBoxe = driver.findElement(By.className("ooa-9tzypk"));
            textBoxe = driver.findElement(By.className("e1p19lg720"));
            text = textBoxe.getText();
            System.out.println("To jest tekst poszukiwanego elementu : " + text);
        } finally {
            driver.quit();
        }
        return text;
    }
}