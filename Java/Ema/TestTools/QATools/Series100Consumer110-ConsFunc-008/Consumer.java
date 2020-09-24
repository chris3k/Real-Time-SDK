///*|----------------------------------------------------------------------------------------------------
// *|            This source code is provided under the Apache 2.0 license      	--
// *|  and is provided AS IS with no warranty or guarantee of fit for purpose.  --
// *|                See the project's LICENSE.md for details.                  					--
// *|           Copyright (C) 2019 Refinitiv. All rights reserved.            		--
///*|----------------------------------------------------------------------------------------------------

//APIQA this file is QATools standalone. See qa_readme.txt for details about this tool.

package com.rtsdk.ema.examples.training.consumer.series100.ex110_MP_FileCfg;

import com.rtsdk.ema.access.Msg;
import com.rtsdk.ema.access.AckMsg;
import com.rtsdk.ema.access.ReqMsg.Rate;
import com.rtsdk.ema.access.ReqMsg.Timeliness;
import com.rtsdk.ema.access.GenericMsg;
import com.rtsdk.ema.access.RefreshMsg;
import com.rtsdk.ema.access.StatusMsg;
import com.rtsdk.ema.access.UpdateMsg;
import com.rtsdk.ema.access.EmaFactory;
import com.rtsdk.ema.access.OmmConsumer;
import com.rtsdk.ema.access.OmmConsumerClient;
import com.rtsdk.ema.access.OmmConsumerEvent;
import com.rtsdk.ema.access.OmmException;

class AppClient implements OmmConsumerClient
{
    public void onRefreshMsg(RefreshMsg refreshMsg, OmmConsumerEvent event)
    {
        System.out.println(refreshMsg);
    }

    public void onUpdateMsg(UpdateMsg updateMsg, OmmConsumerEvent event)
    {
        System.out.println(updateMsg);
    }

    public void onStatusMsg(StatusMsg statusMsg, OmmConsumerEvent event)
    {
        System.out.println(statusMsg);
    }

    public void onGenericMsg(GenericMsg genericMsg, OmmConsumerEvent consumerEvent)
    {
    }

    public void onAckMsg(AckMsg ackMsg, OmmConsumerEvent consumerEvent)
    {
    }

    public void onAllMsg(Msg msg, OmmConsumerEvent consumerEvent)
    {
    }
}

public class Consumer
{
    public static void main(String[] args)
    {
        OmmConsumer consumer = null;
        try
        {
            AppClient appClient = new AppClient();

            consumer = EmaFactory.createOmmConsumer(EmaFactory.createOmmConsumerConfig().consumerName("Consumer_2"));

            consumer.registerClient(EmaFactory.createReqMsg().serviceName("DIRECT_FEED").name("IBM.N").qos(Timeliness.BEST_TIMELINESS, Rate.BEST_RATE), appClient, 0);

            Thread.sleep(60000); // API calls onRefreshMsg(), onUpdateMsg() and
                                 // onStatusMsg()
        }
        catch (InterruptedException | OmmException excp)
        {
            System.out.println(excp.getMessage());
        }
        finally
        {
            if (consumer != null)
                consumer.uninitialize();
        }
    }
}

//END APIQA
