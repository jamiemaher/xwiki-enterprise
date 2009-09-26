/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.xpn.xwiki.it.selenium;

import junit.framework.Test;

import com.xpn.xwiki.it.selenium.framework.AbstractWysiwygTestCase;
import com.xpn.xwiki.it.selenium.framework.ColibriSkinExecutor;
import com.xpn.xwiki.it.selenium.framework.XWikiTestSuite;

/**
 * Integration tests for macro support inside the WYSIWYG editor.
 * 
 * @version $Id$
 */
public class MacroTest extends AbstractWysiwygTestCase
{
    public static final String MENU_MACRO = "Macro";

    public static final String MENU_REFRESH = "Refresh";

    public static final String MENU_COLLAPSE = "Collapse";

    public static final String MENU_COLLAPSE_ALL = "Collapse All";

    public static final String MENU_EXPAND = "Expand";

    public static final String MENU_EXPAND_ALL = "Expand All";

    public static final String MENU_EDIT = "Edit Macro Properties...";

    public static final String MENU_INSERT = "Insert Macro...";

    public static final String MACRO_CATEGORY_SELECTOR = "//select[@title='Select a macro category']";

    public static final String MACRO_LIVE_FILTER_SELECTOR = "//input[@title = 'Type to filter']";

    public static final String MACRO_SELECTOR_LIST = "//div[contains(@class, 'xListBox')]";

    public static Test suite()
    {
        XWikiTestSuite suite = new XWikiTestSuite("Integration tests for macro support inside the WYSIWYG editor.");
        suite.addTestSuite(MacroTest.class, ColibriSkinExecutor.class);
        return suite;
    }

    /**
     * Tests that after deleting the last character before a macro the caret remains before the macro and not inside the
     * macro.
     */
    public void testDeleteCharacterBeforeMacro()
    {
        setWikiContent("a{{html}}b{{/html}}");
        typeDelete();
        typeText("x");
        assertWiki("x{{html}}b{{/html}}");
    }

    /**
     * Tests that by holding the Delete key down before a macro the caret doesn't get inside the macro, but, instead,
     * the macro is deleted.
     */
    public void testHoldDeleteKeyBeforeMacro()
    {
        setWikiContent("c{{html}}def{{/html}}g");
        typeDelete(2, true);
        typeText("x");
        assertWiki("xg");
    }

    /**
     * Tests that after deleting with Backspace a text selection ending before a macro the caret remains before the
     * macro and not inside the macro.
     */
    public void testSelectCharacterBeforeMacroAndPressBackspace()
    {
        setWikiContent("g{{html}}h{{/html}}");
        // Select the character preceding the macro.
        selectNode("XWE.body.firstChild.firstChild");
        typeBackspace();
        typeText("x");
        assertWiki("x{{html}}h{{/html}}");
    }

    /**
     * Tests that if we select the text before a macro and insert a symbol instead of it then the symbol is inserted
     * before the macro and not inside the macro.
     */
    public void testSelectCharacterBeforeMacroAndInsertSymbol()
    {
        setWikiContent("i{{html}}j{{/html}}");
        // Select the character preceding the macro.
        selectNode("XWE.body.firstChild.firstChild");
        clickSymbolButton();
        getSelenium().click("//div[@title='copyright sign']");
        typeText("x");
        assertWiki("\u00A9x{{html}}j{{/html}}");
    }

    /**
     * Tests that a macro can be deleted by pressing Delete key when the caret is placed before that macro.
     */
    public void testPressDeleteJustBeforeMacro()
    {
        setWikiContent("j{{html}}k{{/html}}l");
        // Move the caret just before the macro.
        moveCaret("XWE.body.firstChild.firstChild", 1);
        typeDelete();
        typeText("x");
        assertWiki("jxl");
    }

    /**
     * Tests that after deleting the last character after a macro the caret remains after the macro and not inside the
     * macro.
     */
    public void testDeleteCharacterAfterMacro()
    {
        setWikiContent("a{{html}}b{{/html}}c");
        // Move the caret at the end.
        moveCaret("XWE.body.firstChild.lastChild", 1);
        typeBackspace();
        typeText("x");
        assertWiki("a{{html}}b{{/html}}x");
    }

    /**
     * Tests that by holding the Backspace key down after a macro the caret doesn't get inside the macro, but, instead,
     * the macro is deleted.
     */
    public void testHoldBackspaceKeyAfterMacro()
    {
        setWikiContent("c{{html}}def{{/html}}g");
        // Move the caret at the end.
        moveCaret("XWE.body.firstChild.lastChild", 1);
        typeBackspace(2, true);
        typeText("x");
        assertWiki("cx");
    }

    /**
     * Tests that after deleting with Delete key a text selection starting after a macro the caret remains after the
     * macro and not inside the macro.
     */
    public void testSelectCharacterAfterMacroAndPressDelete()
    {
        setWikiContent("g{{html}}h{{/html}}i");
        // Select the character following the macro.
        selectNode("XWE.body.firstChild.lastChild");
        typeDelete();
        typeText("x");
        assertWiki("g{{html}}h{{/html}}x");
    }

    /**
     * Tests that if we select the text after a macro and insert a symbol instead of it then the symbol is inserted
     * after the macro and not inside the macro.
     */
    public void testSelectCharacterAfterMacroAndInsertSymbol()
    {
        setWikiContent("i{{html}}j{{/html}}k");
        // Select the character following the macro.
        selectNode("XWE.body.firstChild.lastChild");
        clickSymbolButton();
        getSelenium().click("//div[@title='copyright sign']");
        typeText("x");
        assertWiki("i{{html}}j{{/html}}\u00A9x");
    }

