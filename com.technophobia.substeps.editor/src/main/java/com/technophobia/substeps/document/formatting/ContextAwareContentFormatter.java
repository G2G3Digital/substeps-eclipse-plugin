/*
 *	Copyright Technophobia Ltd 2012
 *
 *   This file is part of Substeps.
 *
 *    Substeps is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    Substeps is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public License
 *    along with Substeps.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.technophobia.substeps.document.formatting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.DefaultPositionUpdater;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.jface.text.IPositionUpdater;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.TextUtilities;
import org.eclipse.jface.text.TypedPosition;
import org.eclipse.jface.text.formatter.ContentFormatter;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.formatter.IFormattingStrategy;

import com.technophobia.substeps.FeatureEditorPlugin;
import com.technophobia.substeps.supplier.Supplier;

/**
 * Unfortunately, this is a copy and paste of {@link ContentFormatter}. We need
 * to be able to update the {@link FormattingContext} per iteration of the
 * {@link TypedPosition}s during formatting, which in the original class is
 * buried under 3 levels of private method. As the class contains alot of
 * private state, extending these methods is also not an option.
 * 
 * @author sforbes
 * 
 */
public class ContextAwareContentFormatter implements IContentFormatter, Supplier<FormattingContext> {

    private FormattingContext currentContext = null;
    private final FormattingContextFactory formattingContextFactory;

    /** Internal position category used for the formatter partitioning */
    private final static String PARTITIONING = "__formatter_partitioning"; //$NON-NLS-1$

    /** The map of <code>IFormattingStrategy</code> objects */
    private Map<String, IFormattingStrategy> strategies;
    /**
     * The indicator of whether the formatter operates in partition aware mode
     * or not
     */
    private boolean isPartitionAware = true;

    /** The partition information managing document position categories */
    private String[] partitionManagingCategories;
    /**
     * The list of references to offset and end offset of all overlapping
     * positions
     */
    private List<PositionReference> overlappingPositionReferences;
    /** Position updater used for partitioning positions */
    private IPositionUpdater partitioningUpdater;
    /**
     * The document partitioning used by this formatter.
     * 
     * @since 3.0
     */
    private String partitioning;
    /**
     * The document this formatter works on.
     * 
     * @since 3.0
     */
    private IDocument document;
    /**
     * The external partition managing categories.
     * 
     * @since 3.0
     */
    private String[] externalPartitonManagingCategories;
    /**
     * Indicates whether <code>fPartitionManagingCategories</code> must be
     * computed.
     * 
     * @since 3.0
     */
    private boolean needsComputation = true;


    public ContextAwareContentFormatter(final FormattingContextFactory formattingContextFactory) {
        super();
        this.partitioning = IDocumentExtension3.DEFAULT_PARTITIONING;
        this.formattingContextFactory = formattingContextFactory;
    }


    @Override
    public FormattingContext get() {
        return currentContext;
    }


    /**
     * Registers a strategy for a particular content type. If there is already a
     * strategy registered for this type, the new strategy is registered instead
     * of the old one. If the given content type is <code>null</code> the given
     * strategy is registered for all content types as is called only once per
     * formatting session.
     * 
     * @param strategy
     *            the formatting strategy to register, or <code>null</code> to
     *            remove an existing one
     * @param contentType
     *            the content type under which to register
     */
    public void setFormattingStrategy(final IFormattingStrategy strategy, final String contentType) {

        Assert.isNotNull(contentType);

        if (strategies == null)
            strategies = new HashMap<String, IFormattingStrategy>();

        if (strategy == null)
            strategies.remove(contentType);
        else
            strategies.put(contentType, strategy);
    }


    /**
     * Informs this content formatter about the names of those position
     * categories which are used to manage the document's partitioning
     * information and thus should be ignored when this formatter updates
     * positions.
     * 
     * @param categories
     *            the categories to be ignored
     * @deprecated incompatible with an open set of document partitionings. The
     *             provided information is only used if this formatter can not
     *             compute the partition managing position categories.
     */
    @Deprecated
    public void setPartitionManagingPositionCategories(final String[] categories) {
        this.externalPartitonManagingCategories = TextUtilities.copy(categories);
    }


    /**
     * Sets the document partitioning to be used by this formatter.
     * 
     * @param partitioning
     *            the document partitioning
     * @since 3.0
     */
    public void setDocumentPartitioning(final String partitioning) {
        this.partitioning = partitioning;
    }


