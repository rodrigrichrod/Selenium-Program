package webportal;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class TimesheetPage {
	private WebDriver driver;

	public TimesheetPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}
	
	By page = By.className("pagesize");
	By save = By.xpath("//input[@value='Save as Draft']");
	By submit = By.id("submitit");
	By delete = By.id("deleteit");
	By dayText = By.xpath("//table/tbody/tr/td/div/div/form/table/thead/tr/th[@width='12%']");
	
	@FindBy(xpath="//table/tbody/tr[@seq='1']")
	private WebElement timesheetRow;
	
	@FindBy(id="deleteit")
	private WebElement deleteButton;

	@FindBy(id="submitit")
	private WebElement submitButton;
	
	@FindBy(xpath="//input[@value='Save as Draft']")
	private WebElement saveButton;
	
	@FindBy(id="emp_comment")
	private WebElement commentText;
	
	@FindBy(id="logoutBtn")
	private WebElement logout;
	
	public WebElement enterComments() {
		return commentText;
	}
	
	public WebElement selectRow() {
		return timesheetRow;
	}
	
	public WebElement submitTimesheet() {
		return submitButton;
	}
	
	public WebElement saveTimesheet() {
		return saveButton;
	}
	
	public WebElement logout() {
		return logout;
	}
	
	public WebElement deleteHours() {
		return deleteButton;
	}
	
	
	
	
}