    /**
     * Tests that a macro can be deleted by pressing Backspace key when the caret is placed after that macro.
     */
    public void testPressBackspaceJustAfterMacro()
    {
        setWikiContent("k{{html}}l{{/html}}m");
        // Move the caret at the end.
        moveCaret("XWE.body.firstChild.lastChild", 0);
        typeBackspace();
        typeText("x");
        assertWiki("kxm");
    }

    /**
     * Tests that Undo/Redo operations don't affect the macros present in the edited document.
     */
    public void testUndoRedoWhenMacrosArePresent()
    {
        setWikiContent("{{html}}pq{{/html}}");
        // We have to manually place the caret to be sure it is before the macro. The caret is before the macro when the
        // browser window is focused but inside the macro when the tests run in background.
        moveCaret("XWE.body", 0);
        applyStylePlainText();
        typeText("uv");
        clickUndoButton();
        clickRedoButton();
        assertWiki("uv{{html}}pq{{/html}}");
    }

    /**
     * Clicks on a macro and deletes it.
     */
    public void testSelectAndDeleteMacro()
    {
        setWikiContent("{{html}}<p>foo</p>{{/html}}\n\nbar");
        getSelenium().clickAt(getDOMLocator("getElementsByTagName('button')[0]"), "0, 0");
        typeDelete();
        assertWiki("bar");
    }

    /**
     * @see XWIKI-3221: New lines inside code macro are lost when saving
     */
    public void testWhiteSpacesInsideCodeMacroArePreserved()
    {
        String wikiText = "{{code}}\nfunction foo() {\n    alert('bar');\n}\n{{/code}}";
        setWikiContent(wikiText);
        assertWiki(wikiText);
    }

    /**
     * Test if the user can collapse and expand all the macros using the macro menu and if this menu is synchronized
     * with the current state of the rich text area.
     */
    public void testCollapseAndExpandAllMacros()
    {
        typeText("no macro");

        clickMenu(MENU_MACRO);
        assertTrue(isMenuEnabled(MENU_REFRESH));
        // If there's no macro present then the Collapse/Expand menu items must be disabled.
        assertFalse(isMenuEnabled(MENU_COLLAPSE_ALL));
        assertFalse(isMenuEnabled(MENU_EXPAND_ALL));
        closeMenuContaining(MENU_REFRESH);

        setWikiContent("k\n\n{{html}}l{{/html}}\n\nm\n\n{{code}}n{{/code}}\n\no");
        clickMenu(MENU_MACRO);
        // By default all macros are expanded.
        assertTrue(isMenuEnabled(MENU_COLLAPSE_ALL));
        assertFalse(isMenuEnabled(MENU_EXPAND_ALL));

        // Let's collapse all macros.
        clickMenu(MENU_COLLAPSE_ALL);

        clickMenu(MENU_MACRO);
        // Now all macros should be collapsed.
        assertFalse(isMenuEnabled(MENU_COLLAPSE_ALL));
        assertTrue(isMenuEnabled(MENU_EXPAND_ALL));

        // Let's expand all macros.
        clickMenu(MENU_EXPAND_ALL);

        clickMenu(MENU_MACRO);
        // Now all macros should be expanded.
        assertTrue(isMenuEnabled(MENU_COLLAPSE_ALL));
        assertFalse(isMenuEnabled(MENU_EXPAND_ALL));
        closeMenuContaining(MENU_REFRESH);

        // Let's collapse the first macro by clicking it.
        // First click selects the macro.
        clickMacro(0);
        // Second click toggles between collapsed and expanded state.
        clickMacro(0);
        // Finally unselect the macro.
        runScript("XWE.selection.collapseToEnd()");
        triggerToolbarUpdate();

        // Now let's check the menu. Both Collapse All and Expand All menu items should be enabled.
        clickMenu(MENU_MACRO);
        assertTrue(isMenuEnabled(MENU_COLLAPSE_ALL));
        assertTrue(isMenuEnabled(MENU_EXPAND_ALL));
    }

    /**
     * Test if the user can collapse and expand the selected macros using the macro menu and if this menu is
     * synchronized with the current state of the rich text area.
     */
    public void testCollapseAndExpandSelectedMacros()
    {
        setWikiContent("o\n\n{{html}}n{{/html}}\n\nm\n\n{{code}}l{{/code}}\n\nk");

        // If no macro is selected then Expand and Collapse menu entries shouldn't be present.
        clickMenu(MENU_MACRO);
        assertTrue(isMenuEnabled(MENU_REFRESH));
        assertFalse(isMenuEnabled(MENU_COLLAPSE));
        assertFalse(isMenuEnabled(MENU_EXPAND));
        closeMenuContaining(MENU_REFRESH);

        // Select the first macro and collapse it (by default macros should be expanded).
        clickMacro(0);
        clickMenu(MENU_MACRO);
        assertTrue(isMenuEnabled(MENU_COLLAPSE));
        assertFalse(isMenuEnabled(MENU_EXPAND));
        clickMenu(MENU_COLLAPSE);

        // Now expand it back.
        clickMenu(MENU_MACRO);
        assertFalse(isMenuEnabled(MENU_COLLAPSE));
        assertTrue(isMenuEnabled(MENU_EXPAND));
        clickMenu(MENU_EXPAND);

        // Let's select the second macro too.
        getSelenium().controlKeyDown();
        clickMacro(1);
        getSelenium().controlKeyUp();

        // Collapse both selected macros.
        clickMenu(MENU_MACRO);
        assertTrue(isMenuEnabled(MENU_COLLAPSE));
        assertFalse(isMenuEnabled(MENU_EXPAND));
        clickMenu(MENU_COLLAPSE);

        // Let's check if the menu reports them as collapsed.
        clickMenu(MENU_MACRO);
        assertFalse(isMenuEnabled(MENU_COLLAPSE));
        assertTrue(isMenuEnabled(MENU_EXPAND));

        // Expand both.
        clickMenu(MENU_EXPAND);

        // Let's check if the menu reports them as expanded.
        clickMenu(MENU_MACRO);
        assertTrue(isMenuEnabled(MENU_COLLAPSE));
        assertFalse(isMenuEnabled(MENU_EXPAND));
        closeMenuContaining(MENU_COLLAPSE);
    }

