package com.technophobia.substeps.test.steps;

import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.exceptions.WidgetNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.test.controller.ProjectSWTController;

public class BasicWorkspaceSteps extends AbstractSteps {

    private static final Logger LOG = LoggerFactory.getLogger(BasicWorkspaceSteps.class);


    @Step("Given the \"([^\"]*)\" view is not visible")
    public void ensureViewIsHidden(final String view) {
        LOG.debug("Given the " + view + " view is not visible");
        final SWTWorkbenchBot workbenchBot = new SWTWorkbenchBot();
        try {
            final SWTBotView activeView = workbenchBot.activeView();
            if (view.equals(activeView.getTitle())) {
                workbenchBot.viewByTitle(view).close();
            }
        } catch (final WidgetNotFoundException ex) {
            // no view is active, no need to do anything
        }
    }


    @Step("Given I am working on the \"([^\"]*)\" Perspective")
    public void navigateToPerspective(final String perspectiveName) {
        LOG.debug("Given I am working on the " + perspectiveName + " Perspective");

        final ProjectSWTController controller = createController(ProjectSWTController.class);
        controller.navigateToPerspective(perspectiveName);
    }


    @Step("Given there is a general project named \"([^\"]*)\"")
    public void ensureProjectExistsNamed(final String projectName) {
        LOG.debug("Given there is a general project named " + projectName);

        final ProjectSWTController controller = createController(ProjectSWTController.class);

        if (!controller.isProjectExist(projectName)) {
            controller.createGeneralProject(projectName);
        }
    }
}
