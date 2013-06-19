package com.technophobia.substeps.actions;


public class DisableSubstepsNatureAction extends AbstractSubstepsNatureAction {

    @Override
    protected String[] getUpdatedNatureIds(final String natureId, final String[] originalNatureIds,
            final boolean naturePresent) {
        if (naturePresent) {
            return removeNatureId(originalNatureIds, natureId);
        }
        return originalNatureIds;
    }


    private String[] removeNatureId(final String[] oldNatureIds, final String natureId) {
        final String[] newNatureIds = new String[oldNatureIds.length - 1];
        int i = 0;
        for (final String oldNatureId : oldNatureIds) {
            if (!oldNatureId.equals(natureId)) {
                newNatureIds[i++] = oldNatureId;
            }
        }
        return newNatureIds;
    }
}