    /**
     * Test if the user can select a macro by clicking it and then toggle between collapsed and expanded states.
     */
    public void testClickToSelectMacroAndToggleCollapse()
    {
        // Let's use a macro without definition.
        setWikiContent("{{foo}}bar{{/foo}}");

        // By default macros are expanded. Let's check this.
        assertFalse(getSelenium().isVisible(getMacroPlaceHolderLocator(0)));
        assertTrue(getSelenium().isVisible(getMacroOutputLocator(0)));

        // Select the macro.
        clickMacro(0);

        // Let's collapse the selected macro and check its state.
        clickMacro(0);
        assertTrue(getSelenium().isVisible(getMacroPlaceHolderLocator(0)));
        assertFalse(getSelenium().isVisible(getMacroOutputLocator(0)));

        // Let's expand the selected macro and check its state.
        clickMacro(0);
        assertFalse(getSelenium().isVisible(getMacroPlaceHolderLocator(0)));
        assertTrue(getSelenium().isVisible(getMacroOutputLocator(0)));
    }

    /**
     * Tests the refresh feature when there's no macro present in the edited document.
     */
    public void testRefreshContentWithoutMacros()
    {
        typeText("a b");
        assertXHTML("a b<br class=\"spacer\">");

        // If no macros are present then the refresh shoudn't affect too much the edited content.
        refreshMacros();
        assertXHTML("<p>a b</p>");
    }

    /**
     * Tests that the user can refresh all the macros from the edited document by using the Refresh menu.
     */
    public void testRefreshMacros()
    {
        setWikiContent("{{box}}p{{/box}}\n\n{{code}}q{{/code}}");

        // Collapse the second macro.
        assertFalse(getSelenium().isVisible(getMacroPlaceHolderLocator(1)));
        assertTrue(getSelenium().isVisible(getMacroOutputLocator(1)));
        clickMacro(1);
        clickMacro(1);
        assertTrue(getSelenium().isVisible(getMacroPlaceHolderLocator(1)));
        assertFalse(getSelenium().isVisible(getMacroOutputLocator(1)));

        // Unselect the macro.
        runScript("XWE.selection.collapseToEnd()");
        triggerToolbarUpdate();

        // Refresh the content
        refreshMacros();

        // Check if the second macro is expanded.
        assertFalse(getSelenium().isVisible(getMacroPlaceHolderLocator(1)));
        assertTrue(getSelenium().isVisible(getMacroOutputLocator(1)));
    }

    /**
     * Tests that the user can refresh the Table Of Contents macro after adding more headers.
     */
    public void testRefreshTocMacro()
    {
        setWikiContent("{{toc start=\"1\"/}}\n\n= Title 1\n\n== Title 2");
        // We should have two list items in the edited document.
        String listItemCountExpression = "window." + getDOMLocator("getElementsByTagName('li')") + ".length";
        assertEquals("2", getSelenium().getEval(listItemCountExpression));

        // Place the caret after the second heading and insert a new one.
        moveCaret(getDOMLocator("getElementsByTagName('h2')[0].firstChild.firstChild"), 7);
        typeEnter();
        typeText("Title 3");
        applyStyleTitle3();

        // Refresh the content and the TOC macro.
        refreshMacros();

        // We should have three list items in the edited document now.
        assertEquals("3", getSelenium().getEval(listItemCountExpression));
    }

    /**
     * Tests the edit macro feature by editing a HTML macro instance and changing its content and a parameter.
     */
    public void testEditHTMLMacro()
    {
        setWikiContent("{{html}}white{{/html}}");
        editMacro(0);

        // Change the content of the HTML macro.
        setFieldValue("pd-content-input", "black");

        // Set the Wiki parameter to true.
        getSelenium().select("pd-wiki-input", "yes");

        // Apply changes.
        applyMacroChanges();

        // Test if our changes have been applied.
        assertWiki("{{html wiki=\"true\"}}black{{/html}}");

        // Edit again, this time using the default value for the Wiki parameter.
        editMacro(0);

        // Set the Wiki parameter to its default value, false.
        assertEquals("true", getSelenium().getValue("pd-wiki-input"));
        getSelenium().select("pd-wiki-input", "no");

        // Apply changes.
        applyMacroChanges();

        // Test if our changes have been applied. This time the Wiki parameter is missing from the output because it has
        // the default value.
        assertWiki("{{html}}black{{/html}}");
    }

