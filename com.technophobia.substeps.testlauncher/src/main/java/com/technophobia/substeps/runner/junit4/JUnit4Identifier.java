/*******************************************************************************
 * Copyright Technophobia Ltd 2012
 * 
 * This file is part of the Substeps Eclipse Plugin.
 * 
 * The Substeps Eclipse Plugin is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License v1.0.
 * 
 * The Substeps Eclipse Plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 * 
 * You should have received a copy of the Eclipse Public License
 * along with the Substeps Eclipse Plugin.  If not, see <http://www.eclipse.org/legal/epl-v10.html>.
 ******************************************************************************/
package com.technophobia.substeps.runner.junit4;

import org.junit.runner.Description;

import com.technophobia.substeps.runner.ITestIdentifier;

public class JUnit4Identifier implements ITestIdentifier {
    private final Description plan;


    public JUnit4Identifier(final Description plan) {
        this.plan = plan;
    }


    @Override
    public String getName() {
        return plan.getDisplayName();
    }


    @Override
    public int hashCode() {
        return plan.hashCode();
    }


    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof JUnit4Identifier))
            return false;

        final JUnit4Identifier id = (JUnit4Identifier) obj;
        return plan.equals(id.plan);
    }

}
