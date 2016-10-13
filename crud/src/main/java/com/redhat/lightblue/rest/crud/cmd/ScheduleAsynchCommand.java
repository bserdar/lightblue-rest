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

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.redhat.lightblue.util.Error;
import com.redhat.lightblue.AsynchRequest;
import com.redhat.lightblue.AsynchResponse;
import com.redhat.lightblue.Request;
import com.redhat.lightblue.mediator.Mediator;
import com.redhat.lightblue.rest.CallStatus;
import com.redhat.lightblue.rest.crud.RestCrudConstants;
import com.redhat.lightblue.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScheduleAsynchCommand extends AbstractRestCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleAsynchCommand.class);

    private final String request;

    public ScheduleAsynchCommand(String request) {
        this.request = request;
    }

    @Override
    public CallStatus run() {
        LOGGER.debug("Asynchronous Request");
        Error.reset();
        Error.push("rest");
        Error.push(getClass().getSimpleName());
        try {
            AsynchRequest req;
            try {
                req = getJsonTranslator().parse(AsynchRequest.class, JsonUtils.json(request));
            } catch (Exception e) {
                LOGGER.error("AsynchRequest:parse failure: {}", e);
                return new CallStatus(Error.get(RestCrudConstants.ERR_REST_ERROR, "Error parsing request"));
            }
            try {
                if(req.getRequest()!=null) {
                    addCallerId(req.getRequest());
                } else if(req.getBulkRequest()!=null) {
                    for (Request r : req.getBulkRequest().getEntries()) {
                        addCallerId(r);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("asynch:validate failure: {}", e);
                return new CallStatus(Error.get(RestCrudConstants.ERR_REST_ERROR, "Request is not valid"));
            }
            AsynchResponse r = getMediator().submitAsynchRequest(req);
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