    /**
     * Tests if the edit macro feature doesn't fail when the user inputs special characters like {@code "} (used for
     * wrapping parameter values) or {@code |-|} (used to separate the macro name, parameter list and macro content in
     * macro serialization).
     * 
     * @see XWIKI-3270: Quotes inside macro parameter values need to be escaped
     */
    public void testEditMacroWithSpecialCharactersInParameterValues()
    {
        setWikiContent("{{box title =  \"1~\"2|-|3=~~~\"4~~\" }}=~\"|-|~~{{/box}}");
        editMacro(0);

        // Check if the content of the macro has the right value.
        assertEquals("=~\"|-|~~", getSelenium().getValue("pd-content-input"));

        // Check if the title parameter has the right value (it should be the first text input).
        assertEquals("1\"2|-|3=~\"4~", getSelenium().getValue("pd-title-input"));

        // Change the title parameter.
        setFieldValue("pd-title-input", "a\"b|-|c=~\"d~");
        applyMacroChanges();

        assertWiki("{{box title=\"a~\"b|-|c=~~~\"d~~\"}}=~\"|-|~~{{/box}}");
    }

    /**
     * Tests if the edit macro feature doesn't fail when the user tries to edit an unregistered macro (a macro who's
     * descriptor can't be found).
     */
    public void testEditUnregisteredMacro()
    {
        setWikiContent("{{foo}}bar{{/foo}}");
        editMacro(0);

        // Check if the dialog shows the error message
        assertTrue(getSelenium().isVisible("//div[@class = 'xDialogBody']/div[contains(@class, 'errormessage')]"));
    }

    /**
     * Tests that macro edits can be undone.
     */
    public void testUndoMacroEdit()
    {
        setWikiContent("{{velocity}}$context.user{{/velocity}}");

        // First edit.
        editMacro(0);
        setFieldValue("pd-content-input", "$util.date");
        applyMacroChanges();

        // Second edit.
        editMacro(0);
        setFieldValue("pd-content-input", "$xwiki.version");
        applyMacroChanges();

        clickUndoButton(2);
        clickRedoButton();
        assertWiki("{{velocity}}$util.date{{/velocity}}");
    }

    /**
     * Tests the basic insert macro scenario, using the code macro.
     */
    public void testInsertCodeMacro()
    {
        insertMacro("Code");

        setFieldValue("pd-content-input", "function f(x) {\n  return x;\n}");
        applyMacroChanges();

        editMacro(0);
        setFieldValue("pd-title-input", "Identity function");
        applyMacroChanges();

        assertWiki("{{code title=\"Identity function\"}}function f(x) {\n  return x;\n}{{/code}}");
    }

    /**
     * Tests if the ToC macro can be inserted in an empty paragraph without receiving the "Not an inline macro" error
     * message.
     * 
     * @see XWIKI-3551: Cannot insert standalone macros
     */
    public void testInsertTOCMacro()
    {
        // Create two headings to be able to detect if the ToC macro has the right output.
        typeText("Title 1");
        applyStyleTitle1();

        typeEnter();
        typeText("Title 2");
        applyStyleTitle2();

        // Let's insert a ToC macro between the two headings.
        // First, place the caret at the end of first heading.
        moveCaret("XWE.body.getElementsByTagName('h1')[0].firstChild", 7);
        // Get out of the heading.
        typeEnter();
        // Insert the ToC macro
        insertMacro("Table Of Contents");
        // Make sure the ToC starts with level 2 headings.
        setFieldValue("pd-start-input", "2");
        applyMacroChanges();

        // Check the output of the ToC macro.
        assertEquals("1", getSelenium().getEval("window." + getDOMLocator("getElementsByTagName('li')") + ".length"));

        // Check the XWiki syntax.
        assertWiki("= Title 1 =\n\n{{toc start=\"2\"/}}\n\n== Title 2 ==");
    }

    /**
     * Inserts a HTML macro, whose output contains block-level elements, in the middle of a paragraph's text and tests
     * if the macro can be fixed by separating it in an empty paragraph.
     * 
     * @see XWIKI-3551: Cannot insert standalone macros
     */
    public void testInsertHTMLMacroWithBlockContentInANotEmptyParagraph()
    {
        // Create a paragraph with some text inside.
        typeText("beforeafter");
        applyStyleTitle1();
        applyStylePlainText();

        // Place the caret in the middle of the paragraph.
        moveCaret("XWE.body.firstChild.firstChild", 6);

        // Insert the HTML macro.
        insertMacro("HTML");
        // Make the macro render a list, which is forbidden inside a paragraph.
        setFieldValue("pd-content-input", "<ul><li>xwiki</li></ul>");
        applyMacroChanges();

        // At this point the macro should render an error message instead of the list.
        String listItemCountExpression = "window." + getDOMLocator("getElementsByTagName('li')") + ".length";
        assertEquals("0", getSelenium().getEval(listItemCountExpression));

        // Let's fix the macro by separating it in an empty paragraph.
        // Move the caret before the macro and press Enter twice to move it into a new paragraph.
        moveCaret("XWE.body.firstChild.firstChild", 6);
        typeEnter();
        // Move the caret after the macro and press Enter twice to move the following text in a new paragraph.
        moveCaret("XWE.body.lastChild.lastChild", 0);
        typeEnter();

        // Now the macro should be in an empty paragraph.
        // Let's refresh the content to see if the macro was fixed.
        refreshMacros();
        // Check the output of the HTML macro.
        assertEquals("1", getSelenium().getEval(listItemCountExpression));

        // Check the XWiki syntax.
        assertWiki("before\n\n{{html}}<ul><li>xwiki</li></ul>{{/html}}\n\nafter");
    }