    /**
     * Sets the formatter's operation mode.
     * 
     * @param enable
     *            indicates whether the formatting process should be partition
     *            ware
     */
    public void enablePartitionAwareFormatting(final boolean enable) {
        this.isPartitionAware = enable;
    }


    /*
     * @see IContentFormatter#getFormattingStrategy(String)
     */
    @Override
    public IFormattingStrategy getFormattingStrategy(final String contentType) {

        Assert.isNotNull(contentType);

        if (strategies == null)
            return null;

        return strategies.get(contentType);
    }


    /*
     * @see IContentFormatter#format(IDocument, IRegion)
     */
    @Override
    public void format(final IDocument doc, final IRegion region) {
        this.needsComputation = true;
        this.document = doc;
        try {

            if (isPartitionAware)
                formatPartitions(region);
            else
                formatRegion(region);

        } finally {
            this.needsComputation = true;
            this.document = null;
        }
    }


    /**
     * Determines the partitioning of the given region of the document. Informs
     * the formatting strategies of each partition about the start, the process,
     * and the termination of the formatting session.
     * 
     * @param region
     *            the document region to be formatted
     * @since 3.0
     */
    private void formatPartitions(final IRegion region) {

        addPartitioningUpdater();

        try {

            final TypedPosition[] ranges = getPartitioning(region);
            if (ranges != null) {
                start(ranges, getIndentation(region.getOffset()));
                format(ranges);
                stop(ranges);
            }

        } catch (final BadLocationException x) {
            // no-op
        }

        removePartitioningUpdater();
    }


    /**
     * Formats the given region with the strategy registered for the default
     * content type. The strategy is informed about the start, the process, and
     * the termination of the formatting session.
     * 
     * @param region
     *            the region to be formatted
     * @since 3.0
     */
    private void formatRegion(final IRegion region) {

        final IFormattingStrategy strategy = getFormattingStrategy(IDocument.DEFAULT_CONTENT_TYPE);
        if (strategy != null) {
            strategy.formatterStarts(getIndentation(region.getOffset()));
            format(strategy, new TypedPosition(region.getOffset(), region.getLength(), IDocument.DEFAULT_CONTENT_TYPE));
            strategy.formatterStops();
        }
    }


    /**
     * Returns the partitioning of the given region of the document to be
     * formatted. As one partition after the other will be formatted and
     * formatting will probably change the length of the formatted partition, it
     * must be kept track of the modifications in order to submit the correct
     * partition to all formatting strategies. For this, all partitions are
     * remembered as positions in a dedicated position category. (As formatting
     * strategies might rely on each other, calling them in reversed order is
     * not an option.)
     * 
     * @param region
     *            the region for which the partitioning must be determined
     * @return the partitioning of the specified region
     * @exception BadLocationException
     *                of region is invalid in the document
     * @since 3.0
     */
    private TypedPosition[] getPartitioning(final IRegion region) throws BadLocationException {

        final ITypedRegion[] regions = TextUtilities.computePartitioning(document, partitioning, region.getOffset(),
                region.getLength(), false);
        final TypedPosition[] positions = new TypedPosition[regions.length];

        for (int i = 0; i < regions.length; i++) {
            positions[i] = new TypedPosition(regions[i]);
            try {
                document.addPosition(PARTITIONING, positions[i]);
            } catch (final BadPositionCategoryException x) {
                // should not happen
            }
        }

        return positions;
    }


    /**
     * Fires <code>formatterStarts</code> to all formatter strategies which will
     * be involved in the forthcoming formatting process.
     * 
     * @param regions
     *            the partitioning of the document to be formatted
     * @param indentation
     *            the initial indentation
     */
    private void start(final TypedPosition[] regions, final String indentation) {
        for (int i = 0; i < regions.length; i++) {
            final IFormattingStrategy s = getFormattingStrategy(regions[i].getType());
            if (s != null)
                s.formatterStarts(indentation);
        }
    }


    /**
     * Formats one partition after the other using the formatter strategy
     * registered for the partition's content type.
     * 
     * @param ranges
     *            the partitioning of the document region to be formatted
     * @since 3.0
     */
    private void format(final TypedPosition[] ranges) {
        final TypedPosition[] allTypedPositions = allTypedPositions();

        for (int i = 0; i < ranges.length; i++) {
            final IFormattingStrategy s = getFormattingStrategy(ranges[i].getType());
            updateCurrentContext(allTypedPositions, i);
            if (s != null) {
                format(s, ranges[i]);
            }
        }
    }


