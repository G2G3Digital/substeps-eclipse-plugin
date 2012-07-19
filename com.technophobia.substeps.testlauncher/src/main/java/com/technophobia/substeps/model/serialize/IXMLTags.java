package com.technophobia.substeps.model.serialize;

public interface IXMLTags {

    public static final String NODE_TESTRUN = "testrun"; //$NON-NLS-1$
    public static final String NODE_TESTSUITES = "testsuites"; //$NON-NLS-1$
    public static final String NODE_TESTSUITE = "testsuite"; //$NON-NLS-1$
    public static final String NODE_PROPERTIES = "properties"; //$NON-NLS-1$
    public static final String NODE_PROPERTY = "property"; //$NON-NLS-1$
    public static final String NODE_TESTCASE = "testcase"; //$NON-NLS-1$
    public static final String NODE_ERROR = "error"; //$NON-NLS-1$
    public static final String NODE_FAILURE = "failure"; //$NON-NLS-1$
    public static final String NODE_EXPECTED = "expected"; //$NON-NLS-1$
    public static final String NODE_ACTUAL = "actual"; //$NON-NLS-1$
    public static final String NODE_SYSTEM_OUT = "system-out"; //$NON-NLS-1$
    public static final String NODE_SYSTEM_ERR = "system-err"; //$NON-NLS-1$
    public static final String NODE_SKIPPED = "skipped"; //$NON-NLS-1$

    /**
     * value: String
     */
    public static final String ATTR_NAME = "name"; //$NON-NLS-1$
    /**
     * value: String
     */
    public static final String ATTR_PROJECT = "project"; //$NON-NLS-1$
    /**
     * value: Integer
     */
    public static final String ATTR_TESTS = "tests"; //$NON-NLS-1$
    /**
     * value: Integer
     */
    public static final String ATTR_STARTED = "started"; //$NON-NLS-1$
    /**
     * value: Integer
     */
    public static final String ATTR_FAILURES = "failures"; //$NON-NLS-1$
    /**
     * value: Integer
     */
    public static final String ATTR_ERRORS = "errors"; //$NON-NLS-1$
    /**
     * value: Boolean
     */
    public static final String ATTR_IGNORED = "ignored"; //$NON-NLS-1$
    /**
     * value: String
     */
    public static final String ATTR_PACKAGE = "package"; //$NON-NLS-1$
    /**
     * value: String
     */
    public static final String ATTR_ID = "id"; //$NON-NLS-1$
    /**
     * value: String
     */
    public static final String ATTR_CLASSNAME = "classname"; //$NON-NLS-1$
    /**
     * value: Boolean
     */
    public static final String ATTR_INCOMPLETE = "incomplete"; //$NON-NLS-1$
    /**
     * value: Double
     */
    public static final String ATTR_TIME = "time"; //$NON-NLS-1$
    //  public static final String ATTR_MESSAGE= "message"; //$NON-NLS-1$
    //  public static final String ATTR_TYPE= "type"; //$NON-NLS-1$
}
