/*
 * Copyright (c) 2002-2019 "Neo4j,"
 * Neo4j Sweden AB [http://neo4j.com]
 *
 * This file is part of Neo4j.
 *
 * Neo4j is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.neo4j.bolt.v3.runtime;

import org.neo4j.bolt.runtime.BoltStateMachineState;
import org.neo4j.bolt.runtime.StateMachineContext;
import org.neo4j.logging.Log;
import org.neo4j.logging.internal.LogService;

public class TransactionStreamingState extends AbstractStreamingState
{
    private final Log logging; // ch add
    public TransactionStreamingState( LogService logService )
    {
        this.logging = logService.getUserLog( getClass() );
    }

    @Override
    public String name()
    {
        return "TX_STREAMING";
    }

    @Override
    protected BoltStateMachineState processStreamResultMessage( boolean pull, StateMachineContext context ) throws Throwable
    {
        long start = System.currentTimeMillis(); // ch add

        context.connectionState().getStatementProcessor().streamResult(
                recordStream -> context.connectionState().getResponseHandler().onRecords( recordStream, pull ) );

        long end = System.currentTimeMillis(); // ch add
        logging.info( "processStreamResult:" + Long.toString( end - start ) );
        return readyState;
    }
}
