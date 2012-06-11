package com.technophobia.substeps.editor.steps;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.VoidResult;
import org.eclipse.ui.PlatformUI;

import com.technophobia.substeps.editor.SWTTestUtil;
import com.technophobia.substeps.runner.JunitFeatureRunner.BeforeEveryScenario;

public class SWTBotInitialiser {

	private static SWTBot bot;
	private static SWTWorkbenchBot workbenchBot;

	@BeforeEveryScenario
	public void initialise() {
		bot = new SWTBot();
		workbenchBot = new SWTWorkbenchBot();

		prepareActiveShell();
	}

	@BeforeEveryScenario
	public void setUp() throws Exception {
		UIThreadRunnable.syncExec(new VoidResult() {

			@Override
			public void run() {
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
						.forceFocus();
				PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell()
						.forceActive();
				PlatformUI.getWorkbench().getDisplay().getActiveShell();
			}
		});
	}

	public static SWTBot bot() {
		return bot;
	}

	public static SWTWorkbenchBot workbenchBot() {
		return workbenchBot;
	}

	private void prepareActiveShell() {
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				final Shell shell = bot().getDisplay().getShells()[0];

				SWTTestUtil.setActiveShellHack(shell);

			}
		});
	}
}
