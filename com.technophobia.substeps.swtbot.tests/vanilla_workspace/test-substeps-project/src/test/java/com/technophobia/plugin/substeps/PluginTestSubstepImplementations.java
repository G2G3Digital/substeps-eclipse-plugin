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
package com.technophobia.plugin.substeps;

import java.util.List;
import java.util.Map;

import com.technophobia.substeps.model.SubSteps.Step;
import com.technophobia.substeps.model.SubSteps.StepImplementations;
import com.technophobia.substeps.model.SubSteps.StepParameter;
import com.technophobia.substeps.model.parameter.IntegerConverter;

/**
 * 
 * @author ian
 * 
 */
@StepImplementations
public class PluginTestSubstepImplementations {

    @Step("CallMethod One with parameter \"([^\"]*)\"")
    public void meth1(final String param) {
    	System.out.println("meth1: " + param);
    }

    @Step("CallMethod Two")
    public void meth2() {
    	System.out.println("meth2");
    }

    @Step("CallMethod Three")
    public void meth3() {
    	System.out.println("meth3");
    }
    
    @Step("CallMethod four")
    public void meth4() {
    	System.out.println("method 4");
    }
    
    /**
     * Another example substep implementation
     * 
     * @example DoSomething with an int parameter "fred"
     * @section Custom
     * @param param
     */
    @Step("DoSomething with an int parameter \"([^\"]*)\"")
    public void exampleTwo(
            @StepParameter(converter = IntegerConverter.class) final int param) {

    }


    /**
     * Yet another example substep implementation
     * 
     * @example DoSomething with a table parameter |col1 | col2 | col3 | |row1-1
     *          | row1-2 | row1-3 | |row2-1 | row2-2 | row3-3 |
     * @section Custom
     * @param param
     */
    @Step("DoSomething with a table parameter")
    public void exampleThree(final List<Map<String, String>> table) {


    }

}
