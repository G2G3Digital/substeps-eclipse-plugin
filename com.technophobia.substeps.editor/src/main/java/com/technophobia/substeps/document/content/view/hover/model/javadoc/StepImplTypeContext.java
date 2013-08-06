package com.technophobia.substeps.document.content.view.hover.model.javadoc;

import org.eclipse.jdt.core.IType;

import com.technophobia.substeps.model.StepImplementation;

public class StepImplTypeContext {
    static StepImplTypeContext with(final IType type, final StepImplementation stepImpl) {
        return new StepImplTypeContext(type, stepImpl);
    }

    private final IType type;
    private final StepImplementation stepImpl;


    private StepImplTypeContext(final IType type, final StepImplementation stepImpl) {
        this.type = type;
        this.stepImpl = stepImpl;
    }


    public IType type() {
        return type;
    }


    public StepImplementation stepImpl() {
        return stepImpl;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((stepImpl == null) ? 0 : stepImpl.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }


    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final StepImplTypeContext other = (StepImplTypeContext) obj;
        if (stepImpl == null) {
            if (other.stepImpl != null)
                return false;
        } else if (!stepImpl.equals(other.stepImpl))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        return true;
    }

}
