package io.cyborgcode.ui.complex.test.framework.ui.components.link;

import io.cyborgcode.ui.complex.test.framework.ui.types.LinkFieldTypes;
import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.components.base.BaseComponent;
import io.cyborgcode.roa.ui.components.link.Link;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebDriver;
import io.cyborgcode.roa.ui.selenium.smart.SmartWebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;

import java.util.Objects;

/**
 * Vaadin-specific implementation of the {@link Link} component interface.
 * <p>
 * This class provides the concrete logic for interacting with Vaadin navigation tabs
 * ({@code <vaadin-tab>} elements) in the Bakery Flow application. It is automatically
 * selected by ROA's component resolution mechanism when a link element is tagged with
 * {@link LinkFieldTypes#VA_LINK_TYPE} via the {@code @ImplementationOfType} annotation.
 * </p>
 * <p>
 * Key capabilities:
 * <ul>
 *   <li>Click or double-click links by text, locator, or within a container</li>
 *   <li>Check enabled/disabled state via {@code disabled} class attribute</li>
 *   <li>Check visibility via {@code hidden} DOM attribute</li>
 *   <li>Find links dynamically by text content</li>
 * </ul>
 * </p>
 * <p>
 * This implementation handles Vaadin's DOM structure and attribute-based state
 * management, ensuring reliable interaction with navigation tabs in dynamic UIs.
 * </p>
 */
@ImplementationOfType(LinkFieldTypes.Data.VA_LINK)
public class LinkVaImpl extends BaseComponent implements Link {

   private static final By LINK_TAG_NAME_SELECTOR = By.tagName("vaadin-tab");
   private static final String DISABLED_STATE = "disabled";
   private static final String NOT_VISIBLE_STATE_INDICATOR = "hidden";

   public LinkVaImpl(SmartWebDriver driver) {
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
      return container.findSmartElements(LINK_TAG_NAME_SELECTOR).stream()
            .filter(element -> linkText == null || element.getText().contains(linkText))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("Link with text %s not found", linkText)));
   }


   private SmartWebElement findLinkByText(String linkText) {
      return driver.findSmartElements(LINK_TAG_NAME_SELECTOR).stream()
            .filter(element -> element.getText().contains(linkText))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(String.format("Link with text %s not found", linkText)));
   }


   private boolean isLinkEnabled(SmartWebElement link) {
      return !Objects.requireNonNull(link.getDomAttribute("class")).contains(DISABLED_STATE);
   }

   private boolean isLinkVisible(SmartWebElement link) {
       return link.getDomAttribute(NOT_VISIBLE_STATE_INDICATOR) == null;
   }
}