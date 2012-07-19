package com.technophobia.substeps.junit.ui.component;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;

import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * A selection provider for view parts with more that one viewer. Tracks the
 * focus of the viewers to provide the correct selection.
 */
public class SelectionProviderMediator implements IPostSelectionProvider {

    private class InternalListener implements ISelectionChangedListener, FocusListener {
        /*
         * @see ISelectionChangedListener#selectionChanged
         */
        @Override
        public void selectionChanged(final SelectionChangedEvent event) {
            doSelectionChanged(event);
        }


        /*
         * @see FocusListener#focusGained
         */
        @Override
        public void focusGained(final FocusEvent e) {
            doFocusChanged(e.widget);
        }


        /*
         * @see FocusListener#focusLost
         */
        @Override
        public void focusLost(final FocusEvent e) {
            // do not reset due to focus behavior on GTK
            // fViewerInFocus= null;
        }
    }

    private class InternalPostSelectionListener implements ISelectionChangedListener {
        @Override
        public void selectionChanged(final SelectionChangedEvent event) {
            doPostSelectionChanged(event);
        }

    }

    private final StructuredViewer[] viewers;

    private StructuredViewer viewerInFocus;
    private final ListenerList selectionChangedListeners;
    private final ListenerList postSelectionChangedListeners;


    /**
     * @param viewers
     *            All viewers that can provide a selection
     * @param viewerInFocus
     *            the viewer currently in focus or <code>null</code>
     */
    public SelectionProviderMediator(final StructuredViewer[] viewers, final StructuredViewer viewerInFocus) {
        Assert.isNotNull(viewers);
        this.viewers = viewers;
        final InternalListener listener = new InternalListener();
        this.selectionChangedListeners = new ListenerList();
        this.postSelectionChangedListeners = new ListenerList();
        this.viewerInFocus = viewerInFocus;

        for (int i = 0; i < viewers.length; i++) {
            final StructuredViewer viewer = viewers[i];
            viewer.addSelectionChangedListener(listener);
            viewer.addPostSelectionChangedListener(new InternalPostSelectionListener());
            final Control control = viewer.getControl();
            control.addFocusListener(listener);
        }
    }


    private void doFocusChanged(final Widget control) {
        for (int i = 0; i < viewers.length; i++) {
            if (viewers[i].getControl() == control) {
                propagateFocusChanged(viewers[i]);
                return;
            }
        }
    }


    final void doPostSelectionChanged(final SelectionChangedEvent event) {
        final ISelectionProvider provider = event.getSelectionProvider();
        if (provider == viewerInFocus) {
            firePostSelectionChanged();
        }
    }


    final void doSelectionChanged(final SelectionChangedEvent event) {
        final ISelectionProvider provider = event.getSelectionProvider();
        if (provider == viewerInFocus) {
            fireSelectionChanged();
        }
    }


    final void propagateFocusChanged(final StructuredViewer viewer) {
        if (viewer != viewerInFocus) { // OK to compare by identity
            viewerInFocus = viewer;
            fireSelectionChanged();
            firePostSelectionChanged();
        }
    }


    private void fireSelectionChanged() {
        if (selectionChangedListeners != null) {
            final SelectionChangedEvent event = new SelectionChangedEvent(this, getSelection());

            final Object[] listeners = selectionChangedListeners.getListeners();
            for (int i = 0; i < listeners.length; i++) {
                final ISelectionChangedListener listener = (ISelectionChangedListener) listeners[i];
                listener.selectionChanged(event);
            }
        }
    }


    private void firePostSelectionChanged() {
        if (postSelectionChangedListeners != null) {
            final SelectionChangedEvent event = new SelectionChangedEvent(this, getSelection());

            final Object[] listeners = postSelectionChangedListeners.getListeners();
            for (int i = 0; i < listeners.length; i++) {
                final ISelectionChangedListener listener = (ISelectionChangedListener) listeners[i];
                listener.selectionChanged(event);
            }
        }
    }


    /*
     * @see ISelectionProvider#addSelectionChangedListener
     */
    @Override
    public void addSelectionChangedListener(final ISelectionChangedListener listener) {
        selectionChangedListeners.add(listener);
    }


    /*
     * @see ISelectionProvider#removeSelectionChangedListener
     */
    @Override
    public void removeSelectionChangedListener(final ISelectionChangedListener listener) {
        selectionChangedListeners.remove(listener);
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IPostSelectionProvider#
     * addPostSelectionChangedListener
     * (org.eclipse.jface.viewers.ISelectionChangedListener)
     */
    @Override
    public void addPostSelectionChangedListener(final ISelectionChangedListener listener) {
        postSelectionChangedListeners.add(listener);
    }


    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.viewers.IPostSelectionProvider#
     * removePostSelectionChangedListener
     * (org.eclipse.jface.viewers.ISelectionChangedListener)
     */
    @Override
    public void removePostSelectionChangedListener(final ISelectionChangedListener listener) {
        postSelectionChangedListeners.remove(listener);
    }


    /*
     * @see ISelectionProvider#getSelection
     */
    @Override
    public ISelection getSelection() {
        if (viewerInFocus != null) {
            return viewerInFocus.getSelection();
        }
        return StructuredSelection.EMPTY;
    }


    /*
     * @see ISelectionProvider#setSelection
     */
    @Override
    public void setSelection(final ISelection selection) {
        if (viewerInFocus != null) {
            viewerInFocus.setSelection(selection);
        }
    }


    public void setSelection(final ISelection selection, final boolean reveal) {
        if (viewerInFocus != null) {
            viewerInFocus.setSelection(selection, reveal);
        }
    }


    /**
     * Returns the viewer in focus or null if no viewer has the focus
     * 
     * @return returns the current viewer in focus
     */
    public StructuredViewer getViewerInFocus() {
        return viewerInFocus;
    }
}
