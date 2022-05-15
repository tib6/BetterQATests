package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.AjaxElementLocatorFactory;

public class Login {
	WebDriver driver;
	AjaxElementLocatorFactory factory;
	JavascriptExecutor executor;
	
	@FindBy(name = "post_password")
	WebElement passwordField;
	@FindBy(name = "Submit")
	WebElement submitButton;
	
	private final String validPassword = "do_not_share!1";
	
	public Login(WebDriver driver) {
		this.driver = driver;
		factory = new AjaxElementLocatorFactory(driver,15);
		executor = (JavascriptExecutor)driver;
		//This initElements method will create all WebElements
		PageFactory.initElements(factory, this);
	}
	
	public void insertPassword(String password) {
		passwordField.sendKeys(password);
	}
	
	public void clickEnterButton() {
		submitButton.click();
	}
	
	public void login() {
		this.insertPassword(validPassword);
		this.clickEnterButton();
	}
}
