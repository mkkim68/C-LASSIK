package com.ssafy.a604.crawling;

import com.ssafy.a604.config.CrawlingSetting;
import com.ssafy.a604.dao.ClassicDao;
import com.ssafy.a604.entity.Classic;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CrawlingClassic {

    private static int composerCardsCount;
    private static int composerTracksCount;
    private static int maxWaitSecond;
    private static String url;

    private WebDriver driver;
    private WebDriverWait wait;

    public CrawlingClassic() {
        WebDriverManager.chromedriver().setup();
        composerCardsCount = (int)CrawlingSetting.COMPOSER_CARDS_COUNT.getValue();
        composerTracksCount = (int)CrawlingSetting.COMPOSER_TRACKS_COUNT.getValue();
        maxWaitSecond = (int)CrawlingSetting.MAX_WAIT_SECOND.getValue();
        url = (String)CrawlingSetting.URL.getValue();
    }

    public void startCrawling() {
        initDriverAndWaitTime();
        saveData(parsing());
    }

    private void initDriverAndWaitTime() {
        this.driver = new ChromeDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(maxWaitSecond));
        driver.get(url);
    }

    private List<Classic> parsing() {
        List<Classic> classics = new ArrayList<>();
        for (int composerNumber = 0; composerNumber < composerCardsCount; composerNumber++) {
            try {
                classics.addAll(getClassicsDataPerComposer(composerNumber));
            } catch(TimeoutException ignore) {}
        }
        return classics;
    }

    private List<Classic> getClassicsDataPerComposer(int cardNumber) {
        List<Classic> classics = new ArrayList<>();
        List<WebElement> composerCards = driver.findElements(By.cssSelector("li[itemtype='http://schema.org/MusicGroup']"));
        try {
            clickComposerCard(composerCards, cardNumber);
            clickMoreInfoButton();
            classics.addAll(getClassicsData());
            returnComposerCardsPage();
        } catch (InterruptedException ignore) {}
        return classics;
    }

    private void clickComposerCard(List<WebElement> composerCards, int cardNumber) throws InterruptedException {
        WebElement composerCard = composerCards.get(cardNumber);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", composerCard);
        Thread.sleep(500);
        composerCard.click();
    }

    private void clickMoreInfoButton() throws TimeoutException {
        WebElement linkElement = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("p.more-link-fullwidth-right.more-link-fullwidth--no-border a")));
        linkElement.click();
    }

    private List<Classic> getClassicsData() throws InterruptedException {
        List<Classic> classics = new ArrayList<>();
        for (int trackNumber = 0; trackNumber < composerTracksCount; trackNumber++) {
            try {
                Classic classic = getClassicData(trackNumber);
                if(classic != null) classics.add(classic);
                driver.navigate().back();
            } catch (TimeoutException ignore) {}
        }
        return classics;
    }

    private Classic getClassicData(int trackNumber) throws InterruptedException {
        WebElement tbodyElement;
        try {
            tbodyElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("tbody[data-playlisting-add-entries]")));
        }catch (TimeoutException e) {
            driver.navigate().back();
            return null;
        }

        WebElement trElement;
        try {
            trElement = wait.until(ExpectedConditions.elementToBeClickable(tbodyElement.findElements(By.tagName("tr")).get(
                    trackNumber)));
        } catch (TimeoutException e) {
            System.out.println("Retry (track access failure)");
            Thread.sleep(1000);
            throw new TimeoutException();
        }

        return parseClassicCData(trElement, trackNumber);
    }

    private Classic parseClassicCData(WebElement trElement, int trackNumber) throws InterruptedException {
        try {
            WebElement chartlistNameElement = trElement.findElement(By.className("chartlist-name"));
            WebElement trackDetail = chartlistNameElement.findElement(By.tagName("a"));
            trackDetail.click();
            Thread.sleep(1500);

            String composerText = driver.findElement(By.cssSelector("a[itemprop='url'] span[itemprop='name']")).getText();
            String titleText = driver.findElement(By.cssSelector("h1[itemprop='name']")).getText();
            List<WebElement> tags = driver.findElements(By.className("tag"));
            String hrefText = driver.findElement(By.className("header-new-playlink")).getAttribute("href").split("v=")[1];

            Set<String> tagSet = new HashSet<>();
            for (WebElement tag : tags) {
                String tagText = tag.findElement(By.tagName("a")).getText();
                tagSet.add(tagText);
            }

            StringBuilder sb = new StringBuilder();
            for (String tag : tagSet) {
                sb.append(tag).append(",");
            }

            print(trackNumber + 1, composerText, titleText, sb, hrefText);
            return hrefText.isEmpty() ? null : new Classic(titleText, composerText, sb.toString(), hrefText);
        } catch (NoSuchElementException e) {
            return parseClassicCData(trElement, trackNumber);
        } catch (StaleElementReferenceException e) {
            return null;
        }
    }

    private void returnComposerCardsPage() throws InterruptedException {
        Thread.sleep(200);
        driver.navigate().back();
        Thread.sleep(200);
        driver.navigate().back();
        Thread.sleep(1000);
    }

    private void saveData(List<Classic> tracks) {
        ClassicDao classicDAO = new ClassicDao();
        for (Classic classic : tracks) {
            classicDAO.saveClassic(classic);
        }
    }

    private void print(int trackNumber, String composerText, String titleText, StringBuilder tags, String hrefText) {
        System.out.println(
            new StringBuilder()
                    .append(trackNumber)
                    .append("\n")
                    .append(composerText)
                    .append(" - ")
                    .append(titleText)
                    .append("\ntag - ")
                    .append(tags)
                    .append("\nlink - ")
                    .append(hrefText)
                    .append("\n")
                    .append("******************************************")
        );
    }

}