    private TypedPosition[] allTypedPositions() {
        try {
            final ITypedRegion[] allRegions = TextUtilities.computePartitioning(document, partitioning, 0,
                    document.getLength(), false);
            final TypedPosition[] positions = new TypedPosition[allRegions.length];

            for (int i = 0; i < allRegions.length; i++) {
                positions[i] = new TypedPosition(allRegions[i]);
            }
            return positions;
        } catch (final BadLocationException ex) {
            FeatureEditorPlugin.log(IStatus.ERROR,
                    "Could not get all typed positions for document, message was " + ex.getMessage());
        }
        return new TypedPosition[0];
    }


    /**
     * Formats the given region of the document using the specified formatting
     * strategy. In order to maintain positions correctly, first all affected
     * positions determined, after all document listeners have been informed
     * about the coming change, the affected positions are removed to avoid that
     * they are regularly updated. After all position updaters have run, the
     * affected positions are updated with the formatter's information and added
     * back to their categories, right before the first document listener is
     * informed about that a change happened.
     * 
     * @param strategy
     *            the strategy to be used
     * @param region
     *            the region to be formatted
     * @since 3.0
     */
    private void format(final IFormattingStrategy strategy, final TypedPosition region) {
        try {

            final int offset = region.getOffset();
            final int length = region.getLength();

            final String content = document.get(offset, length);
            final int[] positions = getAffectedPositions(offset, length);
            final String formatted = strategy.format(content, isLineStart(offset), getIndentation(offset), positions);

            if (formatted != null && !formatted.equals(content)) {

                final IPositionUpdater first = new RemoveAffectedPositions();
                document.insertPositionUpdater(first, 0);
                final IPositionUpdater last = new UpdateAffectedPositions(positions, offset);
                document.addPositionUpdater(last);

                document.replace(offset, length, formatted);

                document.removePositionUpdater(first);
                document.removePositionUpdater(last);
            }

        } catch (final BadLocationException x) {
            // should not happen
        }
    }


    /**
     * Fires <code>formatterStops</code> to all formatter strategies which were
     * involved in the formatting process which is about to terminate.
     * 
     * @param regions
     *            the partitioning of the document which has been formatted
     */
    private void stop(final TypedPosition[] regions) {
        for (int i = 0; i < regions.length; i++) {
            final IFormattingStrategy s = getFormattingStrategy(regions[i].getType());
            if (s != null)
                s.formatterStops();
        }
    }


    /**
     * Installs those updaters which the formatter needs to keep track of the
     * partitions.
     * 
     * @since 3.0
     */
    private void addPartitioningUpdater() {
        partitioningUpdater = new NonDeletingPositionUpdater(PARTITIONING);
        document.addPositionCategory(PARTITIONING);
        document.addPositionUpdater(partitioningUpdater);
    }


    /**
     * Removes the formatter's internal position updater and category.
     * 
     * @since 3.0
     */
    private void removePartitioningUpdater() {

        try {

            document.removePositionUpdater(partitioningUpdater);
            document.removePositionCategory(PARTITIONING);
            partitioningUpdater = null;

        } catch (final BadPositionCategoryException x) {
            // should not happen
        }
    }


    /**
     * Returns the partition managing position categories for the formatted
     * document.
     * 
     * @return the position managing position categories
     * @since 3.0
     */
    private String[] getPartitionManagingCategories() {
        if (needsComputation) {
            needsComputation = false;
            partitionManagingCategories = TextUtilities.computePartitionManagingCategories(document);
            if (partitionManagingCategories == null)
                partitionManagingCategories = externalPartitonManagingCategories;
        }
        return partitionManagingCategories;
    }


    /**
     * Determines whether the given document position category should be ignored
     * by this formatter's position updating.
     * 
     * @param category
     *            the category to check
     * @return <code>true</code> if the category should be ignored,
     *         <code>false</code> otherwise
     */
    private boolean ignoreCategory(final String category) {

        if (PARTITIONING.equals(category))
            return true;

        final String[] categories = getPartitionManagingCategories();
        if (categories != null) {
            for (int i = 0; i < categories.length; i++) {
                if (categories[i].equals(category))
                    return true;
            }
        }

        return false;
    }