    /**
     * @see XWIKI-3570: Code macro fails to escape properly in GWT editor
     */
    public void testInsertCodeMacroWithXMLComments()
    {
        // Insert the Code macro.
        insertMacro("Code");
        // Set the language parameter to XML.
        setFieldValue("pd-language-input", "xml");
        // Set the content. Include XML comments in the content.
        setFieldValue("pd-content-input",
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- this is a test -->\n<test>123</test>");
        applyMacroChanges();

        // Check the XWiki syntax.
        assertWiki("{{code language=\"xml\"}}<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
            + "<!-- this is a test -->\n<test>123</test>{{/code}}");

        // Edit the inserted macro.
        editMacro(0);
        assertEquals("xml", getSelenium().getValue("pd-language-input"));
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!-- this is a test -->\n<test>123</test>",
            getSelenium().getValue("pd-content-input"));
        closeDialog();
    }

    /**
     * @see XWIKI-3581: WYSIWYG editor treats macro parameter names as case sensitive
     */
    public void testDetectMacroParameterNamesIgnoringCase()
    {
        setWikiContent("{{box CSSClaSS=\"foo\"}}bar{{/box}}");
        // Edit the Box macro.
        editMacro(0);
        // See if the CSSClaSS parameter was correctly detected.
        assertEquals("foo", getSelenium().getValue("pd-cssClass-input"));
        // Change the value of the CSSClaSS parameter.
        setFieldValue("pd-cssClass-input", "xyz");
        applyMacroChanges();
        // Check the XWiki syntax.
        assertWiki("{{box CSSClaSS=\"xyz\"}}bar{{/box}}");
    }

    /**
     * @see XWIKI-3735: Differentiate macros with empty content from macros without content.
     */
    public void testDifferentiateMacrosWithEmptyContentFromMacrosWithoutContent()
    {
        StringBuffer macros = new StringBuffer();
        macros.append("{{code/}}");
        macros.append("{{code}}{{/code}}");
        macros.append("{{code title=\"1|-|2\"/}}");
        macros.append("{{code title=\"1|-|2\"}}{{/code}}");

        // Insert the macros.
        setWikiContent(macros.toString());
        // See if the macro syntax is left unchanged when the macros are not edited.
        assertWiki(macros.toString());

        // Edit the first macro (the one without content and without arguments).
        editMacro(0);
        setFieldValue("pd-content-input", "|-|");
        applyMacroChanges();
        assertWiki("{{code}}|-|{{/code}}{{code}}{{/code}}{{code title=\"1|-|2\"/}}{{code title=\"1|-|2\"}}{{/code}}");

        // Edit the second macro (the one with empty content but without arguments).
        editMacro(1);
        setFieldValue("pd-title-input", "|-|");
        setFieldValue("pd-content-input", "|-|");
        applyMacroChanges();

        // Edit the third macro (the one without content but with arguments).
        editMacro(2);
        setFieldValue("pd-title-input", "");
        setFieldValue("pd-content-input", "|-|");
        applyMacroChanges();

        // Edit the forth macro (the one with empty content and with arguments).
        editMacro(3);
        setFieldValue("pd-content-input", "|-|");
        applyMacroChanges();

        // Check the result.
        assertWiki("{{code}}|-|{{/code}}{{code title=\"|-|\"}}|-|{{/code}}"
            + "{{code}}|-|{{/code}}{{code title=\"1|-|2\"}}|-|{{/code}}");
    }

    /**
     * @see XWIKI-4085: Content duplicated if i have a macro (toc, id..) in an html macro.
     */
    public void testNestedMacrosAreNotDuplicated()
    {
        StringBuffer content = new StringBuffer();
        content.append("{{html wiki=\"true\"}}\n\n");
        content.append("= Hello title 1 =\n\n");
        content.append("{{toc start=\"2\"/}}\n\n");
        content.append("= Hello title 2 =\n\n");
        content.append("{{/html}}");
        setWikiContent(content.toString());

        // Check if only one macro was detected (which should be the top level macro).
        assertEquals(1, getMacroCount());

        // Check if the top level macro was correctly detected.
        deleteMacro(0);
        assertWiki("");

        // Reset the initial content.
        setWikiContent(content.toString());

        // Check if the nested macro is duplicated.
        assertWiki(content.toString());
    }

    /**
     * @see XWIKI-4155: Use double click or Enter to select the macro to insert.
     */
    public void testDoubleClickToSelectMacroToInsert()
    {
        openSelectMacroDialog();

        // We have to wait for the specified macro to be displayed on the dialog because the loading indicator is
        // removed just before the list of macros is displayed and the Selenium click command can interfere.
        waitForMacroListItem("Info Message");
        // Each double click event should be preceded by a click event.
        getSelenium().click(getMacroListItemLocator("Info Message"));
        // Fire the double click event.
        getSelenium().doubleClick(getMacroListItemLocator("Info Message"));
        waitForDialogToLoad();

        // Fill the macro content.
        setFieldValue("pd-content-input", "x");
        applyMacroChanges();

        // Check the result.
        assertWiki("{{info}}x{{/info}}");
    }

    /**
     * @see XWIKI-4155: Use double click or Enter to select the macro to insert.
     */
    public void testPressEnterToSelectMacroToInsert()
    {
        openSelectMacroDialog();

        // We have to wait for the specified macro to be displayed on the dialog because the loading indicator is
        // removed just before the list of macros is displayed and the Selenium click command can interfere.
        waitForMacroListItem("HTML");
        // Select a macro.
        getSelenium().click(getMacroListItemLocator("HTML"));
        // Press Enter to choose the selected macro.
        getSelenium().keyUp("//div[@class = 'xListBox']", "\\13");
        waitForDialogToLoad();

        // Fill the macro content.
        setFieldValue("pd-content-input", "a");
        applyMacroChanges();

        // Check the result.
        assertWiki("{{html}}a{{/html}}");
    }

