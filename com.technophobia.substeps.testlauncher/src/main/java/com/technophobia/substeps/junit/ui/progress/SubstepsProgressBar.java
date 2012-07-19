package com.technophobia.substeps.junit.ui.progress;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.technophobia.substeps.junit.ui.component.ProgressBar;

public class SubstepsProgressBar extends Canvas implements ProgressBar {
    private static final int DEFAULT_WIDTH = 160;
    private static final int DEFAULT_HEIGHT = 18;

    private int currentTickCount = 0;
    private int maxTickCount = 0;
    private int colorBarWidth = 0;
    private final Color okColor;
    private final Color failureColor;
    private final Color stoppedColor;
    private boolean error;
    private boolean stopped = false;


    public SubstepsProgressBar(final Composite parent) {
        super(parent, SWT.NONE);

        addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(final ControlEvent e) {
                colorBarWidth = scale(currentTickCount);
                redraw();
            }
        });
        addPaintListener(new PaintListener() {
            @Override
            public void paintControl(final PaintEvent e) {
                paint(e);
            }
        });
        addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(final DisposeEvent e) {
                failureColor.dispose();
                okColor.dispose();
                stoppedColor.dispose();
            }
        });
        final Display display = parent.getDisplay();
        this.failureColor = new Color(display, 159, 63, 63);
        this.okColor = new Color(display, 95, 191, 95);
        this.stoppedColor = new Color(display, 120, 120, 120);
    }


    public void setMaximum(final int max) {
        this.maxTickCount = max;
    }


    public void reset() {
        this.error = false;
        this.stopped = false;
        this.currentTickCount = 0;
        this.maxTickCount = 0;
        this.colorBarWidth = 0;
        redraw();
    }


    @Override
    public void reset(final boolean hasErrors, final boolean stopped, final int ticksDone, final int maximum) {
        final boolean noChange = this.error == hasErrors && stopped == this.stopped
                && this.currentTickCount == ticksDone && this.maxTickCount == maximum;
        this.error = hasErrors;
        this.stopped = stopped;
        this.currentTickCount = ticksDone;
        this.maxTickCount = maximum;
        this.colorBarWidth = scale(ticksDone);
        if (!noChange)
            redraw();
    }


    private void paintStep(int startX, final int endX) {
        final GC gc = new GC(this);
        setStatusColor(gc);
        final Rectangle rect = getClientArea();
        startX = Math.max(1, startX);
        gc.fillRectangle(startX, 1, endX - startX, rect.height - 2);
        gc.dispose();
    }


    private void setStatusColor(final GC gc) {
        if (stopped)
            gc.setBackground(stoppedColor);
        else if (error)
            gc.setBackground(failureColor);
        else
            gc.setBackground(okColor);
    }


    public void stopped() {
        stopped = true;
        redraw();
    }


    private int scale(final int value) {
        if (maxTickCount > 0) {
            final Rectangle r = getClientArea();
            if (r.width != 0)
                return Math.max(0, value * (r.width - 2) / maxTickCount);
        }
        return value;
    }


    private void drawBevelRect(final GC gc, final int x, final int y, final int w, final int h, final Color topleft,
            final Color bottomright) {
        gc.setForeground(topleft);
        gc.drawLine(x, y, x + w - 1, y);
        gc.drawLine(x, y, x, y + h - 1);

        gc.setForeground(bottomright);
        gc.drawLine(x + w, y, x + w, y + h);
        gc.drawLine(x, y + h, x + w, y + h);
    }


    private void paint(final PaintEvent event) {
        final GC gc = event.gc;
        final Display disp = getDisplay();

        final Rectangle rect = getClientArea();
        gc.fillRectangle(rect);
        drawBevelRect(gc, rect.x, rect.y, rect.width - 1, rect.height - 1,
                disp.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW),
                disp.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));

        setStatusColor(gc);
        colorBarWidth = Math.min(rect.width - 2, colorBarWidth);
        gc.fillRectangle(1, 1, colorBarWidth, rect.height - 2);
    }


    @Override
    public Point computeSize(final int wHint, final int hHint, final boolean changed) {
        checkWidget();
        final Point size = new Point(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        if (wHint != SWT.DEFAULT)
            size.x = wHint;
        if (hHint != SWT.DEFAULT)
            size.y = hHint;
        return size;
    }


    public void step(final int failures) {
        currentTickCount++;
        int x = colorBarWidth;

        colorBarWidth = scale(currentTickCount);

        if (!error && failures > 0) {
            error = true;
            x = 1;
        }
        if (currentTickCount == maxTickCount)
            colorBarWidth = getClientArea().width - 1;
        paintStep(x, colorBarWidth);
    }


    public void refresh(final boolean hasErrors) {
        error = hasErrors;
        redraw();
    }

}
