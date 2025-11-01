package io.cyborgcode.ui.test.framework.ui.components.link;

import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.components.base.BaseComponent;
import io.cyborgcode.roa.ui.components.link.Link;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import io.cyborgcode.ui.test.framework.ui.types.LinkFieldTypes;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

@ImplementationOfType(LinkFieldTypes.Data.BOOTSTRAP_LINK)
public class LinkBootstrapImpl extends BaseComponent implements Link {

   private static final By LINK_LOCATOR = By.cssSelector("span[class='headers']");
   private static final String DISABLED_STATE = "disabled";


   public LinkBootstrapImpl(SmartWebDriver driver) {
      super(driver);
   }


   @Override
   public void click(final SmartWebElement container, final String linkText) {
      SmartWebElement link = findLinkInContainer(container, linkText);
      link.click();
   }


   @Override
   public void click(final SmartWebElement container) {
      SmartWebElement link = findLinkInContainer(container, null);
      link.click();
   }


   @Override
   public void click(final String linkText) {
      SmartWebElement link = findLinkByText(linkText);
      link.click();
   }


   @Override
   public void click(final By linkLocator) {
      SmartWebElement link = driver.findSmartElement(linkLocator);
      link.click();
   }


   @Override
   public void doubleClick(final SmartWebElement container, final String linkText) {
      SmartWebElement link = findLinkInContainer(container, linkText);
      link.doubleClick();
   }


   @Override
   public void doubleClick(final SmartWebElement container) {
      SmartWebElement link = findLinkInContainer(container, null);
      link.doubleClick();
   }


   @Override
   public void doubleClick(final String linkText) {
      SmartWebElement link = findLinkByText(linkText);
      link.doubleClick();
   }


   @Override
   public void doubleClick(final By linkLocator) {
      SmartWebElement link = driver.findSmartElement(linkLocator);
      link.doubleClick();
   }


   @Override
   public boolean isEnabled(final SmartWebElement container, final String linkText) {
      SmartWebElement link = findLinkInContainer(container, linkText);
      return isLinkEnabled(link);
   }


   @Override
   public boolean isEnabled(final SmartWebElement container) {
      SmartWebElement link = findLinkInContainer(container, null);
      return isLinkEnabled(link);
   }


   @Override
   public boolean isEnabled(final String linkText) {
      SmartWebElement link = findLinkByText(linkText);
      return isLinkEnabled(link);
   }


   @Override
   public boolean isEnabled(final By linkLocator) {
      SmartWebElement link = driver.findSmartElement(linkLocator);
      return isLinkEnabled(link);
   }


   @Override
   public boolean isVisible(final SmartWebElement container, final String linkText) {
      SmartWebElement link = findLinkInContainer(container, linkText);
      return isLinkEnabled(link);
   }


   @Override
   public boolean isVisible(final SmartWebElement container) {
      SmartWebElement link = findLinkInContainer(container, null);
      return isLinkEnabled(link);
   }


   @Override
   public boolean isVisible(final String linkText) {
      SmartWebElement link = findLinkByText(linkText);
      return isLinkEnabled(link);
   }


   @Override
   public boolean isVisible(final By linkLocator) {
      SmartWebElement link = driver.findSmartElement(linkLocator);
      return isLinkEnabled(link);
   }


   private SmartWebElement findLinkInContainer(SmartWebElement container, String linkText) {
      return container.findSmartElements(LINK_LOCATOR).stream()
            .filter(element -> linkText == null || element.getText().contains(linkText))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("Link with text %s not found", linkText)));
   }


   private SmartWebElement findLinkByText(String linkText) {
      return driver.findSmartElements(LINK_LOCATOR).stream()
            .filter(element -> element.getText().contains(linkText))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("Link with text %s not found", linkText)));
   }

   @Override
   public void clickElementInCell(SmartWebElement cell) {
      cell.findSmartElement(By.tagName("a")).click();
   }


   private boolean isLinkEnabled(SmartWebElement link) {
      return Objects.isNull(link.getAttribute(DISABLED_STATE));
   }
}