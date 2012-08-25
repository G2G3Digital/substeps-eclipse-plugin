package com.technophobia.substeps.editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

public class SWTTestUtil {

    // public static void setActiveShellHack(final String shellTitle) {
    // final SWTBot bot = SWTBotInitialiser.bot();
    // setActiveShellHack(bot.shell(shellTitle).widget);
    // }
    //
    //
    // public static void setActiveShellHack(final Shell shell) {
    // final SWTBot bot = SWTBotInitialiser.bot();
    // Field field;
    // try {
    // field = Display.class.getDeclaredField("activeShell");
    // field.setAccessible(true);
    // field.set(bot.getDisplay(), shell);
    // } catch (final Exception e) {
    // e.printStackTrace();
    // }
    //
    // Display.getDefault().syncExec(new Runnable() {
    // @Override
    // public void run() {
    // shell.setVisible(true);
    // }
    // });
    // }
    //
    //
    // public static void setMainFrameToActiveShellHack() {
    // final SWTBot bot = SWTBotInitialiser.bot();
    // setActiveShellHack(bot.shells()[0].widget);
    // }

    public static SWTBotShell dialogNamed(final SWTBot bot, final String name) {
        return bot.shell(name);
    }


    public static SWTBotShell dialogAtDepth(final SWTBot bot, final int depth) {
        // ideally we'd use the dialogName, but in Xvfb this doesn't appear to
        // be set
        final SWTBotShell[] shells = bot.shells();
        final List<SWTBotShell> sortedShells = new ArrayList<SWTBotShell>();
        Display.getDefault().syncExec(new Runnable() {
            @Override
            public void run() {
                try {
                    sortedShells.addAll(sortShells(shells));

                } catch (final Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        if (depth >= sortedShells.size()) {
            throw new IllegalStateException("Tried to get shell at depth " + depth + ", but only found "
                    + sortedShells.size() + " shells");
        }
        return sortedShells.get(depth);
    }


    private static List<SWTBotShell> sortShells(final SWTBotShell[] shells) {

        final Map<Composite, Set<SWTBotShell>> parentToShellMap = parentToShellMap(shells);

        if (!parentToShellMap.containsKey(null)) {
            throw new IllegalStateException("Could not find root shell");
        }

        final List<SWTBotShell> sortedShells = new LinkedList<SWTBotShell>();
        Set<SWTBotShell> currentItems = parentToShellMap.remove(null);
        for (final SWTBotShell swtBotShell : currentItems) {
            sortedShells.add(swtBotShell);
        }
        while (!parentToShellMap.isEmpty()) {
            currentItems = allChildrenOf(currentItems, parentToShellMap);

            if (currentItems.isEmpty()) {
                throw new IllegalStateException("Still have [" + parentToShellMap.size()
                        + " items in the map, but couldn't find child");
            }
            sortedShells.addAll(currentItems);
        }
        return sortedShells;
    }


    private static Set<SWTBotShell> allChildrenOf(final Set<SWTBotShell> currentItems,
            final Map<Composite, Set<SWTBotShell>> parentToShellMap) {
        final Set<SWTBotShell> childShells = new HashSet<SWTBotShell>();
        for (final SWTBotShell currentItem : currentItems) {
            if (parentToShellMap.containsKey(currentItem.widget)) {
                childShells.addAll(parentToShellMap.remove(currentItem.widget));
            }
        }
        return childShells;
    }


    private static Map<Composite, Set<SWTBotShell>> parentToShellMap(final SWTBotShell[] shells) {
        final Map<Composite, Set<SWTBotShell>> parentToShellMap = new HashMap<Composite, Set<SWTBotShell>>();
        for (final SWTBotShell shell : shells) {
            final Composite parent = shell.widget.getParent();
            if (!parentToShellMap.containsKey(parent)) {
                parentToShellMap.put(parent, new HashSet<SWTBotShell>());
            }
            parentToShellMap.get(parent).add(shell);
        }
        return parentToShellMap;
    }
}
