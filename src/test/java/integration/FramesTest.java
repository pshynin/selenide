package integration;

import static com.codeborne.selenide.Condition.name;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.isChrome;
import static com.codeborne.selenide.WebDriverRunner.source;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

public class FramesTest extends IntegrationTest {
  @Before
  public void openPage() {
    openFile("page_with_frames.html");
  }

  @Test
  public void canSwitchFramesViaSequence() {
    assertEquals("Test::frames", title());

    switchToLastFrame("parentFrame");
    $("frame").shouldHave(name("childFrame_1"));

    switchToLastFrame("parentFrame", "childFrame_1");
    assertTrue(source().contains("Hello, WinRar!"));

    switchToLastFrame("parentFrame", "childFrame_2");
    $("frame").shouldHave(name("childFrame_2_1"));

    switchToLastFrame("parentFrame", "childFrame_2", "childFrame_2_1");
    assertTrue(source().contains("This is last frame!"));

    switchToLastFrame("parentFrame");
    $("frame").shouldHave(name("childFrame_1"));
  }

  @Test
  public void switchToLastFrame_withoutParameters_switchesToDefaultContent() {
    switchToLastFrame("parentFrame");
    $("frame").shouldHave(name("childFrame_1"));
    
    switchToLastFrame();
    $("frame").shouldHave(name("topFrame"));
  }

  @Test
  public void canSwitchBetweenFramesByTitle() {
    assertEquals("Test::frames", title());

    switchTo().frame("topFrame");
    assertTrue(source().contains("Hello, WinRar!"));

    switchTo().defaultContent();
    switchTo().frame("leftFrame");
    $("h1").shouldHave(text("Page with dynamic select"));

    switchTo().defaultContent();
    switchTo().frame("mainFrame");
    $("h1").shouldHave(text("Page with JQuery"));
  }

  @Test
  public void canSwitchBetweenFramesByIndex() {
    assumeFalse(isChrome());
    assertEquals("Test::frames", title());

    switchTo().frame(0);
    assertTrue(source().contains("Hello, WinRar!"));

    switchTo().defaultContent();
    switchTo().frame(1);
    $("h1").shouldHave(text("Page with dynamic select"));

    switchTo().defaultContent();
    switchTo().frame(2);
    $("h1").shouldHave(text("Page with JQuery"));
  }

  @AfterClass
  public static void tearDown() {
    close();
  }
}