    /**
     * @see XWIKI-4137: Pop up the "Edit macro properties" dialog when double-clicking on a macro block.
     */
    public void testDoubleClickToEditMacro()
    {
        // Insert two macros.
        setWikiContent("{{error}}x{{/error}}{{info}}y{{/info}}");

        // Double click to edit the second macro.
        // Each double click event should be preceded by a click event.
        clickMacro(1);
        // Fire the double click event on the macro.
        getSelenium().doubleClick(getMacroLocator(1));
        waitForDialogToLoad();

        // Fill the macro content.
        setFieldValue("pd-content-input", "z");
        applyMacroChanges();

        // Check the result.
        assertWiki("{{error}}x{{/error}}{{info}}z{{/info}}");
    }

    /**
     * @see XWIKI-3437: List macros by category/library
     */
    public void testSelectMacroFromCategory()
    {
        openSelectMacroDialog();

        // "All Macros" category should be selected by default.
        assertEquals("All Macros", getSelectedMacroCategory());

        // Make sure the "Code" and "Velocity" macros are present in "All Macros" category.
        waitForMacroListItem("Code");
        waitForMacroListItem("Velocity");

        // "Velocity" shouldn't be in the "Formatting" category.
        selectMacroCategory("Formatting");
        waitForMacroListItem("Code");
        assertElementNotPresent(getMacroListItemLocator("Velocity"));

        // "Code" shouldn't be in the "Development" category.
        selectMacroCategory("Development");
        waitForMacroListItem("Velocity");
        assertElementNotPresent(getMacroListItemLocator("Code"));

        // Select the "Velocity" macro.
        getSelenium().click(getMacroListItemLocator("Velocity"));
        getSelenium().click("//div[@class = 'xDialogFooter']/button[text() = 'Select']");
        waitForDialogToLoad();

        // Set the content field.
        setFieldValue("pd-content-input", "$xwiki.version");
        applyMacroChanges();

        // Open the "Select Macro" dialog again to see if its state was preserved.
        openSelectMacroDialog();
        assertEquals("Development", getSelectedMacroCategory());
        closeDialog();

        // Check the result.
        assertWiki("{{velocity}}$xwiki.version{{/velocity}}");
    }

    /**
     * @see XWIKI-4206: Add the ability to search in the list of macros
     */
    public void testFilterMacrosFromCategory()
    {
        openSelectMacroDialog();

        // Make sure the current category is "All Macros".
        selectMacroCategory("All Macros");

        // Check if "Velocity", "Footnote" and "Error Message" macros are present.
        waitForMacroListItem("Velocity");
        waitForMacroListItem("Footnote");
        waitForMacroListItem("Error Message");

        // Check if the filter can make a difference.
        int expectedMacroCountAfterFilterAllMacros = getMacroListItemCount("note");
        assertTrue(expectedMacroCountAfterFilterAllMacros < getMacroListItemCount());

        // Filter the macros.
        filterMacrosContaining("note");
        waitForMacroListItem("Footnote");

        // Check the number of macros.
        assertEquals(expectedMacroCountAfterFilterAllMacros, getMacroListItemCount());

        // Check what macros are present.
        assertElementNotPresent(getMacroListItemLocator("Velocity"));
        assertElementPresent(getMacroListItemLocator("Error Message"));

        // Check if the filter is preserved when switching the category.
        // Select the category of the "Footnote" macro.
        selectMacroCategory("Content");
        waitForMacroListItem("Footnote");
        assertElementNotPresent(getMacroListItemLocator("Error Message"));
        // Select the category of the "Error Message" macro.
        selectMacroCategory("Formatting");
        waitForMacroListItem("Error Message");
        assertElementNotPresent(getMacroListItemLocator("Footnote"));

        // Save the current macro list item count to be able to check if the filter state is preserved.
        int previousMacroListItemCount = getMacroListItemCount();

        // Select the "Error Message" macro.
        getSelenium().click(getMacroListItemLocator("Error Message"));
        getSelenium().click("//div[@class = 'xDialogFooter']/button[text() = 'Select']");
        waitForDialogToLoad();

        // Set the content field.
        setFieldValue("pd-content-input", "test");
        applyMacroChanges();

        // Open the "Select Macro" dialog again to see if the filter was preserved.
        openSelectMacroDialog();
        waitForMacroListItem("Error Message");
        assertEquals("note", getMacroFilterValue());
        assertEquals(previousMacroListItemCount, getMacroListItemCount());
        assertEquals(previousMacroListItemCount, getMacroListItemCount("note"));
        closeDialog();

        // Check the result.
        assertWiki("{{error}}test{{/error}}");
    }

    /**
     * @see XWIKI-3434: Use the dialog wizard for insert macro UI
     */
    public void testReturnToSelectMacroStep()
    {
        openSelectMacroDialog();

        // Filter the macros.
        selectMacroCategory("Development");
        filterMacrosContaining("script");

        // Select the "Groovy" macro.
        waitForMacroListItem("Groovy");
        getSelenium().click(getMacroListItemLocator("Groovy"));
        getSelenium().click("//div[@class = 'xDialogFooter']/button[text() = 'Select']");
        waitForDialogToLoad();

        // Return to the "Select Macro" step.
        getSelenium().click("//div[@class = 'xDialogContent']//button[text() = 'Previous']");
        waitForDialogToLoad();

        // Check if the state of the "Select Macro" dialog was preserved.
        assertEquals("Development", getSelectedMacroCategory());
        assertEquals("script", getMacroFilterValue());

        // Select a different macro.
        waitForMacroListItem("Velocity");
        getSelenium().click(getMacroListItemLocator("Velocity"));
        getSelenium().click("//div[@class = 'xDialogFooter']/button[text() = 'Select']");
        waitForDialogToLoad();

        // Set the content field.
        setFieldValue("pd-content-input", "$context.user");
        applyMacroChanges();

        // Check the result.
        assertWiki("{{velocity}}$context.user{{/velocity}}");
    }

