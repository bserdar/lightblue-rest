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
package com.redhat.lightblue.rest.crud;

import javax.servlet.ServletContextEvent;  
import javax.servlet.ServletContextListener;

import com.redhat.lightblue.rest.RestConfiguration;

import com.redhat.lightblue.extensions.asynch.AsynchronousExecutionConfiguration;

import com.redhat.lightblue.asynch.DriverThread;
import com.redhat.lightblue.asynch.AsynchProcessorConfiguration;

public class AsynchListener implements ServletContextListener {

    private DriverThread driver;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            AsynchronousExecutionConfiguration cfg=RestConfiguration.getFactory().getFactory().getAsynchronousExecutionConfiguration();
            if(cfg!=null) {
                AsynchProcessorConfiguration processorCfg=AsynchProcessorConfiguration.initializeFromCfg(cfg);
                if(processorCfg!=null&&processorCfg.isProcess()) {
                    DriverThread driver=new DriverThread(processorCfg,RestConfiguration.getFactory());
                    driver.start();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if(driver!=null)
            driver.requestStop(true);
    }
}
