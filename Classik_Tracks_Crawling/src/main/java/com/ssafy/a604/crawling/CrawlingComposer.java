package com.ssafy.a604.crawling;

import com.ssafy.a604.config.CrawlingSetting;
import com.ssafy.a604.dao.ClassicDao;
import com.ssafy.a604.entity.Composer;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CrawlingComposer {

    private static int composerCardsCount = 21;
    private static int maxWaitSecond = 5;
    private static String url;

    private WebDriver driver;
    private WebDriverWait wait;

    public CrawlingComposer() {
        WebDriverManager.chromedriver().setup();
        composerCardsCount = (int)CrawlingSetting.COMPOSER_CARDS_COUNT.getValue();
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

    private List<Composer> parsing() {
        List<Composer> composers = new ArrayList<>();
        for (int composerNumber = 0; composerNumber < composerCardsCount; composerNumber++) {
            try {
                composers.addAll(getComposersData(composerNumber));
            } catch(TimeoutException ignore) {}
        }
        return composers;
    }

    private List<Composer> getComposersData(int composerNumber) {
        List<Composer> composers = new ArrayList<>();
        List<WebElement> composerCards = driver.findElements(By.cssSelector("li[itemtype='http://schema.org/MusicGroup']"));
        try {
            clickComposerCard(composerCards, composerNumber);
            clickReadMoreButton();
            Thread.sleep(500);
            composers.add(getComposerData());
            returnComposerCardsPage();
        } catch (InterruptedException ignore) {}
        return composers;
    }

    private void clickComposerCard(List<WebElement> composerCards, int cardNumber) throws InterruptedException {
        WebElement composerCard = composerCards.get(cardNumber);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", composerCard);
        Thread.sleep(500);
        composerCard.click();
    }

    private void clickReadMoreButton() throws TimeoutException, InterruptedException {
        String cssSelector = ".wiki-block-inner-2.wiki-truncate-6-lines a";
        List<WebElement> linkElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(cssSelector)));
        WebElement readMoreLink = null;

        for (WebElement linkElement : linkElements) {
            if (linkElement.getText().equalsIgnoreCase("read more")) {
                readMoreLink = linkElement;
            }
        }

        if (readMoreLink != null) {
            wait.until(ExpectedConditions.elementToBeClickable(readMoreLink)).click();
        }
    }

    private Composer getComposerData() {
        String cssSelector = "h1.header-new-title";
        WebElement composerNameElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector)));
        String nameText = composerNameElement.getText();

        String cssWikiSelector = "div.wiki-content p";
        List<WebElement> paragraphElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(cssWikiSelector)));
        StringBuilder sb = new StringBuilder();
        for (WebElement paragraph : paragraphElements) {
            String paragraphText = paragraph.getText();
            sb.append(paragraphText).append("\n");
        }

        return new Composer(nameText, sb.toString());
    }

    private void returnComposerCardsPage() throws InterruptedException {
        Thread.sleep(200);
        driver.navigate().back();
        Thread.sleep(200);
        driver.navigate().back();
        Thread.sleep(1000);
    }

    private void saveData(List<Composer> composers) {
        ClassicDao classicDAO = new ClassicDao();
        for (Composer composer : composers) {
            classicDAO.saveComposer(composer);
        }
    }

}
