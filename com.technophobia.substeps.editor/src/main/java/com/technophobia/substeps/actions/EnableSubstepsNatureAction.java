package com.technophobia.substeps.actions;

public class EnableSubstepsNatureAction extends AbstractSubstepsNatureAction {

    @Override
    protected String[] getUpdatedNatureIds(final String natureId, final String[] originalNatureIds,
            final boolean naturePresent) {
        if (!naturePresent) {
            return addNatureId(natureId, originalNatureIds);
        }
        return originalNatureIds;
    }


    private String[] addNatureId(final String natureId, final String[] oldNatureIds) {
        final String[] newNatureIds = new String[oldNatureIds.length + 1];
        System.arraycopy(oldNatureIds, 0, newNatureIds, 0, oldNatureIds.length);
        newNatureIds[newNatureIds.length - 1] = natureId;
        return newNatureIds;
    }
}