    /**
     * Tests that the user can't move to the Edit Macro step without selection a macro first.
     */
    public void testValidateSelectMacroStep()
    {
        openSelectMacroDialog();
        // Wait for the list of macros to be filled.
        waitForMacroListItem("Velocity");
        // Make sure no macro is selected.
        assertFalse(isMacroListItemSelected());
        // The validation message should be hidden.
        assertFieldErrorIsNotPresent();
        // Try to move to the next step.
        getSelenium().click("//div[@class = 'xDialogFooter']/button[text() = 'Select']");
        // Check if the validation message is visible.
        assertFieldErrorIsPresent("Please select a macro from the list below.", MACRO_SELECTOR_LIST);

        // The validation message should be hidden when we change the macro category.
        selectMacroCategory("Navigation");
        waitForMacroListItem("Table Of Contents");
        // Make sure no macro is selected.
        assertFalse(isMacroListItemSelected());
        // The validation message should be hidden.
        assertFieldErrorIsNotPresent();
        // Try to move to the next step.
        getSelenium().click("//div[@class = 'xDialogFooter']/button[text() = 'Select']");
        // Check if the validation message is visible.
        assertFieldErrorIsPresent("Please select a macro from the list below.", MACRO_SELECTOR_LIST);

        // The validation message should be hidden when we filter the macros.
        filterMacrosContaining("anchor");
        waitForMacroListItem("Id");
        // Make sure no macro is selected.
        assertFalse(isMacroListItemSelected());
        // The validation message should be hidden.
        assertFieldErrorIsNotPresent();
        // Try to move to the next step.
        getSelenium().click("//div[@class = 'xDialogFooter']/button[text() = 'Select']");
        // Check if the validation message is visible.
        assertFieldErrorIsPresent("Please select a macro from the list below.", MACRO_SELECTOR_LIST);

        // The validation message should be hidden when we cancel the dialog.
        closeDialog();
        openSelectMacroDialog();
        assertFieldErrorIsNotPresent();
        // Make sure no macro is selected.
        assertFalse(isMacroListItemSelected());
        // Try to move to the next step.
        getSelenium().click("//div[@class = 'xDialogFooter']/button[text() = 'Select']");
        // Check if the validation message is visible.
        assertFieldErrorIsPresent("Please select a macro from the list below.", MACRO_SELECTOR_LIST);

        // Finally select a macro.
        getSelenium().click(getMacroListItemLocator("Id"));
        getSelenium().click("//div[@class = 'xDialogFooter']/button[text() = 'Select']");
        waitForDialogToLoad();

        // The validation message shouldn't be visible (we moved to the next step).
        assertFieldErrorIsNotPresent();

        // Set the name field.
        setFieldValue("pd-name-input", "foo");
        applyMacroChanges();

        // Check the result.
        assertWiki("{{id name=\"foo\"/}}");
    }

    /**
     * Tests if the user can select from the previously inserted macros.
     */
    public void testSelectFromPreviouslyInsertedMacros()
    {
        openSelectMacroDialog();

        // The "Previously Inserted Macros" category should be initially empty.
        selectMacroCategory("Previously Inserted Macros");
        assertEquals(0, getMacroListItemCount());

        // Insert a macro to see if it appears under the "Previously Inserted Macros" category.
        selectMacroCategory("All Macros");
        waitForMacroListItem("HTML");
        getSelenium().click(getMacroListItemLocator("HTML"));
        getSelenium().click("//div[@class = 'xDialogFooter']/button[text() = 'Select']");
        waitForDialogToLoad();
        setFieldValue("pd-content-input", "xwiki");
        applyMacroChanges();

        // Check if the inserted macro is listed under the "Previously Inserted Macros" category.
        openSelectMacroDialog();
        selectMacroCategory("Previously Inserted Macros");
        waitForMacroListItem("HTML");
        assertEquals(1, getMacroListItemCount());

        // Close the dialog and check the result.
        closeDialog();
        assertWiki("{{html}}xwiki{{/html}}");
    }

    /**
     * @see XWIKI-4415: Context document not set when refreshing macros.
     */
    public void testRefreshContextSensitiveVelocity()
    {
        setWikiContent("{{velocity}}$doc.fullName{{/velocity}}");
        String expected = getEval("window.XWE.body.textContent");
        refreshMacros();
        assertEquals(expected, getEval("window.XWE.body.textContent"));
    }

    /**
     * @param index the index of a macro inside the edited document
     * @return a {@link String} representing a DOM locator for the specified macro
     */
    public String getMacroLocator(int index)
    {
        return getDOMLocator("getElementsByTagName('button')[" + index + "]");
    }

    /**
     * @return the number of macros detected in the edited document
     */
    public int getMacroCount()
    {
        String expression = "window." + getDOMLocator("getElementsByTagName('button').length");
        return Integer.parseInt(getSelenium().getEval(expression));
    }

    /**
     * The macro place holder is shown when the macro is collapsed. In this state the output of the macro is hidden.
     * 
     * @param index the index of a macro inside the edited document
     * @return a {@link String} representing a DOM locator for the place holder of the specified macro
     */
    public String getMacroPlaceHolderLocator(int index)
    {
        return getMacroLocator(index) + ".childNodes[1]";
    }

