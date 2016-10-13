/*
 Copyright 2013 Red Hat, Inc. and/or its affiliates.

 This file is part of lightblue.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.redhat.lightblue.rest.crud.cmd;

import com.redhat.lightblue.util.Error;
import com.redhat.lightblue.AsynchResponse;
import com.redhat.lightblue.mediator.Mediator;
import com.redhat.lightblue.rest.CallStatus;
import com.redhat.lightblue.rest.crud.RestCrudConstants;
import com.redhat.lightblue.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsynchStatusCommand extends AbstractRestCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(AsynchStatusCommand.class);

    private final String jobId;

    public AsynchStatusCommand(String jobId) {
        this.jobId=jobId;
    }

    @Override
    public CallStatus run() {
        LOGGER.debug("Asynchronous Status Request");
        Error.reset();
        Error.push("rest");
        Error.push(getClass().getSimpleName());
        Error.push(jobId);
        try {
            AsynchResponse r = getMediator().getAsynchResponse(jobId);
            return new CallStatus(r);
        } catch (Error e) {
            LOGGER.error("asynch:generic_error failure: {}", e);
            return new CallStatus(e);
        } catch (Exception e) {
            LOGGER.error("asynch:generic_exception failure: {}", e);
            return new CallStatus(Error.get(RestCrudConstants.ERR_REST_ERROR, e.toString()));
        }
    }
}