    /**
     * Determines all embracing, overlapping, and follow up positions for the
     * given region of the document.
     * 
     * @param offset
     *            the offset of the document region to be formatted
     * @param length
     *            the length of the document to be formatted
     * @since 3.0
     */
    private void determinePositionsToUpdate(final int offset, final int length) {

        final String[] categories = document.getPositionCategories();
        if (categories != null) {
            for (int i = 0; i < categories.length; i++) {

                if (ignoreCategory(categories[i]))
                    continue;

                try {

                    final Position[] positions = document.getPositions(categories[i]);

                    for (int j = 0; j < positions.length; j++) {

                        final Position p = positions[j];
                        if (p.overlapsWith(offset, length)) {

                            if (offset < p.getOffset())
                                overlappingPositionReferences.add(new PositionReference(p, true, categories[i]));

                            if (p.getOffset() + p.getLength() < offset + length)
                                overlappingPositionReferences.add(new PositionReference(p, false, categories[i]));
                        }
                    }

                } catch (final BadPositionCategoryException x) {
                    // can not happen
                }
            }
        }
    }


    /**
     * Returns all offset and the end offset of all positions overlapping with
     * the specified document range.
     * 
     * @param offset
     *            the offset of the document region to be formatted
     * @param length
     *            the length of the document to be formatted
     * @return all character positions of the interleaving positions
     * @since 3.0
     */
    private int[] getAffectedPositions(final int offset, final int length) {

        overlappingPositionReferences = new ArrayList<PositionReference>();

        determinePositionsToUpdate(offset, length);

        Collections.sort(overlappingPositionReferences);

        final int[] positions = new int[overlappingPositionReferences.size()];
        for (int i = 0; i < positions.length; i++) {
            final PositionReference r = overlappingPositionReferences.get(i);
            positions[i] = r.getCharacterPosition() - offset;
        }

        return positions;
    }


    /**
     * Removes the affected positions from their categories to avoid that they
     * are invalidly updated.
     * 
     * @param document
     *            the document
     */
    private void removeAffectedPositions(final IDocument doc) {
        final int size = overlappingPositionReferences.size();
        for (int i = 0; i < size; i++) {
            final PositionReference r = overlappingPositionReferences.get(i);
            try {
                doc.removePosition(r.getCategory(), r.getPosition());
            } catch (final BadPositionCategoryException x) {
                // can not happen
            }
        }
    }


    /**
     * Updates all the overlapping positions. Note, all other positions are
     * automatically updated by their document position updaters.
     * 
     * @param document
     *            the document to has been formatted
     * @param positions
     *            the adapted character positions to be used to update the
     *            document positions
     * @param offset
     *            the offset of the document region that has been formatted
     */
    protected void updateAffectedPositions(final IDocument doc, final int[] positions, final int offset) {

        if (doc != document)
            return;

        if (positions.length == 0)
            return;

        for (int i = 0; i < positions.length; i++) {

            final PositionReference r = overlappingPositionReferences.get(i);

            if (r.refersToOffset())
                r.setOffset(offset + positions[i]);
            else
                r.setLength((offset + positions[i]) - r.getOffset());

            final Position p = r.getPosition();
            final String category = r.getCategory();
            if (!document.containsPosition(category, p.offset, p.length)) {
                try {
                    if (positionAboutToBeAdded(document, category, p))
                        document.addPosition(r.getCategory(), p);
                } catch (final BadPositionCategoryException x) {
                    // can not happen
                } catch (final BadLocationException x) {
                    // should not happen
                }
            }

        }

        overlappingPositionReferences = null;
    }


    /**
     * The given position is about to be added to the given position category of
     * the given document.
     * <p>
     * This default implementation return <code>true</code>.
     * 
     * @param document
     *            the document
     * @param category
     *            the position category
     * @param position
     *            the position that will be added
     * @return <code>true</code> if the position can be added,
     *         <code>false</code> if it should be ignored
     */
    protected boolean positionAboutToBeAdded(@SuppressWarnings("unused") final IDocument doc, final String category,
            final Position position) {
        return true;
    }


    /**
     * Returns the indentation of the line of the given offset.
     * 
     * @param offset
     *            the offset
     * @return the indentation of the line of the offset
     * @since 3.0
     */
    private String getIndentation(final int offset) {

        try {
            int start = document.getLineOfOffset(offset);
            start = document.getLineOffset(start);

            int end = start;
            char c = document.getChar(end);
            while ('\t' == c || ' ' == c)
                c = document.getChar(++end);

            return document.get(start, end - start);
        } catch (final BadLocationException x) {
            // no-op
        }

        return ""; //$NON-NLS-1$
    }


    /**
     * Determines whether the offset is the beginning of a line in the given
     * document.
     * 
     * @param offset
     *            the offset
     * @return <code>true</code> if offset is the beginning of a line
     * @exception BadLocationException
     *                if offset is invalid in document
     * @since 3.0
     */
    private boolean isLineStart(final int offset) throws BadLocationException {
        int start = document.getLineOfOffset(offset);
        start = document.getLineOffset(start);
        return (start == offset);
    }


