package webportal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

	private WebDriver driver;
	
	public LoginPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	By login = By.id("login");
	
	@FindBy(id="login")
	private WebElement username;
	
	@FindBy(id="pass")
	private WebElement password;
	
	@FindBy(id="submitButton")
	private WebElement submit;
	
	public WebElement username() {
		return username;
	}
	
	public WebElement password() {
		return password;
	}
	
	public WebElement submit() {
		return submit;
	}
	
}
