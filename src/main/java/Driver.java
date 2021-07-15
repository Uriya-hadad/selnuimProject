import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.Scanner;

public class Driver {

    static WebDriver driver;
    static WebDriverWait driverWait;
    static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        System.setProperty("webdriver.chrome.driver", "c:\\chromedriver.exe");
        String password, userName;
        System.out.println("what is your moodle's userName?");
        userName = scanner.next();
        System.out.println("what is your moodle's password?");
        password = scanner.next();
        boolean debt = getDebt();
        driver = new ChromeDriver();
        driver.get("https://www.aac.ac.il/");
        driver.manage().window().maximize();
        driverWait = new WebDriverWait(driver, 10);
        logIn(userName, password);
        if (debt) {
            WebElement element = driverWait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#debtdismissmodal")));
            element.click();
        }
        driver.findElement(By.cssSelector("a[href='https://moodle.aac.ac.il/login/index.php']")).click();
        driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[class = 'aalink coursename']")));
        List<WebElement> coursesList = (driver.findElements(By.cssSelector("a[class = 'aalink coursename']")));
        getCoursesList(coursesList);
        accessCourse(coursesList);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logout();
    }

    private static boolean getDebt() {
        System.out.println("do you have a debt?");
        System.out.println("1 for yes");
        System.out.println("2 for no");
        return scanner.nextInt() == 1;
    }

    private static void accessCourse(List<WebElement> coursesList) {
        System.out.println("which course do you want?");
        int answer = scanner.nextInt();
        coursesList.get(answer - 1).click();
    }

    private static void getCoursesList(List<WebElement> coursesList) {
        System.out.println("your courses list:");
        for (int i = 0; i < coursesList.size(); i++) {
            System.out.println("\t" + (i + 1) + ")" + getTextNode(coursesList.get(i)));
        }
    }

    private static void logIn(String userName, String password) {
        driver.findElement(By.cssSelector("a[href='https://portal.aac.ac.il']")).click();
        WebElement usernameInput, passwordInput;
        usernameInput = driver.findElement(By.cssSelector("#Ecom_User_ID"));
        usernameInput.sendKeys(userName);
        passwordInput = driver.findElement(By.cssSelector("#Ecom_Password"));
        passwordInput.sendKeys(password);
        driver.findElement(By.cssSelector("#wp-submit")).click();

    }

    private static void logout() {
        driver.findElement(By.cssSelector("#action-menu-toggle-1")).click();
        WebElement logOut = driverWait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("a[data-title = 'logout,moodle']")));
        logOut.click();
        driver.findElement(By.cssSelector("a[href='https://portal.aac.ac.il/AGLogout']")).click();

    }

    public static String getTextNode(WebElement e) {
        String text = e.getText().trim();
        List<WebElement> children = e.findElements(By.xpath("./*"));
        for (WebElement child : children) {
            text = text.replaceFirst(child.getText(), "").trim();
        }
        return text;
    }
}