import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class TagesQAAutomation {
    public static void main(String[] args) {
        // Set up the WebDriver and navigate to the main page
        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver"); // Set the path to your ChromeDriver
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        
        String url = "https://tages.ru/";
        driver.get(url);

        try {
            checkLinksClickability(driver);
            checkEmailTelLinks(driver);
            validateFeedbackForm(driver, wait);
            sendFeedbackForm(driver, wait);
        } finally {
            driver.quit();
        }
    }

    // 1. Check the clickability of all links and sections
    public static void checkLinksClickability(WebDriver driver) {
        List<WebElement> links = driver.findElements(By.tagName("a"));
        
        for (WebElement link : links) {
            String href = link.getAttribute("href");
            try {
                link.click();
                System.out.println("Clickable: " + href);
                driver.navigate().back();  // Go back after clicking
            } catch (Exception e) {
                System.out.println("Not clickable: " + href + " - Error: " + e.getMessage());
                driver.navigate().to("https://tages.ru/"); // Reload main page if navigation was interrupted
            }
        }
    }

    // 2. Check for mailto and tel links
    public static void checkEmailTelLinks(WebDriver driver) {
        List<WebElement> emailLinks = driver.findElements(By.xpath("//a[starts-with(@href, 'mailto:')]"));
        List<WebElement> telLinks = driver.findElements(By.xpath("//a[starts-with(@href, 'tel:')]"));
        
        for (WebElement email : emailLinks) {
            System.out.println("Email link found: " + email.getAttribute("href"));
        }
        
        for (WebElement tel : telLinks) {
            System.out.println("Tel link found: " + tel.getAttribute("href"));
        }
    }

    // 3. Validate the feedback form fields
    public static void validateFeedbackForm(WebDriver driver, WebDriverWait wait) {
        try {
            WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("name")));
            WebElement emailField = driver.findElement(By.name("email"));
            WebElement messageField = driver.findElement(By.name("message"));
            
            // Fill in valid test data
            nameField.sendKeys("Test User");
            emailField.sendKeys("testuser@example.com");
            messageField.sendKeys("This is a test message for validation.");
            
            System.out.println("Feedback form fields are accessible and fillable.");
        } catch (Exception e) {
            System.out.println("Error validating form fields: " + e.getMessage());
        }
    }

    // 4. Send a test feedback form submission
    public static void sendFeedbackForm(WebDriver driver, WebDriverWait wait) {
        try {
            WebElement submitButton = driver.findElement(By.xpath("//button[contains(text(), 'Submit') or @type='submit']"));
            submitButton.click();
            System.out.println("Feedback form submitted.");

            // Check for confirmation message
            WebElement confirmationMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(), 'Thank you')]")));
            System.out.println("Feedback form submission confirmed.");
        } catch (Exception e) {
            System.out.println("Error submitting feedback form: " + e.getMessage());
        }
    }
}
