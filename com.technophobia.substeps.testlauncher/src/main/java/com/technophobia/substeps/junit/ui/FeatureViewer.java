package com.technophobia.substeps.junit.ui;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.internal.junit.model.TestCaseElement;
import org.eclipse.jdt.internal.junit.model.TestElement;
import org.eclipse.jdt.internal.junit.model.TestElement.Status;
import org.eclipse.jdt.internal.junit.model.TestRoot;
import org.eclipse.jdt.internal.junit.model.TestRunSession;
import org.eclipse.jdt.internal.junit.model.TestSuiteElement;
import org.eclipse.jdt.internal.junit.ui.JUnitMessages;
import org.eclipse.jdt.internal.junit.ui.TestSessionTableContentProvider;
import org.eclipse.jdt.internal.junit.ui.TestSessionTreeContentProvider;
import org.eclipse.jdt.internal.ui.viewsupport.ColoringLabelProvider;
import org.eclipse.jdt.internal.ui.viewsupport.SelectionProviderMediator;
import org.eclipse.jdt.junit.model.ITestElement;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.part.PageBook;

import com.technophobia.eclipse.transformer.Supplier;
import com.technophobia.eclipse.ui.Notifier;
import com.technophobia.eclipse.ui.view.ViewLayout;
import com.technophobia.substeps.junit.action.OpenFeatureAction;
import com.technophobia.substeps.junit.action.RerunTestAction;
import com.technophobia.substeps.junit.ui.label.TestSessionLabelProvider;

public class FeatureViewer {
    private final class TestSelectionListener implements ISelectionChangedListener {
        @Override
        public void selectionChanged(final SelectionChangedEvent event) {
            handleSelected();
        }
    }

    private final class TestOpenListener extends SelectionAdapter {
        @Override
        public void widgetDefaultSelected(final SelectionEvent e) {
            handleDefaultSelected();
        }
    }

    private final class FailuresOnlyFilter extends ViewerFilter {
        @Override
        public boolean select(final Viewer viewer, final Object parentElement, final Object element) {
            return select(((TestElement) element));
        }


        public boolean select(final TestElement testElement) {
            final Status status = testElement.getStatus();
            if (status.isErrorOrFailure())
                return true;
            else
                return !testRunSession.isRunning() && status == Status.RUNNING; // rerunning
        }
    }

    private static class ReverseList<E> extends AbstractList<E> {
        private final List<E> fList;


        public ReverseList(final List<E> list) {
            fList = list;
        }


        @Override
        public E get(final int index) {
            return fList.get(fList.size() - index - 1);
        }


        @Override
        public int size() {
            return fList.size();
        }
    }

    private class ExpandAllAction extends Action {
        public ExpandAllAction() {
            setText(JUnitMessages.ExpandAllAction_text);
            setToolTipText(JUnitMessages.ExpandAllAction_tooltip);
        }


        @Override
        public void run() {
            treeViewer.expandAll();
        }
    }

    private final FailuresOnlyFilter failuresOnlyFilter = new FailuresOnlyFilter();

    private final Clipboard clipboard;

    private PageBook viewerbook;
    private TreeViewer treeViewer;
    private TestSessionTreeContentProvider treeContentProvider;
    private TestSessionLabelProvider treeLabelProvider;
    private TableViewer tableViewer;
    private TestSessionTableContentProvider tableContentProvider;
    private TestSessionLabelProvider tableLabelProvider;
    private SelectionProviderMediator selectionProvider;

    private ViewLayout layoutMode;
    private boolean treeHasFilter;
    private boolean tableHasFilter;

    private TestRunSession testRunSession;

    private boolean treeNeedsRefresh;
    private boolean tableNeedsRefresh;
    private HashSet<TestElement> needUpdate;
    private TestCaseElement autoScrollTarget;

    private LinkedList<TestSuiteElement> autoClose;
    private HashSet<TestSuiteElement> autoExpand;

