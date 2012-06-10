package com.technophobia.substeps.editor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;

import com.technophobia.substeps.editor.steps.SWTBotInitialiser;

public class SWTTestUtil {

	public static void setActiveShellHack(final String shellTitle) {
		final SWTBot bot = SWTBotInitialiser.bot();
		setActiveShellHack(bot.shell(shellTitle).widget);
	}

	public static void setActiveShellHack(final Shell shell) {
		final SWTBot bot = SWTBotInitialiser.bot();
		Field field;
		try {
			field = Display.class.getDeclaredField("activeShell");
			field.setAccessible(true);
			field.set(bot.getDisplay(), shell);
		} catch (final Exception e) {
			e.printStackTrace();
		}

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				shell.setVisible(true);
			}
		});
	}

	public static void setMainFrameToActiveShellHack() {
		final SWTBot bot = SWTBotInitialiser.bot();
		setActiveShellHack(bot.shells()[0].widget);
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
			throw new IllegalStateException("Tried to get shell at depth "
					+ depth + ", but only found " + sortedShells.size()
					+ " shells");
		}
		return sortedShells.get(depth);
	}

	private static List<SWTBotShell> sortShells(final SWTBotShell[] shells) {

		final Map<SWTBotShell, Shell> shellToWidgetMap = swtBotShellToWidgetMap(shells);
		final Map<Composite, SWTBotShell> parentToShellMap = parentToShellMap(shells);

		if (!parentToShellMap.containsKey(null)) {
			throw new IllegalStateException("Could not find root shell");
		}

		final List<SWTBotShell> sortedShells = new LinkedList<SWTBotShell>();
		SWTBotShell currentItem = parentToShellMap.remove(null);
		sortedShells.add(currentItem);
		while (!parentToShellMap.isEmpty()) {
			currentItem = parentToShellMap.remove(currentItem.widget);
			if (currentItem == null) {
				throw new IllegalStateException("Still have ["
						+ parentToShellMap.size()
						+ " items in the map, but couldn't find child");
			}
			sortedShells.add(currentItem);
		}
		return sortedShells;
	}

	private static Map<Composite, SWTBotShell> parentToShellMap(
			final SWTBotShell[] shells) {
		final Map<Composite, SWTBotShell> parentToShellMap = new HashMap<Composite, SWTBotShell>();
		for (final SWTBotShell shell : shells) {
			if (parentToShellMap.containsKey(shell.widget.getParent())) {
				throw new IllegalStateException(
						"Currently cannot handle 2 shells at the same depth");
			}
			parentToShellMap.put(shell.widget.getParent(), shell);
		}
		return parentToShellMap;
	}

	private static Map<SWTBotShell, Shell> swtBotShellToWidgetMap(
			final SWTBotShell[] shells) {
		final Map<SWTBotShell, Shell> shellToWidgetMap = new HashMap<SWTBotShell, Shell>();
		for (final SWTBotShell swtBotShell : shells) {
			shellToWidgetMap.put(swtBotShell, swtBotShell.widget);
		}
		return shellToWidgetMap;
	}

}
