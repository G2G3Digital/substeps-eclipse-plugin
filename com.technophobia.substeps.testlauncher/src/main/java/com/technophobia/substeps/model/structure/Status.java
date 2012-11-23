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
package com.technophobia.substeps.model.structure;

public enum Status {
    RUNNING_ERROR(false, true, false, true, -1), //
    RUNNING_FAILURE(false, true, true, false, -1), //
    RUNNING(false, true, false, false, -1), //
    ERROR(true, false, false, true, 1), //
    FAILURE(true, false, true, false, 2), //
    OK(true, false, false, false, 0), //
    NOT_RUN(false, false, false, false, -1);

    private final boolean done;
    private final boolean running;
    private final boolean failure;
    private final boolean error;
    private final int value;


    private Status(final boolean done, final boolean running, final boolean failure, final boolean error,
            final int value) {
        this.done = done;
        this.running = running;
        this.failure = failure;
        this.error = error;
        this.value = value;
    }


    public boolean isErrorOrFailure() {
        return error || failure;
    }


    public boolean isNotRun() {
        return !running && !done;
    }


    public boolean isRunning() {
        return running;
    }


    public boolean isError() {
        return error;
    }


    public boolean isFailure() {
        return failure;
    }


    public boolean isComplete() {
        return done;
    }


    public boolean isOK() {
        return !isErrorOrFailure();
    }


    public Result asResult() {
        if (!done) {
            return Result.UNDEFINED;
        }
        if (isError()) {
            return Result.ERROR;
        }
        if (isFailure()) {
            return Result.FAILURE;
        }
        return Result.OK;
    }


    public static Status forValue(final int statusCode) {
        for (final Status status : values()) {
            if (status.value == statusCode) {
                return status;
            }
        }
        return null;
    }


    public static Status combineStatus(final Status one, final Status two) {
        final Status progress = combineProgress(one, two);
        final Status error = combineError(one, two);
        return combineProgressAndErrorStatus(progress, error);
    }


    private static Status combineProgress(final Status one, final Status two) {
        if (one.isNotRun() && two.isNotRun())
            return NOT_RUN;
        else if (one.isComplete() && two.isComplete())
            return OK;
        else if (!one.isRunning() && !two.isRunning())
            return OK; // one done, one not-run -> a parent failed and its
                       // children are not run
        else
            return RUNNING;
    }


    private static Status combineError(final Status one, final Status two) {
        if (one.isError() || two.isError())
            return ERROR;
        else if (one.isFailure() || two.isFailure())
            return FAILURE;
        else
            return OK;
    }


    private static Status combineProgressAndErrorStatus(final Status progress, final Status error) {
        if (progress.isComplete()) {
            if (error.isError())
                return ERROR;
            if (error.isFailure())
                return FAILURE;
            return OK;
        }

        if (progress.isNotRun()) {
            // Assert.isTrue(!error.isErrorOrFailure());
            return NOT_RUN;
        }

        // Assert.isTrue(progress.isRunning());
        if (error.isError())
            return RUNNING_ERROR;
        if (error.isFailure())
            return RUNNING_FAILURE;
        // Assert.isTrue(error.isOK());
        return RUNNING;
    }
}