    private void updateCurrentContext(final TypedPosition[] ranges, final int currentPosition) {
        currentContext = formattingContextFactory.createFor(ranges, currentPosition);
    }

    /**
     * Defines a reference to either the offset or the end offset of a
     * particular position.
     */
    static class PositionReference implements Comparable<PositionReference> {

        /** The referenced position */
        protected Position position;
        /** The reference to either the offset or the end offset */
        protected boolean refersToOffset;
        /** The original category of the referenced position */
        protected String category;


        /**
         * Creates a new position reference.
         * 
         * @param position
         *            the position to be referenced
         * @param refersToOffset
         *            <code>true</code> if position offset should be referenced
         * @param category
         *            the category the given position belongs to
         */
        protected PositionReference(final Position position, final boolean refersToOffset, final String category) {
            this.position = position;
            this.refersToOffset = refersToOffset;
            this.category = category;
        }


        /**
         * Returns the offset of the referenced position.
         * 
         * @return the offset of the referenced position
         */
        protected int getOffset() {
            return position.getOffset();
        }


        /**
         * Manipulates the offset of the referenced position.
         * 
         * @param offset
         *            the new offset of the referenced position
         */
        protected void setOffset(final int offset) {
            position.setOffset(offset);
        }


        /**
         * Returns the length of the referenced position.
         * 
         * @return the length of the referenced position
         */
        protected int getLength() {
            return position.getLength();
        }


        /**
         * Manipulates the length of the referenced position.
         * 
         * @param length
         *            the new length of the referenced position
         */
        protected void setLength(final int length) {
            position.setLength(length);
        }


        /**
         * Returns whether this reference points to the offset or end offset of
         * the references position.
         * 
         * @return <code>true</code> if the offset of the position is
         *         referenced, <code>false</code> otherwise
         */
        protected boolean refersToOffset() {
            return refersToOffset;
        }


        /**
         * Returns the category of the referenced position.
         * 
         * @return the category of the referenced position
         */
        protected String getCategory() {
            return category;
        }


        /**
         * Returns the referenced position.
         * 
         * @return the referenced position
         */
        protected Position getPosition() {
            return position;
        }


        /**
         * Returns the referenced character position
         * 
         * @return the referenced character position
         */
        protected int getCharacterPosition() {
            if (refersToOffset)
                return getOffset();
            return getOffset() + getLength();
        }


        /*
         * @see Comparable#compareTo(Object)
         */
        @Override
        public int compareTo(final PositionReference obj) {
            return getCharacterPosition() - obj.getCharacterPosition();
        }
    }

    /**
     * The position updater used to update the remembered partitions.
     * 
     * @see IPositionUpdater
     * @see DefaultPositionUpdater
     */
    class NonDeletingPositionUpdater extends DefaultPositionUpdater {

        /**
         * Creates a new updater for the given category.
         * 
         * @param category
         *            the category
         */
        protected NonDeletingPositionUpdater(final String category) {
            super(category);
        }


        /*
         * @see DefaultPositionUpdater#notDeleted()
         */
        @Override
        protected boolean notDeleted() {
            return true;
        }
    }

    /**
     * The position updater which runs as first updater on the document's
     * positions. Used to remove all affected positions from their categories to
     * avoid them from being regularly updated.
     * 
     * @see IPositionUpdater
     */
    class RemoveAffectedPositions implements IPositionUpdater {
        /*
         * @see IPositionUpdater#update(DocumentEvent)
         */
        @Override
        public void update(final DocumentEvent event) {
            removeAffectedPositions(event.getDocument());
        }
    }

    /**
     * The position updater which runs as last updater on the document's
     * positions. Used to update all affected positions and adding them back to
     * their original categories.
     * 
     * @see IPositionUpdater
     */
    class UpdateAffectedPositions implements IPositionUpdater {

        /** The affected positions */
        private final int[] fPositions;
        /** The offset */
        private final int fOffset;


        /**
         * Creates a new updater.
         * 
         * @param positions
         *            the affected positions
         * @param offset
         *            the offset
         */
        public UpdateAffectedPositions(final int[] positions, final int offset) {
            fPositions = positions;
            fOffset = offset;
        }


        /*
         * @see IPositionUpdater#update(DocumentEvent)
         */
        @Override
        public void update(final DocumentEvent event) {
            updateAffectedPositions(event.getDocument(), fPositions, fOffset);
        }
    }
}
