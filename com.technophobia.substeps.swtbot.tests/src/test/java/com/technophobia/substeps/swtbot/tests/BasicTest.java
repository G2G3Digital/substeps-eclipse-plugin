package com.technophobia.substeps.swtbot.tests;

import static org.eclipse.swtbot.swt.finder.matchers.WidgetMatcherFactory.withMnemonic;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.results.Result;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotMenu;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTable;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTableItem;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTreeItem;
import org.eclipse.ui.PlatformUI;
import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class BasicTest {

	private static SWTWorkbenchBot	bot;
	private static SWTBotShell outerShell; 
	
	/*
	
	swtbot api
	http://download.eclipse.org/technology/swtbot/helios/dev-build/apidocs/
	
	// fire up an eclipse with the right plugin?
	 * 
	 * http://wiki.eclipse.org/SWTBot/Automate_test_execution - have a look at the maven tycho examples
	
	 */
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		
		bot = new SWTWorkbenchBot();
		
		outerShell = bot.activeShell();
		
		outerShell.activate();
		
	}
 
	private void setupSubStepsProject(){
		
		switchToJavaPerspective();
		
		// TODO - fix the path for this as it will only work on my computer :-)
		importProjectIntoWorkspace("test-substeps-project", "/home/ian/projects/github/substeps-eclipse-plugin/com.technophobia.substeps.swtbot.tests/vanilla_workspace/test-substeps-project");
		
		System.out.println("done setupSubStepsProject");
	}

	
    public Shell getActiveShell() {

        return UIThreadRunnable.syncExec(new WidgetResult<Shell>() {
            @Override
            public Shell run() {
            	Shell rtn = null;
            	
            	Shell active = PlatformUI.getWorkbench().getDisplay().getActiveShell();
            	
            	if (active == null)
            	{
            		Shell[] shells = PlatformUI.getWorkbench().getDisplay().getShells();
            		
            		for (Shell s : shells){
            			
            			if (s.isFocusControl()){
            				rtn = s;
            			}
            		}
            	}
            	else {
            		rtn = active;
            	}	
                return rtn;
            }
        });
    }
	
	private void importProjectIntoWorkspace(String projectName, String path){

		bot.menu("File").menu("Import...").click();		
		SWTBotTreeItem[] allItems = bot.tree().getAllItems();
		SWTBotTreeItem generalTreeItem = null;
		if (allItems != null)
		{
			for (SWTBotTreeItem it : allItems)
			{
				if (it.getText().equalsIgnoreCase("General")){
					generalTreeItem= it;
					break;
				}
			}
		}
		
		if (generalTreeItem != null)
		{
			generalTreeItem.expand();
			
			generalTreeItem.getNode(1).select();
			
			bot.button("Next >").click();

			bot.text().setText(path);
			
			bot.button("Refresh").click();
			
			bot.tree().getTreeItem(projectName + " (" + path + ")").select();
			
			bot.checkBox("Copy projects into workspace").click();
			bot.button("Finish").click();
		}
	}
	
	private void switchToJavaPerspective(){
		bot.menu("Window").menu("Open Perspective").menu("Java").click();
		Assert.assertEquals("Java", 
		bot.activePerspective().getLabel());
	}
	
	// bug when running via tycho - no classpath variables page..?
	private void setM2Variable(){
		
		bot.menu("Window").menu("Preferences").click();
		
		SWTBotTree tree = bot.tree();

		// Java / Build path / Classpath variables
		SWTBotTreeItem javaTreeItem = tree.getTreeItem("Java");
		javaTreeItem.expand();
		SWTBotTreeItem buildPathTreeItem = javaTreeItem.getNode("Build Path");
		buildPathTreeItem.expand();
		buildPathTreeItem.getNode("Classpath Variables").select();
		
		// see if there is an M2 variable already bound
		
		System.out.println("selected item in tree, should be on classpath vars page");
		
		SWTBotTable tableWithLabel = bot.tableWithLabel("Defined classpath variables:");
				
		System.out.println("table cols: " + 
		tableWithLabel.columnCount() + " rows: " +
		tableWithLabel.rowCount());
		
		System.out.println("contains m2_repo item: " + 
		tableWithLabel.containsItem("M2_REPO"));
		
		boolean m2RepoSet = false;
		
		for (int i = 0 ; i < tableWithLabel.rowCount() ; i++)
		{
			SWTBotTableItem tableItem = tableWithLabel.getTableItem(i);
			
			System.out.println("row: " + i + " text: " + 
			tableItem.getText());
			
			if (tableItem.getText().startsWith("M2_REPO")){
				m2RepoSet = true;
			}
			
		}
		
		if (!m2RepoSet)
		{
			bot.button("New...").click();
			bot.textWithLabel("Name:").setText("M2_REPO");
			bot.textWithLabel("Path:").setText("/home/ian/.m2/repository");
			
			bot.button("OK").click();
		}
		else
		{
			System.out.println("M2_REPO var already exists - bail");
		}
		bot.button("OK").click();

	}
	
	@Test
	public void testBasicEditorFunctionality() throws Exception {
		
		setupSubStepsProject();
		
		// TODO how to wait till we're up and running?
		bot.sleep(1000 * 10);
		
		// initial setup should have some errors
		testForInitialErrors();
		
		setupSubstepsProjectForMavenPattern("test-substeps-project");

		cleanAndBuildProject("test-substeps-project");
		
		bot.sleep(1000 * 5);
		
		testForNoErrors();
		
		String[] feature = new String[] {"src/test/resources", "features", "plugin-tests-sample.feature"};
		runFeature("test-substeps-project", feature);
		bot.sleep(1000 * 5);

		// TODO ....
		// WIP - how to check something running / passed ?
		checkSubtepsView();

		
		bot.sleep(1000 * 60 * 10);
	}

	
	private void checkSubtepsView() {
		
		SWTBotView viewByTitle = bot.viewByTitle("Substeps");
		
		viewByTitle.setFocus();
				
		SWTBotTree subStepsTree = viewByTitle.bot().tree();
		
		SWTBotTreeItem[] allItems = subStepsTree.getAllItems();
		SWTBotTreeItem substepsTreeItem = null;
		
		for (SWTBotTreeItem item : allItems){
			System.out.println("substeps tree item: " + 
			item.getText());
			
			if (item.getText().contains("Features")){
				item.expand();
				substepsTreeItem = item;
			}
		}	
		
		Assert.assertNotNull(substepsTreeItem);
		
		
		List<String> nodes = substepsTreeItem.getNodes();
		
		System.out.println("nodes size: " + nodes.size());
		
//		Assert.assertTrue(nodes.size() == 3);
		
		for (String s : nodes){
			System.out.println("substep node: " + s);
			
		}
		FeatureTreeNode rootNode = new FeatureTreeNode ("Features");
		FeatureTreeNode featureNode = rootNode.addChildNode("A feature to write some scenarios for the plugin tests");

		FeatureTreeNode scenarioNode = featureNode.addChildNode("a test scenario");
		
		scenarioNode.addChildNode("Given I can call method one with <param>").addChildNode("CallMethod One with parameter \"param\"");
		scenarioNode.addChildNode("And I can call a method that doesn't exist yet").addChildNode("CallMethod four");
		scenarioNode.addChildNode("Then I can do something else").addChildNode("CallMethod Two");
		/*

		feature tree:
		Features [.....
			A feature to write some ...
				a test scenario
					Given I can call method one with <param>
						CallMethod One with parameter "param"
					And I can call a method that doesn't exist yet
						CallMethod four
					Then I can do something else
						CallMethod Two	

		console ends with DefinableFeatureTest notifyTestFinished

		 */

		recurseAndcheckSWTBotTree(substepsTreeItem, rootNode);		

		
		
//		Assert.assertTrue(
//		substepsTreeItem.getNode(0).getText().startsWith("Duplicate pattern detected: Pattern [And I can call a method that doesn't exist yet] is defined"));
//
//		Assert.assertTrue(
//		substepsTreeItem.getNode(1).getText().startsWith("Duplicate pattern detected: Pattern [Given I can call method one with \"?([^\"]*)\"?] is defined in"));
//
//		Assert.assertTrue(
//		substepsTreeItem.getNode(2).getText().startsWith("Duplicate pattern detected: Pattern [Then I can do something else]"));

	
	}

	/**
	 * a representation of a feature / scenario / step / substep tree
	 * @author ian
	 *
	 */
	private static class FeatureTreeNode
	{
		private final String text;
		private List<FeatureTreeNode> children;

		public FeatureTreeNode(String text){
			this.text = text;
		}
		
		public FeatureTreeNode addChildNode(String text){
			
			FeatureTreeNode child = new FeatureTreeNode(text);
			
			if (this.children == null)
			{	
				this.children = new ArrayList<FeatureTreeNode>();
			}
			this.children.add(child);
			return child;
		}
		
		public boolean hasChildren(){
			return this.children != null && !this.children.isEmpty();
		}

		public FeatureTreeNode getChild(int i) {

			FeatureTreeNode child = null;
			
			if (children != null && i < children.size())
			{
				child = children.get(i);
			}
			return child;
		}
	}
	
	
	private void recurseAndcheckSWTBotTree(SWTBotTreeItem swtNode, FeatureTreeNode treeNode) {
		
		if (swtNode != null){
			
			System.out.println("node text: "  + swtNode.getText() + " expecting: " + treeNode.text);
			
			Assert.assertTrue(swtNode.getText().startsWith(treeNode.text));
			
			if (swtNode.getNodes() != null && !swtNode.getNodes().isEmpty()){
				
				for (int i = 0 ; i < swtNode.getNodes().size(); i++){
					
					SWTBotTreeItem child = swtNode.getNode(i);
					child.expand();
					recurseAndcheckSWTBotTree(child, treeNode.getChild(i));
				}
			}
		}
		else if (treeNode != null){
			Assert.fail("expecting an swt not tree node item with text: " + treeNode.text);
		}
	}

	
	
	private void runFeature(String projectName, String[] path){
		
		SWTBotTreeItem projectTreeItem = bot.tree().getTreeItem(projectName);
		
		SWTBotTreeItem currentParent = projectTreeItem;
		for (String pathElement : path){
			System.out.println("finding path element : " + pathElement);
			
			// expand the current parent before looking beneath
			currentParent.expand();
			
			currentParent = currentParent.getNode(pathElement);
			
			Assert.assertNotNull("failed to find path element: " + pathElement, currentParent);
		}
		currentParent.select();
		
		
		String actualRunMenuText = UIThreadRunnable.syncExec( new Result<String>() {
            @Override
            public String run() {
	
				MenuItem[] items = bot.menu("Run As").widget.getMenu().getItems();
				
				Assert.assertNotNull(items);
				String actualRunMenuText = null;
				for (MenuItem menuItem : items){
					
					if (menuItem.getText().contains("Substeps Feature Test")){
						
						actualRunMenuText = menuItem.getText();
						break;
					}
				}
				return actualRunMenuText;
            }
		});
		
		Assert.assertNotNull("failed to find the actual run as menu text", actualRunMenuText);
		bot.menu("Run As").menu(actualRunMenuText).click();
		
//		bot.menu("Run As").menu("2 Substeps Feature Test").click();
		//bot.menu("2 Substeps Feature Test").click();
	
	}
	
	

	// copied from http://www.eclipse.org/forums/index.php/m/723219/ and http://www.eclipse.org/forums/index.php/t/11863/
	
	  /**
	   * Clicks the context menu matching the text.
	   * 
	   * @param text
	   *          the text on the context menu.
	   * @throws WidgetNotFoundException
	   *           if the widget is not found.
	   */
	public static void clickContextMenu(final SWTBotShell shell,
			final String... texts) {
		// fetch
		final MenuItem menuItem = getContextMenu(shell, texts);

		// click
		click(menuItem);
		
		// hide
		UIThreadRunnable.syncExec(new VoidResult() {
			public void run() {
				hide(menuItem.getParent());
			}
		});
	}

	private static void click(final MenuItem menuItem) {
		final Event event = new Event();
		event.time = (int) System.currentTimeMillis();
		event.widget = menuItem;
		event.display = menuItem.getDisplay();
		event.type = SWT.Selection;

		UIThreadRunnable.asyncExec(menuItem.getDisplay(), new VoidResult() {
		public void run() {
		menuItem.notifyListeners(SWT.Selection, event);
		}
		});
		}
	
	private static void hide(final Menu menu) {
		menu.notifyListeners(SWT.Hide, new Event());
		if (menu.getParentMenu() != null) {
		hide(menu.getParentMenu());
		}
		}
		
	private static MenuItem show(final Menu menu, final Matcher<?> matcher) {
		if (menu != null) {
		menu.notifyListeners(SWT.Show, new Event());
		MenuItem[] items = menu.getItems();
		for (final MenuItem menuItem : items) {
		if (matcher.matches(menuItem)) {
		return menuItem;
		}
		}
		menu.notifyListeners(SWT.Hide, new Event());
		}
		return null;
		}
	
	private static MenuItem getContextMenu(final SWTBotShell shell,
			final String... texts) {
		final MenuItem menuItem = getContextMenuImpl(shell, texts);
		if (menuItem == null) {
			throw new WidgetNotFoundException("Could not find menu: " + texts);
//					+ computeMenuTrace(texts));
		}
		return menuItem;
	}

	private static MenuItem getContextMenuImpl(final SWTBotShell shell,
			final String... texts) {
		//traceMenu(texts);

		// show
		final MenuItem menuItem = UIThreadRunnable
				.syncExec(new WidgetResult<MenuItem>() {
					public MenuItem run() {
						MenuItem menuItem = null;
						Menu menu = shell.widget.getMenuBar();
						for (String text : texts) {
							@SuppressWarnings("unchecked")
							Matcher<?> matcher = allOf(
									instanceOf(MenuItem.class),
									withMnemonic(text));
							menuItem = show(menu, matcher);
							if (menuItem != null) {
								menu = menuItem.getMenu();
							} else {
								hide(menu);
								break;
							}
						}

						return menuItem;
					}
				});
		return menuItem;
	}	
	
	
	
	
	
 
	private void cleanAndBuildProject(String projectName) {

		System.out.println("cleanAndBuildProject: " + projectName);
				
	    SWTBotMenu menu = new SWTBotMenu(getContextMenu(bot.activeShell(), "Project", "Clean..."));
	    menu.click();
	    bot.shell("Clean").activate();
	    bot.button().click();
	    
		System.out.println("cleanAndBuildProject: " + projectName + " done");
	}
	
	private void testForNoErrors(){

		SWTBotView viewByTitle = bot.viewByTitle("Problems");
		
		viewByTitle.setFocus();
				
		SWTBotTree problemsTree = viewByTitle.bot().tree();

		SWTBotTreeItem[] allItems = problemsTree.getAllItems();
		
		for (SWTBotTreeItem item : allItems){
			
			Assert.assertFalse(item.getText().contains("Errors"));
		}
	}
	
	private void testForInitialErrors() {

		SWTBotView viewByTitle = bot.viewByTitle("Problems");
		
		viewByTitle.setFocus();
				
		SWTBotTree problemsTree = viewByTitle.bot().tree();
		
		SWTBotTreeItem[] allItems = problemsTree.getAllItems();
		SWTBotTreeItem errorsTreeItem = null;
		
		for (SWTBotTreeItem item : allItems){
			System.out.println("probs view tree item: " + 
			item.getText());
			
			if (item.getText().contains("Errors")){
				item.expand();
				errorsTreeItem = item;
			}
		}	
		
		Assert.assertNotNull(errorsTreeItem);
		
		List<String> nodes = errorsTreeItem.getNodes();
		
		System.out.println("nodes size: " + nodes.size());
		
		Assert.assertTrue(nodes.size() == 3);
		
		for (String s : nodes){
			System.out.println("error node: " + s);
		}
		
		Assert.assertTrue(
		errorsTreeItem.getNode(0).getText().startsWith("Duplicate pattern detected: Pattern [And I can call a method that doesn't exist yet] is defined"));

		Assert.assertTrue(
		errorsTreeItem.getNode(1).getText().startsWith("Duplicate pattern detected: Pattern [Given I can call method one with \"?([^\"]*)\"?] is defined in"));

		Assert.assertTrue(
		errorsTreeItem.getNode(2).getText().startsWith("Duplicate pattern detected: Pattern [Then I can do something else]"));

	}


	private void setupSubstepsProjectForMavenPattern(String projectName) {

		bot.tree().getTreeItem(projectName).select();
		bot.menu("Properties").click();
		bot.text().setText("Substeps");
		
		bot.tree().getTreeItem("Substeps").select();
		
		bot.checkBox("Enable project specific settings").click();
		bot.textWithLabel("Feature folder").setText("src/test/resources/features");
		
		bot.textWithLabel("Substeps folder").setText("src/test/resources/substeps");
		bot.button("Apply").click();
		bot.button("OK").click();
		
	}


	@AfterClass
	public static void sleep() {
	//	bot.sleep(2000);
		System.out.println("closing all shells");
		bot.closeAllShells();
	}

	private void createSomeSubsteps(){
		
		// Generated code goes there
		bot.tree().getTreeItem("test-substeps-project").select();
		bot.text().setText("");
		bot.tree().getTreeItem("Substeps").select();
		bot.checkBox("Enable project specific settings").click();
		bot.checkBox("Show substeps problems").click();
		bot.button("Browse").click();
		bot.tree().getTreeItem(".classpath").select();
		bot.tree().getTreeItem("src").expand();
		bot.tree().getTreeItem("src").getNode("test").expand();
		bot.tree().getTreeItem("src").getNode("test").getNode("resources").expand();
		bot.tree().getTreeItem("src").getNode("test").getNode("resources").getNode("features").expand();
		bot.tree().getTreeItem("src").getNode("test").getNode("resources").getNode("features").select();
		bot.button("OK").click();
		bot.button("Browse").click();
		bot.tree().getTreeItem(".classpath").select();
		bot.tree().getTreeItem("src").expand();
		bot.tree().getTreeItem("src").getNode("test").expand();
		bot.tree().getTreeItem("src").getNode("test").getNode("resources").expand();
		bot.tree().getTreeItem("src").getNode("test").getNode("resources").getNode("substeps").select();
		bot.button("OK").click();
		bot.button("Apply").click();
		bot.button("OK").click();
		bot.tree().getTreeItem("test-substeps-project").expand();
		bot.tree().getTreeItem("test-substeps-project").getNode("src/test/resources").expand();
		bot.tree().getTreeItem("test-substeps-project").getNode("src/test/resources").getNode("substeps").expand();
		bot.tree().getTreeItem("test-substeps-project").getNode("src/test/resources").getNode("features").expand();
		bot.tree().getTreeItem("test-substeps-project").getNode("src/test/resources").getNode("substeps").getNode("plugin-tests-sample.substeps").select();
		bot.tree().getTreeItem("test-substeps-project").getNode("src/test/resources").getNode("substeps").getNode("plugin-tests-sample.substeps").select();
		bot.tree().getTreeItem("test-substeps-project").getNode("src/test/java").expand();
		bot.tree().getTreeItem("test-substeps-project").getNode("src/test/java").getNode("com.technophobia.plugin.substeps").expand();
		bot.tree().getTreeItem("test-substeps-project").select();
		bot.menu("Project").menu("Clean...").click();
		bot.button("OK").click();
		bot.tree().getTreeItem("test-substeps-project").getNode("src/test/java").getNode("com.technophobia.plugin.substeps").getNode("PluginTestSubstepImplementations.java").select();
		bot.tree().getTreeItem("test-substeps-project").getNode("src/test/java").getNode("com.technophobia.plugin.substeps").getNode("PluginTestSubstepImplementations.java").select();
		bot.tree().getTreeItem("test-substeps-project").getNode("src/test/resources").getNode("substeps").getNode("plugin-tests-sample.substeps").select();
		bot.tree().getTreeItem("test-substeps-project").getNode("src/test/resources").getNode("substeps").getNode("plugin-tests-sample.substeps").select();
		
		
		bot.tree().getTreeItem("test-substeps-project").getNode("src/test/resources").getNode("features").getNode("plugin-tests-sample.feature").select();
		bot.tree().getTreeItem("test-substeps-project").getNode("src/test/resources").getNode("features").getNode("plugin-tests-sample.feature").select();
		bot.menu("Run As").menu("2 Substeps Feature Test").click();
		bot.menu("2 Substeps Feature Test").click();
		bot.button("Details >>").click();
		bot.button("OK").click();
		
		
	}

	/*

setting prefs

seeing errors

// Generated code goes there
bot.button("No").click();
bot.tree().getTreeItem("test-substeps-project").select();
bot.text().setText("");
bot.tree().getTreeItem("Substeps").select();
bot.checkBox("Enable project specific settings").click();
bot.button("Apply").click();
bot.button("OK").click();
bot.tree().getTreeItem("Errors (3 items)").select();
bot.tree().getTreeItem("Errors (3 items)").expand();
bot.tree().getTreeItem("Errors (3 items)").getNode("Step "CallMethod four" is not defined").select();
bot.tree().getTreeItem("Errors (3 items)").getNode("Step "CallMethod One with parameter "param"" is not defined").select();
bot.tree().getTreeItem("Errors (3 items)").getNode("Step "CallMethod Two" is not defined").select();
bot.tree().getTreeItem("test-substeps-project").select();
bot.menu("Properties").click();
bot.text().setText("");
bot.checkBox("Enable project specific settings").click();
bot.text().setText("src/test/resources/features");
bot.text(1).setText("src/test/resources/substeps");
bot.button("Apply").click();
bot.button("OK").click();


	 */
	
	
	
	
	/*

recorded code via the swt bot plugin

// Generated code goes there
bot.tree().getTreeItem("substeps-junit-runner").expand();
bot.tree().getTreeItem("substeps-junit-runner").getNode("src/main/java").expand();
bot.tree().getTreeItem("substeps-junit-runner").getNode("src/main/java").getNode("com.technophobia.substeps.runner").expand();
bot.tree().getTreeItem("substeps-junit-runner").select();
bot.tree().getTreeItem("webdriver-substeps [substeps-webdriver master]").select();
bot.text().setText("");
bot.tree().getTreeItem("Substeps").select();
bot.checkBox("Enable project specific settings").click();
bot.button("Browse").click();
bot.tree().getTreeItem(".classpath").select();
bot.tree().getTreeItem("src").expand();
bot.tree().getTreeItem("src").getNode("test").expand();
bot.tree().getTreeItem("src").getNode("test").getNode("resources").expand();
bot.tree().getTreeItem("src").getNode("test").getNode("resources").getNode("features").select();
bot.button("OK").click();
bot.button("Browse").click();
bot.tree().getTreeItem(".classpath").select();
bot.tree().getTreeItem("src").expand();
bot.tree().getTreeItem("src").getNode("test").expand();
bot.tree().getTreeItem("src").getNode("test").getNode("resources").expand();
bot.tree().getTreeItem("src").getNode("test").getNode("resources").getNode("substeps").select();
bot.button("OK").click();
bot.button("Apply").click();
bot.button("OK").click();
bot.tree().getTreeItem("webdriver-substeps [substeps-webdriver master]").expand();
bot.tree().getTreeItem("webdriver-substeps [substeps-webdriver master]").getNode("src/test/resources").expand();
bot.tree().getTreeItem("webdriver-substeps [substeps-webdriver master]").getNode("src/test/resources").getNode("features").expand();
bot.tree().getTreeItem("webdriver-substeps [substeps-webdriver master]").getNode("src/test/resources").getNode("features").getNode("self-test.feature").select();
bot.menu("Run As").menu("2 Substeps Feature Test").click();
bot.menu("2 Substeps Feature Test").click();
bot.tree().getTreeItem("Features [Runner: org.eclipse.jdt.junit.loader.junit4] (6.473 s)").select();
bot.tree().getTreeItem("Features [Runner: org.eclipse.jdt.junit.loader.junit4] (6.473 s)").expand();
bot.tree().getTreeItem("Features [Runner: org.eclipse.jdt.junit.loader.junit4] (6.473 s)").getNode("A feature to self test the webdriver substeps implementations (6.473 s)").expand();
bot.tree().getTreeItem("webdriver-substeps [substeps-webdriver master]").getNode("src/test/resources").getNode("features").getNode("self-test.feature").select();
bot.tree().getTreeItem("webdriver-substeps [substeps-webdriver master]").getNode("src/test/resources").getNode("features").getNode("self-test.feature").select();
bot.tree().getTreeItem("webdriver-substeps [substeps-webdriver master]").getNode("src/test/resources").getNode("features").getNode("self-test.feature").select();
bot.menu("Run As").menu("2 Substeps Feature Test").click();
bot.menu("2 Substeps Feature Test").click();
bot.button("OK").click();
bot.tree().getTreeItem("Features [Runner: org.eclipse.jdt.junit.loader.junit4] (5.763 s)").select();
bot.tree().getTreeItem("Features [Runner: org.eclipse.jdt.junit.loader.junit4] (5.763 s)").expand();
bot.tree().getTreeItem("Features [Runner: org.eclipse.jdt.junit.loader.junit4] (5.763 s)").getNode("A feature to self test the webdriver substeps implementations (5.763 s)").expand();
bot.tree().getTreeItem("Features [Runner: org.eclipse.jdt.junit.loader.junit4] (5.763 s)").getNode("A feature to self test the webdriver substeps implementations (5.763 s)").getNode("a scenario (5.763 s)").expand();
bot.tree().getTreeItem("Features [Runner: org.eclipse.jdt.junit.loader.junit4] (5.763 s)").getNode("A feature to self test the webdriver substeps implementations (5.763 s)").select();
bot.tree().getTreeItem("Features [Runner: org.eclipse.jdt.junit.loader.junit4] (5.763 s)").getNode("A feature to self test the webdriver substeps implementations (5.763 s)").getNode("a scenario (5.763 s)").select();
bot.tree().getTreeItem("Features [Runner: org.eclipse.jdt.junit.loader.junit4] (5.763 s)").getNode("A feature to self test the webdriver substeps implementations (5.763 s)").getNode("a scenario (5.763 s)").getNode("Given I go to the self test page (0.143 s)").expand();
bot.tree().getTreeItem("Features [Runner: org.eclipse.jdt.junit.loader.junit4] (5.763 s)").getNode("A feature to self test the webdriver substeps implementations (5.763 s)").getNode("a scenario (5.763 s)").getNode("Given I go to the self test page (0.143 s)").getNode("NavigateTo /self-test.html (0.143 s)").select();



	 */
}