    /**
     * The output of a macro is shown when the macro is expanded. In this state the macro place holder is hidden.
     * 
     * @param index the index of a macro inside the edited document
     * @return a {@link String} representing a DOM locator for the output of the specified macro
     */
    public String getMacroOutputLocator(int index)
    {
        return getMacroLocator(index) + ".childNodes[2]";
    }

    /**
     * Waits for the edited content to be refreshed.
     */
    public void waitForRefresh()
    {
        waitForCondition("window.document.getElementsByTagName('iframe')[0].previousSibling.className == 'xToolbar'");
        focusRichTextArea();
    }

    /**
     * Simulates a click on the macro with the specified index.
     * 
     * @param index the index of the macro to be clicked
     */
    public void clickMacro(int index)
    {
        String locator = getMacroLocator(index);
        getSelenium().mouseOver(locator);
        getSelenium().mouseMove(locator);
        getSelenium().mouseDown(locator);
        getSelenium().mouseUp(locator);
        getSelenium().click(locator);
        getSelenium().mouseMove(locator);
        getSelenium().mouseOut(locator);
    }

    /**
     * Opens the edit macro dialog to edit the specified macro.
     * 
     * @param index the index of the macro to be edited
     */
    public void editMacro(int index)
    {
        clickMacro(index);
        clickMenu(MENU_MACRO);
        clickMenu(MENU_EDIT);
        waitForDialogToLoad();
    }

    /**
     * Deletes the specified macro by selecting it and then pressing the Delete key.
     * 
     * @param index the index of the macro to be deleted
     */
    public void deleteMacro(int index)
    {
        clickMacro(index);
        typeDelete();
    }

    /**
     * Opens the insert macro dialog, chooses the specified macro and then opens the edit macro dialog to fill the
     * parameters of the selected macro.
     * 
     * @param macroName the name of the macro to insert
     */
    public void insertMacro(String macroName)
    {
        openSelectMacroDialog();

        // We have to wait for the specified macro to be displayed on the dialog because the loading indicator is
        // removed just before the list of macros is displayed and the Selenium click command can interfere.
        waitForMacroListItem(macroName);
        getSelenium().click(getMacroListItemLocator(macroName));
        getSelenium().click("//div[@class = 'xDialogFooter']/button[text() = 'Select']");
        waitForDialogToLoad();
    }

    /**
     * Applies the changes from the edit macro dialog.<br/>
     * NOTE: This method can be called after both edit and insert macro actions.
     */
    public void applyMacroChanges()
    {
        // The label of the finish button is "Apply" when we edit a macro and "Insert Macro" when we insert a macro.
        getSelenium().click("//div[@class = 'xDialogFooter']/button[text() = 'Apply' or text() = 'Insert Macro']");
        waitForRefresh();
    }

    /**
     * Refreshes the macros present in the edited document.
     */
    public void refreshMacros()
    {
        clickMenu(MENU_MACRO);
        clickMenu(MENU_REFRESH);
        waitForRefresh();
    }

    /**
     * Selects the specified macro category.
     * 
     * @param category the name of the macro category to select
     */
    public void selectMacroCategory(String category)
    {
        getSelenium().select(MACRO_CATEGORY_SELECTOR, category);
    }

    /**
     * @return the selected macro category
     */
    public String getSelectedMacroCategory()
    {
        return getSelenium().getSelectedLabel(MACRO_CATEGORY_SELECTOR);
    }

    /**
     * Waits for the specified macro to be displayed on the "Select Macro" dialog.
     * 
     * @param macroName the name of a macro
     */
    public void waitForMacroListItem(String macroName)
    {
        waitForCondition("selenium.isElementPresent(\"" + getMacroListItemLocator(macroName) + "\");");
    }

    /**
     * @param macroName a macro name
     * @return the selector for the specified macro in the list of macros from the "Select Macro" dialog
     */
    public String getMacroListItemLocator(String macroName)
    {
        return "//div[contains(@class, 'xListBox')]//div[text() = '" + macroName + "']";
    }

    /**
     * @return the number of macro list items on the "Select Macro" dialog.
     */
    public int getMacroListItemCount()
    {
        return getSelenium().getXpathCount("//div[contains(@class, 'xListItem xMacro')]").intValue();
    }

    /**
     * @param filter a text used to filter the macro list items
     * @return the number of macro list items containing the specified text
     */
    public int getMacroListItemCount(String filter)
    {
        return getSelenium().getXpathCount(
            "//div[contains(@class, 'xListItem xMacro') and contains(., '" + filter + "')]").intValue();
    }

    /**
     * Sets the value of the live filter to the given string.
     * 
     * @param filter the value to set to the live macro filter
     */
    public void filterMacrosContaining(String filter)
    {
        focus(MACRO_LIVE_FILTER_SELECTOR);
        getSelenium().typeKeys(MACRO_LIVE_FILTER_SELECTOR, filter);
    }

    /**
     * @return the value of the live macro filter
     */
    public String getMacroFilterValue()
    {
        return getSelenium().getValue(MACRO_LIVE_FILTER_SELECTOR);
    }

    /**
     * Opens the "Select Macro" dialog.
     */
    public void openSelectMacroDialog()
    {
        clickMenu(MENU_MACRO);
        assertTrue(isMenuEnabled(MENU_INSERT));
        clickMenu(MENU_INSERT);
        waitForDialogToLoad();
    }

    /**
     * @return {@code true} if there is a macro list item selected in the list of macros from the "Select Macro" dialog,
     *         {@code false} otherwise
     */
    public boolean isMacroListItemSelected()
    {
        return isElementPresent("//div[contains(@class, 'xMacro') and contains(@class, 'xListItem-selected')]");
    }
}
