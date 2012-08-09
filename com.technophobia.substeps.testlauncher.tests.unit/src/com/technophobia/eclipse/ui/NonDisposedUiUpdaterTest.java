package com.technophobia.eclipse.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.technophobia.eclipse.ui.render.NonDisposedUiUpdater;
import com.technophobia.substeps.supplier.Supplier;

@RunWith(JMock.class)
public class NonDisposedUiUpdaterTest {

    private Mockery context;

    private Supplier<Boolean> disposedChecker;

    private FakeUiUpdater updater;


    @SuppressWarnings("unchecked")
    @Before
    public void initialise() {
        this.context = new Mockery();

        this.disposedChecker = context.mock(Supplier.class);

        this.updater = new FakeUiUpdater(disposedChecker);
    }


    @Test
    public void updatedIfNotDisposed() {
        context.checking(new Expectations() {
            {
                oneOf(disposedChecker).get();
                will(returnValue(Boolean.FALSE));
            }
        });

        updater.doUpdate();
        assertTrue(updater.isUpdated);
    }


    @Test
    public void notUpdatedIfDisposed() {
        context.checking(new Expectations() {
            {
                oneOf(disposedChecker).get();
                will(returnValue(Boolean.TRUE));
            }
        });

        updater.doUpdate();
        assertFalse(updater.isUpdated);
    }

    private class FakeUiUpdater extends NonDisposedUiUpdater {
        private boolean isUpdated = false;


        public FakeUiUpdater(final Supplier<Boolean> disposedChecker) {
            super(disposedChecker);
        }


        @Override
        protected void safeUpdate() {
            this.isUpdated = true;
        }


        @Override
        public void reset() {
            // TODO Auto-generated method stub

        }
    }
}
