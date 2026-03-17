package io.cyborgcode.ui.simple.test.framework.ui.components.link;

import com.microsoft.playwright.Page;
import io.cyborgcode.roa.ui.annotations.ImplementationOfType;
import io.cyborgcode.roa.ui.playwright.base.PwBy;
import io.cyborgcode.roa.ui.playwright.base.PwElement;
import io.cyborgcode.roa.ui.playwright.components.base.BaseComponent;
import io.cyborgcode.roa.ui.playwright.components.link.Link;
import io.cyborgcode.ui.simple.test.framework.ui.types.LinkFieldTypes;
import java.util.Objects;

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
 * </ul>
 *
 * <p>This implementation aligns with Bootstrap’s markup and attribute conventions to provide
 * reliable interactions with links in dynamic UIs.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
@ImplementationOfType(LinkFieldTypes.Data.BOOTSTRAP_LINK)
public class LinkBootstrapImpl extends BaseComponent implements Link {

   private static final PwBy LINK_LOCATOR = PwBy.css("span[class='headers']");
   private static final String DISABLED_STATE = "disabled";
   private static final String HIDDEN_STATE = "hidden";


   public LinkBootstrapImpl(Page driver) {
      super(driver);
   }


   @Override
   public void click(final PwElement container, final String linkText) {
      PwElement link = findLinkInContainer(container, linkText);
      link.click();
   }


   @Override
   public void click(final PwElement container) {
      PwElement link = findLinkInContainer(container, null);
      link.click();
   }


   @Override
   public void click(final String linkText) {
      PwElement link = findLinkByText(linkText);
      link.click();
   }


   @Override
   public void click(final PwBy linkLocator) {
      PwElement link = PwElement.of(driver.locator(linkLocator.selector()));
      link.click();
   }


   @Override
   public void doubleClick(final PwElement container, final String linkText) {
      PwElement link = findLinkInContainer(container, linkText);
      link.dblclick();
   }


   @Override
   public void doubleClick(final PwElement container) {
      PwElement link = findLinkInContainer(container, null);
      link.dblclick();
   }


   @Override
   public void doubleClick(final String linkText) {
      PwElement link = findLinkByText(linkText);
      link.dblclick();
   }


   @Override
   public void doubleClick(final PwBy linkLocator) {
      PwElement link = PwElement.of(driver.locator(linkLocator.selector()));
      link.dblclick();
   }


   @Override
   public boolean isEnabled(final PwElement container, final String linkText) {
      PwElement link = findLinkInContainer(container, linkText);
      return isLinkEnabled(link);
   }


   @Override
   public boolean isEnabled(final PwElement container) {
      PwElement link = findLinkInContainer(container, null);
      return isLinkEnabled(link);
   }


   @Override
   public boolean isEnabled(final String linkText) {
      PwElement link = findLinkByText(linkText);
      return isLinkEnabled(link);
   }


   @Override
   public boolean isEnabled(final PwBy linkLocator) {
      PwElement link = PwElement.of(driver.locator(linkLocator.selector()));
      return isLinkEnabled(link);
   }


   @Override
   public boolean isVisible(final PwElement container, final String linkText) {
      PwElement link = findLinkInContainer(container, linkText);
      return isLinkVisible(link);
   }


   @Override
   public boolean isVisible(final PwElement container) {
      PwElement link = findLinkInContainer(container, null);
      return isLinkVisible(link);
   }


   @Override
   public boolean isVisible(final String linkText) {
      PwElement link = findLinkByText(linkText);
      return isLinkVisible(link);
   }


   @Override
   public boolean isVisible(final PwBy linkLocator) {
      PwElement link = PwElement.of(driver.locator(linkLocator.selector()));
      return isLinkVisible(link);
   }


   private PwElement findLinkInContainer(PwElement container, String linkText) {
      return container.locator(LINK_LOCATOR.selector()).allElements().stream()
            .filter(element -> linkText == null || element.textContent().contains(linkText))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(String.format("Link with text %s not found", linkText)));
   }


   private PwElement findLinkByText(String linkText) {
      return PwElement.of(driver.locator(LINK_LOCATOR.selector())).allElements().stream()
            .filter(element -> element.textContent().contains(linkText))
            .findFirst()
            .orElseThrow(() -> new RuntimeException(String.format("Link with text %s not found", linkText)));
   }

   @Override
   public void clickElementInCell(PwElement cell) {
      cell.locator("a").click();
   }


   private boolean isLinkEnabled(PwElement link) {
      return Objects.isNull(link.getAttribute(DISABLED_STATE));
   }

   private boolean isLinkVisible(PwElement link) {
      return Objects.isNull(link.getAttribute(HIDDEN_STATE));
   }
}