package com.technophobia.substeps.editor.steps;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.finders.UIThreadRunnable;
import org.eclipse.swtbot.swt.finder.results.WidgetResult;
import org.eclipse.ui.PlatformUI;

import com.technophobia.substeps.runner.setupteardown.Annotations.AfterEveryFeature;
import com.technophobia.substeps.runner.setupteardown.Annotations.BeforeAllFeatures;
import com.technophobia.substeps.runner.setupteardown.Annotations.BeforeEveryFeature;

public class SWTBotInitialiser {

    private static SWTWorkbenchBot bot;
    private static ShellStack shellStack = new ShellStack();


    @BeforeAllFeatures
    public void initialise() {
        bot = new SWTWorkbenchBot();
    }


    @BeforeEveryFeature
    public void setUp() {
        final Shell platformShell = UIThreadRunnable.syncExec(new WidgetResult<Shell>() {

            @Override
            public Shell run() {
                return PlatformUI.getWorkbench().getDisplay().getActiveShell();

            }
        });
        shellStack.resetTo(platformShell);
    }


    @AfterEveryFeature
    public void tearDown() {
        shellStack.clear();
    }


    public static SWTWorkbenchBot bot() {
        return bot;
    }


    public static ShellStack shellStack() {
        return shellStack;
    }

}
