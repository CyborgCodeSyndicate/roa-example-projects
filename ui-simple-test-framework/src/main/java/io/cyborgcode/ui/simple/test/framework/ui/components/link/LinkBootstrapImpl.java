package io.cyborgcode.ui.simple.test.framework.ui.components.link;

import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.components.base.BaseComponent;
import io.cyborgcode.roa.ui.components.link.Link;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import io.cyborgcode.ui.simple.test.framework.ui.types.LinkFieldTypes;
import java.util.Objects;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

/**
 * Bootstrap-specific implementation of the {@link Link} component.
 *
 * <p>This class provides concrete logic for interacting with Bootstrap-styled link elements.
 * It is automatically selected by ROA component resolution mechanism when a link field is
 * tagged with {@link LinkFieldTypes#BOOTSTRAP_LINK_TYPE} via the {@link ImplementationOfType} annotation.
 *
 * <p>Key capabilities:
 * <ul>
 *   <li>Click or double-click links by text, locator, or within a container</li>
 *   <li>Check enabled/disabled state via {@code disabled} class attribute</li>
 *   <li>Check visibility via {@code hidden} DOM attribute</li>
 *   <li>Find links dynamically by text content</li>
 *   <li>Interact with links embedded inside table cells via {@link #clickElementInCell(SmartWebElement)}</li>
 * </ul>
 *
 * <p>This implementation aligns with Bootstrap’s markup and attribute conventions to provide
 * reliable interactions with links in dynamic UIs.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@ImplementationOfType(LinkFieldTypes.Data.BOOTSTRAP_LINK)
public class LinkBootstrapImpl extends BaseComponent implements Link {

   private static final By LINK_LOCATOR = By.cssSelector("span[class='headers']");
   private static final String DISABLED_STATE = "disabled";
   private static final String HIDDEN_STATE = "hidden";


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
      return isLinkVisible(link);
   }


   @Override
   public boolean isVisible(final SmartWebElement container) {
      SmartWebElement link = findLinkInContainer(container, null);
      return isLinkVisible(link);
   }


   @Override
   public boolean isVisible(final String linkText) {
      SmartWebElement link = findLinkByText(linkText);
      return isLinkVisible(link);
   }


   @Override
   public boolean isVisible(final By linkLocator) {
      SmartWebElement link = driver.findSmartElement(linkLocator);
      return isLinkVisible(link);
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
      return Objects.isNull(link.getDomAttribute(DISABLED_STATE));
   }

   private boolean isLinkVisible(SmartWebElement link) {
      return Objects.isNull(link.getDomAttribute(HIDDEN_STATE));
   }
}