    private final Supplier<IWorkbenchPartSite> siteSupplier;
    private final Notifier<Boolean> autoScrollSupplier;
    private final Notifier<TestElement> failedTestNotifier;
    private final Supplier<String> testKindDisplayNameSupplier;
    private final SubstepsIconProvider iconProvider;
    private final Notifier<TestContext> testRunner;


    public FeatureViewer(final Composite parent, final Clipboard clipboard,
            final Supplier<IWorkbenchPartSite> siteSupplier, final Notifier<TestElement> failedTestNotifier,
            final Notifier<TestContext> testRunner, final Notifier<Boolean> autoScrollSupplier,
            final Supplier<String> testKindDisplayNameSupplier, final SubstepsIconProvider iconProvider) {
        this.clipboard = clipboard;
        this.siteSupplier = siteSupplier;
        this.failedTestNotifier = failedTestNotifier;
        this.testRunner = testRunner;
        this.autoScrollSupplier = autoScrollSupplier;
        this.testKindDisplayNameSupplier = testKindDisplayNameSupplier;
        this.iconProvider = iconProvider;

        this.layoutMode = ViewLayout.HIERARCHICAL;

        createTestViewers(parent);

        registerViewersRefresh();

        initContextMenu();
    }


    private void createTestViewers(final Composite parent) {
        viewerbook = new PageBook(parent, SWT.NULL);

        treeViewer = new TreeViewer(viewerbook, SWT.V_SCROLL | SWT.SINGLE);
        treeViewer.setUseHashlookup(true);
        treeContentProvider = new TestSessionTreeContentProvider();
        treeViewer.setContentProvider(treeContentProvider);
        treeLabelProvider = new TestSessionLabelProvider(testKindDisplayNameSupplier, iconProvider,
                ViewLayout.HIERARCHICAL);
        treeViewer.setLabelProvider(new ColoringLabelProvider(treeLabelProvider));

        tableViewer = new TableViewer(viewerbook, SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE);
        tableViewer.setUseHashlookup(true);
        tableContentProvider = new TestSessionTableContentProvider();
        tableViewer.setContentProvider(tableContentProvider);
        tableLabelProvider = new TestSessionLabelProvider(testKindDisplayNameSupplier, iconProvider, ViewLayout.FLAT);
        tableViewer.setLabelProvider(new ColoringLabelProvider(tableLabelProvider));

        selectionProvider = new SelectionProviderMediator(new StructuredViewer[] { treeViewer, tableViewer },
                treeViewer);
        selectionProvider.addSelectionChangedListener(new TestSelectionListener());
        final TestOpenListener testOpenListener = new TestOpenListener();
        treeViewer.getTree().addSelectionListener(testOpenListener);
        tableViewer.getTable().addSelectionListener(testOpenListener);

        siteSupplier.get().setSelectionProvider(selectionProvider);

        viewerbook.showPage(treeViewer.getTree());
    }


    private void initContextMenu() {
        final MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            @Override
            public void menuAboutToShow(final IMenuManager manager) {
                handleMenuAboutToShow(manager);
            }
        });
        siteSupplier.get().registerContextMenu(menuMgr, selectionProvider);
        final Menu menu = menuMgr.createContextMenu(viewerbook);
        treeViewer.getTree().setMenu(menu);
        tableViewer.getTable().setMenu(menu);
    }


    void handleMenuAboutToShow(final IMenuManager manager) {
        final IStructuredSelection selection = (IStructuredSelection) selectionProvider.getSelection();
        if (!selection.isEmpty()) {
            final TestElement testElement = (TestElement) selection.getFirstElement();

            final String testLabel = testElement.getTestName();
            final String className = testElement.getClassName();
            if (testElement instanceof TestSuiteElement) {
                manager.add(new OpenFeatureAction());
                manager.add(new Separator());
                if (testClassExists(className) && !testRunSession.isKeptAlive()) {
                    manager.add(new RerunTestAction(JUnitMessages.RerunAction_label_run, testRunner, testElement
                            .getId(), className, null, ILaunchManager.RUN_MODE));
                    manager.add(new RerunTestAction(JUnitMessages.RerunAction_label_debug, testRunner, testElement
                            .getId(), className, null, ILaunchManager.DEBUG_MODE));
                }
            } else {
                final TestCaseElement testCaseElement = (TestCaseElement) testElement;
                final String testMethodName = testCaseElement.getTestMethodName();
                manager.add(new OpenFeatureAction());
                manager.add(new Separator());
                if (testRunSession.isKeptAlive()) {
                    manager.add(new RerunTestAction(JUnitMessages.RerunAction_label_rerun, testRunner, testElement
                            .getId(), className, testMethodName, ILaunchManager.RUN_MODE));

                } else {
                    manager.add(new RerunTestAction(JUnitMessages.RerunAction_label_run, testRunner, testElement
                            .getId(), className, testMethodName, ILaunchManager.RUN_MODE));
                    manager.add(new RerunTestAction(JUnitMessages.RerunAction_label_debug, testRunner, testElement
                            .getId(), className, testMethodName, ILaunchManager.DEBUG_MODE));
                }
            }
            if (layoutMode.equals(ViewLayout.HIERARCHICAL)) {
                manager.add(new Separator());
                manager.add(new ExpandAllAction());
            }

        }
        // if (testRunSession != null && testRunSession.getFailureCount() +
        // testRunSession.getErrorCount() > 0) {
        // if (!layoutMode.equals(ViewLayout.HIERARCHICAL))
        // manager.add(new Separator());
        // manager.add(new CopyFailureListAction(testRunnerPart, clipboard));
        // }
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS + "-end")); //$NON-NLS-1$
    }


    private boolean testClassExists(final String className) {
        throw new UnsupportedOperationException("This is not implemented - needs to be features, not classes");
        // final IJavaProject project = testRunnerPart.getLaunchedProject();
        // if (project == null)
        // return false;
        // try {
        // final IType type = project.findType(className);
        // return type != null;
        // } catch (final JavaModelException e) {
        // // fall through
        // }
        // return false;
    }


    public Control getTestViewerControl() {
        return viewerbook;
    }


    public synchronized void registerActiveSession(final TestRunSession testRunSession) {
        this.testRunSession = testRunSession;
        registerAutoScrollTarget(null);
        registerViewersRefresh();
    }


    void handleDefaultSelected() {
        final IStructuredSelection selection = (IStructuredSelection) selectionProvider.getSelection();
        if (selection.size() != 1)
            return;

        final TestElement testElement = (TestElement) selection.getFirstElement();

        OpenFeatureAction action;
        if (testElement instanceof TestSuiteElement) {
            action = new OpenFeatureAction();
        } else if (testElement instanceof TestCaseElement) {
            final TestCaseElement testCase = (TestCaseElement) testElement;
            action = new OpenFeatureAction();
        } else {
            throw new IllegalStateException(String.valueOf(testElement));
        }

        if (action.isEnabled())
            action.run();
    }


    private void handleSelected() {
        final IStructuredSelection selection = (IStructuredSelection) selectionProvider.getSelection();
        TestElement testElement = null;
        if (selection.size() == 1) {
            testElement = (TestElement) selection.getFirstElement();
        }
        failedTestNotifier.notify(testElement);
    }


    public synchronized void setShowTime(final boolean showTime) {
        try {
            viewerbook.setRedraw(false);
            treeLabelProvider.setShowTime(showTime);
            tableLabelProvider.setShowTime(showTime);
        } finally {
            viewerbook.setRedraw(true);
        }
    }


    public synchronized void setShowFailuresOnly(final boolean failuresOnly, final int layoutMode) {
        /*
         * Management of fTreeViewer and fTableViewer
         * ****************************************** - invisible viewer is
         * updated on registerViewerUpdate unless its f*NeedsRefresh is true -
         * invisible viewer is not refreshed upfront - on layout change, new
         * viewer is refreshed if necessary - filter only applies to "current"
         * layout mode / viewer
         */
        try {
            viewerbook.setRedraw(false);

            IStructuredSelection selection = null;
            final boolean switchLayout = layoutMode != this.layoutMode.value();
            if (switchLayout) {
                selection = (IStructuredSelection) selectionProvider.getSelection();
                if (layoutMode == ViewLayout.HIERARCHICAL.value()) {
                    if (treeNeedsRefresh) {
                        clearUpdateAndExpansion();
                    }
                } else {
                    if (tableNeedsRefresh) {
                        clearUpdateAndExpansion();
                    }
                }
                this.layoutMode = ViewLayout.forValue(layoutMode);
                viewerbook.showPage(getActiveViewer().getControl());
            }

            // avoid realizing all TableItems, especially in flat mode!
            final StructuredViewer viewer = getActiveViewer();
            if (failuresOnly) {
                if (!getActiveViewerHasFilter()) {
                    setActiveViewerNeedsRefresh(true);
                    setActiveViewerHasFilter(true);
                    viewer.setInput(null);
                    viewer.addFilter(failuresOnlyFilter);
                }

            } else {
                if (getActiveViewerHasFilter()) {
                    setActiveViewerNeedsRefresh(true);
                    setActiveViewerHasFilter(false);
                    viewer.setInput(null);
                    viewer.removeFilter(failuresOnlyFilter);
                }
            }
            processChangesInUI();

            if (selection != null) {
                // workaround for
                // https://bugs.eclipse.org/bugs/show_bug.cgi?id=125708
                // (ITreeSelection not adapted if TreePaths changed):
                final StructuredSelection flatSelection = new StructuredSelection(selection.toList());
                selectionProvider.setSelection(flatSelection, true);
            }

        } finally {
            viewerbook.setRedraw(true);
        }
    }


    private boolean getActiveViewerHasFilter() {
        if (layoutMode.equals(ViewLayout.HIERARCHICAL))
            return treeHasFilter;
        else
            return tableHasFilter;
    }


    private void setActiveViewerHasFilter(final boolean filter) {
        if (layoutMode.equals(ViewLayout.HIERARCHICAL))
            treeHasFilter = filter;
        else
            tableHasFilter = filter;
    }


    private StructuredViewer getActiveViewer() {
        if (layoutMode.equals(ViewLayout.HIERARCHICAL))
            return treeViewer;
        else
            return tableViewer;
    }


    private boolean getActiveViewerNeedsRefresh() {
        if (layoutMode.equals(ViewLayout.HIERARCHICAL))
            return treeNeedsRefresh;
        else
            return tableNeedsRefresh;
    }


    private void setActiveViewerNeedsRefresh(final boolean needsRefresh) {
        if (layoutMode.equals(ViewLayout.HIERARCHICAL))
            treeNeedsRefresh = needsRefresh;
        else
            tableNeedsRefresh = needsRefresh;
    }


    /**
     * To be called periodically by the TestRunnerViewPart (in the UI thread).
     */
    public void processChangesInUI() {
        TestRoot testRoot;
        if (testRunSession == null) {
            registerViewersRefresh();
            treeNeedsRefresh = false;
            tableNeedsRefresh = false;
            treeViewer.setInput(null);
            tableViewer.setInput(null);
            return;
        }

        testRoot = testRunSession.getTestRoot();

        final StructuredViewer viewer = getActiveViewer();
        if (getActiveViewerNeedsRefresh()) {
            clearUpdateAndExpansion();
            setActiveViewerNeedsRefresh(false);
            viewer.setInput(testRoot);

        } else {
            Object[] toUpdate;
            synchronized (this) {
                toUpdate = needUpdate.toArray();
                needUpdate.clear();
            }
            if (!treeNeedsRefresh && toUpdate.length > 0) {
                if (treeHasFilter)
                    for (final Object element : toUpdate)
                        updateElementInTree((TestElement) element);
                else {
                    final HashSet<Object> toUpdateWithParents = new HashSet<Object>();
                    toUpdateWithParents.addAll(Arrays.asList(toUpdate));
                    for (final Object element : toUpdate) {
                        TestElement parent = ((TestElement) element).getParent();
                        while (parent != null) {
                            toUpdateWithParents.add(parent);
                            parent = parent.getParent();
                        }
                    }
                    treeViewer.update(toUpdateWithParents.toArray(), null);
                }
            }
            if (!tableNeedsRefresh && toUpdate.length > 0) {
                if (tableHasFilter)
                    for (final Object element : toUpdate)
                        updateElementInTable((TestElement) element);
                else
                    tableViewer.update(toUpdate, null);
            }
        }
        autoScrollInUI();
    }


    private void updateElementInTree(final TestElement testElement) {
        if (isShown(testElement)) {
            updateShownElementInTree(testElement);
        } else {
            TestElement current = testElement;
            do {
                if (treeViewer.testFindItem(current) != null)
                    treeViewer.remove(current);
                current = current.getParent();
            } while (!(current instanceof TestRoot) && !isShown(current));

            while (current != null && !(current instanceof TestRoot)) {
                treeViewer.update(current, null);
                current = current.getParent();
            }
        }
    }


    private void updateShownElementInTree(final TestElement testElement) {
        if (testElement == null || testElement instanceof TestRoot) // paranoia
                                                                    // null
                                                                    // check
            return;

        final TestSuiteElement parent = testElement.getParent();
        updateShownElementInTree(parent); // make sure parent is shown and
                                          // up-to-date

        if (treeViewer.testFindItem(testElement) == null) {
            treeViewer.add(parent, testElement); // if not yet in tree: add
        } else {
            treeViewer.update(testElement, null); // if in tree: update
        }
    }


    private void updateElementInTable(final TestElement element) {
        if (isShown(element)) {
            if (tableViewer.testFindItem(element) == null) {
                final TestElement previous = getNextFailure(element, false);
                int insertionIndex = -1;
                if (previous != null) {
                    final TableItem item = (TableItem) tableViewer.testFindItem(previous);
                    if (item != null)
                        insertionIndex = tableViewer.getTable().indexOf(item);
                }
                tableViewer.insert(element, insertionIndex);
            } else {
                tableViewer.update(element, null);
            }
        } else {
            tableViewer.remove(element);
        }
    }


    private boolean isShown(final TestElement current) {
        return failuresOnlyFilter.select(current);
    }


    private void autoScrollInUI() {
        if (!autoScrollSupplier.currentValue().booleanValue()) {
            clearAutoExpand();
            autoClose.clear();
            return;
        }

        if (layoutMode.equals(ViewLayout.FLAT)) {
            if (autoScrollTarget != null)
                tableViewer.reveal(autoScrollTarget);
            return;
        }

        synchronized (this) {
            for (final TestSuiteElement suite : autoExpand) {
                treeViewer.setExpandedState(suite, true);
            }
            clearAutoExpand();
        }

        final TestCaseElement current = autoScrollTarget;
        autoScrollTarget = null;

        TestSuiteElement parent = current == null ? null : (TestSuiteElement) treeContentProvider.getParent(current);
        if (autoClose.isEmpty() || !autoClose.getLast().equals(parent)) {
            // we're in a new branch, so let's close old OK branches:
            for (final ListIterator<TestSuiteElement> iter = autoClose.listIterator(autoClose.size()); iter
                    .hasPrevious();) {
                final TestSuiteElement previousAutoOpened = iter.previous();
                if (previousAutoOpened.equals(parent))
                    break;

                if (previousAutoOpened.getStatus() == TestElement.Status.OK) {
                    // auto-opened the element, and all children are OK -> auto
                    // close
                    iter.remove();
                    treeViewer.collapseToLevel(previousAutoOpened, AbstractTreeViewer.ALL_LEVELS);
                }
            }

            while (parent != null && !testRunSession.getTestRoot().equals(parent)
                    && treeViewer.getExpandedState(parent) == false) {
                autoClose.add(parent); // add to auto-opened elements -> close
                                       // later if STATUS_OK
                parent = (TestSuiteElement) treeContentProvider.getParent(parent);
            }
        }
        if (current != null)
            treeViewer.reveal(current);
    }


    public void selectFirstFailure() {
        final TestCaseElement firstFailure = getNextChildFailure(testRunSession.getTestRoot(), true);
        if (firstFailure != null)
            getActiveViewer().setSelection(new StructuredSelection(firstFailure), true);
    }


    public void selectFailure(final boolean showNext) {
        final IStructuredSelection selection = (IStructuredSelection) getActiveViewer().getSelection();
        final TestElement selected = (TestElement) selection.getFirstElement();
        TestElement next;

        if (selected == null) {
            next = getNextChildFailure(testRunSession.getTestRoot(), showNext);
        } else {
            next = getNextFailure(selected, showNext);
        }

        if (next != null)
            getActiveViewer().setSelection(new StructuredSelection(next), true);
    }


    private TestElement getNextFailure(final TestElement selected, final boolean showNext) {
        if (selected instanceof TestSuiteElement) {
            final TestElement nextChild = getNextChildFailure((TestSuiteElement) selected, showNext);
            if (nextChild != null)
                return nextChild;
        }
        return getNextFailureSibling(selected, showNext);
    }


    private TestCaseElement getNextFailureSibling(final TestElement current, final boolean showNext) {
        final TestSuiteElement parent = current.getParent();
        if (parent == null)
            return null;

        List<ITestElement> siblings = Arrays.asList(parent.getChildren());
        if (!showNext)
            siblings = new ReverseList<ITestElement>(siblings);

        final int nextIndex = siblings.indexOf(current) + 1;
        for (int i = nextIndex; i < siblings.size(); i++) {
            final TestElement sibling = (TestElement) siblings.get(i);
            if (sibling.getStatus().isErrorOrFailure()) {
                if (sibling instanceof TestCaseElement) {
                    return (TestCaseElement) sibling;
                } else {
                    return getNextChildFailure((TestSuiteElement) sibling, showNext);
                }
            }
        }
        return getNextFailureSibling(parent, showNext);
    }


    private TestCaseElement getNextChildFailure(final TestSuiteElement root, final boolean showNext) {
        List<ITestElement> children = Arrays.asList(root.getChildren());
        if (!showNext)
            children = new ReverseList<ITestElement>(children);
        for (int i = 0; i < children.size(); i++) {
            final TestElement child = (TestElement) children.get(i);
            if (child.getStatus().isErrorOrFailure()) {
                if (child instanceof TestCaseElement) {
                    return (TestCaseElement) child;
                } else {
                    return getNextChildFailure((TestSuiteElement) child, showNext);
                }
            }
        }
        return null;
    }


    public synchronized void registerViewersRefresh() {
        treeNeedsRefresh = true;
        tableNeedsRefresh = true;
        clearUpdateAndExpansion();
    }


    private void clearUpdateAndExpansion() {
        needUpdate = new LinkedHashSet<TestElement>();
        autoClose = new LinkedList<TestSuiteElement>();
        autoExpand = new HashSet<TestSuiteElement>();
    }


    /**
     * @param testElement
     *            the added test
     */
    public synchronized void registerTestAdded(final TestElement testElement) {
        // TODO: performance: would only need to refresh parent of added element
        treeNeedsRefresh = true;
        tableNeedsRefresh = true;
    }


    public synchronized void registerViewerUpdate(final TestElement testElement) {
        needUpdate.add(testElement);
    }


    private synchronized void clearAutoExpand() {
        autoExpand.clear();
    }


    public void registerAutoScrollTarget(final TestCaseElement testCaseElement) {
        autoScrollTarget = testCaseElement;
    }


    public synchronized void registerFailedForAutoScroll(final TestElement testElement) {
        final TestSuiteElement parent = (TestSuiteElement) treeContentProvider.getParent(testElement);
        if (parent != null)
            autoExpand.add(parent);
    }


    public void expandFirstLevel() {
        treeViewer.expandToLevel(2);
    }

}
